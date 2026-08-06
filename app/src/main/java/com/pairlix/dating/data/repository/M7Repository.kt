package com.pairlix.dating.data.repository

import android.content.Context
import com.google.gson.Gson
import com.pairlix.dating.helper.EmpBaseRepository
import com.pairlix.dating.network.AddTicketRequest
import com.pairlix.dating.network.ApiServices
import com.pairlix.dating.requests.CallStatusRequest
import com.pairlix.dating.requests.NotificationSettingRequest
import com.pairlix.dating.requests.UpdateLanguageRequest
import com.pairlix.dating.requests.VisibilityFilterRequest
import com.pairlix.dating.response.FaqResponse
import com.pairlix.dating.response.HelpResponse
import com.pairlix.dating.response.PrivacyResponse
import com.pairlix.dating.response.SafetyAndSupportResponse
import com.pairlix.dating.response.TermsAndConditionResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class M7Repository @Inject constructor(
    var apiService: ApiServices, @ApplicationContext context: Context
) : EmpBaseRepository() {

    private val gson = Gson()

    suspend fun addTicket(access: String, request: AddTicketRequest) = safeApiCall {
        apiService.addTicket(access, request)

    }

    suspend fun callStatus(access: String, request: CallStatusRequest) = safeApiCall {
        apiService.callStatus(access, request)

    }

    suspend fun updateLanguage(access: String, request: UpdateLanguageRequest) = safeApiCall {
        apiService.updateLanguage(access, request)

    }


    suspend fun getTicket(access: String, search: String, from: String, to: String, status: Int? = null) = safeApiCall {
        apiService.getTicket(access, search, from, to,status)

    }

    suspend fun notificationSetting(access: String, request: NotificationSettingRequest) = safeApiCall {
        apiService.notificationSetting(access, request)

    }

    suspend fun getNotification(access: String) = safeApiCall {
        apiService.getNotification(access)
    }

 suspend fun logout(access: String) = safeApiCall {
        apiService.logout(access)
    }
    suspend fun deleteAccount(access: String) = safeApiCall {
        apiService.deleteAccount(access)
    }


    suspend fun <T> getCmsSection(
        token: String,
        section: String,
        lang: String,
        clazz: Class<T>
    ) = safeApiCall {

        val json = apiService.getCms(token, section, lang)

        gson.fromJson(json.toString(), clazz)

    }


    suspend fun getPrivacy(token: String, lang: String) =
        getCmsSection(token, "privacyPolicy", lang, PrivacyResponse::class.java)

    suspend fun getFaq(token: String, lang: String) =
        getCmsSection(token, "faq", lang, FaqResponse::class.java)

    suspend fun getHelp(token: String, lang: String) =
        getCmsSection(token, "help", lang, HelpResponse::class.java)

    suspend fun getSafetyAndSupport(token: String, lang: String) =
        getCmsSection(token, "safetyAndSupport", lang, SafetyAndSupportResponse::class.java)

    suspend fun getTermsAndCondition(token: String, lang: String) =
        getCmsSection(token, "termsAndConditions", lang, TermsAndConditionResponse::class.java)



}