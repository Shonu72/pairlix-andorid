package com.pairlix.dating.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pairlix.dating.view.allLoginScreen.GoogleLoginData

object SingletonObject {

    var isComeFromRegister: Boolean = false
    var isComeFromBlockedProfile: Boolean = false
    var isComeFromHomePage: Boolean = false
    var accessToken: String = ""
    var isComeFromChat: Boolean = false
    var isComeFromGoldPlan: Boolean = false
    var isComeFromPlatinumPlan: Boolean = false
    var isSkip: Boolean = false
    var loginFromMobile: Boolean = false
    var isFromProfileView: Boolean = false
    var loginFromEmail: Boolean = false
    var isFromEditProfile: Boolean = false
    var isCreateFlowInitialized = false
    var userActivityId = ""
    var googleLoginData: GoogleLoginData = GoogleLoginData()
    var isGoogleLogin: Boolean = false
    var isComeFromUploadIdPage by mutableStateOf(false)
    var hasPlanPopupShownThisSession = false
    var isFromOwnProfile = false

}
