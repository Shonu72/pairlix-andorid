package com.pairlix.dating.view.M6

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.GradientExpandableCardIndexVisibilityControl
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.VisibilityCard
import com.pairlix.dating.ReusedComponents.VisibilityCardToggle
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.requests.VisibilityFilterRequest
import com.pairlix.dating.response.VisibilityFilterResponse
import com.pairlix.dating.view.profileDetails.PreviewProfileObserver
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M6ViewModel
import kotlinx.coroutines.flow.collectLatest

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.pairlix.dating.ReusedComponents.PlanPopUp
import com.pairlix.dating.viewModel.M4ViewModel

data class ProfileStatus(val heading: String, val subHeading: String = "")
data class PrivacyToggle(
    val key: String,
    val title: String,
    val description: String,
    val enabled: Boolean
)



@Composable
fun VisibilityControlScreen(navController: NavHostController, viewModelM6: M6ViewModel, viewModel: AuthViewModel,viewModelM4:M4ViewModel) {

    var profileStatus by remember { mutableIntStateOf(0) }
    var visibilityStatus by remember { mutableIntStateOf(0) }
    var messageControl by remember { mutableIntStateOf(0) }
    var showAge by remember { mutableStateOf(false) }
    var showPlanType by remember { mutableStateOf(false) }
    val visibilityFilter by viewModelM6.visibilityFilter.collectAsState()
    val context = LocalContext.current
    val visibilityData: VisibilityFilterResponse
    val userData = viewModel.getPreviewProfileData.value
    val isFreeUser = userData?.activePlanType == 1

    val lifecycleOwner= LocalLifecycleOwner.current

    val profileStatusList = listOf(
        ProfileStatus(
            stringResource(R.string.active),
            stringResource(R.string.active_description)
        ),
        ProfileStatus(
            stringResource(R.string.hidden),
            stringResource(R.string.hidden_description)
        ),
        ProfileStatus(
            stringResource(R.string.paused),
            stringResource(R.string.paused_description)
        ),
        ProfileStatus(
            stringResource(R.string.deactivated),
            stringResource(R.string.deactivated_description)
        )
    )
    val messageList = listOf(
        ProfileStatus(stringResource(R.string.everyone), ""),
        ProfileStatus(stringResource(R.string.verified_users_only), ""),
        ProfileStatus(stringResource(R.string.premium_users_only), "")
    )
    val visibilityFilterList = listOf(
        ProfileStatus(stringResource(R.string.everyone)),
        ProfileStatus(stringResource(R.string.verified_users_only)),
        ProfileStatus(stringResource(R.string.premium_users_gold_platinum)),
        ProfileStatus(stringResource(R.string.people_i_liked_only))
    )

    val showAgeText = stringResource(R.string.show_age)
    val showAgeDesc = stringResource(R.string.show_age_description)

    val showLocation = stringResource(R.string.show_location)
    val showLocationDesc = stringResource(R.string.show_location_description)

    val showDistance = stringResource(R.string.show_distance)
    val showDistanceDesc = stringResource(R.string.show_distance_description)

    val showLastActive = stringResource(R.string.show_last_active)
    val showLastActiveDesc = stringResource(R.string.show_last_active_description)

    val blurProfile = stringResource(R.string.blur_profile)
    val blurProfileDesc = stringResource(R.string.blur_profile_description)
    var privacyList by remember {
        mutableStateOf(
            listOf(
                PrivacyToggle(
                    key = "blur_profile",
                    title = blurProfile,
                    description = blurProfileDesc,
                    enabled = true
                ),

                PrivacyToggle(
                    key = "show_age",
                    title = showAgeText,
                    description = showAgeDesc,
                    enabled = true
                ),
                PrivacyToggle(
                    key = "show_location",
                    title = showLocation,
                    description = showLocationDesc,
                    enabled = true
                ),
                PrivacyToggle(
                    key = "show_distance",
                    title = showDistance,
                    description = showDistanceDesc,
                    enabled = true
                ),
                PrivacyToggle(
                    key = "show_last_active",
                    title = showLastActive,
                    description = showLastActiveDesc,
                    enabled = true
                )

            )
        )
    }
    fun getToggleValue(key: String): Boolean {
        return privacyList.find { it.key == key }?.enabled ?: false
    }


    LaunchedEffect(Unit) {
        viewModel.hitPreviewProfile(access_token = SharedPreference.get(context).accessToken)

    }
    PreviewProfileObserver(
        viewModel = viewModel,
        viewModelM4=viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            viewModel.getPreviewProfileData.value = it
        })
    var originalBlurValue by remember { mutableStateOf(false) }
    val homeData = viewModel.getPreviewProfileData.value

    LaunchedEffect(homeData) {
        homeData?.let { data ->
            profileStatus = data.profileStatus ?: 0
            visibilityStatus = data.seeFilter ?: 0
            messageControl = data.messageFilter ?: 0

            privacyList = privacyList.map {item ->
                when (item.key) {
                    "show_age" -> item.copy(enabled = data.ageSetting == true)
                    "show_location" -> item.copy(enabled = data.locationSetting == true)
                    "show_distance" -> item.copy(enabled = data.distanceSetting == true)
                    "show_last_active" -> item.copy(enabled = data.isActive == true)
                    "blur_profile" -> item.copy(enabled = data.blurProfile == true)
                    else -> item
                }
            }
            originalBlurValue = data.blurProfile == true
        }
    }



    LaunchedEffect(visibilityFilter) {
        visibilityFilter.let { it ->
            when (it) {
                is EmpResource.Loading -> {
                    CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                  context.showToast( it.value.message?:"")

                    viewModelM6.resetVisibilityFilter()

                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                    viewModelM6.resetVisibilityFilter()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }






    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp)

        ) {
            TopBackBtnHeading(navController = navController, text = stringResource(R.string.visibility_filter))
            verticalSpace(20)

            GradientExpandableCardIndexVisibilityControl(
                title = stringResource(R.string.profile_visibility),
                subHeading = stringResource(R.string.profile_status),
                list = profileStatusList,
                selectedIndex = profileStatus,
                onItemSelected = {
                    if (isFreeUser) showPlanType = true else profileStatus = it
                }
            )

            verticalSpace(20)


            VisibilityCardToggle(
                heading = stringResource(R.string.privacy_settings),
                list = privacyList,
                onToggleChanged = { index, value ->
                    val toggleKey = privacyList[index].key
                    if (isFreeUser && toggleKey != "blur_profile") {
                        showPlanType = true
                    } else {
                        privacyList = privacyList.toMutableList().also {
                            it[index] = it[index].copy(enabled = value)
                        }
                    }
                }
            )


            verticalSpace(30)

            GradientExpandableCardIndexVisibilityControl(
                title = stringResource(R.string.visibility_filter),
                subHeading = stringResource(R.string.who_can_see_me),
                list = visibilityFilterList,
                selectedIndex = visibilityStatus,
                onItemSelected = {
                    if (isFreeUser) showPlanType = true else visibilityStatus = it
                }            )

            verticalSpace(20)


            GradientExpandableCardIndexVisibilityControl(
                title = stringResource(R.string.message_filter),
                subHeading = stringResource(R.string.who_can_message_me),
                list = messageList,
                selectedIndex = messageControl,
                onItemSelected = {
                    if (isFreeUser) showPlanType = true else messageControl = it
                }            )

            /*  verticalSpace(20)

              VisibilityCard(
                  heading = "Who Can Message Me:",
                  list = messageList,
                  selectedIndex = messageControl,
                  onItemSelected = { messageControl = it })
  */
            verticalSpace(100)


        }

        AppButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp), text = stringResource(R.string.save),

            onClick = {
                val currentBlurValue = getToggleValue("blur_profile")
                val onlyBlurChanged = isFreeUser && currentBlurValue != originalBlurValue
                if (isFreeUser && !onlyBlurChanged) {
                    showPlanType = true
                } else {
                    viewModelM6.hitVisibilityFilter(access_token = SharedPreference.get(context).accessToken, request = VisibilityFilterRequest(
                        ageSetting = getToggleValue("show_age"),
                        blurProfile = getToggleValue("blur_profile"),
                        distanceSetting = getToggleValue("show_distance"),
                        locationSetting = getToggleValue("show_location"),
                        isActive = getToggleValue("show_last_active"),
                        messageFilter = messageControl,
                        profileStatus = profileStatus,
                        seeFilter = visibilityStatus
                    ))
                }


            })
    }


    if(showPlanType){

        PlanPopUp(onDismiss = {showPlanType=false},navController)
    }
}