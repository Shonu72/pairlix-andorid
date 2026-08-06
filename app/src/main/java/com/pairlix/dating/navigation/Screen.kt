package com.pairlix.dating.navigation

import android.media.Image
import android.net.Uri
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route :String) {
    object Splash : Screen("splash")
    object ChangeLanguage : Screen("change_language/{isFromSetting}"){
        fun createRoute(isFromSettings: Boolean = false) =
            "change_language/$isFromSettings"
    }
    object SettingLanguageScreen : Screen("SettingLanguageScreen")
    object IntroductionScreens : Screen("introduction_screen")
    object LoginScreen : Screen("login_screen")
    object OtpScreen : Screen("otp_screen")
    object CreateAccountScreen : Screen("createAccount_screen")
    object CompleteProfile1 : Screen("completeProfile1_screen")
    object CompleteProfile2 : Screen("completeProfile2_screen")
    object CompleteProfile3 : Screen("completeProfile3_screen")
    object CompleteProfile4 : Screen("completeProfile4_screen")
    object CompleteProfile5 : Screen("completeProfile5_screen")
    object CompleteProfile6 : Screen("completeProfile6_screen")
    object CompleteProfile7 : Screen("completeProfile7_screen")
    object UploadIdScreen : Screen("UploadIdScreen")
    object ProfileApprovedStatusScreen : Screen("ProfileApprovedStatusScreen")
    object HomeScreen : Screen("home_screen")
    object MainScreen : Screen("main_screen")
    object ProfileScreen : Screen("profile_screen")
    object FaceVerificationScreen : Screen("FaceVerificationScreen")
    object HomeScreenDetailScreen : Screen("HomeScreenDetailScreen")
    object SelectPlanScreen : Screen("SelectPlanScreen")
    object Practice : Screen("Practice")
    object PlanScreen : Screen("PlanScreen")
    object PlanUpgradeScreen : Screen("PlanUpgradeScreen")
    object CurrentPlaDetailsScreen : Screen("CurrentPlaDetailsScreen")
    object ViewProfileScreen : Screen("ViewProfileScreen")
    object ActivityScreen : Screen("ActivityScreen")
    object FilterScreen : Screen("FilterScreen")
    object ChatScreen : Screen("ChatScreen")
    object CallPickUpScreen : Screen("CallPickUpScreen")
    object VideoCallScreen : Screen("VideoCallScreen/{roomId}/{userId}/{image}") {
        fun createRoute(roomId: String,userId: String,image: String): String {
            val encodedImage = Uri.encode(image)

            return "VideoCallScreen/$roomId/$userId/$encodedImage"
        }
    }

    object AudioCallScreen : Screen("AudioCallScreen")
    object BlockedProfileScreen : Screen("BlockProfileScreen")
    object VisibilityControlScreen : Screen("VisibilityControlScreen")
    object SettingScreen : Screen("SettingScreen")
    object NotificationScreen : Screen("NotificationScreen")
    object ChatScreenOneToOne : Screen("ChatScreenOneToOne/{id}/{name}/{age}/{image}/{isOnline}/{isActive}/{isDocument}/{isFace}?matchDate={matchDate}") {
        fun passId(
            id: String,
            name: String,
            age: String?=null,
            image: String,
            isOnline: Boolean,
            isActive: Boolean,
            isDocument: Boolean,
            isFace: Boolean,
            matchDate: String?
        ): String {

            val encodedName = Uri.encode(name)
            val encodedAge = Uri.encode(age ?: "")
            val encodedImage = Uri.encode(image)
            val encodedMatchDate = Uri.encode(matchDate ?: "")

            return "ChatScreenOneToOne/$id/$encodedName/$encodedAge/$encodedImage/$isOnline/$isActive/$isDocument/$isFace?matchDate=$encodedMatchDate"
        }
    }
    object TicketScreen : Screen("TicketScreen")
    object TicketDetailsScreen : Screen("TicketDetailsScreen")
    object FaqScreen : Screen("FaqScreen")
    object  HelpScreen: Screen("HelpScreen")
    object  PrivacyAndPolicyScreen : Screen("privacyAndPolicy")
    object SafetyAndSupportScreen : Screen("SafetyAndSupportScreen")
    object TermsAndConditionScreen : Screen("TermsAndConditionScreen")
    object ThemeScreen : Screen("ThemeScreen")




}