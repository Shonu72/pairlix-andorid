package com.pairlix.dating.view.allLoginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import ir.kaaveh.sdpcompose.sdp

@Composable
fun IntroductionScreen(navController: NavController) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(R.drawable.intro_bg), // Ensure this file exists in res/drawable
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Overlay for visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Gradient Scrim at the bottom for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 0.5f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Logo and App Name
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
                Image(
                    painter = painterResource(R.drawable.app_logo_white),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(84.sdp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "Pairlix",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y = (-35).dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.shariah_aligned),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.matchmaking_for_marriage),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                    textAlign = TextAlign.Center
                )

                verticalSpace(40)

                AppButton(
                    text = "Find Your Match",
                    onClick = {
                        SharedPreference.get(context).isFirstLaunch = false
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )

                verticalSpace(10)

                val annotatedText = buildAnnotatedString {
                    append("By continuing, you agree to our ")

                    pushStringAnnotation(tag = "POLICY", annotation = "policy")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            textDecoration = TextDecoration.Underline,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold))
                        )
                    ) {
                        append("Privacy Policy")
                    }
                    pop()

                    append(" & ")

                    pushStringAnnotation(tag = "TERMS", annotation = "terms")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            textDecoration = TextDecoration.Underline,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold))
                        )
                    ) {
                        append("Terms & Condition")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedText,
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        textAlign = TextAlign.Center
                    ),
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(tag = "POLICY", start = offset, end = offset)
                            .firstOrNull()?.let {
                                navController.navigate(Screen.PrivacyAndPolicyScreen.route)
                            }

                        annotatedText.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                            .firstOrNull()?.let {
                                navController.navigate(Screen.TermsAndConditionScreen.route)
                            }
                    }
                )
            }
        }
    }
}
