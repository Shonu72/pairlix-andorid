package com.pairlix.dating.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.MatchDialog
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.agora.AgoraManager
import com.pairlix.dating.view.M4.ActivityScreen
import com.pairlix.dating.view.M4.FilterScreen
import com.pairlix.dating.view.M5.AudioCallScreen
import com.pairlix.dating.view.M5.BlockedProfilesScreen
import com.pairlix.dating.view.M5.ChatScreen
import com.pairlix.dating.view.M5.ChatScreenOneToOne
import com.pairlix.dating.view.M5.VideoCallScreen
import com.pairlix.dating.view.allLoginScreen.ChangeLanguageScreen
import com.pairlix.dating.view.allLoginScreen.IntroductionScreen
import com.pairlix.dating.view.allLoginScreen.LoginScreen
import com.pairlix.dating.view.allLoginScreen.OtpScreen
import com.pairlix.dating.view.home.HomeScreen
import com.pairlix.dating.view.home.HomeScreenDetailScreen
import com.pairlix.dating.view.home.MainScreen
import com.pairlix.dating.view.home.ProfileScreen
import com.pairlix.dating.view.newAccountRegistrationScreen.CompleteProfileScreen1
import com.pairlix.dating.view.newAccountRegistrationScreen.CompleteProfileScreen2
import com.pairlix.dating.view.newAccountRegistrationScreen.CompleteProfileScreen3
import com.pairlix.dating.view.newAccountRegistrationScreen.CompleteProfileScreen4
import com.pairlix.dating.view.newAccountRegistrationScreen.CompleteProfileScreen5
import com.pairlix.dating.view.newAccountRegistrationScreen.CompleteProfileScreen6
import com.pairlix.dating.view.newAccountRegistrationScreen.CompleteProfileScreen7
import com.pairlix.dating.view.newAccountRegistrationScreen.CreateAccountScreen
import com.pairlix.dating.view.newAccountRegistrationScreen.FaceVerificationScreen
import com.pairlix.dating.view.newAccountRegistrationScreen.Practice
import com.pairlix.dating.view.newAccountRegistrationScreen.ProfileApprovedStatusScreen
import com.pairlix.dating.view.newAccountRegistrationScreen.UploadIdScreen
import com.pairlix.dating.view.plans.CurrentPlaDetailsScreen
import com.pairlix.dating.view.plans.PlanUpgradeScreen
import com.pairlix.dating.view.profileDetails.ViewProfileScreen
import com.pairlix.dating.view.splash.SplashScreen
import com.pairlix.dating.view.updragePlan.PlanScreen
import com.pairlix.dating.view.updragePlan.SelectPlanScreen
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.agora.CallViewModel
import com.pairlix.dating.firbase.NotificationBus
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.notification.NotificationType
import com.pairlix.dating.requests.CallStatusRequest
import com.pairlix.dating.requests.MatchNotificationData
import com.pairlix.dating.view.M5.CallPickUpScreen
import com.pairlix.dating.view.M6.NotificationScreen
import com.pairlix.dating.view.M6.VisibilityControlScreen
import com.pairlix.dating.view.M7.FaqScreen
import com.pairlix.dating.view.M7.HelpScreen
import com.pairlix.dating.view.M7.PrivacyAndPolicyScreen
import com.pairlix.dating.view.M7.SafetyAndSupportScreen
import com.pairlix.dating.view.M7.SettingLanguageScreen
import com.pairlix.dating.view.M7.TermsAndConditionScreen
import com.pairlix.dating.view.M7.ThemeScreen
import com.pairlix.dating.view.M7.TicketDetailsScreen
import com.pairlix.dating.view.M7.TicketScreen
import com.pairlix.dating.view.SettingScreen.SettingScreen
import com.pairlix.dating.view.home.MatchUser
import com.pairlix.dating.viewModel.ChatViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import com.pairlix.dating.viewModel.M6ViewModel
import com.pairlix.dating.viewModel.M7ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation(
    executor: ExecutorService,
    navController: NavHostController,
    viewModel: AuthViewModel, m4ViewModel: M4ViewModel, viewModelM5: M5ViewModel, viewModelM6: M6ViewModel, viewModelM7: M7ViewModel, socketViewModel: SocketViewModel,chatViewModel: ChatViewModel, callViewModel: CallViewModel,
    byNoti:Boolean,data:Bundle?


) {
    val incomingCall by callViewModel.incomingCall.collectAsStateWithLifecycle()
    Log.e("incc", "${incomingCall}: ", )
    var navigate by remember { mutableStateOf(true) }
    val userData = viewModel.getPreviewProfileData?.value
    val context = LocalContext.current

    val startDestination = when {
        SharedPreference.get(context).isFirstLaunch -> Screen.ChangeLanguage.route
        !SharedPreference.get(context).isLogin -> Screen.LoginScreen.route
        else -> Screen.MainScreen.route }

    var matchData by remember { mutableStateOf<MatchNotificationData?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        NotificationBus.match.collect{
            matchData = it
            showDialog=true

        }
    }

    if (showDialog && matchData != null) {
        val data = matchData!!
        MatchDialog(
            user1 = MatchUser(
                name = data.senderName,
                imageUrl = data.senderImage
            ),
            user2 = MatchUser(name = data.receiverName, imageUrl = data.receiverImage),
            buttonText = stringResource(R.string.say_salam),
            onClick = {
                showDialog = false
                m4ViewModel.showBottomActions = 1
                m4ViewModel.selectedChipIndex.value = 0
                navController.navigate(
                    Screen.ChatScreenOneToOne.passId(
                        data.matchId,
                        name = data.senderName,
                        age = data.senderAge,
                        image = data.senderImage,
                        isOnline = data.senderOnline,
                        isActive = data.senderOnline,
                        matchDate = data.matchedOn,
                        isDocument = data.senderDocumentVerified,
                        isFace = data.senderFaceVerified
                    )
                )
            },
            onDismiss = {
                chatViewModel.joinRoom(data.matchId)
                showDialog = false
            },
            topHeart = R.drawable.heart_background,
            bottomHeart = R.drawable.heart_background
        )
    }

    fun navigateWithAuthCheck(route: String) {

        val isLogin = SharedPreference.get(context).isLogin

        if (!isLogin) {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(0)
            }
        } else {
            navController.navigate(route) {
                popUpTo(Screen.MainScreen.route) { inclusive = false }
                launchSingleTop = true
            }
        }
    }
        fun handleNotificationNavigation(
            type: String,
            data: Bundle,
            navController: NavHostController
        ) {
            when (type) {
                NotificationType.CHAT_MSG,
                NotificationType.UNREAD_MESSAGE_REMINDER -> {
                    val id = data.getString("id") ?: ""
                    val senderName = data.getString("senderName") ?: ""
                    val senderImage = data.getString("senderImage") ?: ""
                    val age = data.getString("senderAge") ?: ""
                    val isActive = data.getString("senderIsActive") == "true"
                    val isFace = data.getString("senderFaceVerified") == "true"
                    val isDocument = data.getString("senderDocumentVerified") == "true"
                    val matchDate = data.getString("matchUpdatedAt")

                    navigateWithAuthCheck(
                        Screen.ChatScreenOneToOne.passId(
                            id,
                            senderName,
                            age,
                            senderImage,
                            isActive,
                            isActive,
                            isDocument,
                            isFace,
                            matchDate,
                        )
                    )
                }

                // 🔥 CALL (ignore if socket handles)
                NotificationType.INCOMING_CALL -> {

                    val roomId = data.getString("roomId") ?: ""
                    val token = data.getString("token") ?: ""
                    val uid = data.getString("uid")?.toIntOrNull() ?: 0
                    val channelName = data.getString("channelName") ?: ""
                    val callType = data.getString("callType") ?: ""

                    val senderName = data.getString("senderName") ?: ""
                    val senderImage = data.getString("senderImage") ?: ""

                    // ✅ Set data in ViewModel (same as socket)
                    callViewModel.updateCallType(callType)

                    callViewModel.updateSenderData(
                        name = senderName,
                        lastName = "",
                        age = "",
                        image = senderImage
                    )

                    callViewModel.setAgoraData(
                        token = token,
                        uid = uid,
                        channelName = channelName,
                        roomId = roomId
                    )

                    navigateWithAuthCheck(Screen.CallPickUpScreen.route)
                }

                NotificationType.MISSED_CALL,
                NotificationType.CALL_ENDED -> {
                    navigateWithAuthCheck( Screen.MainScreen.route)
                }


                NotificationType.LIKE->{
                    navigateWithAuthCheck( Screen.MainScreen.route)
                    m4ViewModel.selectedMainScreenIndex.value=1
                    m4ViewModel.selectedChipIndex.value=0
                }
                NotificationType.SUPERLIKE->{
                    navigateWithAuthCheck( Screen.MainScreen.route)
                    m4ViewModel.selectedMainScreenIndex.value=1
                    m4ViewModel.selectedChipIndex.value=0

                }

                NotificationType.MATCH -> {
                    navigateWithAuthCheck( Screen.MainScreen.route)
                    m4ViewModel.selectedMainScreenIndex.value=1
                    m4ViewModel.selectedChipIndex.value=2
                }

                // 🔥 ADMIN
                NotificationType.ADMIN_MSG,
                NotificationType.ADMIN_WARNING -> {
                    navigateWithAuthCheck( Screen.NotificationScreen.route)
                }

                // 🔥 PROFILE
                NotificationType.PROFILE_NEEDS_UPDATE,
                NotificationType.PROFILE_REMINDER -> {
                    navigateWithAuthCheck( Screen.ViewProfileScreen.route)
                }

                // 🔥 SUBSCRIPTION
                NotificationType.SUBSCRIPTION_EXPIRING_SOON,
                NotificationType.SUBSCRIPTION_EXPIRED -> {
                    navigateWithAuthCheck( Screen.PlanScreen.route)
                }

                // 🔥 SETTINGS
                NotificationType.NEW_DEVICE_LOGIN -> {
                    navigateWithAuthCheck( Screen.SettingScreen.route)
                }

                // 🔥 HOME
                NotificationType.AI_WEEKLY_MATCH -> {
                    navigateWithAuthCheck( Screen.MainScreen.route)
                    m4ViewModel.selectedMainScreenIndex.value=1
                    m4ViewModel.selectedChipIndex.value=2
                }

                // 🔥 DEFAULT
                else -> {
                    navigateWithAuthCheck( Screen.MainScreen.route)
                }
            }
        }


    val callStatus by viewModelM7.callStatus.collectAsState()

    LaunchedEffect(callStatus) {

        callStatus.let { state ->

            when (state) {

                is EmpResource.Loading -> {
                    // loader
                }

                is EmpResource.Success -> {


                    CustomLoader.hideLoader()

                    viewModelM7.resetCallStatus()
                    if (state.value.data?.status!="ended"&&state.value.data?.status!="cancelled"){
                        val type = data?.getString("type") ?: ""
                            handleNotificationNavigation(
                                type = type,
                                data = data!!,
                                navController = navController
                            )
                    }
                }

                is EmpResource.Failure -> {

                    CustomLoader.hideLoader()

                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }

                    viewModelM7.resetCallStatus()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

        val isHandled = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {

            if (byNoti && data != null && !isHandled.value) {

                val roomId = data.getString("roomId") ?: ""

                viewModelM7.hitCallStatus(
                    access_token = SharedPreference.get(context).accessToken,
                    request = CallStatusRequest(roomId = roomId))



                isHandled.value = true

                val type = data.getString("type") ?: ""
                if (type!= NotificationType.INCOMING_CALL) {
                    handleNotificationNavigation(
                        type = type,
                        data = data,
                        navController = navController
                    )
                }
            }
        }

    LaunchedEffect(incomingCall) {
        if (incomingCall?.roomId != null) {
            callViewModel.updateCallType(incomingCall?.callType?:"")
            callViewModel.updateSenderData(
                name = incomingCall?.firstName?:"",
                lastName = incomingCall?.lastName?:"",
                age = incomingCall?.age?:"",
                image = incomingCall?.profileImages?:""
            )
            callViewModel.setAgoraData(
                token = incomingCall?.token ?: "",
                uid = incomingCall?.uid ?: 0,
                channelName = incomingCall?.channelName ?: "", roomId = incomingCall?.roomId?:""
            )
            navigateWithAuthCheck(Screen.CallPickUpScreen.route)
        }

    }

    NavHost(
        navController = navController, startDestination = startDestination
    ) { composable(Screen.Splash.route) { SplashScreen {} }
        composable(Screen.ChangeLanguage.route) { ChangeLanguageScreen(navController, m7ViewModel = viewModelM7,viewModel,m4ViewModel) }
        composable(Screen.SettingLanguageScreen.route) { SettingLanguageScreen(navController, m7ViewModel = viewModelM7,viewModel,m4ViewModel) }
        composable(Screen.IntroductionScreens.route) { IntroductionScreen(navController) }
        composable(Screen.LoginScreen.route) { LoginScreen(navController, viewModel,m4ViewModel) }
        composable(Screen.OtpScreen.route) { OtpScreen(navController, viewModel,m4ViewModel) }
        composable(Screen.CreateAccountScreen.route) { CreateAccountScreen(navController, viewModel) }
        composable(Screen.CompleteProfile1.route) { CompleteProfileScreen1(navController, viewModel) }
        composable(Screen.CompleteProfile2.route) { CompleteProfileScreen2(navController, viewModel) }
        composable(Screen.CompleteProfile3.route) { CompleteProfileScreen3(navController, viewModel,m4ViewModel) }
        composable(Screen.CompleteProfile4.route) { CompleteProfileScreen4(navController, viewModel) }
        composable(Screen.CompleteProfile5.route) { CompleteProfileScreen5(navController, viewModel,m4ViewModel) }
        composable(Screen.CompleteProfile6.route) { CompleteProfileScreen6(navController, viewModel) }
        composable(Screen.CompleteProfile7.route) { CompleteProfileScreen7(navController, viewModel,viewModelM5) }
        composable(Screen.UploadIdScreen.route) { UploadIdScreen(navController, viewModel) }
        composable(Screen.ProfileApprovedStatusScreen.route) { ProfileApprovedStatusScreen(navController) }
        composable(Screen.MainScreen.route) { MainScreen(navController, viewModel,socketViewModel,m4ViewModel,viewModelM5,viewModelM6) }
        composable(Screen.ProfileScreen.route) { ProfileScreen(navController, viewModel,m4ViewModel) }
        composable(Screen.FaceVerificationScreen.route) { FaceVerificationScreen(executor, navController, viewModel) }
        composable(Screen.HomeScreen.route) { HomeScreen(navController, viewModel, m4ViewModel,viewModelM5,viewModelM6,socketViewModel,) }
        composable(Screen.HomeScreenDetailScreen.route,

            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },

            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },

            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            },

            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            HomeScreenDetailScreen(
                navController,
                viewModel,
                m4ViewModel,
                viewModelM5,
                socketViewModel
            )
        }
        composable(Screen.SelectPlanScreen.route) { SelectPlanScreen(navController, viewModel) }
        composable(Screen.PlanScreen.route) { PlanScreen(navController, viewModel) }
        composable(Screen.Practice.route) { Practice() }
        composable(Screen.PlanUpgradeScreen.route) { PlanUpgradeScreen(navController, viewModel) }
        composable(Screen.CurrentPlaDetailsScreen.route) { CurrentPlaDetailsScreen(navController, viewModel)}
        composable(Screen.ViewProfileScreen.route) { ViewProfileScreen(navController, viewModel,m4ViewModel,viewModelM5) }
        composable(Screen.ActivityScreen.route) { ActivityScreen(navController, m4ViewModel,viewModel) }
        composable(Screen.FilterScreen.route) { FilterScreen(navController, m4ViewModel,viewModel) }
        composable(Screen.ChatScreen.route) { ChatScreen(navController, m4ViewModel,viewModelM5,viewModel) }
        composable(Screen.CallPickUpScreen.route) { CallPickUpScreen(navController,callViewModel) }
        composable(route = Screen.VideoCallScreen.route, arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType },
            )
        ) { backStackEntry ->

            val roomId =
                backStackEntry.arguments?.getString("roomId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val image = Uri.decode(backStackEntry.arguments?.getString("image") ?: "")
            VideoCallScreen(
                roomId = roomId,
                userId = userId,
                image=image,navController,callViewModel

            )
        }

        composable(Screen.AudioCallScreen.route) {AudioCallScreen(navController, viewModelM5) }
        composable(Screen.BlockedProfileScreen.route) { BlockedProfilesScreen(navController, viewModelM5,m4ViewModel,viewModel) }
        composable(Screen.VisibilityControlScreen.route) { VisibilityControlScreen(navController,viewModelM6,viewModel,m4ViewModel) }
        composable(Screen.SettingScreen.route) { SettingScreen(
            navController,
            m4ViewModel = m4ViewModel,
            m7ViewModel = viewModelM7,
            socketViewModel = socketViewModel,
            viewModel = viewModel
        ) }
        composable(Screen.NotificationScreen.route) { NotificationScreen(navController,viewModelM7) }
        composable(
            route = Screen.ChatScreenOneToOne.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType },
                navArgument("isOnline") { type = NavType.BoolType },
                navArgument("isActive") { type = NavType.BoolType },
                navArgument("isDocument") { type = NavType.BoolType },
                navArgument("isFace") { type = NavType.BoolType },

                // ✅ matchDate as Query Param
                navArgument("matchDate") { type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },

                navArgument("age") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
            )
        ) { backstack ->

            val id = backstack.arguments?.getString("id") ?: ""

            val name = Uri.decode(backstack.arguments?.getString("name") ?: "")
            val age = Uri.decode(backstack.arguments?.getString("age") ?: "")

            val image = Uri.decode(backstack.arguments?.getString("image") ?: "")

            val isOnline = backstack.arguments?.getBoolean("isOnline") ?: false

            val isActive = backstack.arguments?.getBoolean("isActive") ?: false

            val matchDate = Uri.decode(backstack.arguments?.getString("matchDate") ?: "")
            val isDocument = backstack.arguments?.getBoolean("isDocument") ?: false
            val isFace = backstack.arguments?.getBoolean("isFace") ?: false
            ChatScreenOneToOne(navController, viewModelM5, viewModel,m4ViewModel,userId = id?:"", name = name?:"",  age = age.ifEmpty { null }, image = image?:"",isOnline = isOnline?:false,isActive=isActive?:false, isDocument = isDocument, isFace = isFace, matchDate = matchDate?:"",  callViewModel =callViewModel, socketViewModel = socketViewModel)
        }

        composable(Screen.TicketScreen.route) { TicketScreen(navController,viewModel,viewModelM7) }
        composable(Screen.TicketDetailsScreen.route) { TicketDetailsScreen(navController,viewModelM7) }
        composable(Screen.FaqScreen.route) { FaqScreen(navController,viewModelM7) }
        composable(Screen.HelpScreen.route) { HelpScreen(navController,viewModelM7) }
        composable(Screen.ChangeLanguage.route) { ChangeLanguageScreen( navController,viewModelM7,viewModel,m4ViewModel,) }
        composable(Screen.PrivacyAndPolicyScreen.route) { PrivacyAndPolicyScreen(navController,viewModelM7) }
        composable(Screen.SafetyAndSupportScreen.route) { SafetyAndSupportScreen(navController,viewModelM7) }
        composable(Screen.TermsAndConditionScreen.route) { TermsAndConditionScreen(navController,viewModelM7) }
        composable(Screen.ThemeScreen.route) { ThemeScreen(navController) }


    }

}

sealed class BottomNavItem(
    val icon: Int
) {
    object Home : BottomNavItem( R.drawable.home_icon)
    object Activity : BottomNavItem( R.drawable.heart_icon)
    object Chat : BottomNavItem( R.drawable.message_icon)
    object Profile : BottomNavItem( R.drawable.profile_ic)
}