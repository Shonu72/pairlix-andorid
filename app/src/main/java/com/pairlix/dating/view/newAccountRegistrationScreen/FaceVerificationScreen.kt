package com.pairlix.dating.view.newAccountRegistrationScreen

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.amplifyframework.ui.liveness.ui.FaceLivenessDetector
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomDialog
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest8
import com.pairlix.dating.requests.LiveNessResultRequest
import com.pairlix.dating.response.CreateSessionResponse
import com.pairlix.dating.response.LiveNessResultResponse
import com.pairlix.dating.viewModel.AuthViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService

@Composable
fun FaceVerificationScreen(
    executor: ExecutorService,
    navController: NavHostController,
    viewmodel: AuthViewModel
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showPhotoDialog by remember { mutableStateOf(true) }
    var permissionGranted by remember { mutableStateOf(false) }
    var showFaceLiveness by remember { mutableStateOf(false) }
    var livenessComplete by remember { mutableStateOf(false) }
    var livenessSuccess by remember { mutableStateOf(false) }
    var livenessError by remember { mutableStateOf<String?>(null) }
    val createSession by viewmodel.createSession.collectAsState()
    val getLiveNessResult by viewmodel.getLiveNessResult.collectAsState()
    var createSessionData by remember { mutableStateOf<CreateSessionResponse.Data?>(null) }
    var getLiveNessResultData by remember { mutableStateOf<LiveNessResultResponse.Data?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> permissionGranted = granted }

    // Request camera permission
    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    // Create liveness session
    LaunchedEffect(Unit) {
        createSessionData = null
        showFaceLiveness = false
        livenessComplete = false
        livenessSuccess = false
        viewmodel.hitCreateSession(
            access_token = SharedPreference.get(context).accessToken
        )
    }

    BackHandler(enabled = showFaceLiveness || livenessComplete) {
        if (livenessComplete || livenessError != null) {
            // Allow going back if verification done or errored
            navController.popBackStack()
        }
        // During active liveness scan, ignore back press
    }

    DisposableEffect(Unit) {
        onDispose {
            viewmodel.resetLivenessState()
        }
    }

    // Handle create session response
    LaunchedEffect(createSession) {
        createSession.let {
            when (it) {
                is EmpResource.Loading -> {
                    CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    createSessionData = it.value.data
                    showFaceLiveness = true
                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable?.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

    // Handle liveness result response
    LaunchedEffect(getLiveNessResult) {
        getLiveNessResult.let {
            when (it) {
                is EmpResource.Loading -> {
                    CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    getLiveNessResultData = it.value.data

                    if (it.value.data?.isLive == true) {

                        // ✅ SUCCESS
                        livenessSuccess = true
                        livenessComplete = true

                        viewmodel.hitCompleteProfile8(
                            access_token = SharedPreference.get(context).accessToken,
                            request = CompleteProfileRequest8(
                                step = 8,
                                data = CompleteProfileRequest8.Data(
                                    isFaceVerified = true
                                )
                            )
                        )

                    } else {

                        // ❌ FAILED (LOW CONFIDENCE)
                        livenessSuccess = false
                        livenessComplete = true          // 🔥 THIS SHOWS ResultView
                        showFaceLiveness = false         // 🔥 STOP CAMERA
                        livenessError =
                            it.value.message ?: context.getString(R.string.liveness_confidence_too_low_please_try_again)
                    }
                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable?.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                        livenessError = err.message?:""
                        livenessComplete = true
                        livenessSuccess = false
                    }
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

    // Observe complete profile step 8
    LaunchedEffect(Unit) {
        createAccountStep8Observer(
            context = context as MainActivity,
            viewModel = viewmodel,
            lifecycleOwner = context,
            navController = navController
        ) { dialogVisible ->
            showDialog = dialogVisible
        }
    }

    if (!permissionGranted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.camera_permission_required))
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(
                    text = stringResource(R.string.grant_permission),
                    onClick = { launcher.launch(Manifest.permission.CAMERA) }
                )
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding().statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {


            TopBackBtnHeading(
                navController,
                text = stringResource(R.string.face_verification),
                modifier = Modifier.padding(start = 15.dp),
            )

            verticalSpace(20)

            if (showDialog) {
                CustomDialog(
                    id = R.drawable.dialog_ic,
                    text1 = stringResource(R.string.face_verification_done_successfully),
                    text2 = "",
                    onDismiss = { showDialog = false }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    // Show liveness detector
                    showFaceLiveness && !livenessComplete && createSessionData?.sessionId != null -> {
                        LivenessDetectorView(
                            sessionId = createSessionData?.sessionId ?: "",
                            region = "us-east-1",
                            onComplete = {
                                Log.d("Liveness", "Liveness check completed")
                                viewmodel.hitLiveNessResult(
                                    access_token = SharedPreference.get(context).accessToken,
                                    request = LiveNessResultRequest(
                                        sessionId = createSessionData?.sessionId ?: "",
                                    ),
                                )
                            },
                            onError = { error ->


                               Log.e("Liveness", "Error: $error")
                                livenessError = error
                                livenessComplete = true
                                livenessSuccess = false
                                showFaceLiveness = false
                            }
                        )
                    }

                    // Show result
                    livenessComplete -> {
                        ResultView(
                            success = livenessSuccess,
                            error = livenessError,
                            onRetry = {
                                // Reset and restart
                                showFaceLiveness = false
                                livenessComplete = false
                                livenessSuccess = false
                                livenessError = null
                                createSessionData = null

                                // Create new session
                                viewmodel.hitCreateSession(
                                    access_token = SharedPreference.get(context).accessToken
                                )
                            }
                        )
                    }

                    // Show loading
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                if (livenessError != null) {
                                    // ✅ Show error + retry instead of white screen
                                    Text(
                                        text = "✗",
                                        style = MaterialTheme.typography.displayLarge,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = livenessError ?: stringResource(R.string.verification_failed),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    AppButton(
                                        text = stringResource(R.string.try_again),
                                        onClick = {
                                            livenessError = null
                                            livenessComplete = false
                                            livenessSuccess = false
                                            showFaceLiveness = false
                                            createSessionData = null
                                            viewmodel.hitCreateSession(
                                                access_token = SharedPreference.get(context).accessToken
                                            )
                                        }
                                    )
                                } else {
                                    // Loading state
                                    Text(stringResource(R.string.initializing_face_verification))
                                }
                            }
                        }
                    }
                }
            }





            // Submit Button
            /*Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                   .background(MaterialTheme.colorScheme.background)
            ) {
                val statusText = when {
                    livenessSuccess -> "Face Verified Successfully"
                    livenessError != null -> livenessError ?: "Verification failed"
                    else -> "Complete the face verification"
                }

                Text(
                    text = statusText,
                    color = if (livenessSuccess) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .zIndex(3f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )

                AppButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    text = "Submit",
                    onClick = {
                        if (livenessSuccess) {
                            viewmodel.hitCompleteProfile8(
                                access_token = SharedPreference.get(context).accessToken,
                                request = CompleteProfileRequest8(
                                    step = 8,
                                    data = CompleteProfileRequest8.Data(
                                        isFaceVerified = true
                                    )
                                )
                            )



                        } else {
                            Toast.makeText(
                                context,
                                "Please complete face verification first",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }*/




        }
    }

    if(showPhotoDialog){
        CustomDialog(
            id = R.drawable.face_detection_icon,
            text1 = stringResource(R.string.this_photo_is_only_for) +
                    stringResource(R.string.verification_purpose),
            text2 = stringResource(R.string.this_photo_will_not_show_to_other_users),
            onDismiss = { showPhotoDialog = false },
            appBtn = true,
            btnText = stringResource(R.string.Continue)
        )


    }
}

@Composable
fun LivenessDetectorView(
    sessionId: String,
    region: String,
    onComplete: () -> Unit,
    onError: (String) -> Unit
) {

    FaceLivenessDetector(
        sessionId = sessionId, // provided by your backend
        region = "us-east-1", // AWS region
        onComplete={
            onComplete()
        },

        onError = { error ->
            onError(error.message?:"")
            Log.e("Liveness", "Liveness failed", error.throwable)
        }
    )



}

@Composable
fun ResultView(
    success: Boolean,
    error: String?,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            if (success) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.liveness_verified),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(R.string.your_face_has_been_successfully_verified),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Text(
                    text = "✗",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.verification_failed),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = error ?: stringResource(R.string.please_try_again),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!success) {
                AppButton(
                    text = stringResource(R.string.try_again),
                    onClick = onRetry
                )
            }
        }
    }
}

fun createAccountStep8Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    showDialog: (Boolean) -> Unit = {}
) {
    viewModel.completeProfile8.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { ErrorUtil.handlerGeneralError(context, it) }
            }

            EmpResource.Idle -> {}

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (state.value.success == true) {
                    context.showToast(state.value.message?:"")
                    showDialog(true)
                    state.value.success = false

                    context.lifecycleScope.launchWhenStarted {
                        delay(2000)
                        showDialog(false)

                        navController.navigate(Screen.UploadIdScreen.route) {
                            popUpTo(Screen.FaceVerificationScreen.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}