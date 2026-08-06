package com.pairlix.dating.data.repository

import android.content.Context
import com.pairlix.dating.helper.EmpBaseRepository
import com.pairlix.dating.network.ApiServices
import com.pairlix.dating.requests.ActionRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class M4Repository @Inject constructor(
    var apiService: ApiServices, @ApplicationContext context: Context
) : EmpBaseRepository() {


    suspend fun action(access: String, request: ActionRequest, ) = safeApiCall {
        apiService.action(access, request)
    }

    suspend fun getUserActivity(access: String,actionType: String) = safeApiCall {
        apiService.getUserActivity(access,actionType)
    }

  suspend fun matchPopup(access: String) = safeApiCall {
        apiService.matchPopup(access)
    }




}