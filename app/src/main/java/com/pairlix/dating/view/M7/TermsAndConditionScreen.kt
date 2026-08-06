package com.pairlix.dating.view.M7

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.response.TermsAndConditionResponse
import com.pairlix.dating.viewModel.M7ViewModel

@Composable
fun TermsAndConditionScreen(navController: NavController, m7ViewModel: M7ViewModel) {

    val context = LocalContext.current
    val terms by m7ViewModel.termAndCondition.collectAsState()

    var termsData = remember { mutableStateOf<TermsAndConditionResponse?>(null) }

    /*LaunchedEffect(Unit) {

        m7ViewModel.hitTermAndCondition(
            token = SharedPreference.get(context).accessToken,
            lang = "en"
        )
    }*/

    val languageManager = LocalLanguageManager.current
    LaunchedEffect(languageManager.currentLanguage) {

        m7ViewModel.hitTermAndCondition(
            token = SharedPreference.get(context).accessToken,
            lang = if (languageManager.currentLanguage == "ar") "ar" else "en"
        )
    }
    LaunchedEffect(terms) {

        terms.let { state ->

            when (state) {

                is EmpResource.Loading -> {}

                is EmpResource.Success -> {

                    CustomLoader.hideLoader()

                    termsData.value = state.value

                    m7ViewModel.resetTermAndCondition()
                }

                is EmpResource.Failure -> {

                    CustomLoader.hideLoader()

                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }

                    m7ViewModel.resetTermAndCondition()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val maxHeight = this.maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {

            TopBackBtnHeading(navController, stringResource(R.string.terms_and_conditions))

            HtmlWebView(
                htmlText = termsData.value?.data ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )


        }


    }

}