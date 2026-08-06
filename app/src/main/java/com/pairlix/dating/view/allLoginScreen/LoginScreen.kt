package com.pairlix.dating.view.allLoginScreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.abi.simplecountrypicker.DialogCountryPicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.getUserCountry
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.noInitialSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.isValidEmail
import com.pairlix.dating.helper.isValidPhone
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.LoginRequest
import com.pairlix.dating.utils.LocationManagers.LocationPermissionAndGpsBottomSheet
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.viewModel.AuthViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.coroutineScope
import androidx.compose.runtime.rememberCoroutineScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import com.pairlix.dating.ReusedComponents.getUserCountryLogin
import com.pairlix.dating.helper.getCountryFromLatLng
import com.pairlix.dating.requests.SocialLoginRequest
import com.pairlix.dating.response.HelpResponse
import com.pairlix.dating.viewModel.M4ViewModel
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.LatLng
import com.gravito.waiter_.Localization.Const

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel, m4ViewModel: M4ViewModel) {

    val context = LocalContext.current
    var loginOption by remember { mutableStateOf("mobile") }
    var token by remember { mutableStateOf("") }
    var mobileNo by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val isPermissionGranted by viewModel.permissionGranted.collectAsStateWithLifecycle()
    val isGpsEnabled by viewModel.isGpsEnabled.collectAsStateWithLifecycle()
    val latLng by viewModel.latLngFlow.collectAsStateWithLifecycle()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /*
        var countryCode by remember { mutableStateOf(getUserCountryLogin(context = context)) }
    */
    var countryCode by remember { mutableStateOf(Const.countryCode) }
    /*
        val userCountryIso = remember { getUserCountryLogin(context) }// returns "in", "pk", "us" }
    */
    var userCountryIso by remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as Activity
    val firebaseAuth = FirebaseAuth.getInstance()
    var socialLoginType by remember { mutableStateOf("") }
    val credentialManager = CredentialManager.create(context)
    // 🔹 Device token ko state me rakhenge
    var deviceToken by remember { mutableStateOf(SharedPreference.get(context).deviceToken ?: "") }
    val coroutineScope = rememberCoroutineScope()
    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(context.getString(R.string.default_web_client_id))
        .setFilterByAuthorizedAccounts(false).build()

    val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

    suspend fun signInWithGoogle() {

        try {

            val result = credentialManager.getCredential(
                request = request, context = context
            )

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val email = googleIdTokenCredential.id
                val name = googleIdTokenCredential.displayName
                val profilePic = googleIdTokenCredential.profilePictureUri

                viewModel.socialUniqueId = idToken // ✅ SAVE HERE

                SingletonObject.googleLoginData = GoogleLoginData(
                    email = email,
                    firstName = name?.split(" ")?.firstOrNull() ?: "",
                    lastName = name?.split(" ")?.drop(1)?.joinToString(" ") ?: "",
                    idToken = idToken,
                    profilePic = profilePic.toString()
                )

                val nameParts = name?.trim()?.split(" ") ?: emptyList()
                val firstName = nameParts.firstOrNull() ?: ""
                val lastName = if (nameParts.size > 1) nameParts.drop(1).joinToString(" ") else ""
                Log.d(
                    "GoogleLogin",
                    "Name: $name Email: $email firstName: $firstName lastName: $lastName"
                )
                socialLoginType = "google"
                // 🔹 Send to your backend API

                val location = latLng

                if (location?.latitude == null || location.longitude == null) {
                    Toast.makeText(context, "Location not fetch", Toast.LENGTH_SHORT).show()
                    return
                }

                viewModel.hitSocialLogin(
                    model = SocialLoginRequest(
                        deviceToken = token,
                        deviceType = 1,
                        firstName = SingletonObject.googleLoginData.firstName,
                        lastName = SingletonObject.googleLoginData.lastName,
                        socialType = 1,
                        uniqueId = idToken,
                        currentLatitude = location.latitude.toString(),
                        currentLongitude = location.longitude.toString()
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("errrr", "signInWithGoogle: ${e}", )
            e.printStackTrace()
        }
    }


    val socialLogin by viewModel.getSocialLoginData.collectAsState()


    LaunchedEffect(socialLogin) {

        socialLogin.let { state ->

            when (state) {

                is EmpResource.Loading -> {
                    CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {

                    val response = state.value

                    if (response?.success == true) {

                        val currentStep = response.data?.currentStep
                        val isProfileComplete = response.data?.isProfileCompleted ?: false
                        val authType = response.data?.authType ?: ""
                        CustomLoader.hideLoader()
                        context.showToast(response.message ?: "")

                        SharedPreference.get(context).accessToken =
                            response.data?.accesstoken.toString()
                        SingletonObject.accessToken=response.data?.accesstoken.toString()

                        // 🔥 CREATE ACCOUNT
                        if (authType == "signup") {
                            navController.navigate(Screen.CreateAccountScreen.route) {
                                popUpTo(0)
                            }
                            viewModel.resetSocialLogin()
                            return@LaunchedEffect
                        }

// 🔥 MAIN SCREEN — profile already complete
                        if (isProfileComplete) {
                            SharedPreference.get(context).isLogin = true
                            SharedPreference.get(context).userID = response.data?.user?._id ?: ""
                            m4ViewModel.selectedMainScreenIndex.value = 0
                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo(0)
                            }
                            viewModel.resetSocialLogin()
                            return@LaunchedEffect
                        }

// 🔥 LOGIN + currentStep is null or 0 → go to CompleteProfile1
                        if (authType == "login" && (currentStep == null || currentStep == 0)) {
                            navController.navigate(Screen.CompleteProfile1.route) {
                                popUpTo(0)
                            }
                            viewModel.resetSocialLogin()
                            return@LaunchedEffect
                        }

// 🔥 STEP FLOW — currentStep is valid (not null, not 0)
                        when (currentStep) {
                            1 -> navController.navigate(Screen.CompleteProfile2.route) { popUpTo(0) }
                            2 -> navController.navigate(Screen.CompleteProfile3.route) { popUpTo(0) }
                            3 -> navController.navigate(Screen.CompleteProfile4.route) { popUpTo(0) }
                            4 -> navController.navigate(Screen.CompleteProfile5.route) { popUpTo(0) }
                            5 -> navController.navigate(Screen.CompleteProfile6.route) { popUpTo(0) }
                            6 -> navController.navigate(Screen.CompleteProfile7.route) { popUpTo(0) }
                            7 -> navController.navigate(Screen.FaceVerificationScreen.route) {
                                popUpTo(
                                    0
                                )
                            }

                            8 -> navController.navigate(Screen.UploadIdScreen.route) { popUpTo(0) }
                            else -> navController.navigate(Screen.CompleteProfile1.route) {
                                popUpTo(
                                    0
                                )
                            }
                        }

                        viewModel.resetSocialLogin()
                    }
                }

                is EmpResource.Failure -> {

                    CustomLoader.hideLoader()
                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }

                    viewModel.resetSocialLogin()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

    LocationPermissionAndGpsBottomSheet(
        model = viewModel,
        activity = (context as Activity),
    )

    LaunchedEffect(Unit) {
        Log.d("CountryCheck", "ISO = $userCountryIso")
        generateToken(context) { token = it }
    }

    LaunchedEffect(
        isPermissionGranted,
        isGpsEnabled
    ) @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]) {

        if (isGpsEnabled && isPermissionGranted) {
            viewModel.fetchCurrentLocation()
        }
    }

    loginObserver(

        context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController,
        viewModel = viewModel,

        )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .imePadding()

    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight * 0.35f)
                    .appGradientBackground(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(200.sdp),
                    painter = painterResource(R.drawable.pairlix_logo), contentDescription = "logo"
                )
            }

            Column(
                modifier = Modifier
                    .offset(y = -10.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE190C3).copy(alpha = 0.2f))
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        color = Color(0xFF590988),
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                }

                verticalSpace(15)


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(62.dp))
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(62.dp)
                        )
                        .padding(8.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(62.dp))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                loginOption = "mobile"
                            }
                            .then(
                                if (loginOption == "mobile") Modifier.appGradientBackground()
                                else Modifier.background(Color.Transparent)
                            )
                            .padding(vertical = 10.sdp),
                        text = stringResource(R.string.mobile_number),
                        fontSize = 16.sp,
                        color = if (loginOption == "mobile") Color(0xFFFFFFFF) else Color(0xFF530386),
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        textAlign = TextAlign.Center)

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(62.dp))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                loginOption = "email"
                            } then (if (loginOption == "email") Modifier.appGradientBackground()
                        else Modifier.background(Color.Transparent)).padding(vertical = 10.sdp),
                        text = stringResource(R.string.email_id),
                        fontSize = 16.sp,
                        color = if (loginOption == "email") Color(0xFFFFFFFF) else Color(0xFF530386),
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        textAlign = TextAlign.Center)
                }

                verticalSpace(20)

                if (loginOption == "mobile") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(12.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            key(userCountryIso) {
                                DialogCountryPicker(
                                    modifier = Modifier,
                                    defaultCountryIdentifier = countryCode,
                                    pickedCountry = {
                                        countryCode = it.countryCode
                                    },
                                    countryCodeTextColorAndIconColor = MaterialTheme.colorScheme.onBackground,
                                    trailingIconComposable = {
                                        Image(
                                            painter = painterResource(R.drawable.arrow_top_ic),
                                            modifier = Modifier.rotate(180f),
                                            colorFilter = ColorFilter.tint(Color(0xFF999999)),
                                            contentDescription = "Open country picker"
                                        )
                                    },
                                    isCircleShapeFlag = false,
                                    isCountryFlagVisible = false,

                                )
                            }
                        }
                        horizontalSpace(10)
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(shape = RoundedCornerShape(12.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            TextField(
                                value = mobileNo, onValueChange = {
                                    if (it.length <= 15 && it.all { char -> char.isDigit() }) {
                                        mobileNo = it
                                    }
                                }, placeholder = {
                                    Text(
                                        text = stringResource(R.string.enter_mobile_number),
                                        style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                            fontSize = 14.sp,
                                        ),
                                        color = Color(0xFF6D6D6D),
                                        textAlign = TextAlign.Center
                                    )
                                }, keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                                ), singleLine = true, colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent,
                                    cursorColor = MaterialTheme.colorScheme.onBackground
                                ), textStyle = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )


                        }


                    }
                } else if (loginOption == "email") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        TextField(
                            value = email, onValueChange = {
                                email = noInitialSpace(it)
                            }, placeholder = {
                                Text(
                                    text = stringResource(R.string.enter_email_id),
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                        fontSize = 14.sp,
                                    ),
                                    color = Color(0xFF6D6D6D),
                                    textAlign = TextAlign.Center
                                )
                            }, keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                            ), singleLine = true, colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onBackground
                            ), textStyle = TextStyle(
                                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )


                    }


                }


                verticalSpace(20, true)

                AppButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.next),
                    onClick = {

                        if (latLng?.latitude == null || latLng?.longitude == null) {
                          context.showToast( context.getString(R.string.location_not_fetch))
                            return@AppButton
                        }
                        if (loginOption == "mobile") {

                            if (mobileNo.isBlank()) {
                                context.showToast( context.getString(R.string.please_enter_mobile_number))
                                return@AppButton
                            }

                            if (!isValidPhone(mobileNo)) {
                                context.showToast( context.getString(R.string.please_enter_a_valid_mobile_number_7_to_15_digits))
                                return@AppButton
                            }
                            viewModel.hitLogin(
                                model = LoginRequest(
                                    phoneNumber = mobileNo,

                                    countryCode = countryCode,
                                    email = null,
                                    deviceToken = token ?: "asdfghjkl",
                                    latitude = latLng?.latitude.toString(),
                                    longitude = latLng?.longitude.toString()
                                )
                            )

                            SingletonObject.loginFromEmail = false
                            SingletonObject.loginFromMobile = true
                            viewModel.loginMobile = mobileNo
                            viewModel.loginEmail = ""
                            viewModel.countryCode = countryCode
                            SingletonObject.isComeFromRegister = false

                        } else {  // EMAIL TAB

                            if (email.isBlank()) {
                                context.showToast( context.getString(R.string.please_enter_email))

                                return@AppButton
                            }

                            if (!email.isValidEmail()) {
                                context.showToast( context.getString(R.string.please_enter_valid_email))
                                return@AppButton
                            }

                            // ✅ UPDATED: ADD LAT LONG HERE (IMPORTANT)
                            viewModel.hitLogin(
                                model = LoginRequest(
                                    email = email,
                                    phoneNumber = null,
                                    countryCode = null,
                                    deviceToken = token ?: "asdfghjkl",
                                    latitude = latLng?.latitude.toString(),
                                    longitude = latLng?.longitude.toString()
                                )
                            )

                            SingletonObject.loginFromMobile = false
                            SingletonObject.loginFromEmail = true
                            SingletonObject.isComeFromRegister = false
                            viewModel.loginMobile = ""
                            viewModel.loginEmail = email
                        }

                        SingletonObject.isComeFromRegister = false
                    }
                )


                verticalSpace(25)


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                    )
                    horizontalSpace(5)
                    Text(
                        text = stringResource(R.string.or_sign_in_with),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )
                    horizontalSpace(5)

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }

                verticalSpace(20)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                coroutineScope.launch { signInWithGoogle() }
                            },
                        painter = painterResource(R.drawable.google_icon),
                        contentDescription = "google icon"
                    )

                    horizontalSpace(15)

                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                socialLoginType = "apple"
                                appleSignIn(
                                    activity,
                                    navController,
                                    viewModel,
                                    token,
                                    latLng,
                                    context
                                )
                            },
                        painter = painterResource(R.drawable.apple_icon),
                        contentDescription = "apple icon"
                    )
                }


                verticalSpace(20)

                val interaction = remember { MutableInteractionSource() }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {


                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(MaterialTheme.colorScheme.onBackground)) {
                                append(
                                    text = stringResource(R.string.new_user),
                                )
                            }
                            withStyle(style = SpanStyle(MaterialTheme.colorScheme.onBackground)) {
                                append(
                                    text = " ",
                                )
                            }
                            withStyle(style = SpanStyle(color = Color(0xFF590988))) {
                                append(
                                    text = stringResource(R.string.create_account),
                                )
                            }
                        },
                        modifier = Modifier.clickable(
                            interactionSource = interaction, indication = null
                        ) {
                            viewModel.profileImage.value = null
                            SingletonObject.isFromEditProfile = false
                            navController.navigate(Screen.CreateAccountScreen.route)
                        },
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),

                        )


                }
                verticalSpace(5)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    val annotatedText = buildAnnotatedString {
                        pushStringAnnotation(tag = "terms", annotation = "terms")
                        withStyle(style = SpanStyle(color = Color(0xFF590988))) {
                            append(stringResource(R.string.terms_and_conditions))
                        }
                        pop()

                        append("  ")
                        pushStringAnnotation(tag = "And", annotation = "And")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                            append(stringResource(R.string.and_symbol))
                        }
                        pop()
                        append("  ")

                        pushStringAnnotation(tag = "privacy", annotation = "privacy")
                        withStyle(style = SpanStyle(color = Color(0xFF590988))) {
                            append(stringResource(R.string.privacy_and_policy))
                        }
                        pop()
                    }
                    ClickableText(
                        text = annotatedText, style = TextStyle(
                            fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        ), onClick = { offset ->

                            annotatedText.getStringAnnotations(
                                tag = "terms", start = offset, end = offset
                            ).firstOrNull()?.let {

                                navController.navigate(Screen.TermsAndConditionScreen.route)

                            }

                            annotatedText.getStringAnnotations(
                                tag = "privacy", start = offset, end = offset
                            ).firstOrNull()?.let {

                                navController.navigate(Screen.PrivacyAndPolicyScreen.route)

                            }
                        })
                }
                verticalSpace(20)


            }
        }
    }
}

fun loginObserver(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
) {
    viewModel.getLoginData.observe(lifecycleOwner) {
        when (it) {
            is EmpResource.Failure -> {
                it.throwable?.let { it1 -> ErrorUtil.handlerGeneralError(context, it1) }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Success -> {
                if (it.value.success == true) {
                    context.showToast(it.value.message ?: "")
                    // Toast.makeText(context, it.value.message, Toast.LENGTH_SHORT).show()
                    CustomLoader.hideLoader()
                    SharedPreference.get(context).accessToken =
                        it.value.data?.accessToken.toString()
                    SingletonObject.accessToken=it.value.data?.accessToken.toString()
                    navController.navigate(Screen.OtpScreen.route)
                    it.value.success = false
                }
            }

            else -> {}
        }
    }
}

fun generateToken(context: Context, token: (String) -> Unit) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->

        if (!task.isSuccessful) {
            return@OnCompleteListener
        }
        // Get new FCM registration token
        val token = task.result
        SharedPreference.get(context).deviceToken = token ?: ""
        token(token)
        // Log and toast
        val msg = token.toString()
        Log.d("TAG", "deviceToken ==" + msg)
        //  Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

    })


}

fun appleSignIn(
    activity: Activity,
    navController: NavController,
    viewModel: AuthViewModel,
    token: String,
    latLng: LatLng?,   // 👈 yaha aa gaya
    context: Context
) {
    val provider = OAuthProvider.newBuilder("apple.com").apply {
        scopes = arrayOf("email", "name").toMutableList()
        addCustomParameter("locale", "en")
    }

    val auth = FirebaseAuth.getInstance()

    auth.pendingAuthResult?.addOnSuccessListener { authResult ->

        authResult.user?.let { user ->
            handleAppleSignInSuccess(user, navController, viewModel, token, latLng, context)
            //navigate here
            SingletonObject.googleLoginData = GoogleLoginData()  // clear google data
            SingletonObject.isGoogleLogin = false
        }

    }?.addOnFailureListener { e ->
        Log.e("Apple addOnFailureListener -> ", e.message.toString())
    }

        ?: run {

            auth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener { authResult ->

                    authResult.user?.let { user ->
                        SingletonObject.googleLoginData = GoogleLoginData()
                        SingletonObject.isGoogleLogin = false
                        handleAppleSignInSuccess(
                            user,
                            navController,
                            viewModel,
                            token,
                            latLng,
                            context
                        )
                        //navigate here


                    }

                }.addOnFailureListener { e ->

                    Log.d("Apple SignIn", "Fail -> ${e.message}")

                }
        }
}

fun handleAppleSignInSuccess(
    user: FirebaseUser,
    navController: NavController,
    viewModel: AuthViewModel,
    token: String,
    latLng: LatLng?,
    context: Context
) {

    val email = user.email
    val name = user.displayName
    val uid = user.uid

    Log.d("AppleLogin", "Email: $email")
    Log.d("AppleLogin", "Name: $name")
    Log.d("AppleLogin", "UID: $uid")

    val names = name?.trim()?.split(" ")
    val firstName = names?.getOrNull(0) ?: ""
    val lastName = names?.getOrNull(1) ?: ""
    viewModel.socialUniqueId = uid

    if (latLng?.latitude == null || latLng.longitude == null) {
        context.showToast( context.getString(R.string.location_not_fetch))
        return
    }

    viewModel.hitSocialLogin(
        model = SocialLoginRequest(
            deviceToken = token,
            deviceType = 1,
            firstName = firstName,
            lastName = lastName,
            socialType = 2,
            uniqueId = uid,
            currentLatitude = latLng.latitude.toString(),
            currentLongitude = latLng.longitude.toString()
        )
    )
}

data class GoogleLoginData(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val idToken: String = "",
    val profilePic: String? = null
)