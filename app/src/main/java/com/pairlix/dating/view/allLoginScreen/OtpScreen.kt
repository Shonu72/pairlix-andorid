package com.pairlix.dating.view.allLoginScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.getUserCountry
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.OtpVerifyRequest
import com.pairlix.dating.requests.ResendOtpRequest
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.delay
import kotlin.collections.get

@Composable
fun OtpScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    m4ViewModel: M4ViewModel

) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    otpObserver(
        context = context as MainActivity,
        viewModel = viewModel,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController,
        m4ViewModel = m4ViewModel
    )


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()

    ) {
        val max = this.maxHeight
        var otp by remember { mutableStateOf("") }
        var otpMobile = remember { mutableStateListOf("", "", "", "") }   // typing boxes
        var otpEmail = remember { mutableStateListOf("", "", "", "") }    // typing boxes
        var otpValueMobile by remember { mutableStateOf("") }  // final OTP string
        var otpValueEmail by remember { mutableStateOf("") }    // final OTP string
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
               .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)

        ) {
            TopBackBtnHeading(navController, text = stringResource(R.string.otp_verification))
            verticalSpace(30)
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.page_1),
                contentDescription = "page_1"
            )
            verticalSpace(30)
            Text(
                text = stringResource(
                    R.string._4_digit_otp_has_been_sent_to_your,
                    if (SingletonObject.loginFromEmail) stringResource(R.string.registered_email_id) else stringResource(
                        R.string.registered_mobile_number
                    )
                ),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                lineHeight = 24.sp
            )
            verticalSpace(10)
            Text(
                text =
                    if (SingletonObject.loginFromEmail) {
                        viewModel.loginEmail
                    } else {

                        "+${viewModel.countryCode.replace("+", "")} ${viewModel.loginMobile}"
                    },
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
            )

            verticalSpace(15)
            OtpInput(
                otpList = otpMobile,
                onOtpFilled = { otpValueMobile = it })
            verticalSpace(15)

//            Text(
//                modifier = Modifier.fillMaxWidth(), text = buildAnnotatedString {
//                    append("Resend OTP In ")
//                    withStyle(style = SpanStyle(color = Color(0xFF590988))) {
//                        append("90 Sec")
//                    }
//                }, fontSize = 14.sp, textAlign = TextAlign.Center
//            )
            ResendOtpTimerText(
                totalSeconds = 30,
                onResendClick = {

                    val resendType = when {
                        SingletonObject.isComeFromRegister -> 1          // Registration mobile OTP
                        SingletonObject.loginFromMobile -> 1             // Login using mobile
                        SingletonObject.loginFromEmail -> 2              // Login using email
                        else -> 1
                    }

                    viewModel.hitResendOtp(
                        access_token = SharedPreference.get(context).accessToken,
                        request = ResendOtpRequest(
                            type = resendType
                        )
                    )
                }
            )



            verticalSpace(15)
            if (SingletonObject.isComeFromRegister) {

                Text(
                    text = stringResource(R.string._4_digit_otp_has_been_sent_to_your_registered_email_id),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    lineHeight = 24.sp

                )
                verticalSpace(10)
                Text(
                    text = if (SingletonObject.isComeFromRegister) viewModel.loginEmail else "",
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                )

                verticalSpace(15)

                OtpInput(
                    otpList = otpEmail,
                    onOtpFilled = { otpValueEmail = it })
                verticalSpace(15)

                ResendOtpTimerText(
                    totalSeconds = 30, onResendClick = {

                        viewModel.hitResendOtp(
                            access_token = SharedPreference.get(context).accessToken,
                            request = ResendOtpRequest(
                                type = 2
                            )
                        )
                    }
                )
            }
            verticalSpace(15)

            AppButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = 10.dp),
                text = stringResource(R.string.Continue),
                onClick = {

                    Log.e("TAG", "OtpScreen:${SharedPreference.get(context).accessToken} ")

                    val mobileOtp = otpValueMobile.trim()
                    val emailOtp = otpValueEmail.trim()


                    if (mobileOtp.length != 4) {
                        context.showToast(context.getString(R.string.please_enter_valid_4_digit_otp))
                        return@AppButton
                    }

                    if (!mobileOtp.matches(Regex("\\d{4}"))) {
                        context.showToast(context.getString(R.string.otp_must_be_numbers_only))
                        return@AppButton
                    }

                    if (SingletonObject.isComeFromRegister) {

                        if (emailOtp.length != 4) {
                            context.showToast(context.getString(R.string.please_enter_valid_4_digit_email_otp))
                            return@AppButton
                        }

                        if (!emailOtp.matches(Regex("\\d{4}"))) {
                            context.showToast(context.getString(R.string.email_otp_must_be_numbers_only))
                            return@AppButton
                        }

                        viewModel.hitVerifyOtp(
                            token = SharedPreference.get(context).accessToken,
                            model = OtpVerifyRequest(
                                phoneOtp = mobileOtp.toInt(),
                                emailOtp = emailOtp.toInt()

                            )
                        )


                    }
                    if (SingletonObject.loginFromEmail) {

                        if (mobileOtp.length != 4) {
                            context.showToast(context.getString(R.string.please_enter_valid_4_digit_otp))
                            return@AppButton
                        }

                        viewModel.hitVerifyOtp(
                            token = SharedPreference.get(context).accessToken,
                            model = OtpVerifyRequest(
                                emailOtp = mobileOtp.toInt()

                            )
                        )

                    }

                    if (SingletonObject.loginFromMobile) {

                        viewModel.hitVerifyOtp(
                            token = SharedPreference.get(context).accessToken,
                            model = OtpVerifyRequest(
                                phoneOtp = mobileOtp.toInt()

                            )
                        )
                    }


                    /* if (SingletonObject.isComeFromRegister) {
                         SingletonObject.isComeFromRegister = false
                         // navController.navigate(Screen.CompleteProfile1.route) { ... }
                       } else {
                         // other flow
                       }
                    */
                }
            )
        }
    }
}

@Composable
fun OtpInput(
    otpList: MutableList<String>, onOtpFilled: (String) -> Unit
) {
    val otpLength = otpList.size
    val focusRequesters = List(otpLength) { FocusRequester() }
    var focusedIndex by remember { mutableStateOf(0) }
    val keyboard = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            otpList.forEachIndexed { index, value ->

                TextField(
                    value = value,
                    onValueChange = { newValue ->

                        // Only allow empty or single digit
                        if (newValue.length <= 1 && (newValue.isEmpty() || newValue.matches(Regex("[0-9]")))) {

                            otpList[index] = newValue

                            // 🔥 ALWAYS send combined OTP on any change
                            onOtpFilled(otpList.joinToString(""))

                            // If digit typed → move next
                            if (newValue.isNotEmpty() && index < otpLength - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }

                            // If deleted → move back
                            if (newValue.isEmpty() && index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            }
                        }
                    },

                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onBackground,
                    ),

                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            2.dp,
                            if (focusedIndex == index) Color(0xFF8B5DF6)
                            else  MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .focusRequester(focusRequesters[index])
                        .onFocusChanged {
                            if (it.isFocused) focusedIndex = index
                        }
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {

                                val currentValue = otpList[index]

                                if (currentValue.isNotEmpty()) {
                                    otpList[index] = ""
                                    onOtpFilled(otpList.joinToString(""))
                                    if (index > 0) focusRequesters[index - 1].requestFocus()
                                    return@onKeyEvent true
                                }

                                if (currentValue.isEmpty() && index > 0) {
                                    otpList[index - 1] = ""
                                    onOtpFilled(otpList.joinToString(""))
                                    focusRequesters[index - 1].requestFocus()
                                    return@onKeyEvent true
                                }
                            }
                            false
                        }
                )
            }
        }
    }
}


@Composable
fun ResendOtpTimerText(
    totalSeconds: Int = 90, onResendClick: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(totalSeconds) }

    // Timer starts when this composable enters composition
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1_000)
            timeLeft--
        }
    }

    val isWaiting = timeLeft > 0

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isWaiting) {
                // Only clickable when timer finished
                timeLeft = totalSeconds       // restart timer
                onResendClick()               // call your resend API / logic
            },
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                append(stringResource(R.string.resend_otp))
                append(" ")
            }
            if (isWaiting) {
                append(stringResource(R.string.`in`))
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(stringResource(R.string.sec, timeLeft))
                }

            } else {
                // When time is over, show tappable "Resend"
                withStyle(style = SpanStyle(color = Color(0xFF590988))) {
                    append(stringResource(R.string.now))
                }
            }
        }, fontSize = 14.sp, textAlign = TextAlign.Center
    )
}


fun otpObserver(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    m4ViewModel: M4ViewModel
) {

    viewModel.getVerifyOtp.observe(lifecycleOwner) {
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
                    val currentStep = it.value.data?. currentStep?:1
                    val isProfileComplete = it.value.data?.user?.isProfileCompleted ?: false
                    SharedPreference.get(context).accessToken =
                        it.value.data?.accesstoken.toString()
                    SingletonObject.accessToken= it.value.data?.accesstoken.toString()


                    context.showToast(it.value.message?:"")
                    //Toast.makeText(context, it.value.message, Toast.LENGTH_SHORT).show()
                    CustomLoader.hideLoader()

                    if (SingletonObject.isComeFromRegister) {
                        SingletonObject.isCreateFlowInitialized = false
                        navController.navigate(Screen.CompleteProfile1.route) {
                            popUpTo(Screen.OtpScreen.route) { inclusive = true }
                        }
                        it.value.success = false
                        SharedPreference.get(context).accessToken =
                            it.value.data?.accesstoken.toString()
                        SingletonObject.accessToken= it.value.data?.accesstoken.toString()


                    } else {

                        if (isProfileComplete) {
                            SharedPreference.get(context).accessToken = it.value.data?.accesstoken.toString()
                            SharedPreference.get(context).isLogin = true
                            SharedPreference.get(context).userID = it.value.data?.user?.id ?: ""
                            m4ViewModel.selectedMainScreenIndex.value=0

                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo(0)
                            }
                            SingletonObject.isCreateFlowInitialized = false

                            it.value.success = false
                            return@observe
                        }

                        when (currentStep) {
                            1 -> {
                                navController.navigate(Screen.CompleteProfile2.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }

                            2 -> {
                                navController.navigate(Screen.CompleteProfile3.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }

                            3 -> {
                                navController.navigate(Screen.CompleteProfile4.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }

                            4 -> {
                                navController.navigate(Screen.CompleteProfile5.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }

                            5 -> {
                                navController.navigate(Screen.CompleteProfile6.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }


                            6 -> {
                                navController.navigate(Screen.CompleteProfile7.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }

                            7 -> {
                                navController.navigate(Screen.FaceVerificationScreen.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }

                            8 -> {
                                navController.navigate(Screen.UploadIdScreen.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }



                            else -> {

                                // fallback if server sends invalid step
                                navController.navigate(Screen.CompleteProfile1.route) {
                                    popUpTo(Screen.OtpScreen.route) { inclusive = true }
                                }
                            }
                        }


                        it.value.success = false
                        SharedPreference.get(context).accessToken = it.value.data?.accesstoken.toString()
                        SingletonObject.accessToken= it.value.data?.accesstoken.toString()
                    }
                }
            }

            EmpResource.Idle -> {

            }
        }
    }






    viewModel.resendOtp.observe(lifecycleOwner) {
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
                    context.showToast(it.value.message?:"")

                   // Toast.makeText(context, it.value.message, Toast.LENGTH_SHORT).show()
                    CustomLoader.hideLoader()
                }
            }

            EmpResource.Idle -> {
                            }
        }
    }
}