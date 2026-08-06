package com.pairlix.dating.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.data.repository.M4Repository
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.InternetConnection
import com.pairlix.dating.requests.ActionRequest
import com.pairlix.dating.response.ActionResponse
import com.pairlix.dating.response.UserActivityResponse
import com.pairlix.dating.view.M4.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.requests.GetMatchFilterRequest
import com.pairlix.dating.requests.UpdateProfileRequest
import com.pairlix.dating.response.ActivePlanResponse
import com.pairlix.dating.response.GetAllCategoriesResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.MatchPopupResponse
import com.pairlix.dating.response.PreviewProfileResponse
import com.pairlix.dating.response.ProfileViewResponse
import com.pairlix.dating.view.newAccountRegistrationScreen.SelectedInterest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.HttpException
import kotlin.collections.orEmpty


@HiltViewModel
class M4ViewModel @Inject constructor(
    private val m4Repository: M4Repository
) : ViewModel() {

    var isChatApiCalledOnce by mutableStateOf(false)
    var isNetworkAvailable = MutableLiveData(true)

    val _isFilterChange =MutableStateFlow<Boolean>(false)
    val isFilterChange:StateFlow<Boolean> = _isFilterChange

    fun updateFilter(b:Boolean){
        _isFilterChange.value=b
    }
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
    var currentRequestType: String = ""
    sealed class ActionResult {
        object Idle : ActionResult()
        data class Success(val action: String) : ActionResult()
        data class Error(val message: String) : ActionResult()
        object LimitReached : ActionResult()
    }

    var currentFilterRequest = mutableStateOf<GetMatchFilterRequest?>(null)
        private set

    fun buildFilterRequest(): GetMatchFilterRequest {
        return GetMatchFilterRequest(
            maxDistance = getMaxDistance(),
            minAge = getMinAge(),
            maxAge = getMaxAge(),
            countryName = getCountryFilter(),
            spokenLanguages = getSpokenLanguagesFilter(),
            planType = getPlanTypeFilter(),
            interestedIn = getInterestedIn(),
            sect = getSect(),
            haveChildren = getChildrenStatus(),
            currentProfession = getProfession(),
            maritalStatus = getMaritalStatus(),
            howOftenDrink = getDrinkStatus(),
            howOftenSmoke = getSmokeStatus(),
            interestTags = getInterestTags()
        )
    }



    // In M4ViewModel - replace your current one
    private var lastAppliedFilter: GetMatchFilterRequest? = null
    private var isFirstCheck = true

    private val _isFirstLoading = MutableStateFlow(true)
    val isFirstLoading: StateFlow<Boolean> = _isFirstLoading

    fun resetFilterState() {
        lastAppliedFilter = null
        isFirstCheck = true
    }

    fun getCountryFilter(): String? =
        selectedCountryNamesEn.takeIf { it.isNotEmpty() }
            ?.joinToString(",")


    fun getLanguageIndexList(): List<Int>? =
        tempLanguageIndexes.takeIf { it.isNotEmpty() }

    fun getSpokenLanguagesFilter(): String ? =
        selectedLanguageIndexes
            .takeIf { it.isNotEmpty() }
            ?.joinToString(",")

/*    fun getPlanTypeFilter(): Int? =
        tempPlanTypeIndex.value.takeIf { it != -1 }*/
    fun getPlanTypeFilter(): Int? =
        selectedPlanType

    /*  fun getMinAge(): Int? =
        minAge.floatValue.takeIf { it != 18f }?.toInt()

    fun getMaxAge(): Int? =
        maxAge.floatValue.takeIf { it != 80f }?.toInt()
*/
  fun getMinAge(): Int? =
      if (isAgeTouched.value) minAge.floatValue.toInt() else null

    fun getMaxAge(): Int? =
        if (isAgeTouched.value) maxAge.floatValue.toInt() else null

    fun restoreAgeRange() {
        isAgeTouched.value = false
        minAge.floatValue = 18f
        maxAge.floatValue = 80f
    }

    fun restoreDistance() {
        isDistanceTouched.value = false
        distance.floatValue = 5f
    }

    var isDistanceTouched = mutableStateOf(false)

    fun getMaxDistance(): Int? =
        if (isDistanceTouched.value) distance.floatValue.toInt() else null

    fun getInterestedIn(): Int? =
        tempInterestedInIndex.value.takeIf { it != -1 }


    fun getSect(): Int? =
        tempSectIndex.value.takeIf { it != -1 }
    fun getProfession(): Int? =
        tempProfessionIndex.value.takeIf { it != -1 }

    fun getMaritalStatus(): Int? =
        tempMartialStatusIndex.value.takeIf { it != -1 }

    fun getDrinkStatus(): Int? =
        tempDrinkStatusIndex.value.takeIf { it != -1 }

    fun getChildrenStatus(): Int? =
        tempChildrenIndex.value.takeIf { it != -1 }

    fun getInterestTags(): String? =
        selectedInterestNames.value.takeIf { it.isNotEmpty() }
            ?.joinToString(",")

    fun getSmokeStatus(): Int? =
        tempSmokeStatusIndex.value.takeIf { it != -1 }

    var actionData = mutableStateOf<ActionResponse.Data?>(null)

    var actionEventId = mutableIntStateOf(0)

    fun setActionData(data: ActionResponse.Data?) {
        actionData.value = data
        actionEventId.intValue++
    }

    fun clearActionData() {
        actionData.value = null
    }

    val categoryBackup = mutableStateListOf<GetAllCategoriesResponse.Data?>()


    var showBottomActions = 4   // 4-> homePage ,0->like ,1-> like sent , 2-> matches, 3-> rejected 5-> blocked profile

    var selectedChipIndex =  mutableStateOf(0)
    var selectedMainScreenIndex =  mutableStateOf(0)


    val getUserActivityList = mutableStateListOf<GetMatchResponse.Data?>()

    private val _getUserLike = MutableStateFlow<List<GetMatchResponse.Data?>>(emptyList())
    var getUserLike: StateFlow<List<GetMatchResponse.Data?>> = _getUserLike

    private val _likeSent = MutableStateFlow<List<GetMatchResponse.Data?>>(emptyList())
    var likeSent: StateFlow<List<GetMatchResponse.Data?>> = _likeSent



    private val _matches = MutableStateFlow<List<GetMatchResponse.Data?>>(emptyList())
    var matches: StateFlow<List<GetMatchResponse.Data?>> = _matches

    private val _rejected = MutableStateFlow<List<GetMatchResponse.Data?>>(emptyList())
    var rejected: StateFlow<List<GetMatchResponse.Data?>> = _rejected

    fun updateData(data:List<GetMatchResponse.Data?>){
        when(selectedChipIndex.value){
            0->_getUserLike.value=data
            1->_likeSent.value=data
            2->_matches.value=data
            3->_rejected.value=data
        }
        _isFirstLoading.value = false

    }
    fun getList():List<GetMatchResponse.Data?>{
        when(selectedChipIndex.value){
            0->return _getUserLike.value
            1->return _likeSent.value
            2->return _matches.value
            else->return  _rejected.value
        }
    }

    fun checkEmpty():Boolean{
        return when(selectedChipIndex.value){
            0-> _getUserLike.value.isEmpty()
            1-> _likeSent.value.isEmpty()
            2-> _matches.value.isEmpty()
            else-> _rejected.value.isEmpty()
        }

    }


    var selectedActivity by mutableStateOf<UserActivityResponse.Data?>(null)
        private set

    fun selectActivity(item: UserActivityResponse.Data?) {
        selectedActivity = item
    }

    var popUpDataList = mutableStateListOf<MatchPopupResponse.Data?>(null)


    var distance = mutableFloatStateOf(5f)
        private set

    var isAgeTouched = mutableStateOf(false)

    var minAge = mutableFloatStateOf(18f)
        private set

    var maxAge = mutableFloatStateOf(80f)

        private set

    fun updateDistance(value: Float) {
        isDistanceTouched.value = true
        distance.floatValue = value
    }

    fun updateAgeRange(start: Float, end: Float) {
        isAgeTouched.value = true
        minAge.floatValue = start
        maxAge.floatValue = end
    }





    fun removeInterestByName(tagName: String) {

        val updatedList = tempInterestIds.value.mapNotNull { selectedInterest ->

            val category = categoryBackup.find {
                it?.id == selectedInterest.categoryId
            }

            val language = SharedPreference.get(MyApplication.appContext).language

            val tagIdToRemove = category?.tags
                ?.find {
                    if (language == "ar") {
                        it?.tagName?.ar == tagName
                    } else {
                        it?.tagName?.en == tagName
                    }
                }
                ?.id


            if (tagIdToRemove == null) {
                return@mapNotNull selectedInterest
            }

            val updatedTagIds =
                selectedInterest.tagIds.filterNot { it == tagIdToRemove }

            if (updatedTagIds.isEmpty()) {
                null
            } else {
                selectedInterest.copy(tagIds = updatedTagIds)
            }
        }

        // 🔥 UPDATE SOURCE OF TRUTH
        tempInterestIds.value = updatedList

        // 🔥🔥 THIS WAS MISSING
        syncInterestNames()
    }


    var tempPlanTypeIndex = mutableStateOf(-1)
    private  set

    var selectedPlanType by mutableStateOf<Int?>(null)
        private set

    fun commitPlanType() {
        selectedPlanType = tempPlanTypeIndex.value.takeIf { it != -1 }
    }

    fun restorePlanType() {
        tempPlanTypeIndex.value = selectedPlanType ?: -1
    }


    fun onPlanTypeChange(index:Int){
        tempPlanTypeIndex.value=index
    }

    var tempInterestedInIndex = mutableStateOf(-1)
    private  set
    fun onInterestedInChange(index:Int){
        tempInterestedInIndex.value=index
    }

    var tempSectIndex = mutableStateOf(-1)
    private  set
    fun onSectChange(index:Int){
        tempSectIndex.value=index
    }

    var tempProfessionIndex = mutableStateOf(-1)
    private  set
    fun onProfessionChange(index:Int){
        tempProfessionIndex.value=index
    }
    var tempChildrenIndex = mutableStateOf(-1)
    private  set
    fun onChildrenChange(index:Int){
        tempChildrenIndex.value=index
    }

    var tempMartialStatusIndex = mutableStateOf(-1)
    private  set
    fun onMartialStatusChange(index:Int){
        tempMartialStatusIndex.value=index
    }

    var tempDrinkStatusIndex = mutableStateOf(-1)
    private  set
    fun onDrinkStatusChange(index:Int){
        tempDrinkStatusIndex.value=index
    }


    var tempSmokeStatusIndex = mutableStateOf(-1)
    private  set
    fun onSmokeStatusChange(index:Int){
        tempSmokeStatusIndex.value=index
    }

    var selectedLanguageIndexes by mutableStateOf<List<Int>>(emptyList())
        private set

    var tempLanguageIndexes = mutableStateListOf<Int>()
        private set

    fun onLanguageIndexToggle(index: Int) {
        if (tempLanguageIndexes.contains(index)) {
            tempLanguageIndexes.remove(index)
        } else {
            tempLanguageIndexes.add(index)
        }
    }

    fun commitLanguageSelection() {
        selectedLanguageIndexes = tempLanguageIndexes.toList()
    }

    fun restoreLanguageSelection() {
        tempLanguageIndexes.clear()
        tempLanguageIndexes.addAll(selectedLanguageIndexes)
    }

    fun commitCountry(allCountries: List<Country>) {
        selectedCountryNamesEn = tempCountryIndexes.map { index ->
            allCountries.getOrNull(index)?.nameEn ?: ""
        }.filter { it.isNotEmpty() }

        selectedCountryNamesAr = tempCountryIndexes.map { index ->
            allCountries.getOrNull(index)?.nameAr ?: ""
        }.filter { it.isNotEmpty() }
    }

    fun restoreCountry(allCountries: List<Country>) {
        tempCountryIndexes.clear()
        tempCountryIndexes.addAll(
            selectedCountryNamesEn.mapNotNull { nameEn ->
                allCountries.indexOfFirst { it.nameEn == nameEn }
                    .takeIf { it >= 0 }
            }
        )
    }




    /*fun onLanguageIndexToggle(index: Int) {
        if (tempLanguageIndexes.contains(index)) {
            tempLanguageIndexes.remove(index)
        } else {
            if (tempLanguageIndexes.size >= 10) return
            tempLanguageIndexes.add(index)
        }
    }*/

    var tempCountryIndexes = mutableStateListOf<Int>()
        private set
    var tempCountryNames = mutableStateListOf<String>()
        private set


    var selectedCountryNamesEn by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedCountryNamesAr by mutableStateOf<List<String>>(emptyList())
        private set
    /*init {
        selectedCountryNames = listOf("India")
        tempCountryNames.clear()
        tempCountryNames.addAll(selectedCountryNames)
    }*/



    fun onCountryIndexToggle(index: Int) {
        if (tempCountryIndexes.contains(index)) {
            tempCountryIndexes.remove(index)
        } else {
            tempCountryIndexes.add(index)
        }
    }

    fun updateSelectedCountries(allCountries: List<Country>) {
        selectedCountryNamesEn = tempCountryIndexes.map { index ->
            allCountries.getOrNull(index)?.nameEn ?: ""
        }.filter { it.isNotEmpty() }

        selectedCountryNamesAr = tempCountryIndexes.map { index ->
            allCountries.getOrNull(index)?.nameAr ?: ""
        }.filter { it.isNotEmpty() }
    }



    fun setCategories(list: List<GetAllCategoriesResponse.Data?>) {
        categoryBackup.clear()
        categoryBackup.addAll(list)
        syncInterestNames()
    }

    var selectedInterestedIn by mutableStateOf<Int?>(null)
        private set

    fun commitInterestedIn() {
        selectedInterestedIn = tempInterestedInIndex.value.takeIf { it != -1 }
    }

    fun restoreInterestedIn() {
        tempInterestedInIndex.value = selectedInterestedIn ?: -1
    }


    fun saveInterestState() {
        savedInterestIds = tempInterestIds.value
    }

    fun restoreInterestState() {
        tempInterestIds.value = savedInterestIds.map { it.copy() } // 🔥 NEW LIST
        syncInterestNames()
    }

    fun submitInterestUpdate(context: Context) {
        saveInterestState() // ✅ commit changes
    }

    private fun syncInterestNames() {

        val language = SharedPreference.get(MyApplication.appContext).language

        val names = tempInterestIds.value.flatMap { selected ->

            val category = categoryBackup
                .find { it?.id == selected.categoryId }

            selected.tagIds.mapNotNull { tagId ->

                val tag = category?.tags
                    ?.find { it?.id == tagId }

                if (language == "ar") {
                    tag?.tagName?.ar
                } else {
                    tag?.tagName?.en
                }
            }
        }

        selectedInterestNames.value = names.distinct()
    }

    var selectedSect by mutableStateOf<Int?>(null)
        private set

    fun commitSect() {
        selectedSect = tempSectIndex.value.takeIf { it != -1 }
    }

    fun restoreSect() {
        tempSectIndex.value = selectedSect ?: -1
    }

    var selectedProfession by mutableStateOf<Int?>(null)
        private set

    fun commitProfession() {
        selectedProfession = tempProfessionIndex.value.takeIf { it != -1 }
    }

    fun restoreProfession() {
        tempProfessionIndex.value = selectedProfession ?: -1
    }

  var selectedChildren by mutableStateOf<Int?>(null)
        private set

    fun commitChildren() {
        selectedChildren = tempChildrenIndex.value.takeIf { it != -1 }
    }

    fun restoreChildren() {
        tempChildrenIndex.value = selectedChildren ?: -1
    }


    var selectedMaritalStatus by mutableStateOf<Int?>(null)
        private set

    fun commitMaritalStatus() {
        selectedMaritalStatus = tempMartialStatusIndex.value.takeIf { it != -1 }
    }

    fun restoreMaritalStatus() {
        tempMartialStatusIndex.value = selectedMaritalStatus ?: -1
    }



    var selectedDrinkStatus by mutableStateOf<Int?>(null)
        private set

    fun commitDrinkStatus() {
        selectedDrinkStatus = tempDrinkStatusIndex.value.takeIf { it != -1 }
    }

    fun restoreDrinkStatus() {
        tempDrinkStatusIndex.value = selectedDrinkStatus ?: -1
    }


    var selectedSmokeStatus by mutableStateOf<Int?>(null)
        private set

    fun commitSmokeStatus() {
        selectedSmokeStatus = tempSmokeStatusIndex.value.takeIf { it != -1 }
    }

    fun restoreSmokeStatus() {
        tempSmokeStatusIndex.value = selectedSmokeStatus ?: -1
    }

    fun restoreCountrySelection(allCountries: List<Country>) {
        tempCountryIndexes.clear()
        tempCountryIndexes.addAll(
            selectedCountryNamesEn.mapNotNull { nameEn ->
                allCountries.indexOfFirst { it.nameEn == nameEn }
                    .takeIf { it >= 0 }
            }
        )
    }


    // SAVED
    var interestIds = mutableStateOf<List<SelectedInterest>>(emptyList())
        private set

    // TEMP
    var tempInterestIds = mutableStateOf<List<SelectedInterest>>(emptyList())
        private set

    private var isInterestInitialized = false

    fun onInterestSelectionChanged(newList: List<SelectedInterest>) {
        tempInterestIds.value = newList
        syncInterestNames()
    }


    private var savedInterestIds: List<SelectedInterest> = emptyList()
    val selectedInterestNames = mutableStateOf<List<String>>(emptyList())



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


        interestIds.value = tempInterestIds.value
        tempInterestIds.value = interestIds.value

        onSuccess()
    }

    // Replace existing _action LiveData with StateFlow for easier Compose observation
    private val _actionResult = MutableStateFlow<ActionResult>(ActionResult.Idle)
    val actionResult: StateFlow<ActionResult> = _actionResult

    // Keep your existing LiveData too if other screens observe it
    private val _action = MutableLiveData<EmpResource<ActionResponse>>()
    val action: LiveData<EmpResource<ActionResponse>>
        get() = _action

    fun hitAction(access_token: String, request: ActionRequest) {
        if (checkInternetConnection()) {
            viewModelScope.launch {
                _action.value = EmpResource.Loading
                _actionResult.value = ActionResult.Idle

                val result = m4Repository.action(access_token, request)
                _action.value = result

                when (result) {
                    is EmpResource.Success -> {
                        _actionResult.value = ActionResult.Success(request.action ?: "")
                    }
                    is EmpResource.Failure -> {
                        val errorBody = (result.throwable as? HttpException)
                            ?.response()
                            ?.errorBody()
                            ?.string()

                        val message = try {
                            JSONObject(errorBody ?: "").optString("message")
                        } catch (e: Exception) {
                            result.throwable.message ?: ""
                        }

                        if (message.contains("limit", ignoreCase = true)) {
                            _actionResult.value = ActionResult.LimitReached
                        } else {
                            _actionResult.value = ActionResult.Error(message)
                        }
                    }                    else -> {
                        _actionResult.value = ActionResult.Error("Unknown error")
                    }
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

    // Call this after dialog is shown to reset state
    fun resetActionResult() {
        _actionResult.value = ActionResult.Idle
    }



 /*   private val _action = MutableLiveData<EmpResource<ActionResponse>>()
    val action: LiveData<EmpResource<ActionResponse>>
        get() = _action

    fun hitAction(access_token: String, request: ActionRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _action.value = EmpResource.Loading
            _action.value = m4Repository.action(access_token, request)
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

    fun clearAllFilters() {

        // ✅ Reset Distance
        isDistanceTouched.value = false
        distance.floatValue = 5f

        // ✅ Reset Age Range
        isAgeTouched.value = false
        minAge.floatValue = 18f
        maxAge.floatValue = 80f

        // ✅ Reset Country
        selectedCountryNamesEn = emptyList()
        selectedCountryNamesAr = emptyList()
        tempCountryIndexes.clear()
        // ✅ Reset Language
        selectedLanguageIndexes = emptyList()
        tempLanguageIndexes.clear()

        // ✅ Reset Plan Type
        selectedPlanType = null
        tempPlanTypeIndex.value = -1

        // ✅ Reset Interested In
        selectedInterestedIn = null
        tempInterestedInIndex.value = -1

        // ✅ Reset Sect
        selectedSect = null
        tempSectIndex.value = -1

        // ✅ Reset Profession
        selectedProfession = null
        tempProfessionIndex.value = -1

        // ✅ Reset Children
        selectedChildren = null
        tempChildrenIndex.value = -1

        // ✅ Reset Marital Status
        selectedMaritalStatus = null
        tempMartialStatusIndex.value = -1

        // ✅ Reset Drinking
        selectedDrinkStatus = null
        tempDrinkStatusIndex.value = -1

        // ✅ Reset Smoking
        selectedSmokeStatus = null
        tempSmokeStatusIndex.value = -1

        // ✅ Reset Interests
        interestIds.value = emptyList()
        tempInterestIds.value = emptyList()
        selectedInterestNames.value = emptyList()

        // ✅ Clear Request
        currentFilterRequest.value = null
    }

    fun isFilterApplied(): Boolean {

        // Distance
        if (distance.floatValue > 5f) return true

        // Age
        if (minAge.floatValue > 18f || maxAge.floatValue < 80f) return true

        // Country
        if (selectedCountryNamesEn.isNotEmpty() || selectedCountryNamesAr.isNotEmpty()) return true

        // Language
        if (selectedLanguageIndexes.isNotEmpty()) return true

        // Plan Type
        if (selectedPlanType != null) return true

        // Interested In
        if (selectedInterestedIn != null) return true

        // Sect
        if (selectedSect != null) return true

        // Profession
        if (selectedProfession != null) return true

        // Children
        if (selectedChildren != null) return true

        // Marital Status
        if (selectedMaritalStatus != null) return true

        // Drinking
        if (selectedDrinkStatus != null) return true

        // Smoking
        if (selectedSmokeStatus != null) return true

        // Interests
        if (interestIds.value.isNotEmpty()) return true

        return false
    }
    private val _getUserActivity = MutableLiveData<EmpResource<GetMatchResponse>>()
    val getUserActivity: LiveData<EmpResource<GetMatchResponse>>
          get() = _getUserActivity


    fun resetAllList() {
        _getUserLike.value = emptyList()
        _likeSent.value = emptyList()
        _matches.value = emptyList()
        _rejected.value = emptyList()
    }


    fun reset(){
        _getUserActivity.value=EmpResource.Idle
    }
    fun hitUserActivity(access_token: String,actionType:String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getUserActivity.value = EmpResource.Loading
            _getUserActivity.value = m4Repository.getUserActivity(access_token, actionType)
        }

        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val _matchPopup = MutableLiveData<EmpResource<MatchPopupResponse>>()
    val matchPopup: LiveData<EmpResource<MatchPopupResponse>>
        get() = _matchPopup

    fun hitMatchPopUp(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _matchPopup.value = EmpResource.Loading
            _matchPopup.value = m4Repository.matchPopup(access_token)
        }

        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }



}