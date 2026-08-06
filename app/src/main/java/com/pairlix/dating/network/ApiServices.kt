package com.pairlix.dating.network

import com.pairlix.dating.requests.ActionRequest
import com.pairlix.dating.requests.BoostProfileRequest
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
import com.pairlix.dating.requests.ProfileViewActionRequest
import com.pairlix.dating.requests.PurchasedPlanRequest
import com.pairlix.dating.requests.ResendOtpRequest
import com.pairlix.dating.requests.UpdateProfileRequest
import com.pairlix.dating.requests.VisibilityFilterRequest
import com.pairlix.dating.res.ResendOtpResponse
import com.pairlix.dating.response.ActionResponse
import com.pairlix.dating.response.ActivePlanResponse
import com.pairlix.dating.response.AddTicketResponse
import com.pairlix.dating.response.BoostProfileResponse
import com.pairlix.dating.response.CompareFaceResponse
import com.pairlix.dating.response.CompleteProfileResponse
import com.pairlix.dating.response.CreateAccountResponse
import com.pairlix.dating.response.CreateSessionResponse
import com.pairlix.dating.response.DeleteRecentSearchResponse
import com.pairlix.dating.response.ExtractDocumentDataResponse
import com.pairlix.dating.response.GetAllCategoriesResponse
import com.pairlix.dating.response.GetAllFaithsStep7Response
import com.pairlix.dating.response.GetChatListResponse
import com.pairlix.dating.response.GetCmsResponse
import com.pairlix.dating.response.GetCountryCodeResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.GetNotificationResponse
import com.pairlix.dating.response.GetPlansResponse
import com.pairlix.dating.response.GetTicketResponse
import com.pairlix.dating.response.HomeProfileResponse
import com.pairlix.dating.response.LiveNessResultResponse
import com.pairlix.dating.response.LoginResponse
import com.pairlix.dating.response.MatchPopupResponse
import com.pairlix.dating.response.MatchTimingResponse
import com.pairlix.dating.response.ModerateContentResponse
import com.pairlix.dating.response.OtpResponse
import com.pairlix.dating.response.PreviewProfileResponse
import com.pairlix.dating.response.ProfileViewActionResponse
import com.pairlix.dating.response.ProfileViewResponse
import com.pairlix.dating.response.PurchasedPlanResponse
import com.pairlix.dating.response.RecentSearchHistoryResponse
import com.pairlix.dating.response.RecentSearchResponse
import com.pairlix.dating.response.UpdateProfileResponse
import com.pairlix.dating.response.UploadDocumentFileResponse
import com.pairlix.dating.response.UploadImageFileResponse
import com.pairlix.dating.response.UploadMultipleImageResponse
import com.pairlix.dating.response.UserActivityResponse
import com.google.gson.JsonObject
import com.pairlix.dating.requests.CallStatusRequest
import com.pairlix.dating.requests.NotificationSettingRequest
import com.pairlix.dating.requests.SocialLoginRequest
import com.pairlix.dating.requests.UpdateLanguageRequest
import com.pairlix.dating.res.CallStatusResponse
import com.pairlix.dating.response.CheckAbusiveWordResponse
import com.pairlix.dating.response.DeleteAccountResponse
import com.pairlix.dating.response.LogoutResponse
import com.pairlix.dating.response.NotificationSettingResponse
import com.pairlix.dating.response.SocialLoginResponse
import com.pairlix.dating.response.UpdateLanguageResponse
import com.pairlix.dating.response.VisibilityFilterResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface   ApiServices {

    @POST("api/v1/user/auth/login")
    suspend fun hitLogin(
        @Body request: LoginRequest
    ): LoginResponse


    @POST("api/v1/user/auth/verifyOtp")
    suspend fun hitVerifyOtp(
        @Header("accessToken") accessToken: String,
        @Body request: OtpVerifyRequest
    ): OtpResponse


   @POST("api/v1/user/auth/create-account")
    suspend fun hitCreateAccount(
       @Header("accessToken") accessToken: String,
        @Body request: CreateAccountRequest
    ): CreateAccountResponse


   // original previous
 /*   @Multipart
    @POST("api/v1/user/auth/uploadImage")
    suspend fun uploadImageFile(
       @Header("accessToken") accessToken: String,
        @Part upload_file: MultipartBody.Part
    ): UploadImageFileResponse
*/


//for moderate comment out for moderate build
    @Multipart
    @POST("/api/v1/user/auth/uploadDocument")
    suspend fun uploadImageFile(
       @Header("accessToken") accessToken: String,
        @Part upload_file:List<MultipartBody.Part>
    ): UploadDocumentFileResponse



  /*  @Multipart
    @POST("api/v1/user/auth/uploadImage")
    suspend fun uploadImageFile(
        @Header("accessToken") accessToken: String,
        @Part upload_file: MultipartBody.Part
    ): UploadImageFileResponse
*/




//this api use in everywhere where upload document
/*
  @Multipart
    @POST("/api/v1/user/auth/uploadDocument")
    suspend fun uploadDocumentAws(
       @Header("accessToken") accessToken: String,
        @Part upload_file: MultipartBody.Part
    ): UploadDocumentFileResponse
*/

    @Multipart
    @POST("/api/v1/user/auth/extractAadhaarData")
    suspend fun  extractDocumentData(
        @Header("accessToken") accessToken: String,
        @Part upload_file: MultipartBody.Part
    ):  ExtractDocumentDataResponse


    @Multipart
    @POST("api/v1/user/auth/uploadMultipleImage")
    suspend fun uploadMultipleImage(
       @Header("accessToken") accessToken: String,
       @Part uploadFiles: List<MultipartBody.Part>
    ): UploadMultipleImageResponse



    @PATCH("api/v1/user/auth/resendOtp")
    suspend fun hitResendOtp(
        @Header("accessToken") accessToken: String,
        @Body request: ResendOtpRequest
    ): ResendOtpResponse


    @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile1(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest1
    ): CompleteProfileResponse

    @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile2(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest2
    ): CompleteProfileResponse

  @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile3(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest3
    ): CompleteProfileResponse

    @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile4(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest4
    ): CompleteProfileResponse

  @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile5(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest5
    ): CompleteProfileResponse

    @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile6(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest6
    ): CompleteProfileResponse

    @GET("api/v1/user/auth/getAllCategory")
    suspend fun  getAllCategoriesStep6(
        @Header("accessToken") accessToken: String,
    ): GetAllCategoriesResponse


@GET("api/v1/user/auth/getAllFaiths")
    suspend fun  getAllFaithsStep7(
        @Header("accessToken") accessToken: String,
    ): GetAllFaithsStep7Response



    @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile7(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest7
    ): CompleteProfileResponse

 @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile8(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest8
    ): CompleteProfileResponse


    @POST("api/v1/user/auth/complete-profile")
    suspend fun hitCompleteProfile9(
        @Header("accessToken") accessToken: String,
        @Body request: CompleteProfileRequest9
    ): CompleteProfileResponse


    @GET("api/v1/user/auth/getCityByCountryCodes")
    suspend fun  getCityByCountryCode(
        @Header("accessToken") accessToken: String,
        @Query("country") countryCode: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("lang") lang: String,
        @Query("search") search: String?

    ): GetCountryCodeResponse

 @GET("api/v1/user/search/search-tag")
    suspend fun  getRecentSearchTag(
        @Header("accessToken") accessToken: String,
        @Query("searchText") searchText: String,
    ): RecentSearchResponse

 @GET("/api/v1/user/search/recent-search")
    suspend fun  getRecentSearchHistory(
        @Header("accessToken") accessToken: String,
    ): RecentSearchHistoryResponse

    @DELETE("/api/v1/user/search/delete-recent")
    suspend fun deleteRecentSearch(
        @Header("accessToken") accessToken: String,
    ): DeleteRecentSearchResponse

    @GET("/api/v1/user/plan/get-plan")
    suspend fun  getPlans(
        @Header("accessToken") accessToken: String,
    ) : GetPlansResponse


    @GET("/api/v1/user/profile/homeProfile")
    suspend fun  getHomeProfile(
        @Header("accessToken") accessToken: String,
    ): HomeProfileResponse


    @POST("api/v1/user/plan/upgradeAndsubcription")
    suspend fun purchasePlan(
        @Header("accessToken") accessToken: String,
        @Body request: PurchasedPlanRequest
    ): PurchasedPlanResponse

    @GET("/api/v1/user/plan/getSubscribedUsers")
    suspend fun  getActivePlan(
        @Header("accessToken") accessToken: String,
    ): ActivePlanResponse

    @GET("api/v1/user/profile/previewProfile")
    suspend fun  getPreviewProfile(
        @Header("accessToken") accessToken: String,
    ): PreviewProfileResponse

    @PATCH("api/v1/user/profile/updateProfile")
    suspend fun updateProfile(
        @Header("accessToken") accessToken: String,
        @Body request:UpdateProfileRequest
    ):UpdateProfileResponse


    @GET("api/v1/user/profile/getMatch")
    suspend fun  getMatch(
        @Header("accessToken") accessToken: String,
        @Query("countryName") countryName: String? = null,
        @Query("city") city: String? = null,
        @Query("minAge") minAge: Int? = null,
        @Query("maxAge") maxAge: Int? = null,
        @Query("spokenLanguages") spokenLanguages: String? = null,
        @Query("sect") sect: Int? = null,
        @Query("currentProfession") currentProfession: Int? = null,
        @Query("maritalStatus") maritalStatus: Int? = null,
        @Query("howOftenDrink") howOftenDrink: Int? = null,
        @Query("howOftenSmoke") howOftenSmoke: Int? = null,
        @Query("maxDistance") maxDistance: Int? = null,
        @Query("planType") planType: Int? = null,
        @Query("haveChildren") haveChildren: Int? = null,
        @Query("interestTags") interestTags: String? = null,
        @Query("interestedIn") interestedIn: Int? = null,
        ): GetMatchResponse


    @POST("api/v1/user/activity/action")
    suspend fun action(
        @Header("accessToken") accessToken: String,
        @Body request: ActionRequest
    ): ActionResponse

    @GET("api/v1/user/activity")
    suspend fun  getUserActivity(
        @Header("accessToken") accessToken: String,
        @Query("actionType") actionType: String,
    ):  GetMatchResponse


 @GET("/api/v1/user/activity/popupCard")
    suspend fun  matchPopup(
        @Header("accessToken") accessToken: String,
    ):  MatchPopupResponse



    @GET("/api/v1/user/activity/profileviewList")
    suspend fun  profileViewList(
        @Header("accessToken") accessToken: String,
    ):  GetMatchResponse


@POST("/api/v1/user/activity/profileViewAction")
    suspend fun  profileViewAction(
        @Header("accessToken") accessToken: String,
        @Body request: ProfileViewActionRequest
    ):  ProfileViewActionResponse

    @POST("/api/v1/user/auth/getLivenessResult")
    suspend fun  getLiveNessResult(
        @Header("accessToken") accessToken: String,
        @Body request: LiveNessResultRequest
    ):  LiveNessResultResponse


 @POST("/api/v1/user/auth/compareFace")
    suspend fun  compareFace(
        @Header("accessToken") accessToken: String,
        @Body request: CompareFaceRequest
    ):  CompareFaceResponse


    @GET("/api/v1/user/auth/createLivenessSession")
    suspend fun   createSession(
        @Header("accessToken") accessToken: String,
    ):  CreateSessionResponse


   @POST("/api/v1/user/auth/moderateContent")
    suspend fun moderateContent(
        @Header("accessToken") accessToken: String,
        @Body request: ModerateContentRequest
    ):  ModerateContentResponse


    @GET("/api/v1/user/activity/chatList")
    suspend fun  getChatList(
        @Header("accessToken") accessToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetChatListResponse

    @PATCH("/api/v1/user/activity/boostProfile")
    suspend fun  hitBoostProfile(
        @Header("accessToken") accessToken: String,
        @Body request:BoostProfileRequest
    ): BoostProfileResponse



    @PATCH("/api/v1/user/visibiltyControl")
    suspend fun  hitVisibilityFilter(
        @Header("accessToken") accessToken: String,
      @Body request:VisibilityFilterRequest
    ): VisibilityFilterResponse


    @PATCH("/api/v1/user/notificationSetting")
    suspend fun  notificationSetting(
        @Header("accessToken") accessToken: String,
      @Body request: NotificationSettingRequest
    ): NotificationSettingResponse


    @POST("/api/v1/user/ticket/addTickets")
    suspend fun addTicket(
        @Header("accessToken") accessToken: String,
        @Body request: AddTicketRequest
    ): AddTicketResponse


    @GET("/api/v1/user/activity/matchTiming/{userId}")
    suspend fun matchTiming(
        @Header("accessToken") accessToken: String,
        @Path("userId") userId: String,
    ): MatchTimingResponse


    @GET("api/v1/user/auth/checkAbusiveWord")
    suspend fun checkAbusiveWord(
        @Header("accessToken") accessToken: String,
        @Query ("word") word:String?=null,
    ): CheckAbusiveWordResponse


    @GET("api/v1/user/ticket")
    suspend fun getTicket(
        @Header("accessToken") accessToken: String,
        @Query("search") search: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("status")  status: Int? = null,
    ): GetTicketResponse

  @GET("/api/v1/user/auth/receiveNotificationList")
    suspend fun getNotification(
        @Header("accessToken") accessToken: String,
    ): GetNotificationResponse

    @POST("/api/v1/user/auth/logout")
    suspend fun logout(
        @Header("accessToken") accessToken: String,
    ): LogoutResponse


    @GET("api/v1/user/cms")
    suspend fun getCms(
        @Header("accessToken") accessToken: String,
        @Query("section") section: String,
        @Query("language") language: String
    ): JsonObject


  @GET("/api/v1/user/auth/deleteAccount")
    suspend fun deleteAccount(
        @Header("accessToken") accessToken: String,
    ): DeleteAccountResponse


  @POST("/api/v1/user/auth/socialSignup")
    suspend fun socialLogin(
        @Body request: SocialLoginRequest
    ): SocialLoginResponse


    @PATCH("/api/v1/user/visibiltyControl/changeLanguage")
    suspend fun updateLanguage(
        @Header("accessToken") accessToken: String,
        @Body request: UpdateLanguageRequest
    ) : UpdateLanguageResponse


    @POST("/api/v1/user/visibiltyControl/callStatus")
    suspend fun callStatus(
        @Header("accessToken") accessToken: String,
        @Body request: CallStatusRequest
    ): CallStatusResponse

}