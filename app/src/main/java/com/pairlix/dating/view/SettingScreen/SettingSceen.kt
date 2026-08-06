package com.pairlix.dating.view.SettingScreen

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.SettingNotificationCard
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.VisibilityCard
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.NotificationSettingRequest
import com.pairlix.dating.response.NotificationSettingResponse
import com.pairlix.dating.response.SafetyAndSupportResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.M6.ProfileStatus
import com.pairlix.dating.view.profileDetails.PreviewProfileObserver
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M7ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.whileSelect

@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    m4ViewModel: M4ViewModel,
    m7ViewModel: M7ViewModel,
    socketViewModel: SocketViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

/*
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteAccountDialog = false
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = stringResource(R.string.delete_account_title),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 16.sp,
                    color = if(isSystemInDarkTheme()) Color.White else Color(0xFF262324),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.delete_account_message),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 14.sp,
                    color = if(isSystemInDarkTheme()) Color.White else Color(0xFF6B7280)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        m7ViewModel.hitDeleteAccount(token = SharedPreference.get(context).accessToken)
                        coroutineScope.launch { offsetX.animateTo(0f) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteAccountDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = if(isSystemInDarkTheme()) Color.White else Color(0xFF6B7280),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            }
        )
    }
*/

    var notificationStatus by remember { mutableIntStateOf(-1) }
    val deleteAccount by m7ViewModel.deleteAccount.collectAsState()
    val logout by m7ViewModel.logout.collectAsState()
    val notificationSetting by m7ViewModel.notificationSetting.collectAsState()
    val notificationSettingData =
        remember { mutableStateOf<NotificationSettingResponse.Data?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        viewModel.hitPreviewProfile(
            access_token = SharedPreference.get(context).accessToken
        )
    }

    PreviewProfileObserver(
        viewModel = viewModel,
        viewModelM4=m4ViewModel,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            SharedPreference.get(context).userID = it?.id.toString()
            SharedPreference.get(context).profileImage = it?.profileImages.toString()
            socketViewModel.connectSocket(it?.id.toString())
            viewModel.getPreviewProfileData.value = it
        })
    val homeData = viewModel.getPreviewProfileData.value

    LaunchedEffect(homeData) {
        homeData?.let { data ->
            notificationStatus = data.notificationSetting ?: 1
        }
    }

    LaunchedEffect(notificationSetting) {
        notificationSetting.let { state ->
            when (state) {
                is EmpResource.Loading -> {
                    CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    context.showToast(state?.value?.message ?: "")
                    notificationSettingData.value = state.value.data
                    m7ViewModel.resetNotificationSetting()
                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                    m7ViewModel.resetNotificationSetting()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

    LaunchedEffect(logout) {
        logout.let {
            when (it) {
                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    context.showToast(it?.value?.message ?: "")
                    SharedPreference.get(context).isLogin = false
                    SharedPreference.get(context).accessToken = ""
                    SharedPreference.get(context).deviceToken = ""
                    SharedPreference.get(context).userID = ""
                    viewModel.resetLivenessState()
                    m4ViewModel.selectedMainScreenIndex.value = 0
                    viewModel.resetHeightState()
                    viewModel.clearUserSession()
                    viewModel.resetMatchState()
                    m4ViewModel.resetFilterState()
                    socketViewModel.sendOffline()
                    m4ViewModel.clearAllFilters()
                    SingletonObject.isFromEditProfile = false
                    SingletonObject.isCreateFlowInitialized = false
                    SingletonObject.isComeFromRegister = false
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                    m7ViewModel.resetLogout()
                }

                is EmpResource.Loading -> {
                    CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                    m7ViewModel.resetLogout()
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(deleteAccount) {
        deleteAccount.let {
            when(it) {
                is EmpResource.Loading -> {
                    CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    context.showToast(it?.value?.message ?: "")
                    showDeleteAccountDialog = false
                    SharedPreference.get(context).isLogin = false
                    SharedPreference.get(context).accessToken = ""

                    SharedPreference.get(context).deviceToken = ""
                    SharedPreference.get(context).userID = ""
                    viewModel.resetLivenessState()
                    m4ViewModel.selectedMainScreenIndex.value = 0
                    viewModel.resetHeightState()
                    viewModel.clearUserSession()
                    socketViewModel.sendOffline()
                    SingletonObject.isFromEditProfile = false
                    SingletonObject.isCreateFlowInitialized = false
                    SingletonObject.isComeFromRegister = false
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(0)
                    }
                    m7ViewModel.resetDeleteAccount()
                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                    m7ViewModel.resetDeleteAccount()
                }

                else -> {}
            }
        }
    }

    val notificationList = listOf<ProfileStatus>(
        ProfileStatus(stringResource(R.string.push_notification)),
        ProfileStatus(stringResource(R.string.disable_all_notifications)),
        ProfileStatus(stringResource(R.string.new_messages)),
        ProfileStatus(stringResource(R.string.profile_views)),
        ProfileStatus(stringResource(R.string.liked_received)),
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            TopBackBtnHeading(
                navController = navController,
                text = stringResource(R.string.settings)
            )

            verticalSpace(30)

            SettingNotificationCard(
                heading = stringResource(R.string.notification_settings),
                list = notificationList,
                selectedIndex = notificationStatus - 1,
                onItemSelected = {
                    notificationStatus = it
                    m7ViewModel.hitNotificationSetting(
                        token = SharedPreference.get(context).accessToken,
                        request = NotificationSettingRequest(
                            notificationSetting = notificationStatus
                        )
                    )
                }
            )

            verticalSpace(100)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = (16.dp))
                    .clip(shape = RoundedCornerShape(52.dp))
                    .border(1.dp, Color(0xFFEE404C), shape = RoundedCornerShape(52.dp))
                    .background(Color(0xFF1AEE404C), shape = RoundedCornerShape(52.dp))
                    .clickable {
                        m7ViewModel.hitLogout(SharedPreference.get(context).accessToken)
                    }
                    .padding(vertical = 16.dp),
                text = stringResource(R.string.log_out),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                color = Color(0xFFEE404C),
                textAlign = TextAlign.Center
            )

            verticalSpace(10)

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = (16.dp), vertical = 10.dp)
                    .clip(shape = RoundedCornerShape(52.dp))
                    .border(1.dp, Color(0xFFEE404C), shape = RoundedCornerShape(52.dp))
                    .background(Color(0xFFEE404C), shape = RoundedCornerShape(52.dp))
                    .clickable { showDeleteAccountDialog = true }
                    .padding(vertical = 16.dp),
                text = stringResource(R.string.delete_account),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }

    if (showDeleteAccountDialog) {

        Dialog(onDismissRequest = {
            showDeleteAccountDialog = false
        }) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {



                    Image(painter = painterResource(R.drawable.red_alert_sign_ic), contentDescription = null,modifier=Modifier.size(45.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = stringResource(R.string.delete_account_title),
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Message
                    Text(
                        text = stringResource(R.string.delete_account_message),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // Cancel (Gradient)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color(0xFF7B5CFF),
                                            Color(0xFFD46DFF)
                                        )
                                    )
                                )
                                .clickable {
                                    showDeleteAccountDialog = false
                                    coroutineScope.launch { offsetX.animateTo(0f) }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Delete (Outline)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .border(
                                    1.5.dp,
                                    Color.Red,
                                    RoundedCornerShape(50.dp)
                                )
                                .clickable {
                                    m7ViewModel.hitDeleteAccount(
                                        token = SharedPreference.get(context).accessToken
                                    )
                                    coroutineScope.launch { offsetX.animateTo(0f) }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.delete),
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}