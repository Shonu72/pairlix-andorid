package com.pairlix.dating.view.M7

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pairlix.dating.LanguageManager.LanguageManager
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.response.HelpResponse
import com.pairlix.dating.response.SafetyAndSupportResponse
import com.pairlix.dating.viewModel.M7ViewModel

@Composable
fun SafetyAndSupportScreen(navController: NavController, m7ViewModel: M7ViewModel) {
    val context = LocalContext.current
    val safety by m7ViewModel.getSafetySupport.collectAsState()

    var safetyData = remember { mutableStateListOf<SafetyAndSupportResponse.Data?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    /* LaunchedEffect(Unit) {

         m7ViewModel.hitGetSafetySupport(
             token = SharedPreference.get(context).accessToken,
             lang = "en"
         )
     }
 */

    val languageManager = LocalLanguageManager.current
    LaunchedEffect(languageManager.currentLanguage) {

        m7ViewModel.hitGetSafetySupport(
            token = SharedPreference.get(context).accessToken,
            lang = if (languageManager.currentLanguage == "ar") "ar" else "en"
        )
    }
    LaunchedEffect(safety) {

        safety.let { state ->

            when (state) {

                is EmpResource.Loading -> {
                    // loader
                }

                is EmpResource.Success -> {

                    CustomLoader.hideLoader()

                    safetyData.clear()
                    safetyData.addAll(state.value.data?.filterNotNull() ?: emptyList())

                    m7ViewModel.resetGetSafetySupport()
                }

                is EmpResource.Failure -> {

                    CustomLoader.hideLoader()

                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }

                    m7ViewModel.resetGetSafetySupport()
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


            TopBackBtnHeading(navController, stringResource(R.string.safety_support))
            verticalSpace(10)
            LazyColumn {
                items(safetyData) {

                    if (it != null) {
                        TermAndContionCard(it)
                    }

                    verticalSpace(20)
                }
            }

        }


    }

}

@Composable
private fun TermAndContionCard(data: SafetyAndSupportResponse.Data) {
    var isLoading by remember { mutableStateOf(true) }

    val languageManager = LocalLanguageManager.current

    Text(
        text = data.title ?: "",
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_medium))
    )

    verticalSpace(5)


    Text(
        text = data.description ?: "",
        color = Color(0xFF6D6D6D),
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_medium))
    )
    verticalSpace(10)

    if (!data.image.isNullOrEmpty()) {

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)) {
            AsyncImage(
                model = data.image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.no_dp_icon),
                error = painterResource(R.drawable.no_dp_icon)
            )

        }
    }

}