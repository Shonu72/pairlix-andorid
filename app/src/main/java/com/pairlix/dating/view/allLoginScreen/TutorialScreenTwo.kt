package com.pairlix.dating.view.allLoginScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.PagerIndicator
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import java.nio.file.WatchEvent

@Composable
fun TutorialScreenTwo(navController: NavController) {
    val context= LocalContext.current
    val languageManager = LocalLanguageManager.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
           MaterialTheme.colorScheme.background
            )
            .navigationBarsPadding()
    ) {

        val maxHeight = this.maxHeight
        val pagerState = remember { mutableStateOf(1) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier

                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { navController.navigate(Screen.LoginScreen.route) }
                        .border(1.dp, Color(0xFF590988), RoundedCornerShape(20.dp))
                        .padding(horizontal = 30.dp, vertical = 12.dp),
                    text = stringResource(R.string.skip),
                    textAlign = TextAlign.End,
                    color = Color(0xFF590988),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                )
            }

            verticalSpace(20)
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                painter = painterResource(R.drawable.men_image_tutorial2),
                contentDescription = "img"
            )

            verticalSpace(30,)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.every_profile_is_verified),
                textAlign = TextAlign.Center,
                color = Color(0xFF590988),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_bold))

            )
            verticalSpace(10)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.every_person_is_real),
                textAlign = TextAlign.Center,
                color = Color(0xFF590988),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_bold))
            )

            verticalSpace(25)

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                if (languageManager.currentLanguage == "en") {


                    Image(
                        modifier = Modifier.size(110.dp),
                        painter = painterResource(R.drawable.supporting_badge),
                        contentDescription = "img"
                    )
                }
                else{

                    Image(
                        modifier = Modifier.size(110.dp),
                        painter = painterResource(R.drawable.badge_ar),
                        contentDescription = "img"
                    )
                }
                if (languageManager.currentLanguage == "en"){

                    Image(
                    modifier = Modifier.size(110.dp),
                    painter = painterResource(R.drawable.government_id),
                    contentDescription = "img"
                )
                    }
                else{
                    Image(
                        modifier = Modifier.size(110.dp),
                        painter = painterResource(R.drawable.tick_ar),
                        contentDescription = "img"
                    )
                }

                if (languageManager.currentLanguage == "en"){

                    Image(
                    modifier = Modifier.size(110.dp),
                    painter = painterResource(R.drawable.live_face_scan),
                    contentDescription = "img"
                )

                }
                else{
                    Image(
                        modifier = Modifier.size(110.dp),
                        painter = painterResource(R.drawable.face_ar),
                        contentDescription = "img"
                    )

                }
            }


            verticalSpace(20)

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

                if (languageManager.currentLanguage == "en"){

                    Image(
                    modifier = Modifier.size(110.dp),
                    painter = painterResource(R.drawable.ai_protected),
                    contentDescription = "img"
                )
                }
                else{
                    Image(
                        modifier = Modifier.size(110.dp),
                        painter = painterResource(R.drawable.key_ar),
                        contentDescription = "img"
                    )
                }

                if (languageManager.currentLanguage == "en"){

                    Image(
                    modifier = Modifier.size(110.dp),
                    painter = painterResource(R.drawable.zero_tolerance),
                    contentDescription = "img"
                )}
                else{
                    Image(
                        modifier = Modifier.size(110.dp),
                        painter = painterResource(R.drawable.zero_tolerance_ar),
                        contentDescription = "img"
                    )
                }

            }

        }

    }
}