package com.pairlix.dating.data.repository

import android.content.Context
import com.pairlix.dating.helper.EmpBaseRepository
import com.pairlix.dating.network.ApiServices
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
import com.pairlix.dating.response.GetAllCategoriesResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import javax.inject.Inject

class AuthRepository @Inject constructor(
    var apiService: ApiServices, @ApplicationContext context: Context
) : EmpBaseRepository() {

//    suspend fun hitRegister(model: SignInRequest) = safeApiCall {
//        apiService.hitRegister(model)
//    }


    suspend fun hitLogin(model: LoginRequest) = safeApiCall {
        apiService.hitLogin(model)
    }

  suspend fun hitSocialLogin(model: SocialLoginRequest) = safeApiCall {
        apiService.socialLogin(model)
    }


    suspend fun hitVerifyOtp(access: String, req: OtpVerifyRequest) = safeApiCall {
        apiService.hitVerifyOtp(access, req)
    }

    suspend fun hitCreateAccount(access: String, req: CreateAccountRequest) = safeApiCall {
        apiService.hitCreateAccount(access, req)
    }

   // for moderate
    suspend fun uploadImageFile(access: String, uploadFile: List<MultipartBody.Part>) = safeApiCall {
        apiService.uploadImageFile(
            access,
            upload_file = uploadFile,
        )
    }





    suspend fun uploadMultipleImage(access: String, uploadFiles: List<MultipartBody.Part>) =
        safeApiCall {
            apiService.uploadMultipleImage(access, uploadFiles)
        }


    /*suspend fun uploadDocumentAws(access: String, uploadFile: MultipartBody.Part) = safeApiCall {
        apiService.uploadDocumentAws(
            access,
            upload_file = uploadFile,
        )
    }*/
    suspend fun extractDocumentData(access: String, uploadFile: MultipartBody.Part) = safeApiCall {
        apiService.extractDocumentData(
            access, uploadFile,
        )
    }




    suspend fun hitResendOtp(access: String, request: ResendOtpRequest) = safeApiCall {
        apiService.hitResendOtp(access, request)
    }

    suspend fun hitCompleteProfile1(access: String, request: CompleteProfileRequest1) =
        safeApiCall {
            apiService.hitCompleteProfile1(access, request)
        }

    suspend fun hitCompleteProfile2(access: String, request: CompleteProfileRequest2) =
        safeApiCall {
            apiService.hitCompleteProfile2(access, request)
        }

    suspend fun hitCompleteProfile3(access: String, request: CompleteProfileRequest3) =
        safeApiCall {
            apiService.hitCompleteProfile3(access, request)
        }

    suspend fun hitCompleteProfile4(access: String, request: CompleteProfileRequest4) =
        safeApiCall {
            apiService.hitCompleteProfile4(access, request)
        }

    suspend fun hitCompleteProfile5(access: String, request: CompleteProfileRequest5) =
        safeApiCall {
            apiService.hitCompleteProfile5(access, request)
        }

    suspend fun hitCompleteProfile6(access: String, request: CompleteProfileRequest6) =
        safeApiCall {
            apiService.hitCompleteProfile6(access, request)
        }

    suspend fun getAllCategoriesStep6(access: String) = safeApiCall {
        apiService.getAllCategoriesStep6(access)
    }

    suspend fun hitCompleteProfile7(access: String, request: CompleteProfileRequest7) =
        safeApiCall {
            apiService.hitCompleteProfile7(access, request)
        }

    suspend fun hitCompleteProfile8(access: String, request: CompleteProfileRequest8) =
        safeApiCall {
            apiService.hitCompleteProfile8(access, request)
        }

    suspend fun hitCompleteProfile9(access: String, request: CompleteProfileRequest9) =
        safeApiCall {
            apiService.hitCompleteProfile9(access, request)
        }

    suspend fun getAllFaithsStep7(access: String) = safeApiCall {
        apiService.getAllFaithsStep7(access)
    }

    suspend fun getCityByCountryCode(
        access: String,
        country: String,
        page: Int,
        limit: Int,
        lang: String,
        search: String?
    ) = safeApiCall {
        apiService.getCityByCountryCode(
            access,
            country,
            page = page,
            limit = limit,
            lang=lang,
            search = search
        )
    }

    suspend fun getRecentSearchTag(access: String, searchText: String) = safeApiCall {
        apiService.getRecentSearchTag(access, searchText)
    }

    suspend fun getRecentSearchHistory(access: String) = safeApiCall {
        apiService.getRecentSearchHistory(access)
    }


    suspend fun deleteRecentSearch(access: String, ) = safeApiCall {
        apiService.deleteRecentSearch(access)
    }

    suspend fun getHomeProfile(access: String, ) = safeApiCall {
        apiService.getHomeProfile(access)
    }

    suspend fun getPreviewProfile(access: String, ) = safeApiCall {
        apiService.getPreviewProfile(access)
    }

    suspend fun getPlans(access: String, ) = safeApiCall {
        apiService.getPlans(access)
    }

    suspend fun purchasePlan(access: String, request: PurchasedPlanRequest) = safeApiCall {
        apiService.purchasePlan(access, request)
    }


    suspend fun getActivePlan(access: String, ) = safeApiCall {
        apiService.getActivePlan(access)

    }

    suspend fun getMatch(
        access: String,
        filter: GetMatchFilterRequest? = null
    ) = safeApiCall {
        apiService.getMatch(
            accessToken = access,
            countryName = filter?.countryName,
            city = filter?.city,
            minAge = filter?.minAge,
            maxAge = filter?.maxAge,
            spokenLanguages = filter?.spokenLanguages,
            sect = filter?.sect,
            currentProfession = filter?.currentProfession,
            maritalStatus = filter?.maritalStatus,
            howOftenDrink = filter?.howOftenDrink,
            howOftenSmoke = filter?.howOftenSmoke,
            maxDistance = filter?.maxDistance,
            planType = filter?.planType,
            haveChildren = filter?.haveChildren,
            interestTags = filter?.interestTags,
            interestedIn = filter?.interestedIn
        )
    }


    suspend fun updateProfile(access: String, request: UpdateProfileRequest, ) = safeApiCall {
        apiService.updateProfile(access, request)
    }

 suspend fun createSession(access: String, ) = safeApiCall {
        apiService.createSession(access)
    }
 suspend fun getLiveNessResult(access: String,request: LiveNessResultRequest ) = safeApiCall {
        apiService.getLiveNessResult(access,request)
    }

 suspend fun compareFace(access: String,request: CompareFaceRequest ) = safeApiCall {
        apiService.compareFace(access,request)
    }



    suspend fun moderateContent(access: String,request: ModerateContentRequest) = safeApiCall {
        apiService.moderateContent(access,request)
    }
}


