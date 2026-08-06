package com.pairlix.dating.view.M7

import android.webkit.WebView
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.response.PrivacyResponse
import com.pairlix.dating.viewModel.M7ViewModel
import androidx.core.text.HtmlCompat
import com.pairlix.dating.LanguageManager.LocalLanguageManager

@Composable
fun PrivacyAndPolicyScreen(navController: NavController, m7ViewModel: M7ViewModel) {

    val context = LocalContext.current
    val privacy by m7ViewModel.getPrivacy.collectAsState()
    var privacyData = remember { mutableStateOf<PrivacyResponse?>(null) }
    val htmlText = privacyData.value?.data ?: ""
    val plainText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    /* LaunchedEffect (Unit) {
         m7ViewModel.hitGetPrivacy(
             token = SharedPreference.get(context).accessToken,
             lang = "en"
         )
     }*/


    val languageManager = LocalLanguageManager.current
    LaunchedEffect(languageManager.currentLanguage) {

        m7ViewModel.hitGetPrivacy(
            token = SharedPreference.get(context).accessToken,
            lang = if (languageManager.currentLanguage == "ar") "ar" else "en"
        )
    }


    LaunchedEffect(privacy) {

        privacy.let { state ->

            when (state) {

                is EmpResource.Loading -> {}

                is EmpResource.Success -> {

                    CustomLoader.hideLoader()

                    privacyData.value = state.value

                    m7ViewModel.resetGetPrivacy()
                }

                is EmpResource.Failure -> {

                    CustomLoader.hideLoader()

                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }

                    m7ViewModel.resetGetPrivacy()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        val maxHeight = this.maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)

        ) {


            TopBackBtnHeading(navController, stringResource(R.string.privacy_policy))


            HtmlWebView(
                htmlText = privacyData.value?.data ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )




        }


    }

}

@Composable
fun HtmlWebView(
    htmlText: String,
    modifier: Modifier = Modifier
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
            }
        },
        update = { webView ->

            val htmlData = """
                <html>
                <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body{
                        font-size:16px;
                        color:#6D6D6D;
                        padding:16px;
                        line-height:1.7;
                        font-family: sans-serif;
                    }
                    h2{font-size:20px;}
                    h3{font-size:18px;}
                    ul{padding-left:20px;}
                </style>
                </head>
                <body dir="auto">
                $htmlText
                </body>
                </html>
            """.trimIndent()

            webView.loadDataWithBaseURL(
                null,
                htmlData,
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}