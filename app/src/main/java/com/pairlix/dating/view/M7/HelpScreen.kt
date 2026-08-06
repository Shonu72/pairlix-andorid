package com.pairlix.dating.view.M7

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.response.HelpResponse
import com.pairlix.dating.viewModel.M7ViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.pairlix.dating.LanguageManager.LocalLanguageManager

@Composable
fun HelpScreen(navController: NavController, m7ViewModel: M7ViewModel) {
val context = LocalContext.current
    val help by m7ViewModel.getHelp.collectAsState()

    var helpData = remember { mutableStateOf<HelpResponse.Data?>(null) }

   /* LaunchedEffect(Unit) {

        m7ViewModel.hitGetHelp(
            token = SharedPreference.get(context).accessToken,
            lang = "en"
        )
    }
*/

    val languageManager = LocalLanguageManager.current
    LaunchedEffect(languageManager.currentLanguage) {
        m7ViewModel.hitGetHelp(
            token = SharedPreference.get(context).accessToken,
            lang = if (languageManager.currentLanguage == "ar") "ar" else "en"
        )
    }

    LaunchedEffect(help) {

        help.let { state ->

            when (state) {

                is EmpResource.Loading -> {
                    // loader
                }

                is EmpResource.Success -> {

                    CustomLoader.hideLoader()

                    helpData.value = state.value.data

                    m7ViewModel.resetGetHelp()
                }

                is EmpResource.Failure -> {

                    CustomLoader.hideLoader()

                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }

                    m7ViewModel.resetGetHelp()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier
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


            TopBackBtnHeading(navController, stringResource(R.string.help))

            verticalSpace(30)

            Text(
                text =helpData.value?.description?:"" ,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )

            verticalSpace(20)


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Color(0xFFE6E6E6), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.call_ic_b),
                    contentDescription = "call",
                    modifier = Modifier.size(20.dp)
                )

                horizontalSpace(5)
                Column(modifier = Modifier) {

                    Text(
                        text = stringResource(R.string.call),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )

                    verticalSpace(5)

                    Text(
                        text = helpData?.value?.phoneNumber?:"",
                        color = Color(0xFF6D6D6D),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )


                }
            }

            verticalSpace(20)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Color(0xFFE6E6E6), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.message_email_ic),
                    contentDescription = "mess",
                    modifier = Modifier.size(20.dp)
                )

                horizontalSpace(5)
                Column(modifier = Modifier) {

                    Text(
                        text = stringResource(R.string.email),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )

                    verticalSpace(5)

                    Text(
                        text = helpData?.value?.email?:"",
                        color = Color(0xFF6D6D6D),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )


                }
            }


        }


    }

}