package com.pairlix.dating.data.repository

import android.content.Context
import com.pairlix.dating.helper.EmpBaseRepository
import com.pairlix.dating.network.ApiServices
import com.pairlix.dating.requests.BoostProfileRequest
import com.pairlix.dating.requests.VisibilityFilterRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class M6Repository @Inject constructor(
    var apiService: ApiServices, @ApplicationContext context: Context
) : EmpBaseRepository() {


    suspend fun hitVisibilityFilter(access: String,request: VisibilityFilterRequest) = safeApiCall {
        apiService.hitVisibilityFilter(access,request)
    }


  suspend fun hitBoostProfile(access: String,request: BoostProfileRequest) = safeApiCall {
        apiService.hitBoostProfile(access,request)
    }


}


