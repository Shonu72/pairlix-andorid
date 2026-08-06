package com.pairlix.dating.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.data.repository.M7Repository
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.InternetConnection
import com.pairlix.dating.network.AddTicketRequest
import com.pairlix.dating.requests.CallStatusRequest
import com.pairlix.dating.requests.NotificationSettingRequest
import com.pairlix.dating.requests.UpdateLanguageRequest
import com.pairlix.dating.requests.VisibilityFilterRequest
import com.pairlix.dating.res.CallStatusResponse
import com.pairlix.dating.response.AddTicketResponse
import com.pairlix.dating.response.DeleteAccountResponse
import com.pairlix.dating.response.FaqResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.GetNotificationResponse
import com.pairlix.dating.response.GetTicketResponse
import com.pairlix.dating.response.HelpResponse
import com.pairlix.dating.response.LogoutResponse
import com.pairlix.dating.response.NotificationSettingResponse
import com.pairlix.dating.response.PrivacyResponse
import com.pairlix.dating.response.SafetyAndSupportResponse
import com.pairlix.dating.response.TermsAndConditionResponse
import com.pairlix.dating.response.UpdateLanguageResponse
import com.pairlix.dating.response.VisibilityFilterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



    @HiltViewModel
    class M7ViewModel @Inject constructor(
        private  val m7Repository: M7Repository
    ): ViewModel() {

        private val _selectedTicket = MutableStateFlow<GetTicketResponse.Data?>(null)
        val selectedTicket: StateFlow<GetTicketResponse.Data?>
            get() = _selectedTicket


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

        fun setData(data: GetTicketResponse.Data){
            _selectedTicket.value=data
        }


        private val _addTicket = MutableStateFlow<EmpResource<AddTicketResponse>>(
            EmpResource.Idle)
        val addTicket: StateFlow<EmpResource<AddTicketResponse>>
            get() = _addTicket

        fun resetAddTicket(){
            _addTicket.value= EmpResource.Idle
        }

        fun hitAddTicket(access_token: String,request: AddTicketRequest) {
            if (checkInternetConnection()) viewModelScope.launch {
                _addTicket.value = EmpResource.Loading
                _addTicket.value = m7Repository.addTicket(access_token,request)
            }

            else {
                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        private val _callStatus = MutableStateFlow<EmpResource<CallStatusResponse>>(
            EmpResource.Idle)
        val callStatus: StateFlow<EmpResource<CallStatusResponse>>
            get() = _callStatus

        fun resetCallStatus(){
            _callStatus.value= EmpResource.Idle
        }

        fun hitCallStatus(access_token: String,request: CallStatusRequest) {
            if (checkInternetConnection()) viewModelScope.launch {
                _callStatus.value = EmpResource.Loading
                _callStatus.value = m7Repository.callStatus(access_token,request)
            }

            else {
                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }



           private val _updateLanguage = MutableStateFlow<EmpResource<UpdateLanguageResponse>>(
            EmpResource.Idle)
        val updateLanguage: StateFlow<EmpResource<UpdateLanguageResponse>>
            get() = _updateLanguage

        fun resetUpdateLanguage(){
            _updateLanguage.value= EmpResource.Idle
        }

        fun hitUpdateLanguage(access_token: String,request: UpdateLanguageRequest) {
            if (checkInternetConnection()) viewModelScope.launch {
                _updateLanguage.value = EmpResource.Loading
                _updateLanguage.value = m7Repository.updateLanguage(access_token,request)
            }

            else {
                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }








        private val _getTicket = MutableStateFlow<EmpResource<GetTicketResponse>>(
            EmpResource.Idle)
        val getTicket: StateFlow<EmpResource<GetTicketResponse>>
            get() = _getTicket

        fun resetGetTicket(){
            _getTicket.value= EmpResource.Idle
        }

        fun hitGetTicket(access_token: String,search:String,from:String,to:String,status: Int? = null) {
            if (checkInternetConnection()) viewModelScope.launch {
                _getTicket.value = EmpResource.Loading
                _getTicket.value = m7Repository.getTicket(access_token,search,from,to,status)
            }

            else {
                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        private val _getNotification = MutableStateFlow<EmpResource<GetNotificationResponse>>(
            EmpResource.Idle)
        val getNotification: StateFlow<EmpResource<GetNotificationResponse>>
            get() = _getNotification

        fun resetGetNotification(){
            _getNotification.value= EmpResource.Idle
        }

        fun hitGetNotification(access_token: String) {
            if (checkInternetConnection()) viewModelScope.launch {
                _getNotification.value = EmpResource.Loading
                _getNotification.value = m7Repository.getNotification(access_token)
            }

            else {
                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        private val _getFaq =
            MutableStateFlow<EmpResource<FaqResponse>>(EmpResource.Idle)

        val getFaq: StateFlow<EmpResource<FaqResponse>>
            get() = _getFaq

        fun resetGetFaq() {
            _getFaq.value = EmpResource.Idle
        }

        fun hitGetFaq(token: String, lang: String) {

            if (checkInternetConnection()) viewModelScope.launch {

                _getFaq.value = EmpResource.Loading

                _getFaq.value = m7Repository.getFaq(token, lang)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        private val _getHelp =
            MutableStateFlow<EmpResource<HelpResponse>>(EmpResource.Idle)

        val getHelp: StateFlow<EmpResource<HelpResponse>>
            get() = _getHelp


        fun resetGetHelp() {
            _getHelp.value = EmpResource.Idle
        }

        fun hitGetHelp(token: String, lang: String) {

            if (checkInternetConnection()) viewModelScope.launch {

                _getHelp.value = EmpResource.Loading

                _getHelp.value = m7Repository.getHelp(token, lang)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }


        }

        private val _getSafetySupport =
            MutableStateFlow<EmpResource<SafetyAndSupportResponse>>(EmpResource.Idle)

        val getSafetySupport: StateFlow<EmpResource<SafetyAndSupportResponse>>
            get() = _getSafetySupport
        fun resetGetSafetySupport() {
            _getSafetySupport.value = EmpResource.Idle
        }
        fun hitGetSafetySupport(token: String, lang: String) {

            if (checkInternetConnection()) viewModelScope.launch {

                _getSafetySupport.value = EmpResource.Loading

                _getSafetySupport.value = m7Repository.getSafetyAndSupport(token, lang)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        private val _getPrivacy =
            MutableStateFlow<EmpResource<PrivacyResponse>>(EmpResource.Idle)

        val getPrivacy: StateFlow<EmpResource<PrivacyResponse>>
            get() = _getPrivacy

        fun resetGetPrivacy() {
            _getPrivacy.value = EmpResource.Idle
        }

        fun hitGetPrivacy(token: String, lang: String) {

            if (checkInternetConnection()) viewModelScope.launch {

                _getPrivacy.value = EmpResource.Loading

                _getPrivacy.value = m7Repository.getPrivacy(token, lang)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }


        private val _termAndCondition =
            MutableStateFlow<EmpResource<TermsAndConditionResponse>>(EmpResource.Idle)

        val termAndCondition: StateFlow<EmpResource<TermsAndConditionResponse>>
            get() = _termAndCondition

        fun resetTermAndCondition() {
            _termAndCondition.value = EmpResource.Idle
        }

        fun hitTermAndCondition(token: String, lang: String) {

            if (checkInternetConnection()) viewModelScope.launch {

                _termAndCondition.value = EmpResource.Loading

                _termAndCondition.value = m7Repository.getTermsAndCondition(token, lang)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }




        private val _logout =
            MutableStateFlow<EmpResource<LogoutResponse>>(EmpResource.Idle)

        val logout: StateFlow<EmpResource<LogoutResponse>>
            get() = _logout

        fun resetLogout() {
            _logout.value = EmpResource.Idle
        }

        fun hitLogout(token: String,) {

            if (checkInternetConnection()) viewModelScope.launch {

                _logout.value = EmpResource.Loading

                _logout.value = m7Repository.logout(token)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        private val _deleteAccount =
            MutableStateFlow<EmpResource<DeleteAccountResponse>>(EmpResource.Idle)

        val deleteAccount: StateFlow<EmpResource<DeleteAccountResponse>>
            get() = _deleteAccount

        fun resetDeleteAccount() {
            _deleteAccount.value = EmpResource.Idle
        }

        fun hitDeleteAccount(token: String,) {

            if (checkInternetConnection()) viewModelScope.launch {

                _deleteAccount.value = EmpResource.Loading

                _deleteAccount.value = m7Repository.deleteAccount(token)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }



        private val _notificationSetting =
            MutableStateFlow<EmpResource<NotificationSettingResponse>>(EmpResource.Idle)

        val notificationSetting: StateFlow<EmpResource<NotificationSettingResponse>>
            get() = _notificationSetting

        fun resetNotificationSetting() {
            _notificationSetting.value = EmpResource.Idle
        }

        fun hitNotificationSetting(token: String,request: NotificationSettingRequest) {

            if (checkInternetConnection()) viewModelScope.launch {

                _notificationSetting.value = EmpResource.Loading

                _notificationSetting.value = m7Repository.notificationSetting(token,request)

            } else {

                Toast.makeText(
                    MyApplication.appContext,
                    MyApplication.appContext.getString(R.string.no_network_found),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }




    }