package com.pairlix.dating.view.newAccountRegistrationScreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.pairlix.dating.ReusedComponents.GradientExpandableCardFaithSingle
import com.pairlix.dating.ReusedComponents.GradientExpandableCardIndex
import com.pairlix.dating.ReusedComponents.GradientExpandableCardMultiIdFaith
import com.pairlix.dating.ReusedComponents.GradientExpandableCardWithEditText
import com.pairlix.dating.ReusedComponents.GradientExpandableCardWithLayout
import com.pairlix.dating.ReusedComponents.GradientExpandableCardWithMultipleSelect
import com.pairlix.dating.ReusedComponents.GradientExpandableCardWithMultipleSelectApi
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.noInitialSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest7
import com.pairlix.dating.response.GetAllFaithsStep7Response
import com.pairlix.dating.viewModel.AuthViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import com.pairlix.dating.utils.containsContactInfo
import com.pairlix.dating.viewModel.M5ViewModel

@Composable
fun CompleteProfileScreen7(navController: NavController, viewModel: AuthViewModel,viewModel5:M5ViewModel) {
    val scrollState = rememberScrollState()
    val relocationPreferenceList = stringArrayResource(R.array.relocation_preference_list).toList()
    var selectedFaithIds by rememberSaveable { mutableStateOf(listOf<String>()) }
    var selectedFaithId by rememberSaveable { mutableStateOf("") }
    var relocationPreference by rememberSaveable { mutableStateOf(-1) }
    var height by rememberSaveable { mutableStateOf("") }
    var heightUnit by rememberSaveable { mutableStateOf("CM") }
    var inputText by rememberSaveable { mutableStateOf("") }
    val heightTypeValue = if (heightUnit == "CM") "0" else "1"
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val checkAbusive by viewModel5.checkAbusiveWord.collectAsState()
    var pendingProfileRequest by remember { mutableStateOf<CompleteProfileRequest7?>(null) }


    LaunchedEffect(checkAbusive) {
        checkAbusive.let {
            when (it) {
                is EmpResource.Success -> {
                    // ✅ Not abusive - proceed with API call
                    pendingProfileRequest?.let { request ->
                        viewModel.hitCompleteProfile7(
                            access_token = SharedPreference.get(context).accessToken,
                            request = request
                        )
                        pendingProfileRequest = null
                    }
                    viewModel5.resetCheckAbusiveWord()
                }
                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable?.let { err ->
                        ErrorUtil.handlerGeneralError(context as MainActivity, err)
                    }
                    // ✅ Abusive - clear pending and don't submit
                    pendingProfileRequest = null
                    viewModel5.resetCheckAbusiveWord()
                }
                else -> {}
            }
        }
    }
    createAccountStep7Observer(
        context = context as MainActivity,
        viewModel = viewModel,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController,
        onSuccess = { it ->
            viewModel.getFaithList.clear()
            it?.let {
                viewModel.getFaithList.addAll(it)
            }

        }

    )

    val faithList = viewModel.getFaithList


    fun validateStep7(
        selectedMartial: Int,
        height: String,
        heightUnit: String,
        selectedFaithIds: List<String>,
        description: String,
        context: Context,
        faithList: List<FaithItem>

    ): Boolean {

        if (selectedMartial == -1) {
            context.showToast(context.getString(R.string.please_select_your_move_abroad_after_marriage_preference))
            return false
        }


        if (height.isBlank()) {
            context.showToast(context.getString(R.string.please_enter_your_height))
            return false
        }
        val allowed = setOf('0','1','2','3','4','5','6','7','8','9', '\'', '"', '.')

        if (height.any { it !in allowed }) {
            context.showToast(context.getString(R.string.please_enter_a_valid_height))
            return false
        }


        if (faithList.isNotEmpty()) {
            if (selectedFaithIds.isEmpty()) {
                context.showToast(context.getString(R.string.please_select_at_least_one_faith_identity))
                return false
            }
        }

        if (description.isBlank()) {
            context.showToast(context.getString(R.string.please_write_something_about_your_personality))
            return false
        }

        if (containsContactInfo(description)) {
            context.showToast(context.getString(R.string.please_do_not_write_email_phone_or_links))
            return false
        }

        return true
    }


    LaunchedEffect(Unit) {
        viewModel.hitGetAllFaithsStep7(
            access_token = SharedPreference.get(context).accessToken
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
           .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding( start = 16.dp, end = 16.dp)
    ) {
        TopBackBtnHeading(navController, text = stringResource(R.string.complete_profile))
        verticalSpace(20)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            FormProgressBar(currentPage = 6.0, percentage = "80"
                //SharedPreference.get(context).profileCompletionPercentage

            )
            Spacer(Modifier.height(24.dp))

            GradientExpandableCardIndex(
                title = stringResource(R.string.would_you_move_abroad_after_marriage),
                items = relocationPreferenceList,
                selectedIndex = relocationPreference,
                onItemSelected = { relocationPreference = it })

            Spacer(Modifier.height(24.dp))

            GradientExpandableCardWithLayout(
                title = stringResource(R.string.height),
                heightValue = height,
                unitValue = heightUnit,
                onHeightChange = { newValue ->

                    // BLOCK NEGATIVE VALUES IN BOTH MODES
                    if (newValue.contains("-")) {
                        return@GradientExpandableCardWithLayout
                    }

                    // =======================
                    // CM MODE
                    // =======================
                    if (heightUnit == "CM") {

                        var cleaned = newValue.filter { it.isDigit() }

                        // Block starting 0
                        if (cleaned.startsWith("0")) {
                            cleaned = cleaned.dropWhile { it == '0' }
                        }

                        if (cleaned.isNotEmpty()) {
                            val cmValue = cleaned.toInt()

                            // 🔥 BLOCK > 305
                            if (cmValue > 305) {
                                return@GradientExpandableCardWithLayout
                            }
                        }

                        height = cleaned
                        return@GradientExpandableCardWithLayout
                    }

                    if (heightUnit == "FT") {

                        if (newValue.isEmpty()) {
                            height = ""
                            return@GradientExpandableCardWithLayout
                        }

                        val allowedChars = "0123456789."
                        var cleaned = newValue.filter { it in allowedChars }

                        if (cleaned.startsWith(".")) return@GradientExpandableCardWithLayout
                        if (cleaned.count { it == '.' } > 1) return@GradientExpandableCardWithLayout
                        if (cleaned.startsWith("0")) return@GradientExpandableCardWithLayout

                        val parts = cleaned.split(".")

                        val feet = parts[0].toIntOrNull() ?: return@GradientExpandableCardWithLayout

                        // ❌ Feet > 10 block
                        if (feet > 10) return@GradientExpandableCardWithLayout

                        // ⭐ NEW RULE: If feet = 10 → no dot allowed
                        if (feet == 10 && cleaned.contains(".")) {
                            return@GradientExpandableCardWithLayout
                        }

                        // Validate inches only when feet < 10
                        if (parts.size == 2 && parts[1].isNotEmpty() && feet < 10) {
                            val inches = parts[1].toIntOrNull() ?: return@GradientExpandableCardWithLayout

                            if (inches > 12) return@GradientExpandableCardWithLayout
                        }

                        height = cleaned
                    }



                }
                ,
                onUnitChange = { heightUnit = it
                    height = "" }
            )


            Spacer(Modifier.height(24.dp))


            val isArabic = isArabic()

            val faithItemsList = faithList.mapNotNull { item ->
                item?.let { data ->

                    val titleText = if (isArabic) {
                        data.faithNameAr ?: stringResource(R.string.unknown)
                    } else {
                        data.faithNameEn ?: stringResource(R.string.unknown)
                    }

                    FaithItem(
                        id = data.id ?: "",
                        title = titleText,
                        titleAr = data.faithNameAr ?: stringResource(R.string.unknown)
                    )
                }
            }


            GradientExpandableCardFaithSingle(
                title = stringResource(R.string.select_the_category_that_best_represents_your_faith_identity),
                faithItems = faithItemsList,
                selectedIds = selectedFaithIds,
                onSelectionChanged = { selectedFaithIds = it }
            )





            Spacer(Modifier.height(24.dp))

            GradientExpandableCardWithEditText(
                title = stringResource(R.string.how_would_you_describe_your_personality),
                value = inputText,
                onValueChange = { newValue ->
                    val sanitized = noInitialSpace(newValue)
                    if (!containsContactInfo(sanitized)) {
                        inputText = sanitized
                    }
                }
            )


            Spacer(Modifier.height(24.dp))


            // val finalHeight = convertFeetDotToApiFormat(height)

//            var finalHeight by remember { mutableStateOf("") }
//            if (heightUnit == "FT") {
//
//                finalHeight= convertFeetDotToApiFormat(height)
//            } else{
//                finalHeight=height
//
//        }


            AppButton(
                modifier = Modifier.padding(bottom = 10.dp), text = stringResource(R.string.next),
                onClick = {

                    if (!validateStep7(
                            selectedMartial = relocationPreference,
                            height = height,
                            heightUnit = heightUnit,
                            selectedFaithIds = selectedFaithIds,
                            description = inputText,
                            context = context,
                            faithList = faithItemsList     // ← Pass your API list

                        ))return@AppButton
                    pendingProfileRequest = CompleteProfileRequest7(
                        data = CompleteProfileRequest7.Data(
                            faithIds = selectedFaithIds,
                            aboardAfterMarriage = relocationPreference.toString(),
                            description = inputText,
                            height = height,
                            heightType = heightTypeValue
                        ),
                        step = 7
                    )

                    // ✅ Check abusive word before submitting
                    viewModel5.hitCheckAbusiveWord(
                        SharedPreference.get(context).accessToken,
                        inputText.trim()
                    )
                }
            )




        }

    }

}

@Composable
fun isArabic(): Boolean {
    val locale = LocalConfiguration.current.locales[0]
    return locale.language == "ar"
}
fun createAccountStep7Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    onSuccess: (List<GetAllFaithsStep7Response.Data?>?) -> Unit = {}
) {
    viewModel.completeProfile7.observe(lifecycleOwner) { state ->
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
                    SharedPreference.get(context).profileCompletionPercentage = state.value.data?.profileCompletionPercentage.toString()
                    context.showToast(state.value.message?:"")
                    navController.navigate(Screen.FaceVerificationScreen.route)
                    state.value.success = false
                }
            }
            EmpResource.Idle -> {}

        }
    }

    viewModel.getAllFaithsStep7.observe(lifecycleOwner) { state ->
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
                    onSuccess(state.value.data)
                    state.value.success = false
                }
            }
            EmpResource.Idle -> {}

        }
    }


}

data class FaithItem(
    val id: String,
    val title: String,      // English
    val titleAr: String?    // Arabic
)

fun convertFeetDotToApiFormat(input: String): String {
    if (!input.contains(".")) return "${input}'0\""

    val parts = input.split(".")
    val feet = parts[0]
    val inch = parts.getOrNull(1)?.padEnd(1, '0') ?: "0"

    return "${feet}'${inch}\""
}



