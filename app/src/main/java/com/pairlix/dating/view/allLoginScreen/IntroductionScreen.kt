package com.pairlix.dating.view.allLoginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.PagerIndicator
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun IntroductionScreen(navController: NavController) {


    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val scope = rememberCoroutineScope()
val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize().background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)

    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->

            when (page) {
                0 -> TutorialScreenOne(navController)
                1 -> TutorialScreenTwo(navController)
                2 -> TutorialScreenThree(navController)
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PagerIndicator(
                totalDots = 3,
                selectedIndex = pagerState.currentPage
            )
        }

        verticalSpace(20)
        AppButton(
            text = stringResource(R.string.next), modifier = Modifier.padding(bottom = 10.dp),
            onClick = {
                if (pagerState.currentPage < 2) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }

                } else {
                    SharedPreference.get(context).isFirstLaunch = false
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }

                }

            })

    }
}