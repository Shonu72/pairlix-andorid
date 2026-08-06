package com.pairlix.dating.view.newAccountRegistrationScreen


import android.app.Activity
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.abi.simplecountrypicker.CustomSearchTextField
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomDropdown
import com.pairlix.dating.ReusedComponents.CustomDropdownIndex
import com.pairlix.dating.ReusedComponents.CustomInputField
import com.pairlix.dating.ReusedComponents.FormProgressBar
import com.pairlix.dating.ReusedComponents.GradientExpandableCard
import com.pairlix.dating.ReusedComponents.GradientExpandableCardIndex
import com.pairlix.dating.ReusedComponents.GradientExpandableCardMultipleSelect
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.noInitialSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest3
import com.pairlix.dating.requests.UpdateProfileRequest
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.profileDetails.PreviewProfileObserver
import com.pairlix.dating.view.profileDetails.ViewProfileScreen
import com.pairlix.dating.viewModel.AuthViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.colorResource
import com.pairlix.dating.viewModel.M4ViewModel


@Composable
fun CompleteProfileScreen3(navController: NavController, viewModel: AuthViewModel,viewModelM4:M4ViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedProfessionIndex by rememberSaveable { mutableStateOf(-1) }
    var schoolName by rememberSaveable { mutableStateOf("") }
    var jobTitle by rememberSaveable { mutableStateOf("") }
    var companyName by rememberSaveable { mutableStateOf("") }
    var selectedEducationIndex by rememberSaveable { mutableStateOf(-1) }
    var customProfessionalText by rememberSaveable { mutableStateOf("") }
    var expandedEducation by rememberSaveable { mutableStateOf(false) }

    fun validateStep3(
        selectedEducationIndex: Int,
        selectedProfessionIndex: Int,
        jobTitle: String,
        companyName: String,
        customProfession:String,
        context: Context
    ): Boolean {

        if (selectedEducationIndex == -1) {
            context.showToast(context.getString(R.string.please_select_your_education))
            return false
        }

        if (selectedProfessionIndex == -1) {
            context.showToast(context.getString(R.string.please_select_your_profession))
            return false
        }

     /*   if (jobTitle.isEmpty()) {
            context.showToast("Please enter job title")
            return false
        }
*/
      /*  if (companyName.isEmpty()) {
            context.showToast("Please enter company name")
            return false
        }*/

        if(selectedProfessionIndex==38 && customProfession.isEmpty()){

            context.showToast(context.getString(R.string.please_enter_profession))
            return false
        }

        return true
    }

  /*  val professionList = listOf(
        "Business",
        "Engineer",
        "Doctor",
        "Teacher",
        "Banker",
        "Sales",
        "Designer",
        "Skilled Worker",
        "Student",
        "Homemaker",
        "Freelance",
        "Retired",
        "Unemployed",
        "Prefer not to say",
        "other"
    )*/


    val professionList = stringArrayResource(R.array.profession_list).toList()
    val educationLevels = stringArrayResource(R.array.education_levels).toList()

    LaunchedEffect(SingletonObject.isFromEditProfile) {
        viewModel.hitPreviewProfile(SharedPreference.get(context).accessToken)

    }



    createAccountStep3Observer(
        context = context as MainActivity,
        viewModel = viewModel,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController
    )

    UpdateStep3Observer(
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

            data.personalDetails?.currentProfession
                ?.toIntOrNull()
                ?.let { index ->
                    if (index in professionList.indices) {
                        selectedProfessionIndex = index
                    }
                }

            data.personalDetails?.educationLevel
                ?.toIntOrNull()
                ?.let { index ->
                    if (index in educationLevels.indices) {
                        selectedEducationIndex = index
                    }
                }

            // 🔥 VERY IMPORTANT
           // expandedEducation = true

            schoolName = data.personalDetails?.schoolName ?: ""
            jobTitle = data.personalDetails?.jobTitle ?: ""
            companyName = data.personalDetails?.companyName ?: ""
            customProfessionalText =
                data.personalDetails?.currentProfession ?: ""

            isPrefilled = true
        }
    }

    LaunchedEffect(Unit) {
        expandedEducation = false
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()

    ){
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
               .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()

        ) {
            TopBackBtnHeading(navController, stringResource(R.string.complete_profile))

            verticalSpace(20)
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                if(!SingletonObject.isFromEditProfile){
                    FormProgressBar(
                        percentage = "40",
                        currentPage = 2.0,
                    )
                }

                verticalSpace(20)

                GradientExpandableCardIndex(
                    title = stringResource(R.string.what_is_your_current_profession),
                    items = professionList,
                    selectedIndex = selectedProfessionIndex,
                    onItemSelected = { selectedProfessionIndex = it }
                )

                verticalSpace(20)

                if (selectedProfessionIndex == 66) {        // Other selected
                    CustomInputField(
                        heading = stringResource(R.string.what_is_your_current_profession),
                        placeholder = stringResource(R.string.type_your_profession),
                        value = customProfessionalText,
                        onValueChange = { customProfessionalText = onlyAlphabetsNoInitial(it) }
                    )
                }
                verticalSpace(20)



                GradientExpandableCardIndex(
                    title = stringResource(R.string.education),
                    items = educationLevels,
                    selectedIndex = selectedEducationIndex,
                    onItemSelected = { selectedEducationIndex = it })



              /*  Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp)
                        )
                ) {

                    // ---- Header Row ----
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF8B5DF6).copy(alpha = 0.2f),
                                        Color(0xFFF6A6D6).copy(alpha = 0.2f)
                                    )
                                )
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                expandedEducation = !expandedEducation
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.education),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.arrow_top_ic),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.rotate(if (expandedEducation) 0f else 180f)
                        )
                    }

                    // ---- CONTENT INSIDE ----
                    AnimatedVisibility(visible = expandedEducation) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            verticalSpace(6)
                            Text(
                                text = stringResource(R.string.select_education_level),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )
                            verticalSpace(8)

                            CustomDropdownIndexEdit(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(R.string.select_education_level),
                                items = educationLevels,
                                selectedIndex = selectedEducationIndex,
                                onItemSelected = { selectedEducationIndex = it })

                            verticalSpace(20)

                        }
                    }

                }*/
                verticalSpace(30)

                AppButton(

                    modifier = Modifier.padding(bottom = 10.dp), text = stringResource(R.string.next), onClick = {
                        if (!validateStep3(
                                selectedEducationIndex,
                                selectedProfessionIndex,
                                jobTitle,
                                companyName,
                                customProfessionalText,
                                context
                            )
                        ) return@AppButton
                        val customProfessionalValue = if (selectedProfessionIndex == 38) customProfessionalText else null

                        if( SingletonObject.isFromEditProfile){
                            viewModel.hitUpdateProfile(
                                access_token = SharedPreference.get(context).accessToken,
                                request = UpdateProfileRequest(
                                    personalDetails = UpdateProfileRequest.PersonalDetails(
                                        /*companyName = companyName,*/
                                        currentProfession = selectedProfessionIndex.toString(),
                                        customProfession = customProfessionalValue,
                                        educationLevel = selectedEducationIndex.toString(),
                                      /*  jobTitle = jobTitle,
                                        schoolName = schoolName*/
                                    )
                                )
                            )


                        }else {
                            viewModel.hitCompleteProfile3(
                                access_token = SharedPreference.get(context).accessToken,
                                request = CompleteProfileRequest3(
                                    data = CompleteProfileRequest3.Data(
                                        companyName = companyName,
                                        currentProfession = selectedProfessionIndex.toString(),
                                        customProfession = customProfessionalValue,
                                        educationLevel = selectedEducationIndex.toString(),
                                        jobTitle = jobTitle,
                                        schoolName = schoolName
                                    ), step = 3

                                )
                            )
                        }
                    })
            }

        }

    }

}

fun createAccountStep3Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController
) {
    viewModel.completeProfile3.observe(lifecycleOwner) { state ->
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
                    navController.navigate(Screen.CompleteProfile4.route)
                    SharedPreference.get(context).profileCompletionPercentage =
                        state.value.data?.profileCompletionPercentage.toString()
                    state.value.success = false
                }
            }

            EmpResource.Idle -> {}

        }
    }


}

fun UpdateStep3Observer(
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

            else -> {
            }
        }
    }



}

