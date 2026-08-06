package com.pairlix.dating.view.newAccountRegistrationScreen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomInputField
import com.pairlix.dating.ReusedComponents.FormProgressBar
import com.pairlix.dating.ReusedComponents.GradientExpandableCardIndex
import com.pairlix.dating.ReusedComponents.GradientExpandableCardMultipleSelectIndexSearch
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.noInitialSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest2
import com.pairlix.dating.viewModel.AuthViewModel


@Composable
fun CompleteProfileScreen2(navController: NavController, viewModel: AuthViewModel) {
    var selectedBelongTo by rememberSaveable { mutableIntStateOf(-1) }
    var selectedLanguageIndexStrings by rememberSaveable {
        mutableStateOf(listOf<String>())
    }
    var selectedInterestIndex by rememberSaveable { mutableIntStateOf(-1) }   // -1 means nothing selected


    var customSectText by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
 /*   val belongList = listOf(
        "Suni",
        "Shia",
        "Prefer Not To Say",
        "Other"
    )
    val spokenLanguagesList = listOf(
        "Afrikaans",
        "Albanian",
        "Amharic",
        "Arabic (Modern Standard)",
        "Armenian",
        "Assamese",
        "Azerbaijani",
        "Bahasa Indonesia",
        "Bahasa Melayu (Malay)",
        "Balochi",
        "Bengali",
        "Berber (Kabyle)",
        "Berber (Tamazight)",
        "Bhojpuri",
        "Bosnian",
        "Bulgarian",
        "Burmese (Myanmar)",
        "Cantonese",
        "Cebuano",
        "Chittagonian",
        "Chinese (Mandarin)",
        "Croatian",
        "Czech",
        "Danish",
        "Dari",
        "Dzongkha (Bhutan)",
        "English (Canada)",
        "English (UK)",
        "English (US)",
        "Filipino (Tagalog)",
        "Fijian",
        "Finnish",
        "French",
        "French (Canada)",
        "German",
        "Greek",
        "Gujarati",
        "Guarani",
        "Gulf Arabic",
        "Haitian Creole",
        "Haryanvi",
        "Hausa",
        "Hebrew",
        "Hindi",
        "Hokkien",
        "Hungarian",
        "Igbo",
        "Irish Gaelic",
        "Italian",
        "Japanese",
        "Javanese",
        "Kannada",
        "Kashmiri",
        "Khmer (Cambodian)",
        "Konkani",
        "Korean",
        "Kurdish (Badini)",
        "Kurdish (Kurmanji)",
        "Kurdish (Sorani)",
        "Kurdish (Zazaki)",
        "Lao",
        "Levantine Arabic",
        "Magahi",
        "Malayalam",
        "Malagasy",
        "Maori (New Zealand)",
        "Marathi",
        "Mongolian",
        "Nepali",
        "Navajo",
        "Norwegian",
        "Odia",
        "Pashto",
        "Persian (Farsi)",
        "Polish",
        "Portuguese",
        "Portuguese (Brazil)",
        "Punjabi (India)",
        "Punjabi (Pakistan)",
        "Quechua",
        "Rohingya",
        "Romanian",
        "Russian",
        "Saraiki",
        "Samoan",
        "Scottish Gaelic",
        "Serbian",
        "Shona",
        "Sindhi",
        "Sindhi (India)",
        "Sinhala (Sri Lanka)",
        "Slovak",
        "Somali",
        "Spanish",
        "Spanish (Latin America)",
        "Sudanese Arabic",
        "Sundanese",
        "Swahili",
        "Swedish",
        "Tamil",
        "Telugu",
        "Thai",
        "Tibetan",
        "Tigrinya",
        "Tok Pisin (Papuan)",
        "Tongan",
        "Turkish",
        "Ukrainian",
        "Urdu",
        "Urdu (India)",
        "Vietnamese",
        "Welsh",
        "Yemeni Arabic",
        "Yoruba",
        "Zulu"
    )
    val interestedList = listOf("Men", "Female", "Everyone",)

*/


    val belongList = stringArrayResource(R.array.belong_list).toList()
    val interestedList = stringArrayResource(R.array.interested_list).toList()
    val spokenLanguagesList = stringArrayResource(id = R.array.spoken_languages).toList()

    LaunchedEffect(Unit) {
        createAccountStep2Observer(
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
               .background(MaterialTheme .colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)

        ) {


            TopBackBtnHeading(navController, stringResource( R.string.complete_profile))
            val scrollState = rememberScrollState()
            verticalSpace(20)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                FormProgressBar(
                    currentPage = 1.0,
                    percentage = "20"
                       // SharedPreference.get(context).profileCompletionPercentage
                )


                verticalSpace(20)



                GradientExpandableCardIndex(
                    title = stringResource(R.string.interested_in),
                    items = interestedList,
                    selectedIndex = selectedInterestIndex,
                    onItemSelected = { index -> selectedInterestIndex = index }
                )


                verticalSpace(20)

                val titleText = buildAnnotatedString {
                    append(stringResource(R.string.spoken_language))

                    withStyle(
                        style = SpanStyle(
                            fontSize = 10.sp,          // small size
                            color = Color(0xFF6D6D6D)       // optional
                        )
                    ) {
                        append(stringResource(R.string.select_up_to_10))
                    }
                }


                GradientExpandableCardMultipleSelectIndexSearch(

                    title = titleText.toString(),
                    items = spokenLanguagesList,
                    selectedIndexes = selectedLanguageIndexStrings,
                    onSelectionChange = { selectedLanguageIndexStrings = it }
                )




             /*   GradientExpandableCardMultipleSelectIndexSearch(
                    title = titleText.toString(),
                    items = spokenLanguagesList,
                    selectedIndexes = selectedLanguageIndexStrings,
                    onSelectionChange = { newList ->
                        if (newList.size > 10) {
                            context.showToast("You can select up to 10 languages only")
                        } else {
                            selectedLanguageIndexStrings = newList
                        }
                    }

                )*/




                verticalSpace(20)

                GradientExpandableCardIndex(
                    title = stringResource(R.string.what_sect_do_you_belong_to),
                    items = belongList,
                    selectedIndex = selectedBelongTo,
                    onItemSelected = { selectedBelongTo = it }
                )

                verticalSpace(30)

                if (selectedBelongTo == 3) {        // Other selected
                    CustomInputField(
                        heading = stringResource(R.string.what_sect_do_you_belong_to),
                        placeholder = stringResource(R.string.type_your_sect),
                        value = customSectText,
                        onValueChange = { customSectText = onlyAlphabetsNoInitial(it)}
                    )
                }
                verticalSpace(20)

                AppButton(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = stringResource(R.string.next),
                    onClick = {


                        if (selectedInterestIndex == -1) {
                            context.showToast(context.getString(R.string.please_select_your_interest))   // 🔥 Toast
                            return@AppButton
                        }

                        if (selectedLanguageIndexStrings.isEmpty()) {
                            context.showToast(context.getString(R.string.please_select_at_least_1_language))  // 🔥 Toast
                            return@AppButton
                        }

                        if (selectedBelongTo == -1) {
                            context.showToast(context.getString(R.string.please_select_what_sect_you_belong_to))  // 🔥 Toast
                            return@AppButton
                        }

                        if (selectedBelongTo == 3 && customSectText.isBlank()) {
                            context.showToast(context.getString(R.string.please_enter_your_sect))
                            return@AppButton
                        }


                        val sectValue = selectedBelongTo.toString()
                        val customSectValue = if (selectedBelongTo == 3) customSectText else null


                        viewModel.hitCompleteProfile2(
                            access_token = SharedPreference.get(context).accessToken,
                            request = CompleteProfileRequest2(
                                step = 2,
                                data = CompleteProfileRequest2.Data(
                                    interestedIn = selectedInterestIndex.toString(),
                                    spokenLanguages = selectedLanguageIndexStrings,
                                    sect = sectValue,
                                    customSect = customSectValue
                                )
                            )
                        )
                    }
                )

            }
        }

    }
}


fun createAccountStep2Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController
) {

    viewModel.completeProfile2.observe(lifecycleOwner) { state ->
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

                    SharedPreference.get(context).profileCompletionPercentage =
                        state.value.data?.profileCompletionPercentage.toString()
                    navController.navigate(Screen.CompleteProfile3.route)
                    state.value.success = false
                }
            }
            EmpResource.Idle -> {}

        }
    }

}


/*
fun onlyAlphabetsNoInitial(input: String): String {
    // Remove everything except A–Z & a–z
    val filtered = input.filter { it.isLetter() }

    // Prevent non-alphabet at the start
    return filtered.dropWhile { !it.isLetter() }
}*/

fun onlyAlphabetsNoInitial(input: String): String {
    if (input.isEmpty()) return input

    return buildString {
        input.forEachIndexed { index, ch ->
            when {
                ch.isLetter() -> append(ch)          // ✅ alphabets always allowed
                ch == ' ' && index != 0 -> append(ch) // ✅ space allowed except first
            }
        }
    }
}
