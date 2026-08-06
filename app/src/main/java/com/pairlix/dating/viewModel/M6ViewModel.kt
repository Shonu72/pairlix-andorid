package com.pairlix.dating.viewModel
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.data.repository.M6Repository
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.InternetConnection
import com.pairlix.dating.requests.BoostProfileRequest
import com.pairlix.dating.requests.VisibilityFilterRequest
import com.pairlix.dating.response.BoostProfileResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.VisibilityFilterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class M6ViewModel @Inject constructor(
    private  val m6Repository: M6Repository
): ViewModel() {

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



    private val _visibilityFilter = MutableStateFlow<EmpResource<VisibilityFilterResponse>>(EmpResource.Idle)
    val visibilityFilter: StateFlow<EmpResource<VisibilityFilterResponse>>
        get() = _visibilityFilter

    fun resetVisibilityFilter(){
        _visibilityFilter.value= EmpResource.Idle
    }

    fun hitVisibilityFilter(access_token: String,request: VisibilityFilterRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _visibilityFilter.value = EmpResource.Loading
            _visibilityFilter.value = m6Repository.hitVisibilityFilter(access_token,request)
        }

        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    private val _boostProfile = MutableStateFlow<EmpResource<BoostProfileResponse>>(EmpResource.Idle)
    val boostProfile: StateFlow<EmpResource<BoostProfileResponse>>
        get() = _boostProfile

    fun resetBoostProfile(){
        _boostProfile.value= EmpResource.Idle
    }

    fun hitBoostProfile(access_token: String,request: BoostProfileRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _boostProfile.value = EmpResource.Loading
            _boostProfile.value = m6Repository.hitBoostProfile(access_token,request)
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