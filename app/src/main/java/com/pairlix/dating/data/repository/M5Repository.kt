package com.pairlix.dating.data.repository

import android.content.Context
import com.pairlix.dating.helper.EmpBaseRepository
import com.pairlix.dating.network.ApiServices
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.requests.ProfileViewActionRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class M5Repository @Inject constructor(
var apiService: ApiServices, @ApplicationContext context: Context
) : EmpBaseRepository() {


    suspend fun profileViewList(access: String) = safeApiCall {
        apiService.profileViewList(access)
    }

 suspend fun profileViewAction(access: String,request: ProfileViewActionRequest) = safeApiCall {
        apiService.profileViewAction(access,request)
    }


    suspend fun getChatList(access: String,page: Int,size:Int) = safeApiCall {
        apiService.getChatList(access,page,size)
    }
 suspend fun matchTiming(access: String,userId:String) = safeApiCall {
        apiService.matchTiming(access,userId)
    }

 suspend fun checkAbusiveWord(access: String,word:String) = safeApiCall {
        apiService.checkAbusiveWord(access,word)
    }




}
