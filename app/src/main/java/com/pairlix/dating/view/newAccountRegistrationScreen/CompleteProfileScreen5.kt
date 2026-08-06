package com.pairlix.dating.view.newAccountRegistrationScreen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.FormProgressBar
import com.pairlix.dating.ReusedComponents.GradientExpandableCard
import com.pairlix.dating.ReusedComponents.GradientExpandableCardIndex
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest4
import com.pairlix.dating.requests.CompleteProfileRequest5
import com.pairlix.dating.requests.UpdateProfileRequest
import com.pairlix.dating.response.PreviewProfileResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.profileDetails.PreviewProfileObserver
import com.pairlix.dating.viewModel.AuthViewModel

import androidx.compose.material3.MaterialTheme
import com.pairlix.dating.viewModel.M4ViewModel

@Composable
fun CompleteProfileScreen5(navController: NavController, viewModel: AuthViewModel,viewModelM4:M4ViewModel) {
    var selectedDrink by rememberSaveable { mutableStateOf(-1) }
    var selectedSmoke by rememberSaveable { mutableStateOf(-1) }
    var selectedWorkOut by rememberSaveable { mutableStateOf(-1) }

    val drinkingFrequencyList = stringArrayResource(R.array.drinking_frequency_list).toList()

    val smokingFrequencyList = stringArrayResource(R.array.smoking_frequency_list).toList()

    val workoutFrequencyList = stringArrayResource(R.array.workout_frequency_list).toList()


//    val sleepingHabitList = listOf(
//        "Early Bird",
//        "Night Owl",
//        "In Spectrum"
//    )



    val context = LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current



    LaunchedEffect(SingletonObject.isFromEditProfile) {
        viewModel.hitPreviewProfile(SharedPreference.get(context).accessToken)

    }

    createAccountStep5Observer(
        context = context as MainActivity,
        viewModel = viewModel,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController
    )

    UpdateObserver(
        viewModel = viewModel,
        context = context,
        lifecycleOwner = lifecycleOwner,
        navController = navController
    )


    PreviewProfileObserver(
        viewModel = viewModel,
        viewModelM4=viewModelM4,
        context = context,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            viewModel.getPreviewProfileData.value = it
        }
    )

    val data =  viewModel.getPreviewProfileData.value
    var isPrefilled by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(data) {
        if (
            SingletonObject.isFromEditProfile &&
            data != null &&
            !isPrefilled
        ) {

            // =========================
            // DRINK
            // =========================
            data.personalDetails?.howOftenDrink
                ?.toIntOrNull()
                ?.let { index ->
                    if (index in drinkingFrequencyList.indices) {
                        selectedDrink = index
                    }
                }

            // =========================
            // SMOKE
            // =========================
            data.personalDetails?.howOftenSmoke
                ?.toIntOrNull()
                ?.let { index ->
                    if (index in smokingFrequencyList.indices) {
                        selectedSmoke = index
                    }
                }

            // =========================
            // WORKOUT
            // =========================
            data.personalDetails?.workOut
                ?.toIntOrNull()
                ?.let { index ->
                    if (index in workoutFrequencyList.indices) {
                        selectedWorkOut = index
                    }
                }

            // 🔐 IMPORTANT
            isPrefilled = true
        }
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()

    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
               .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)

        ) {
            TopBackBtnHeading(navController, stringResource(R.string.complete_profile))

            verticalSpace(20)
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                if(!SingletonObject.isFromEditProfile) {
                    FormProgressBar(
                        currentPage = 4.0,
                        percentage ="60"
                            //SharedPreference.get(context).profileCompletionPercentage
                    )
                }

            verticalSpace(20)

                GradientExpandableCardIndex(
                    title = stringResource(R.string.how_often_do_you_drink),
                    items = drinkingFrequencyList,
                    selectedIndex = selectedDrink,
                    onItemSelected = { selectedDrink = it }
                                                             )
            verticalSpace(20)

                GradientExpandableCardIndex(
                title = stringResource(R.string.how_often_do_you_smoke),
                items = smokingFrequencyList,
                selectedIndex = selectedSmoke,
                onItemSelected = { selectedSmoke = it })


            verticalSpace(20)

                GradientExpandableCardIndex(
                title = stringResource(R.string.how_often_do_you_workout),
                items = workoutFrequencyList,
                selectedIndex = selectedWorkOut,
                onItemSelected = { selectedWorkOut = it })


              verticalSpace(30)

                fun validateStep5(
                    selectedDrink: Int,
                    selectedSmoke: Int,

                    selectedWorkOut: Int,
                    context: Context
                ): Boolean {

                    if (selectedDrink == -1) {
                        context.showToast(context.getString(R.string.please_select_how_often_you_drink))
                        return false
                    }
                    if (selectedSmoke == -1) {
                        context.showToast(context.getString(R.string.please_select_how_often_you_smoke))
                        return false
                    }
                    if (selectedWorkOut == -1) {
                        context.showToast(context.getString(R.string.please_select_your_workout_frequency))
                        return false
                    }

                    return true
                }


                AppButton(
                modifier = Modifier.padding(bottom = 10.dp),
                text = stringResource(R.string.next) ,
                onClick = {
                    if (!validateStep5(
                            selectedDrink,
                            selectedSmoke,
                            selectedWorkOut,
                            context
                        )
                    ) return@AppButton

                    if( SingletonObject.isFromEditProfile){
                        viewModel.hitUpdateProfile(
                            access_token = SharedPreference.get(context).accessToken,
                            request = UpdateProfileRequest(
                                personalDetails = UpdateProfileRequest.PersonalDetails(
                                    howOftenDrink = selectedDrink.toString(),
                                    howOftenSmoke = selectedSmoke.toString(),
                                    workOut = selectedWorkOut.toString()
                                )
                            )
                        )


                    }

                    else{

                    viewModel.hitCompleteProfile5(
                        access_token = SharedPreference.get(context).accessToken,
                        request = CompleteProfileRequest5(
                            data = CompleteProfileRequest5.Data(
                                howOftenDrink = selectedDrink.toString(),
                                howOftenSmoke = selectedSmoke.toString(),
                                workOut = selectedWorkOut.toString()
                            ),
                                    step = 5),
                    )}

                }
            )
        }
    }}
}


fun createAccountStep5Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController
) {
    viewModel.completeProfile5.observe(lifecycleOwner) { state ->
        when (state) {
            EmpResource.Idle -> {}

            is EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (state.value.success == true) {
                    context.showToast(state.value.message?:"")
                    SharedPreference.get(context).profileCompletionPercentage = state.value.data?.profileCompletionPercentage.toString()
                    navController.navigate(Screen.CompleteProfile6.route)
                    state.value.success = false
                }
            }
        }
    }

}

fun UpdateObserver(
    viewModel: AuthViewModel,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
){
    viewModel.updateProfile.observe(lifecycleOwner){ state->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                CustomLoader.showLoader(context as Activity?)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (state.value.success == true) {
                    context.showToast(state.value.message?:"")
                    navController.navigate(Screen.ViewProfileScreen.route){
                        popUpTo(Screen.ViewProfileScreen.route){
                            inclusive=true
                        }
                    }

                    SingletonObject.isFromEditProfile=false

                    state.value.success = false
                }
            }
            EmpResource.Idle -> {}


            else -> {
                // no-op
            }

        }
    }



}
