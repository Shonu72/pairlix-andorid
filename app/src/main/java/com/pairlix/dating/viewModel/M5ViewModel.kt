package com.pairlix.dating.viewModel

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.data.repository.M4Repository
import com.pairlix.dating.data.repository.M5Repository
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.InternetConnection
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.requests.ProfileViewActionRequest
import com.pairlix.dating.response.CheckAbusiveWordResponse
import com.pairlix.dating.response.GetChatListResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.MatchTimingData
import com.pairlix.dating.response.MatchTimingResponse
import com.pairlix.dating.response.ModerateContentResponse
import com.pairlix.dating.response.ProfileViewActionResponse
import com.pairlix.dating.response.ProfileViewResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class M5ViewModel @Inject constructor(
    private val m5Repository: M5Repository
) : ViewModel() {

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

       var selectedChipIndex =  mutableStateOf(0)

    private val _profileViewList = MutableStateFlow<EmpResource<GetMatchResponse>>(EmpResource.Idle)
    val profileViewList: StateFlow<EmpResource<GetMatchResponse>>
        get() = _profileViewList

    fun resetProfileViewList(){
        _profileViewList.value= EmpResource.Idle
    }

    fun hitProfileViewList(access_token: String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _profileViewList.value = EmpResource.Loading
            _profileViewList.value = m5Repository.profileViewList(access_token)
        }

        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private val _profileViewAction = MutableStateFlow<EmpResource<ProfileViewActionResponse>>(EmpResource.Idle)
    val profileViewAction: StateFlow<EmpResource<ProfileViewActionResponse>>
        get() = _profileViewAction

    fun hitProfileViewAction(access_token: String,request: ProfileViewActionRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _profileViewAction.value = EmpResource.Loading
            _profileViewAction.value = m5Repository.profileViewAction(access_token,request )
        }

        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


 private val _getChatList = MutableStateFlow<EmpResource<GetChatListResponse>>(EmpResource.Idle)
    val getChatList: StateFlow<EmpResource<GetChatListResponse>>
        get() = _getChatList

    fun resetChatList(){
        _getChatList.value= EmpResource.Idle
    }

    fun hitGetChatList(access_token: String,page:Int,size:Int) {
        if (checkInternetConnection()) viewModelScope.launch {
            _getChatList.value = EmpResource.Loading
            _getChatList.value = m5Repository.getChatList(access_token,page,size )
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

 private val _matchingTiming = MutableStateFlow<EmpResource<MatchTimingResponse>>(EmpResource.Idle)
    val matchingTiming: StateFlow<EmpResource<MatchTimingResponse>>
        get() = _matchingTiming

    fun resetMatchingTiming(){
        _matchingTiming.value= EmpResource.Idle
    }
    fun hitMatchingTiming(access_token: String,userId:String) {
        if (checkInternetConnection()) viewModelScope.launch {
            _matchingTiming.value = EmpResource.Loading
            _matchingTiming.value = m5Repository.matchTiming(access_token,userId )
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }



 private val _checkAbusiveWord = MutableStateFlow<EmpResource<CheckAbusiveWordResponse>>(EmpResource.Idle)
    val checkAbusiveWord: StateFlow<EmpResource<CheckAbusiveWordResponse>>
        get() = _checkAbusiveWord

    private var isCheckingAbusive = false
    var pendingMessageText: String = ""

    fun hitCheckAbusiveWord(access_token: String, word: String) {
        if (isCheckingAbusive) return  // ✅ block double tap

        pendingMessageText = word

        if (checkInternetConnection()) {
            isCheckingAbusive = true
            viewModelScope.launch {
                _checkAbusiveWord.value = EmpResource.Loading
                _checkAbusiveWord.value = m5Repository.checkAbusiveWord(access_token, word)
                isCheckingAbusive = false  // ✅ reset after response
            }
        } else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun resetCheckAbusiveWord() {
        _checkAbusiveWord.value = EmpResource.Idle
        isCheckingAbusive = false  // ✅ also reset here as safety
        pendingMessageText = ""    // ✅ clear pending text on reset
    }





}