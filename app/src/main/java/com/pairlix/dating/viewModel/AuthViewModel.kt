package com.pairlix.dating.viewModel

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.data.repository.AuthRepository
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.InternetConnection
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.requests.ActionRequest
import com.pairlix.dating.requests.CompareFaceRequest
import com.pairlix.dating.requests.CompleteProfileRequest1
import com.pairlix.dating.requests.CompleteProfileRequest2
import com.pairlix.dating.requests.CompleteProfileRequest3
import com.pairlix.dating.requests.CompleteProfileRequest4
import com.pairlix.dating.requests.CompleteProfileRequest5
import com.pairlix.dating.requests.CompleteProfileRequest6
import com.pairlix.dating.requests.CompleteProfileRequest7
import com.pairlix.dating.requests.CompleteProfileRequest8
import com.pairlix.dating.requests.CompleteProfileRequest9
import com.pairlix.dating.requests.CreateAccountRequest
import com.pairlix.dating.requests.GetMatchFilterRequest
import com.pairlix.dating.requests.LiveNessResultRequest
import com.pairlix.dating.requests.LoginRequest
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.requests.OtpVerifyRequest
import com.pairlix.dating.requests.PurchasedPlanRequest
import com.pairlix.dating.requests.ResendOtpRequest
import com.pairlix.dating.requests.SocialLoginRequest
import com.pairlix.dating.requests.UpdateProfileRequest
import com.pairlix.dating.res.ResendOtpResponse
import com.pairlix.dating.response.ActionResponse
import com.pairlix.dating.response.ActivePlanResponse
import com.pairlix.dating.response.CompareFaceResponse
import com.pairlix.dating.response.CompleteProfileResponse
import com.pairlix.dating.response.CreateAccountResponse
import com.pairlix.dating.response.CreateSessionResponse
import com.pairlix.dating.response.DeleteRecentSearchResponse
import com.pairlix.dating.response.ExtractDocumentDataResponse
import com.pairlix.dating.response.GetAllCategoriesResponse
import com.pairlix.dating.response.GetAllFaithsStep7Response
import com.pairlix.dating.response.GetCountryCodeResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.GetPlansResponse
import com.pairlix.dating.response.HomeProfileResponse
import com.pairlix.dating.response.LiveNessResultResponse
import com.pairlix.dating.response.LoginResponse
import com.pairlix.dating.response.ModerateContentResponse
import com.pairlix.dating.response.OtpResponse
import com.pairlix.dating.response.PreviewProfileResponse
import com.pairlix.dating.response.PurchasedPlanResponse
import com.pairlix.dating.response.RecentSearchHistoryResponse
import com.pairlix.dating.response.RecentSearchResponse
import com.pairlix.dating.response.SocialLoginResponse
import com.pairlix.dating.response.UpdateProfileResponse
import com.pairlix.dating.response.UploadDocumentFileResponse
import com.pairlix.dating.response.UploadImageFileResponse
import com.pairlix.dating.response.UploadMultipleImageResponse
import com.pairlix.dating.response.UserActivityResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.newAccountRegistrationScreen.FaithItem
import com.pairlix.dating.view.newAccountRegistrationScreen.Items
import com.pairlix.dating.view.newAccountRegistrationScreen.SelectedInterest
import com.pairlix.dating.view.plans.getCurrentCountryAndRegion
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    var previousPersonalityBio: String = ""
    fun forceSetPersonalityBio(value: String) {
        personalityBio = value  // ✅ No guard, always updates
    }

    fun resetBioInitFlag() {
        isBioInitialized = false
    }

    var showSplash by   mutableStateOf(true)
    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted = _permissionGranted.asStateFlow()

    private val _latLngFlow = MutableStateFlow<LatLng?>(null)
    val latLngFlow = _latLngFlow.asStateFlow()
    val isGpsEnabled = MutableStateFlow(false)

    var dob = mutableStateOf("")

    var selectedPreviewChip = mutableStateOf(0)
    var editProfileChipIndex = mutableStateOf(0)
    var isMatchApiCalledOnce = false


    enum class UploadSource {
        NONE,
        PROFILE_PIC,
        GRID_IMAGE

    }


    // Store image pages as StateFlow so both screens observe the same state
    private val _imagePages = MutableStateFlow<Map<String, Int>>(emptyMap())
    val imagePages: StateFlow<Map<String, Int>> = _imagePages.asStateFlow()

    fun getImagePage(userId: String): Int {
        return _imagePages.value[userId] ?: 0
    }

    fun updateImagePage(userId: String, page: Int) {
        _imagePages.update { current ->
            current.toMutableMap().apply { put(userId, page) }
        }
    }

    var uploadSource by mutableStateOf(UploadSource.NONE)

    private val _uploadSuccessEvent = MutableStateFlow(false)
    val uploadSuccessEvent = _uploadSuccessEvent

    fun notifyUploadSuccess() {
        _uploadSuccessEvent.value = true
    }


    var socialUniqueId by mutableStateOf<String?>(null)

    fun clearSocialUniqueId() {
        socialUniqueId = null
    }

    data class PendingImage(
        val key: String,
        val url: String,
        val index: Int
    )

    var pendingModerationImage=mutableStateListOf<PendingImage>()

    fun resetModerationState() {
        _moderateContent.value = EmpResource.Idle
    }


    fun resetUploadSuccessEvent() {
        _uploadSuccessEvent.value = false
    }


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun fetchCurrentLocation() {

        val fused = LocationServices.getFusedLocationProviderClient(context)

        fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]) { loc ->
                if (loc != null) handleLocation(loc)
                else {
                    // fallback for real device
                    fused.lastLocation.addOnSuccessListener { last ->
                        if (last != null) handleLocation(last)
                    }
                }
            }
    }

    private fun handleLocation(loc: Location) {
        val latLng = LatLng(loc.latitude, loc.longitude)
        _latLngFlow.value= latLng
    }

    fun setGpsEnabled(enabled: Boolean) {
        isGpsEnabled.value = enabled
    }

    fun setPermissionGranted(v: Boolean) {
        _permissionGranted.value = v
    }
//    private val _registerData = MutableLiveData<EmpResource<RegistractionResponse>>()
//    val registerLiveData: LiveData<EmpResource<RegistractionResponse>>
//        get() = _registerData
//
//    fun hitRegister(model: SignInRequest) {
//        if (checkInternetConnection()) viewModelScope.launch {
//            _registerData.value = EmpResource.Loading
//            _registerData.value = authRepository.hitRegister(model)
//        }
//        else {
//            Toast.makeText(
//                MyApplication.appContext,
//                MyApplication.appContext.getString(R.string.no_network_found),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }\



    var loginMobile: String = ""
    var loginEmail: String = ""
    var countryCode: String = ""
    var uploadedCount = mutableStateOf(0)
    val faithIds = mutableListOf<String>()


    var uploadImagesStep1 = mutableStateListOf<CompleteProfileRequest1.Data?>(null)
    var imageUris = mutableStateListOf<String>()
    var profileImage = mutableStateOf<String?>(null)
    var localProfileImage = mutableStateOf<String?>(null) // LOCAL URI

    var getFaithList = mutableStateListOf<GetAllFaithsStep7Response.Data?>(null)
    var getCategoryList = mutableStateListOf<GetAllCategoriesResponse.Data?>(null)
    var getPlansList = mutableStateListOf<GetPlansResponse.Data?>(null)
    var activePlanData = mutableStateOf<ActivePlanResponse.Data?>(null)
    var getMatchList = mutableStateListOf<GetMatchResponse.Data?>()

    private val _selectedProfile = MutableStateFlow<GetMatchResponse.Data?>(null)
    val selectedProfile: StateFlow<GetMatchResponse.Data?>
        get() = _selectedProfile


    var getHomePageData = mutableStateOf<HomeProfileResponse.Data?>(null)
    var getPreviewProfileData = mutableStateOf<PreviewProfileResponse.Data?>(null)
    var getExtractData = mutableStateOf<ExtractDocumentDataResponse.Data?>(null)
    var getUploadDocumentAws = mutableStateOf<UploadDocumentFileResponse.Data?>(null)
    var categoryBackup = mutableStateListOf<GetAllCategoriesResponse.Data?>()
    val isUploading = MutableStateFlow(false)

    val homeData = getPreviewProfileData.value

    var isMatchApiCalled by mutableStateOf(false)
        private set


   /* fun resetProfileData(){
        _selectedProfile.value=null
    }*/

    fun clearUserSession() {

        /* ================= PROFILE CORE ================= */
        profileImage.value = null
        getPreviewProfileData.value = null
        dob.value = ""

        /* ================= PAGINATION ================= */
        cityList1.clear()
        currentCityPage = 1
        totalCityPages = 1

        /* ================= UPLOAD ================= */
        isUploading.value = false

        /* ================= HEIGHT ================= */
        heightValue.value = ""
        heightUnit.value = "CM"
        tempHeightValue.value = ""
        tempHeightUnit.value = "CM"
        isHeightInitialized = false

        personalityBio = ""
        isBioInitialized = false

        /* ================= FAITH ================= */
        tempFaithIds = emptyList()
        isFaithInitialized = false

        /* ================= INTERESTED IN ================= */
        interestedInIndex.value = 0
        tempInterestedInIndex.value = -1
        isInterestedInInitialized = false

        /* ================= INTEREST ================= */
        interestIds.value = emptyList()
        tempInterestIds.value = emptyList()
        isInterestInitialized = false

        /* ================= LANGUAGE ================= */
        languageIndexes.clear()
        tempLanguageIndexes.clear()
        isLanguageInitialized = false

        /* ================= SECT ================= */
        sectIndex.value = -1
        tempSectIndex.value = -1
        isSectInitialized = false

        /* ================= MARITAL STATUS ================= */
        maritalStatusIndex.value = 0
        tempMaritalStatusIndex.value = -1
        isMaritalInitialized = false

        /* ================= RELIGION PRACTICE ================= */
        religionPracticeIndex.value = 0
        tempReligionPracticeIndex.value = -1
        isReligionPracticeInitialized = false

        /* ================= HALAL FOOD ================= */
        halalFoodIndex.value = 0
        tempHalalFoodIndex.value = -1
        isHalalInitialized = false

        /* ================= RELOCATION ================= */
        relocationIndex.value = 0
        tempRelocationIndex.value = -1
        isRelocationInitialized = false

        /* ================= GLOBAL FLAGS ================= */
        SingletonObject.isFromEditProfile = false
        SingletonObject.isCreateFlowInitialized = false
        SingletonObject.isComeFromRegister = false
        SingletonObject.loginFromEmail = false
        SingletonObject.loginFromMobile = false
    }




    fun setData(data: GetMatchResponse.Data){
        _selectedProfile.value=data
    }

    // Fixed options (order MUST match backend)
    val interestedList = listOf("Men", "Female", "Everyone")

    val spokenLanguagesList = listOf(
        "Afrikaans",                // 0
        "Albanian",                 // 1
        "Amharic",                  // 2
        "Arabic (Modern Standard)", // 3
        "Armenian",                 // 4
        "Assamese",                 // 5
        "Azerbaijani",              // 6

        "Bahasa Indonesia",         // 7
        "Bahasa Melayu (Malay)",    // 8
        "Balochi",                  // 9
        "Bengali",                  // 10
        "Berber (Kabyle)",          // 11
        "Berber (Tamazight)",       // 12
        "Bhojpuri",                 // 13
        "Bosnian",                  // 14
        "Bulgarian",                // 15
        "Burmese (Myanmar)",        // 16

        "Cantonese",                // 17
        "Cebuano",                  // 18
        "Chinese (Mandarin)",       // 19
        "Chittagonian",             // 20
        "Croatian",                 // 21
        "Czech",                    // 22

        "Danish",                   // 23
        "Dari",                     // 24
        "Dutch",                    // 25 ✅ added
        "Dzongkha (Bhutan)",        // 26

        "Egyptian Arabic",          // 27 ✅ added
        "English (Canada)",         // 28
        "English (UK)",             // 29
        "English (US)",             // 30

        "Fijian",                   // 31
        "Filipino (Tagalog)",       // 32
        "Finnish",                  // 33
        "French",                   // 34
        "French (Canada)",          // 35

        "German",                   // 36
        "Greek",                    // 37
        "Guarani",                  // 38
        "Gujarati",                 // 39
        "Gulf Arabic",              // 40

        "Haitian Creole",           // 41
        "Haryanvi",                 // 42
        "Hawaiian",                 // 43 ✅ added
        "Hausa",                    // 44
        "Hebrew",                   // 45
        "Hindi",                    // 46
        "Hokkien",                  // 47
        "Hungarian",                // 48

        "Igbo",                     // 49
        "Irish Gaelic",             // 50
        "Italian",                  // 51

        "Japanese",                 // 52
        "Javanese",                 // 53

        "Kannada",                  // 54
        "Kashmiri",                 // 55
        "Khmer (Cambodian)",        // 56
        "Konkani",                  // 57
        "Korean",                   // 58
        "Kurdish (Badini)",         // 59
        "Kurdish (Kurmanji)",       // 60
        "Kurdish (Sorani)",         // 61
        "Kurdish (Zazaki)",         // 62

        "Lao",                      // 63
        "Levantine Arabic",         // 64

        "Magahi",                   // 65
        "Malagasy",                 // 66
        "Malayalam",                // 67
        "Maori (New Zealand)",      // 68
        "Marathi",                  // 69
        "Mongolian",                // 70

        "Navajo",                   // 71
        "Nepali",                   // 72
        "Norwegian",                // 73

        "Odia",                     // 74

        "Pashto",                   // 75
        "Persian (Farsi)",          // 76
        "Polish",                   // 77
        "Portuguese",               // 78
        "Portuguese (Brazil)",      // 79
        "Punjabi (India)",          // 80
        "Punjabi (Pakistan)",       // 81

        "Quechua",                  // 82
        "Rohingya",                 // 83
        "Romanian",                 // 84
        "Russian",                  // 85

        "Samoan",                   // 86
        "Saraiki",                  // 87
        "Scottish Gaelic",          // 88
        "Serbian",                  // 89
        "Shona",                    // 90
        "Sinhala (Sri Lanka)",      // 91
        "Sindhi",                   // 92
        "Sindhi (India)",           // 93
        "Slovak",                   // 94
        "Somali",                   // 95
        "Spanish",                  // 96
        "Spanish (Latin America)",  // 97
        "Sudanese Arabic",          // 98
        "Sundanese",                // 99
        "Swahili",                  // 100
        "Swedish",                  // 101

        "Tahitian",                 // 102 ✅ added
        "Tamil",                    // 103
        "Telugu",                   // 104
        "Thai",                     // 105
        "Tibetan",                  // 106
        "Tigrinya",                 // 107
        "Tok Pisin (Papuan)",       // 108
        "Tongan",                   // 109
        "Turkish",                  // 110

        "Ukrainian",                // 111
        "Urdu",                     // 112
        "Urdu (India)",             // 113

        "Vietnamese",               // 114

        "Welsh",                    // 115

        "Yemeni Arabic",            // 116
        "Yoruba",                   // 117
        "Zulu",                     // 118
        "Others"                    // 119 ✅ added
    )


    val belongList = listOf(
        "Suni", "Shia",
        // "Deobandi",
        // "Wahabi",
        "Prefer Not To Say", "Other"
    )
    val maritalStatusList = listOf(
        "Never Married", "Divorced", "Widowed", "Separated"
    )


    val religiousPracticeList = listOf(
        "Very Practicing (Perform 5 daily prayers)",
        "Practicing (Try to pray regularly)",
        "Moderately Practicing (Pray occasionally)",
        "Cultural Muslim (Identify as Muslim but not regular in worship)"
    )


    val halalFoodList = listOf(
        "Yes", "No","Prefer not to say"
    )

    val relocationPreferenceList = listOf(
        "Yes. I am open to relocating abroad",
        "No. I prefer staying in my country",
        "Depends on spouse’s situation",
        "Already living abroad"
    )



    fun resetHeightState() {
        heightValue.value = ""
        heightUnit.value = "CM"
        tempHeightValue.value = ""
        tempHeightUnit.value = "CM"
        isHeightInitialized = false   // 🔥 compulsory
    }


    // ===== TEMP FAITH SELECTION =====
    var tempFaithIds by mutableStateOf<List<String>>(emptyList())
        private set


    fun onFaithSelectionChange(updated: List<String>) {
        tempFaithIds = updated
    }

    private var isFaithInitialized = false

    fun setFaithFromApi(faithList: List<PreviewProfileResponse.Data.PersonalDetails.Faith?>?) {
        if (isFaithInitialized) return

        tempFaithIds = faithList
            ?.mapNotNull { it?.id }
            ?: emptyList()

        isFaithInitialized = true
    }




    fun submitFaith(
        context: Context,
        onSuccess: () -> Unit
    ) {

        // ❌ No selection
        if (tempFaithIds.isEmpty()) {
            Toast.makeText(
                context,
                "Please select at least one faith",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // ✅ SANITIZE (remove null / blank / duplicates)
        val safeFaithIds = tempFaithIds
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        if (safeFaithIds.isEmpty()) {
            Toast.makeText(
                context,
                "Invalid faith selection",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    faith = safeFaithIds
                )
            )
        )

        tempFaithIds = safeFaithIds

        onSuccess()
    }


    /* ---------------- INTERESTED IN ---------------- */

    var interestedInIndex = mutableStateOf(0)
        private set

    var tempInterestedInIndex = mutableStateOf(-1)
        private set

    fun prepareInterestedInEdit() {
        tempInterestedInIndex.value = interestedInIndex.value
    }


    private var isInterestedInInitialized = false

    fun setInterestedInFromApi(indexFromApi: Int?) {
        if (isInterestedInInitialized) return

        if (indexFromApi != null && indexFromApi in interestedList.indices) {
            interestedInIndex.value = indexFromApi
            tempInterestedInIndex.value = indexFromApi
        }

        isInterestedInInitialized = true
    }

    fun onInterestedInIndexSelect(index: Int) {
        tempInterestedInIndex.value = index
    }

    fun submitInterestedIn(
        context: Context,
        onSuccess: () -> Unit
    ) {
        val index = tempInterestedInIndex.value
        if (index !in interestedList.indices) return

        // 🔥 FINAL SAVE
        interestedInIndex.value = index

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    interestedIn = index.toString()
                )
            )
        )

        onSuccess()
    }


    // SAVED
    var interestIds = mutableStateOf<List<SelectedInterest>>(emptyList())
        private set

    // TEMP
    var tempInterestIds = mutableStateOf<List<SelectedInterest>>(emptyList())
        private set

    private var isInterestInitialized = false


    fun setInterestsFromApi(
        interests: List<PreviewProfileResponse.Data.PersonalDetails.Interest?>?
    ) {
        if (isInterestInitialized) return
        isInterestInitialized = true

        val mapped = interests
            ?.filterNotNull()
            ?.map { apiInterest ->

                SelectedInterest(
                    categoryId = apiInterest.id.orEmpty(),
                    tagIds = apiInterest.tags
                        ?.filterNotNull()
                        ?.map { it.id.orEmpty() }
                        .orEmpty()
                )
            }
            .orEmpty()
        interestIds.value = mapped
        tempInterestIds.value = mapped
    }


    fun submitInterestUpdate(
        context: Context,
        onSuccess: () -> Unit = {}
    ) {
        if (tempInterestIds.value.isEmpty()) {
            context.showToast("Please select at least one interest")
            return
        }

        val safeInterests: List<UpdateProfileRequest.Interest> =
            tempInterestIds.value.mapNotNull { interest ->

                val categoryId = interest.categoryId?.trim().orEmpty()
                val tagIds = interest.tagIds.filter { it.isNotBlank() }

                if (categoryId.isBlank() || tagIds.isEmpty()) {
                    null
                } else {
                    UpdateProfileRequest.Interest(
                        categoryId = categoryId,
                        tagIds = tagIds
                    )
                }
            }

        if (safeInterests.isEmpty()) {
            context.showToast("Invalid interest selection")
            return
        }

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    interests = safeInterests
                )
            )
        )

        interestIds.value = tempInterestIds.value
        tempInterestIds.value = interestIds.value

        onSuccess()
    }



    /* ---------------- LANGUAGE ---------------- */

    var languageIndexes = mutableStateListOf<Int>()
        private set

    var tempLanguageIndexes = mutableStateListOf<Int>()
        private set

    private var isLanguageInitialized = false

    fun setLanguagesFromApi(apiLanguages: List<String?>?) {
        if (isLanguageInitialized) return

        languageIndexes.clear()
        tempLanguageIndexes.clear()

        apiLanguages?.forEach { apiLang ->

            val index = when {
                apiLang.isNullOrBlank() -> null

                // 🔹 API sent index as string
                apiLang.toIntOrNull() != null -> {
                    val i = apiLang.toInt()
                    i.takeIf { it in spokenLanguagesList.indices }
                }

                // 🔹 API sent language name
                else -> {
                    spokenLanguagesList.indexOfFirst {
                        it.equals(apiLang, true)
                    }.takeIf { it >= 0 }
                }
            }

            index?.let {
                languageIndexes.add(it)
                tempLanguageIndexes.add(it)
            }
        }

        isLanguageInitialized = true
    }



    fun onLanguageIndexToggle(index: Int) {
        if (tempLanguageIndexes.contains(index)) {
            tempLanguageIndexes.remove(index)
        } else {
            if (tempLanguageIndexes.size >= 10) return
            tempLanguageIndexes.add(index)
        }
    }

    fun submitLanguage(context: Context, onSuccess: () -> Unit) {
        val selectedIndexes = tempLanguageIndexes
            .sorted()
            .map { it.toString() }   // ✅ INDEX as STRING

        languageIndexes.clear()
        languageIndexes.addAll(tempLanguageIndexes)

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    spokenLanguages = selectedIndexes
                )
            )
        )

        onSuccess()
    }



    /* ---------------- SECT ---------------- */

    var sectIndex = mutableStateOf(-1)
        private set

    var tempSectIndex = mutableStateOf(-1)
        private set

    private var isSectInitialized = false

    var customSectText by mutableStateOf("")
        private set

    private val OTHER_INDEX get() = belongList.lastIndex   // 🔥 no hardcode 3


    fun setSectFromApi(
        indexFromApi: Int?,
        customTextFromApi: String?
    ) {
        if (isSectInitialized) return

        when {
            // ✅ Normal numeric index from API
            indexFromApi != null && indexFromApi in belongList.indices -> {
                sectIndex.value = indexFromApi
                tempSectIndex.value = indexFromApi

                if (indexFromApi == OTHER_INDEX) {
                    customSectText = customTextFromApi.orEmpty()
                }
            }

            // ✅ API sent custom text directly in sect field
            !customTextFromApi.isNullOrBlank() -> {
                sectIndex.value = OTHER_INDEX
                tempSectIndex.value = OTHER_INDEX
                customSectText = customTextFromApi
            }

            else -> {
                sectIndex.value = -1
                tempSectIndex.value = -1
                customSectText = ""
            }
        }

        isSectInitialized = true
    }


    fun onSectIndexSelect(index: Int) {
        tempSectIndex.value = index

        // 👇 agar Other nahi hai to text clear
        if (index != 3) {
            customSectText = ""
        }
    }



    fun onCustomSectChange(value: String) {
        customSectText = value
    }


    fun submitSect(context: Context, onSuccess: () -> Unit) {
        val index = tempSectIndex.value

        if (index !in belongList.indices) {
            Toast.makeText(context, "Please select sect", Toast.LENGTH_SHORT).show()
            return
        }

        if (index == OTHER_INDEX && customSectText.isBlank()) {
            Toast.makeText(context, "Please enter your sect", Toast.LENGTH_SHORT).show()
            return
        }

        sectIndex.value = index

        // 🔥 SAME LOGIC AS CREATE FLOW
        val sectValue = index.toString()
        val customSectValue =
            if (index == OTHER_INDEX) customSectText.trim() else null

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    sect = sectValue,
                    customSect = customSectValue
                )
            )
        )

        onSuccess()
    }

    /* ---------------- MARITAL STATUS ---------------- */

    var maritalStatusIndex = mutableStateOf(0)
        private set

    var tempMaritalStatusIndex = mutableStateOf(-1)
        private set

    private var isMaritalInitialized = false

    fun setMaritalStatusFromApi(indexFromApi: Int?) {
        if (isMaritalInitialized) return

        if (indexFromApi != null && indexFromApi in maritalStatusList.indices) {
            maritalStatusIndex.value = indexFromApi
            tempMaritalStatusIndex.value = indexFromApi
        }
        isMaritalInitialized = true
    }

    fun onMaritalStatusIndexSelect(index: Int) {
        tempMaritalStatusIndex.value = index
    }

    fun submitMaritalStatus(context: Context, onSuccess: () -> Unit) {
        val index = tempMaritalStatusIndex.value
        if (index !in maritalStatusList.indices) return

        maritalStatusIndex.value = index

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    maritalStatus = index.toString()
                )
            )
        )
        onSuccess()
    }


    /* ---------------- RELIGION PRACTICE ---------------- */

    var religionPracticeIndex = mutableStateOf(0)
        private set

    var tempReligionPracticeIndex = mutableStateOf(-1)
        private set

    private var isReligionPracticeInitialized = false

    fun setReligionPracticeFromApi(indexFromApi: Int?) {
        if (isReligionPracticeInitialized) return

        if (indexFromApi != null && indexFromApi in religiousPracticeList.indices) {
            religionPracticeIndex.value = indexFromApi
            tempReligionPracticeIndex.value = indexFromApi
        }
        isReligionPracticeInitialized = true
    }

    fun onReligionPracticeIndexSelect(index: Int) {
        tempReligionPracticeIndex.value = index
    }

    fun submitReligionPractice(context: Context, onSuccess: () -> Unit) {
        val index = tempReligionPracticeIndex.value
        if (index !in religiousPracticeList.indices) return

        religionPracticeIndex.value = index

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    religionPractice = index.toString()
                )
            )
        )
        onSuccess()
    }




    /* ---------------- HALAL FOOD ---------------- */
    var halalFoodIndex = mutableStateOf(0)
        private set

    var tempHalalFoodIndex = mutableStateOf(-1)
        private set

    private var isHalalInitialized = false

    fun setHalalFoodFromApi(indexFromApi: Int?) {
        if (isHalalInitialized) return

        if (indexFromApi != null && indexFromApi in halalFoodList.indices) {
            halalFoodIndex.value = indexFromApi
            tempHalalFoodIndex.value = indexFromApi
        }
        isHalalInitialized = true
    }

    fun onHalalFoodIndexSelect(index: Int) {
        tempHalalFoodIndex.value = index
    }

    fun submitHalalFood(context: Context, onSuccess: () -> Unit) {
        val index = tempHalalFoodIndex.value
        if (index !in halalFoodList.indices) return

        halalFoodIndex.value = index

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    haveChildren = index.toString()
                )
            )
        )
        onSuccess()
    }



    /* ---------------- RELOCATION ---------------- */
    var relocationIndex = mutableStateOf(0)
        private set

    var tempRelocationIndex = mutableStateOf(-1)
        private set

    private var isRelocationInitialized = false

    fun setRelocationFromApi(indexFromApi: Int?) {
        if (isRelocationInitialized) return

        if (indexFromApi != null && indexFromApi in relocationPreferenceList.indices) {
            relocationIndex.value = indexFromApi
            tempRelocationIndex.value = indexFromApi
        }
        isRelocationInitialized = true
    }

    fun onRelocationIndexSelect(index: Int) {
        tempRelocationIndex.value = index
    }

    fun submitRelocation(context: Context, onSuccess: () -> Unit) {
        val index = tempRelocationIndex.value
        if (index !in relocationPreferenceList.indices) return

        relocationIndex.value = index

        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    aboardAfterMarriage = index.toString()
                )
            )
        )
        onSuccess()
    }



    var heightValue = mutableStateOf("")
        private set

    var heightUnit = mutableStateOf("CM")
        private set


    var tempHeightValue = mutableStateOf("")
        private set

    var tempHeightUnit = mutableStateOf("CM")
        private set


    private var isHeightInitialized = false

    fun setHeightFromApi(
        value: String?,
        unit: String?
    ) {
        if (!isHeightInitialized) {

            val uiUnit = when (unit) {
                "0" -> "CM"
                "1" -> "FT"
                else -> "CM"
            }

            // 🔥 FINAL VALUES
            heightValue.value = value.orEmpty()
            heightUnit.value = uiUnit

            // 🔥 TEMP VALUES (MOST IMPORTANT)
            tempHeightValue.value = value.orEmpty()
            tempHeightUnit.value = uiUnit

            isHeightInitialized = true
        }
    }







    fun onHeightUnitChange(unit: String) {
        tempHeightUnit.value = unit
        tempHeightValue.value = ""   // ✅ expected behaviour
    }

    fun onHeightChange(newValue: String) {

        // ❌ block negative
        if (newValue.contains("-")) return

        // ================= CM MODE =================
        if (tempHeightUnit.value == "CM") {

            var cleaned = newValue.filter { it.isDigit() }

            if (cleaned.startsWith("0")) {
                cleaned = cleaned.dropWhile { it == '0' }
            }

            if (cleaned.isNotEmpty()) {
                val cm = cleaned.toIntOrNull() ?: return
                if (cm > 305) return
            }

            tempHeightValue.value = cleaned
            return
        }

        // ================= FT MODE =================
        if (tempHeightUnit.value == "FT") {

            if (newValue.isEmpty()) {
                tempHeightValue.value = ""
                return
            }

            val allowed = "0123456789."
            val cleaned = newValue.filter { it in allowed }

            if (cleaned.startsWith(".")) return
            if (cleaned.count { it == '.' } > 1) return
            if (cleaned.startsWith("0")) return

            val parts = cleaned.split(".")
            val feet = parts[0].toIntOrNull() ?: return
            if (feet > 10) return
            if (feet == 10 && cleaned.contains(".")) return

            if (parts.size == 2 && parts[1].isNotEmpty() && feet < 10) {
                val inch = parts[1].toIntOrNull() ?: return
                if (inch > 12) return
            }

            tempHeightValue.value = cleaned
        }
    }

    fun submitHeight(context: Context, onSuccess: () -> Unit) {

        if (tempHeightValue.value.isBlank()) {
            Toast.makeText(context, "Please enter height", Toast.LENGTH_SHORT).show()
            return
        }

        heightValue.value = tempHeightValue.value
        heightUnit.value = tempHeightUnit.value

        onSuccess()
    }



    var personalityBio by mutableStateOf("")
        private set

    private var isBioInitialized = false

    fun setPersonalityBioFromApi(value: String?) {
        if (!isBioInitialized) {
            personalityBio = value.orEmpty()
            isBioInitialized = true
        }
    }

    fun onPersonalityBioChange(value: String) {
        if (value.length <= 300) {
            personalityBio = value
        }
    }

    // FINAL (saved)
    // FINAL (saved)
    var drinkIndex = mutableStateOf(-1)
        private set

    var smokeIndex = mutableStateOf(-1)
        private set

    var workoutIndex = mutableStateOf(-1)
        private set

    // TEMP (editing)
    var tempDrinkIndex = mutableStateOf(-1)
        private set

    var tempSmokeIndex = mutableStateOf(-1)
        private set

    var tempWorkoutIndex = mutableStateOf(-1)
        private set

    private var isHabitsInitialized = false
    fun resolveIndex(
        apiValue: String?,
        options: List<String>
    ): Int {
        if (apiValue.isNullOrBlank()) return -1

        // Case A: API already index bhejta hai
        apiValue.toIntOrNull()?.let { idx ->
            if (idx in options.indices) return idx
        }

        // Case B: API name bhejta hai
        val nameIndex =
            options.indexOfFirst { it.equals(apiValue, true) }

        return if (nameIndex >= 0) nameIndex else -1
    }

    // In AuthViewModel
    fun resetMatchState() {
        isMatchApiCalledOnce = false
        getMatchList.clear()
    }
    fun setHabitsFromApi(
        drink: String?,
        smoke: String?,
        workout: String?
    ) {
        if (isHabitsInitialized) return

        val drinkingFrequencyList = listOf("Never", "Occasional", "Regular")
        val smokingFrequencyList = listOf(
            "Casual",
            "Smoker",
            "Trying to Quit",
            "Smoking when Drinking",
            "Never"
        )
        val workoutFrequencyList = listOf(
            "Everyday",
            "Often",
            "Sometimes",
            "Never"
        )

        val drinkIdx = resolveIndex(drink, drinkingFrequencyList)
        val smokeIdx = resolveIndex(smoke, smokingFrequencyList)
        val workoutIdx = resolveIndex(workout, workoutFrequencyList)

        drinkIndex.value = drinkIdx
        tempDrinkIndex.value = drinkIdx

        smokeIndex.value = smokeIdx
        tempSmokeIndex.value = smokeIdx

        workoutIndex.value = workoutIdx
        tempWorkoutIndex.value = workoutIdx

        isHabitsInitialized = true
    }


    fun onDrinkIndexChange(index: Int) {
        tempDrinkIndex.value = index
    }

    fun onSmokeIndexChange(index: Int) {
        tempSmokeIndex.value = index
    }

    fun onWorkoutIndexChange(index: Int) {
        tempWorkoutIndex.value = index
    }
    fun submitHabits(context: Context, onSuccess: () -> Unit) {

        if (
            tempDrinkIndex.value < 0 ||
            tempSmokeIndex.value < 0 ||
            tempWorkoutIndex.value < 0
        ) return

        drinkIndex.value = tempDrinkIndex.value
        smokeIndex.value = tempSmokeIndex.value
        workoutIndex.value = tempWorkoutIndex.value



        hitUpdateProfile(
            access_token = SharedPreference.get(context).accessToken,
            request = UpdateProfileRequest(
                personalDetails = UpdateProfileRequest.PersonalDetails(
                    howOftenDrink = drinkIndex.value.toString(),
                    howOftenSmoke = smokeIndex.value.toString(),
                    workOut = workoutIndex.value.toString()
                )
            )
        )

        onSuccess()
    }




    private val _profileUrl = MutableStateFlow<String?>(null)
    val profileUrl: StateFlow<String?> = _profileUrl

    fun setProfileImage(url: String) {
        _profileUrl.value = url
    }

    var isNetworkAvailable = MutableLiveData(true)



    fun checkInternetConnection(): Boolean {
        return if (InternetConnection.checkConnection(MyApplication.appContext)) {
            true
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                isNetworkAvailable.value = false
            }
            false
        }
    }

    class LocationViewModel : ViewModel() {

        var country: String = "Unknown"
        var region: String = "International"

        fun loadLocation(context: Context, onComplete: () -> Unit = {}) {
            getCurrentCountryAndRegion(context) { c, r ->
                country = c
                region = r
                onComplete()
            }
        }

    }


    private val _getLoginData = MutableLiveData<EmpResource<LoginResponse>>()
    val getLoginData: LiveData<EmpResource<LoginResponse>>
        get() = _getLoginData

    fun hitLogin(model: LoginRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getLoginData.value = EmpResource.Loading
            _getLoginData.value = authRepository.hitLogin(model)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _getSocialLoginData = MutableStateFlow<EmpResource<SocialLoginResponse>>(EmpResource.Idle)
    val getSocialLoginData: StateFlow<EmpResource<SocialLoginResponse>>
        get() = _getSocialLoginData


    fun resetSocialLogin() {
        _getSocialLoginData.value = EmpResource.Idle
    }

    fun hitSocialLogin(model: SocialLoginRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getSocialLoginData.value = EmpResource.Loading
            _getSocialLoginData.value = authRepository.hitSocialLogin(model)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _getVerifyOtp = MutableLiveData<EmpResource<OtpResponse>>()
    val getVerifyOtp: LiveData<EmpResource<OtpResponse>>
        get() = _getVerifyOtp

    fun hitVerifyOtp(token: String, model: OtpVerifyRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getVerifyOtp.value = EmpResource.Loading
            _getVerifyOtp.value = authRepository.hitVerifyOtp(token, model)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _createAccount = MutableLiveData<EmpResource<CreateAccountResponse>>()
    val createAccount: LiveData<EmpResource<CreateAccountResponse>>
        get() = _createAccount

    fun hitCreateAccount(token: String, model: CreateAccountRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _createAccount.value = EmpResource.Loading
            _createAccount.value = authRepository.hitCreateAccount(token, model)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    var profileImagess by mutableStateOf<List<String?>>(
        listOf()
    )
        internal set
    fun setImagesFromApi(apiImages: List<String?>?) {
        profileImagess = apiImages ?: emptyList()
    }

    // ----------------------------------------------------
    private val _profileImages = MutableStateFlow(List<String?>(9) { null })

    val profileImages: StateFlow<List<String?>> = _profileImages

    fun setProfileImageAt(index: Int, url: String) {
        val list = _profileImages.value.toMutableList()
        list[index] = url
        _profileImages.value = list
    }

//for moderate
    var currentUploadIndex: Int? = null
  //  var uploadedImageUrls = MutableList(9) { "" }
  var uploadedImageUrls = mutableStateListOf<String>().apply { repeat(9) { add("") } }


    private val _uploadImageFile = MutableLiveData<EmpResource<UploadDocumentFileResponse>>()
    val uploadImageFile: LiveData<EmpResource<UploadDocumentFileResponse>>
        get() = _uploadImageFile

    fun clearUpload(){
        _uploadImageFile.value= EmpResource.Idle
    }
    fun resetUploadState() {
        // Assuming _uploadImageFile is a MutableLiveData<EmpResource<YourResponse>>
        _uploadImageFile.value = EmpResource.Idle
    }
    fun uploadImageFile(token: String, uplodFile: List<MultipartBody.Part>) {
        if (checkInternetConnection()) viewModelScope.launch {
            _uploadImageFile.value = EmpResource.Loading
            _uploadImageFile.value = authRepository.uploadImageFile(token, uplodFile)
        }

        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    private val _uploadMultipleImage = MutableLiveData<EmpResource<UploadMultipleImageResponse>>()
    val uploadImageMultiple: LiveData<EmpResource<UploadMultipleImageResponse>>
        get() = _uploadMultipleImage

    fun uploadImageMultiple(token: String, uploadFiles: List<MultipartBody.Part>) {
        if (checkInternetConnection()) viewModelScope.launch {
            _uploadMultipleImage.value = EmpResource.Loading
            _uploadMultipleImage.value = authRepository.uploadMultipleImage(token, uploadFiles)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

  /*  private val _uploadDocumentAws = MutableLiveData<EmpResource<UploadDocumentFileResponse>>(EmpResource.Idle)

    val uploadDocumentAws: MutableLiveData<EmpResource<UploadDocumentFileResponse>>
        get() = _uploadDocumentAws

    fun hitUploadDocumentAws(token: String,uplodFile: MultipartBody.Part) {
        if (checkInternetConnection()) viewModelScope.launch {
            _uploadDocumentAws.value = EmpResource.Loading
            _uploadDocumentAws.value = authRepository.uploadDocumentAws(token, uplodFile)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
*/

  private val _extractDocumentData = MutableLiveData<EmpResource<ExtractDocumentDataResponse>>()
    val extractDocumentData: LiveData<EmpResource<ExtractDocumentDataResponse>>
        get() = _extractDocumentData

    fun hitExtractDocumentData(token: String,uplodFile: MultipartBody.Part) {
        if (checkInternetConnection()) viewModelScope.launch {
            _extractDocumentData.value = EmpResource.Loading
            _extractDocumentData.value = authRepository.extractDocumentData(token, uplodFile)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    private val _resendOtp = MutableLiveData<EmpResource<ResendOtpResponse>>()
    val resendOtp: LiveData<EmpResource<ResendOtpResponse>>
        get() = _resendOtp

    fun hitResendOtp(access_token: String, request: ResendOtpRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _resendOtp.value = EmpResource.Loading
            _resendOtp.value = authRepository.hitResendOtp(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _completeProfile = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile

    fun hitCompleteProfile1(access_token: String, request: CompleteProfileRequest1) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile.value = EmpResource.Loading
            _completeProfile.value = authRepository.hitCompleteProfile1(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _completeProfile2 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile2: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile2

    fun hitCompleteProfile2(access_token: String, request: CompleteProfileRequest2) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile2.value = EmpResource.Loading
            _completeProfile2.value = authRepository.hitCompleteProfile2(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _completeProfile3 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile3: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile3

    fun hitCompleteProfile3(access_token: String, request: CompleteProfileRequest3) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile3.value = EmpResource.Loading
            _completeProfile3.value = authRepository.hitCompleteProfile3(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _completeProfile4 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile4: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile4

    fun hitCompleteProfile4(access_token: String, request: CompleteProfileRequest4) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile4.value = EmpResource.Loading
            _completeProfile4.value = authRepository.hitCompleteProfile4(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _completeProfile5 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile5: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile5

    fun hitCompleteProfile5(access_token: String, request: CompleteProfileRequest5) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile5.value = EmpResource.Loading
            _completeProfile5.value = authRepository.hitCompleteProfile5(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _completeProfile6 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile6: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile6

    fun hitCompleteProfile6(access_token: String, request: CompleteProfileRequest6) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile6.value = EmpResource.Loading
            _completeProfile6.value = authRepository.hitCompleteProfile6(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _completeProfile7 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile7: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile7

    fun hitCompleteProfile7(access_token: String, request: CompleteProfileRequest7) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile7.value = EmpResource.Loading
            _completeProfile7.value = authRepository.hitCompleteProfile7(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _completeProfile8 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile8: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile8

    fun hitCompleteProfile8(access_token: String, request: CompleteProfileRequest8) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile8.value = EmpResource.Loading
            _completeProfile8.value = authRepository.hitCompleteProfile8(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _completeProfile9 = MutableLiveData<EmpResource<CompleteProfileResponse>>()
    val completeProfile9: LiveData<EmpResource<CompleteProfileResponse>>
        get() = _completeProfile9

    fun hitCompleteProfile9(access_token: String, request: CompleteProfileRequest9) {
        if (checkInternetConnection()) viewModelScope.launch {
            _completeProfile9.value = EmpResource.Loading
            _completeProfile9.value = authRepository.hitCompleteProfile9(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _getAllFaithsStep7 = MutableLiveData<EmpResource<GetAllFaithsStep7Response>>()
    val getAllFaithsStep7: LiveData<EmpResource<GetAllFaithsStep7Response>>
        get() = _getAllFaithsStep7

    fun hitGetAllFaithsStep7(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getAllFaithsStep7.value = EmpResource.Loading
            _getAllFaithsStep7.value = authRepository.getAllFaithsStep7(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _getAllCategoriesStep6 = MutableLiveData<EmpResource<GetAllCategoriesResponse>>()
    val getAllCategoriesStep6: LiveData<EmpResource<GetAllCategoriesResponse>>
        get() = _getAllCategoriesStep6

    fun hitGetAllCategoriesStep6(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getAllCategoriesStep6.value = EmpResource.Loading
            _getAllCategoriesStep6.value = authRepository.getAllCategoriesStep6(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    private val _getCityByCountryCode = MutableLiveData<EmpResource<GetCountryCodeResponse>>()
    val getCityByCountryCode: LiveData<EmpResource<GetCountryCodeResponse>>
        get() = _getCityByCountryCode

    // 👇 List used by Compose
    // adjust type to your actual City model type
    val cityList1 = mutableStateListOf<GetCountryCodeResponse.Data.City?>()

    // 👇 paging state
    var currentCityPage = 1


    var totalCityPages = 1
        private set
    val isTranslationPending = mutableStateOf(false)

    fun hitGetCityByCountryCode(
        access: String, country: String, page: Int, limit: Int,lang: String, search: String?
    ) {
        if (checkInternetConnection()) {
            viewModelScope.launch {
                _getCityByCountryCode.value = EmpResource.Loading
                try {
                    _getCityByCountryCode.value =
                        authRepository.getCityByCountryCode(access, country, page, limit,lang, search)
                } catch (e: Exception) {
                    _getCityByCountryCode.value = EmpResource.Failure(e)
                }
            }
        } else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun resetCityPaging() {
        currentCityPage = 1
        totalCityPages = 1
        cityList1.clear()
    }

    fun updateCityPaging(data: GetCountryCodeResponse.Data?) {
        val page = data?.page ?: 1
        val totalPages = data?.totalPages ?: 1
        val cities = data?.cities ?: emptyList()

        if (page == 1) {
            cityList1.clear()
        }
        cityList1.addAll(cities)

        currentCityPage = page + 1
        totalCityPages = totalPages
        isTranslationPending.value = data?.translationPending == true

    }

    private val _getRecentSearchTag = MutableLiveData<EmpResource<RecentSearchResponse>>()
    val getRecentSearchTag: LiveData<EmpResource<RecentSearchResponse>>
        get() = _getRecentSearchTag

    fun hitGetRecentSearchTag(access_token: String, searchText: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getRecentSearchTag.value = EmpResource.Loading
            _getRecentSearchTag.value = authRepository.getRecentSearchTag(access_token, searchText)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _getRecentSearchHistory =
        MutableLiveData<EmpResource<RecentSearchHistoryResponse>>()
    val getRecentSearchHistory: LiveData<EmpResource<RecentSearchHistoryResponse>>
        get() = _getRecentSearchHistory

    fun hitRecentSearchHistory(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getRecentSearchHistory.value = EmpResource.Loading
            _getRecentSearchHistory.value = authRepository.getRecentSearchHistory(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _getDeleteRecentSearch = MutableLiveData<EmpResource<DeleteRecentSearchResponse>>()
    val getDeleteRecentSearch: LiveData<EmpResource<DeleteRecentSearchResponse>>
        get() = _getDeleteRecentSearch


    fun hitDeleteRecentSearch(access_token: String, searchText: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getDeleteRecentSearch.value = EmpResource.Loading
            _getDeleteRecentSearch.value = authRepository.deleteRecentSearch(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _getHomeProfile = MutableLiveData<EmpResource<HomeProfileResponse>>()
    val getHomeProfile: LiveData<EmpResource<HomeProfileResponse>>
        get() = _getHomeProfile


    fun hitGetHomeProfile(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getHomeProfile.value = EmpResource.Loading
            _getHomeProfile.value = authRepository.getHomeProfile(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    private val _getPreviewProfile = MutableLiveData<EmpResource<PreviewProfileResponse>>()
    val getPreviewProfile: LiveData<EmpResource<PreviewProfileResponse>>
        get() = _getPreviewProfile


    fun hitPreviewProfile(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getPreviewProfile.value = EmpResource.Loading
            _getPreviewProfile.value = authRepository.getPreviewProfile(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _getPlans = MutableLiveData<EmpResource<GetPlansResponse>>()
    val getPlans: LiveData<EmpResource<GetPlansResponse>>
        get() = _getPlans


    fun hitGetPlans(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getPlans.value = EmpResource.Loading
            _getPlans.value = authRepository.getPlans(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _purchasePlan = MutableLiveData<EmpResource<PurchasedPlanResponse>>()
    val purchasePlan: LiveData<EmpResource<PurchasedPlanResponse>>
        get() = _purchasePlan


    fun hitPurchasedPlans(access_token: String, request: PurchasedPlanRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _purchasePlan.value = EmpResource.Loading
            _purchasePlan.value = authRepository.purchasePlan(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _getActivePlan = MutableLiveData<EmpResource<ActivePlanResponse>>()
    val getActivePlan: LiveData<EmpResource<ActivePlanResponse>>
        get() = _getActivePlan


    fun hitGetActivePlan(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getActivePlan.value = EmpResource.Loading
            _getActivePlan.value = authRepository.getActivePlan(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _updateProfile = MutableLiveData<EmpResource<UpdateProfileResponse>>()
    val updateProfile: LiveData<EmpResource<UpdateProfileResponse>>
        get() = _updateProfile

    fun hitUpdateProfile(access_token: String, request: UpdateProfileRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _updateProfile.value = EmpResource.Loading
            _updateProfile.value = authRepository.updateProfile(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _getLiveNessResult = MutableStateFlow<EmpResource<LiveNessResultResponse>>(
        EmpResource.Idle)
    val getLiveNessResult: StateFlow<EmpResource<LiveNessResultResponse>>
        get() = _getLiveNessResult

    fun resetLivenessState() {
        _createSession.value = EmpResource.Idle
        _getLiveNessResult.value = EmpResource.Idle
    }

    fun hitLiveNessResult(access_token: String, request: LiveNessResultRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getLiveNessResult.value = EmpResource.Loading
            _getLiveNessResult.value = authRepository.getLiveNessResult(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _compareFace = MutableLiveData<EmpResource<CompareFaceResponse>>()
    val compareFace: LiveData<EmpResource<CompareFaceResponse>>
        get() = _compareFace

    fun hitCompareFace(access_token: String, request: CompareFaceRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _compareFace.value = EmpResource.Loading
            _compareFace.value = authRepository.compareFace(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _createSession = MutableStateFlow<EmpResource<CreateSessionResponse>>(EmpResource.Idle)
    val createSession: StateFlow<EmpResource<CreateSessionResponse>>
        get() = _createSession

    fun hitCreateSession(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _createSession.value = EmpResource.Loading
            _createSession.value = authRepository.createSession(access_token)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    private val _getMatch = MutableLiveData<EmpResource<GetMatchResponse>>()
    val getMatch: LiveData<EmpResource<GetMatchResponse>>
        get() = _getMatch

    fun hitGetMatch(
        accessToken: String,
        filter: GetMatchFilterRequest? = null
    ) {
        if (checkInternetConnection()) {
            viewModelScope.launch {
                _getMatch.value = EmpResource.Loading
                _getMatch.value = authRepository.getMatch(
                    access = accessToken,
                    filter = filter
                )
            }
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val _moderateContent = MutableStateFlow<EmpResource<ModerateContentResponse>>(EmpResource.Idle)
    val moderateContent: StateFlow<EmpResource<ModerateContentResponse>>
        get() = _moderateContent

    fun resetModerateContent(){
        _moderateContent.value= EmpResource.Idle
    }

    fun hitModerateContent(access_token: String,request: ModerateContentRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _moderateContent.value = EmpResource.Loading
            _moderateContent.value = authRepository.moderateContent(access_token,request )
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun resetData(){
        _moderateContent.value=EmpResource.Idle
        _uploadImageFile.value=EmpResource.Idle
    }





}
