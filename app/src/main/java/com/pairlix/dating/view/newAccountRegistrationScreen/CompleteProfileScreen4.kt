package com.pairlix.dating.view.newAccountRegistrationScreen

import android.content.Context
import androidx.compose.runtime.Composable
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
import com.pairlix.dating.ReusedComponents.GradientExpandableCardMultipleSelect
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest4
import com.pairlix.dating.viewModel.AuthViewModel
import androidx.compose.material3.MaterialTheme


@Composable
fun CompleteProfileScreen4(navController: NavController,viewModel: AuthViewModel) {

    var selectedMartial by rememberSaveable { mutableStateOf(-1) }
    var selectedReligion by rememberSaveable { mutableStateOf(-1) }
    var selectedChildren by rememberSaveable { mutableStateOf(-1) }
    val maritalStatusList = stringArrayResource(R.array.marital_status_list).toList()
    val religiousPracticeList = stringArrayResource(R.array.religious_practice_list).toList()
    val childrenList = stringArrayResource(R.array.children_list).toList()
    val context = LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current


    LaunchedEffect(Unit) {
        createAccountStep4Observer(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            navController = navController as NavHostController
        )
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
            val scrollState = rememberScrollState()
            verticalSpace(20)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                FormProgressBar(currentPage = 3.5, percentage ="50")
                   // SharedPreference.get(context).profileCompletionPercentage
                        // )


                verticalSpace(20)

                GradientExpandableCardIndex (
                    title = stringResource(R.string.marital_status),
                    items = maritalStatusList,
                    selectedIndex = selectedMartial,
                    onItemSelected = { selectedMartial = it }
                )

                verticalSpace(20)
                GradientExpandableCardIndex (
                    title = stringResource(R.string.do_you_have_children),
                    items = childrenList,
                    selectedIndex = selectedChildren,
                    onItemSelected = { selectedChildren = it }
                )


                verticalSpace(20)


                GradientExpandableCardIndex(
                    title = stringResource(R.string.how_do_you_practice_your_religion),
                    items = religiousPracticeList,
                    selectedIndex = selectedReligion,
                    onItemSelected = { selectedReligion = it }
                )

                verticalSpace(30)




                AppButton(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = stringResource(R.string.next),
                    onClick = {

                        if (!validateStep4(
                                selectedMartial,
                                selectedReligion,
                                selectedChildren,
                                context
                            )
                        ) return@AppButton

                        viewModel.hitCompleteProfile4(
                            access_token = SharedPreference.get(context).accessToken,
                            request = CompleteProfileRequest4(
                                data = CompleteProfileRequest4.Data(
                                    haveChildren = selectedChildren.toString(),
                                    maritalStatus = selectedMartial.toString(),
                                    religionPractice = selectedReligion.toString()
                                ) ,
                                step =4
                            )
                        )

                    }
                )


            }
        }

    }

}

fun createAccountStep4Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController
) {
    viewModel.completeProfile4.observe(lifecycleOwner) { state ->
        when (state) {
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
                    navController.navigate(Screen.CompleteProfile5.route)
                    state.value.success = false
                }
            }
            EmpResource.Idle -> {}

        }
    }

}

fun validateStep4(
    selectedMaritalIndex: Int,
    selectedReligionIndex: Int,
    selectedChildrenIndex: Int,
    context: Context
): Boolean {

    if (selectedMaritalIndex == -1) {
        context.showToast(context.getString(R.string.please_select_your_marital_status))
        return false
    }

    if (selectedReligionIndex == -1) {
        context.showToast(context.getString(R.string.please_select_your_religious_practice_level))
        return false
    }

    if (selectedChildrenIndex == -1) {
        context.showToast(context.getString(R.string.please_select_your_children_status))
        return false
    }

    return true
}
