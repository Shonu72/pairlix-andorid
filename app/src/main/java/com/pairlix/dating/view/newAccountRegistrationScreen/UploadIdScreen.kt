package com.pairlix.dating.view.newAccountRegistrationScreen

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.pairlix.dating.MainActivity
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.bitmapToUri
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.extractUniversalDate
import com.pairlix.dating.helper.toMultipartDirect
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest9
import com.pairlix.dating.response.GetAllFaithsStep7Response
import com.pairlix.dating.response.UploadDocumentFileResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.viewModel.AuthViewModel
import ir.kaaveh.sdpcompose.sdp
import okhttp3.MultipartBody

@Composable
fun UploadIdScreen(navController: NavController, viewModel: AuthViewModel) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var multipartFile: MultipartBody.Part? = null
    var frontImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var backImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isFrontSelected by remember { mutableStateOf(true) } // which card was tapped
    var frontImageUri by remember { mutableStateOf<Uri?>(null) }
    var backImageUri by remember { mutableStateOf<Uri?>(null) }
    var extractedFrontText by remember { mutableStateOf("") }
    var extractedBackText by remember { mutableStateOf("") }
    var showSourceDialog by remember { mutableStateOf(false) }
    var frontUploadedUrl by remember { mutableStateOf("") }
    var backUploadedUrl by remember { mutableStateOf("") }
    var tempFrontBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var tempBackBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uploadType by remember { mutableStateOf("") } // "front" or "back"
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)

            if (isFrontSelected) {
                tempFrontBitmap = bitmap
                frontImageUri = it
                frontImageUri?.let { uri ->
                    recognizeTextFromUri(uri) { text ->
                        extractedFrontText = text
                    }
                }

            } else {
                tempBackBitmap = bitmap
                backImageUri = it
                backImageUri?.let { uri ->
                    recognizeTextFromUri(uri) { text ->
                        extractedBackText = text
                    }
                }
            }

            multipartFile = it.toMultipartDirect(context, "upload_file")
            multipartFile?.let { part ->
                viewModel.hitExtractDocumentData(SharedPreference.get(context).accessToken, part)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val imageUri = bitmapToUri(context, it)
            if (isFrontSelected) {
                tempFrontBitmap = it
                frontImageUri = imageUri
                frontImageUri?.let { uri ->
                    recognizeTextFromUri(uri) { text ->
                        extractedFrontText = text
                    }
                }
            } else {
                tempBackBitmap = it
                backImageUri = imageUri
                backImageUri?.let { uri ->
                    recognizeTextFromUri(uri) { text ->
                        extractedBackText = text
                    }
                }
            }

            multipartFile = it.toMultipartDirect(context, "upload_file")
            multipartFile?.let { part ->
                viewModel.hitExtractDocumentData(SharedPreference.get(context).accessToken, part)
            }
        }
    }


    // Permission launcher for CAMERA
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context,
                context.getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT).show()
        }
    }


    LaunchedEffect(Unit) {
        uploadObserverstep9(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            navController = navController as NavHostController,
            uploadType= { uploadType },
            isFrontSelected = { isFrontSelected },
            onFrontUrl = { frontUploadedUrl = it },
            onBackUrl = { backUploadedUrl = it },
            onFrontBitmap = { frontImageBitmap = it },
            onBackBitmap = { backImageBitmap = it },
            tempFrontBitmap = { tempFrontBitmap },
            tempBackBitmap = { tempBackBitmap }
        )

        createAccountStep9Observer(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            navController = navController as NavHostController
        )
    }

    /*  uploadObserverstep9(
          context = context as MainActivity,
          viewModel = viewModel,
          lifecycleOwner = lifecycleOwner,
          navController = navController as NavHostController,
          isFrontSelected = { isFrontSelected },
          onFrontUrl = { frontUploadedUrl = it },
          onBackUrl = { backUploadedUrl = it }
      )*/



    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.upload_id),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
            }
            verticalSpace(20)
            Text(
                text = stringResource(R.string.upload_id_proof),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )
            verticalSpace(20)
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .border(
                            1.dp, color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .weight(1f)
                        .padding(vertical = 25.sdp)
                        .clickable {
                            isFrontSelected = true
                            uploadType = "front"
                            showSourceDialog = true
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (frontImageBitmap != null) {
                        Image(
                            bitmap = frontImageBitmap!!.asImageBitmap(),
                            contentDescription = "Front ID",
                            modifier = Modifier
                                .width(150.dp)
                                .height(120.dp)
                                .padding(horizontal = 8.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.upload_image_ic),
                            contentDescription = null
                        )
                    }

                    verticalSpace(20)

                    Text(
                        text = stringResource(R.string.upload_front_side),
                        color = Color(0xFF6D6D6D),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )
                }

                horizontalSpace(20)

                Column(
                    modifier = Modifier
                        .border(
                            1.dp, color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .weight(1f)
                        .padding(vertical = 25.sdp)
                        .clickable {
                            isFrontSelected = false
                            uploadType = "back"
                            showSourceDialog = true
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    if (backImageBitmap != null) {
                        Image(
                            bitmap = backImageBitmap!!.asImageBitmap(),
                            contentDescription = "Back ID",
                            modifier = Modifier
                                .width(150.dp)
                                .height(120.dp)
                                .padding(horizontal = 8.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.upload_image_ic),
                            contentDescription = null
                        )
                    }


                    verticalSpace(20)
                    Text(
                        text = stringResource(R.string.upload_back_side),
                        color = Color(0xFF6D6D6D),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )
                }

                if (showSourceDialog) {
                    AlertDialog(
                        onDismissRequest = { showSourceDialog = false },
                        title = { Text(stringResource(R.string.select_image)) },
                        text = { Text(stringResource(R.string.choose_image_source)) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showSourceDialog = false
                                    // ask for camera permission, then open camera
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            ) {
                                Text(stringResource(R.string.camera))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showSourceDialog = false
                                    // open gallery picker
                                    galleryLauncher.launch("image/*")
                                }
                            ) {
                                Text(stringResource(R.string.gallery))
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState())

            ) {

              /*  Spacer(Modifier.height(20.dp))

                Spacer(Modifier.height(10.dp))

                Text(stringResource(R.string.user_information), style = MaterialTheme.typography.titleMedium,color = MaterialTheme.colorScheme.onBackground)


                Spacer(Modifier.height(10.dp))

                if (extractedFrontText.isNotEmpty()) {
                    Text(
                        stringResource(R.string.extracted_text_front),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 10.sp
                    )
                    Text(extractedFrontText, fontSize = 8.sp,                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                Spacer(Modifier.height(10.dp))

                if (extractedBackText.isNotEmpty()) {
                    Text(
                        stringResource(R.string.extracted_text_back),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 10.sp
                    )
                    Text(extractedBackText, fontSize = 8.sp, color = MaterialTheme.colorScheme.onBackground,
                    )
                }*/


            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {

            AppButton(
                modifier = Modifier.padding(bottom = 10.sdp, start = 16.dp, end = 16.dp),
                text = stringResource(R.string.submit),
                onClick = {
                    if (frontUploadedUrl.isEmpty()) {
                        context.showToast(context.getString(R.string.please_upload_document))
                        return@AppButton
                    }

                    val combinedExtractedText = extractedFrontText + "\n" + extractedBackText
                    val date = extractUniversalDate(extractedFrontText)
                   /* Log.d(
                        "extractedFrontText",
                        "UploadIdScreen: extractedFrontText$extractedFrontText  date$date"
                    )
                    */



/*
                    if (viewModel.dob.value == (viewModel.getExtractData.value?.extracted?.dob ?: ""))
*/
                    val dob = viewModel.getExtractData.value?.extracted?.dob?.trim()
                    if (!dob.isNullOrEmpty() && dob != "null") {
                        viewModel.hitCompleteProfile9(
                            access_token = SharedPreference.get(context).accessToken,
                            request = CompleteProfileRequest9(
                                data = CompleteProfileRequest9.Data(
                                    uploadIdFront = frontUploadedUrl,
                                    uploadIdBack = backUploadedUrl,
                                    extraData = combinedExtractedText,
                                    documentDob = viewModel.getExtractData.value?.extracted?.dob.toString()),
                                step = 9
                            )
                        )
                    }
                    
                    else context.showToast(context.getString(R.string.date_of_birth_not_fetch_please_re_upload_document))
                }
            )


            verticalSpace(10)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        SharedPreference.get(context).isLogin = true
                        SingletonObject.isSkip = true
                        SingletonObject.isComeFromUploadIdPage = true
                        navController.navigate(Screen.MainScreen.route) {
                            popUpTo(0)

                        }
                    }
                    .padding(bottom = 10.dp),
                text = stringResource(R.string.skip_for_now),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            )
        }
    }
}

fun recognizeTextFromUri(uri: Uri, callback: (String) -> Unit) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    val stream = MyApplication.appContext.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(stream)
    val image = InputImage.fromBitmap(bitmap, 0)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            callback(visionText.text)
        }
        .addOnFailureListener { e ->
            callback("${MyApplication.appContext.getString(R.string.ocr_error)}: ${e.message ?: ""}")        }
}


fun createAccountStep9Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
) {
    viewModel.completeProfile9.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            EmpResource.Idle -> {}

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (state.value.success == true) {
                    context.showToast(state.value.message?:"")
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(0)
                        SingletonObject.isComeFromUploadIdPage=true
                        SharedPreference.get(context).isLogin = true

                    }
                    state.value.success = false
                }
            }
        }
    }
}


fun uploadObserverstep9(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    uploadType: () -> String,
    isFrontSelected: () -> Boolean,
    onFrontUrl: (String) -> Unit,
    onBackUrl: (String) -> Unit,
    onFrontBitmap: (Bitmap?) -> Unit,
    onBackBitmap: (Bitmap?) -> Unit,
    tempFrontBitmap: () -> Bitmap?,
    tempBackBitmap: () -> Bitmap?
)

{
    viewModel.extractDocumentData.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                if (isFrontSelected()) {
                    onFrontBitmap(null)
                } else {
                    onBackBitmap(null)
                }

                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }

            EmpResource.Idle -> {}

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                Log.e("TAG", "Entered DOB: ${viewModel.dob.value} hello, Extracted DOB: ${state.value.data?.extracted?.dob}")

                val uploadedUrl = state.value.data?.uploadUserFile?:""
                viewModel.getExtractData.value = state.value.data

               /* if (isFrontSelected()) {
                    onFrontUrl(uploadedUrl)
                    onFrontBitmap(tempFrontBitmap())
                } else {
                    onBackUrl(uploadedUrl)
                    onBackBitmap(tempBackBitmap())
                }*/

                if (uploadType() == "front") {
                    onFrontUrl(uploadedUrl)
                    onFrontBitmap(tempFrontBitmap())
                } else {
                    onBackUrl(uploadedUrl)
                    onBackBitmap(tempBackBitmap())
                }
            }
        }
    }
}
