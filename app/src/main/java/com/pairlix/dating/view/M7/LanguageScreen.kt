package com.pairlix.dating.view.M7

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.amplifyframework.core.configuration.AmplifyOutputsData
import com.gravito.waiter_.Localization.localizedString
import com.pairlix.dating.LanguageManager.AppLanguageManager
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.LanguageRow
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.UpdateLanguageRequest
import com.pairlix.dating.view.allLoginScreen.AppLanguage
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M7ViewModel


@Composable
fun SettingLanguageScreen(navController: NavController,m7ViewModel: M7ViewModel,viewModel: AuthViewModel,viewModelM4:M4ViewModel) {

    val context = LocalContext.current
    val languageManager = LocalLanguageManager.current
    val updateLanguage by m7ViewModel.updateLanguage.collectAsState()
    val currentLanguage = AppLanguageManager.currentLanguage
    var selectedId by remember(currentLanguage) {
        mutableStateOf(if (currentLanguage == "ar") 2 else 1)
    }

    var permissionsHandled by remember { mutableStateOf(false) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        permissionsHandled = true
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.statusBars)
            .statusBarsPadding()
    ) {
        val maxHeight = this.maxHeight
        var isBackClicked by remember { mutableStateOf(false) }

        val languages = listOf(
            AppLanguage(1, "English", R.drawable.english_lang_flag_ic),
            AppLanguage(2, "عربي", R.drawable.saudi_flag),
        )



        LaunchedEffect(updateLanguage) {
            updateLanguage.let {
                when (it) {
                    is EmpResource.Loading -> {}
                    is EmpResource.Success -> {
                        CustomLoader.hideLoader()
                        //context.showToast(it.value.message?:"")
                        m7ViewModel.resetUpdateLanguage()
                    }

                    is EmpResource.Failure -> {
                        CustomLoader.hideLoader()
                        it.throwable.let { err -> ErrorUtil.handlerGeneralError(context, err) }
                        m7ViewModel.resetUpdateLanguage()
                    }

                    EmpResource.Idle -> {
                        CustomLoader.hideLoader()
                    }
                }
            }
        }





        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 20.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.TopStart)
                            .size(35.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .clickable(enabled = !isBackClicked) {
                                isBackClicked = true
                                navController.popBackStack()
                            },
                        painter = painterResource(R.drawable.back_icon),
                        contentDescription = "back_ic"
                    )

                Text(
                    text = localizedString(R.string.change_language),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            languages.forEach { lang ->
                LanguageRow(
                    item = lang,
                    isSelected = lang.id == selectedId
                ) { id ->
                    selectedId = id
                    // ✅ Only update UI text instantly — no AppLanguageManager here
                    val newLanguage = if (id == 1) "en" else "ar"
                    languageManager.setLanguage(newLanguage)
                }
            }

            Spacer(modifier = Modifier.height(0.dp))
        }

        AppButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            text = localizedString(R.string.Continue),
            onClick = {

                val newLanguage = if (selectedId == 1) "en" else "ar"
                AppLanguageManager.setLanguage(context, newLanguage)
                languageManager.setLanguage(newLanguage)

                viewModel.hitGetMatch(
                    accessToken = SharedPreference.get(context).accessToken,
                    filter = viewModelM4.currentFilterRequest.value
                )
                m7ViewModel.hitUpdateLanguage(
                    access_token = SharedPreference.get(context).accessToken,
                    request = UpdateLanguageRequest(
                        language = newLanguage
                    ),
                )

                navController.popBackStack()
                (context as MainActivity).recreate()


            })
    }
}

data class AppLanguage(
    val id: Int,
    val name: String,
    val flagRes: Int
)
