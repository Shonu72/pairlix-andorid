package com.pairlix.dating.view.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.updragePlan.GoldPlanCardCurrent
import com.pairlix.dating.view.updragePlan.PlatinumPlanCard
import com.pairlix.dating.viewModel.AuthViewModel
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentPlaDetailsScreen(navController: NavController, viewModel: AuthViewModel) {
    var autoRenew by remember { mutableStateOf(true) }



    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current



    Box(
        modifier = Modifier
            .fillMaxSize()
           .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TopBackBtnHeading(
                navController,
                text = stringResource(R.string.your_current_plan),
            )

            verticalSpace(15)

            val scrollState = rememberScrollState()


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoldPlanCardCurrent(onClick = { })
                verticalSpace(10)

                Spacer(Modifier.height(25.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.auto_renewal),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                        color = Color.Black
                    )
                    Box(modifier = Modifier.scale(0.8f)) {
                        Switch(
                            modifier = Modifier,
                            checked = autoRenew, onCheckedChange = { autoRenew = it })
                    }


                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.turn_on_auto_renew_to_keep_your_subscription_active_without_any_hassle),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = Color(0xFF6D6D6D),
                        lineHeight = 16.sp
                    )


                }
                Spacer(Modifier.height(20.dp))

                AppButton(
                    modifier = Modifier, text = stringResource(R.string.upgrade_aed_160), onClick = {})
                verticalSpace(16)


               /* Text(
                    modifier = Modifier
                        .fillMaxWidth().clip(shape = RoundedCornerShape(52.dp))
                        .border(1.dp, Color(0xFFEF505F),
                            shape = RoundedCornerShape(52.dp))
                        .background(Color.White,
                            shape = RoundedCornerShape(52.dp)).padding(vertical = 18.dp),
                    text = "Cancel Subscription",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    color = Color(0xFFEF505F),
                    textAlign = TextAlign.Center
                )*/
                verticalSpace(20)


            }

        }}}