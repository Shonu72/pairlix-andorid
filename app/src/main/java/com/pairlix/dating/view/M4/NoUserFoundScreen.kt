package com.pairlix.dating.view.M4

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.navigation.Screen
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource

@Composable
fun NoProfileFoundScreen(navController: NavController) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
           .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()

            .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.statusBars)
            .statusBarsPadding()
    ) {
        val maxHeight = this.maxHeight
        val pagerState = remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {


            verticalSpace(20)

            Image(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                painter = painterResource(R.drawable.no_profile_found),
                contentDescription = "img"
            )

            verticalSpace(30,true)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.you_ve_seen_the_all_the_profiles),
                textAlign = TextAlign.Center,
                color = Color(0xFF000000),
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_bold))

            )
            verticalSpace(10)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.update_your),
                textAlign = TextAlign.Center,
                color = Color(0xFF000000),
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_bold))
            )

            verticalSpace(15)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.interests_to_explore_more),
                textAlign = TextAlign.Center,
                color = Color(0xFF6D6D6D),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )


            verticalSpace(30)
        }

        AppButton(
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 10.dp),
            text = stringResource(R.string.next),
            onClick = {
                    navController.navigate(Screen.ViewProfileScreen.route){
                        popUpTo(Screen.MainScreen.route){inclusive=true}

                    }

            }
        )
    }


}