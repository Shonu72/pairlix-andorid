package com.pairlix.dating.view.updragePlan

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.response.GetPlansResponse
import com.pairlix.dating.viewModel.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPlanScreen(navController: NavController,viewModel: AuthViewModel) {
    val showDialog = remember { mutableStateOf(false) }



    val context= LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current

    LaunchedEffect (Unit) {

        viewModel.hitGetPlans(
            access_token = SharedPreference.get(context).accessToken
        )
     /*   PlanScreenObserver(
            viewModel = viewModel,
            context = context,
            lifecycleOwner = lifecycleOwner,
            navController = navController,
            onSuccess = { it->
                viewModel.getPlansList.clear()
                it?.let {
                    viewModel.getPlansList.addAll(it)
                }

            }
        )*/
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            TopBackBtnHeading(
                navController,
                text = stringResource(R.string.select_plans),
            )
            verticalSpace(15)

            val scrollState = rememberScrollState()
            val planStack = remember { mutableStateListOf(PlanType.GOLD, PlanType.PLATINUM) }
            val backHeaderHeight = 96.dp
            val frontOffset = 48.dp
            val frontWidthFraction = 0.92f
            val backWidthFraction = 0.82f

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 80.dp), // space so content doesn’t hide behind button
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .heightIn(min = 280.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    planStack.forEachIndexed { index, planType ->
                        val isFront = index == planStack.lastIndex

                        val offsetY by animateDpAsState(
                            targetValue = if (isFront) frontOffset else 0.dp,
                            label = "offsetY"
                        )
                        val scale by animateFloatAsState(
                            targetValue = if (isFront) 1f else 0.99f,
                            label = "scale"
                        )

                        val baseModifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .offset(y = offsetY)
                            .zIndex(if (isFront) 2f else 1f)
                            .align(Alignment.TopCenter)

                        val cardModifier = if (isFront) {
                            baseModifier.fillMaxWidth(frontWidthFraction)
                        } else {
                            baseModifier
                                .fillMaxWidth(backWidthFraction)
                                .height(backHeaderHeight)
                                .clip(RoundedCornerShape(20.dp))
                        }
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                planStack.reverse()
                            }

                        when (planType) {
                            PlanType.GOLD -> GoldPlan(
                                modifier = cardModifier, compact = !isFront,

                            )
                            PlanType.PLATINUM -> PlatinumPlanCard(
                                modifier = cardModifier,
                                compact = !isFront
                            )
                        }
                    }
                }
            }
        }

        AppButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            text = stringResource(R.string.upgrade_plan),
            onClick = {
                showDialog.value = !showDialog.value
            }
        )
        if (showDialog.value) {
            LaunchedEffect(Unit) {
                delay(3000)
                showDialog.value = false
            }

//            AlertDialog(
//                onDismissRequest = {
//                    showDialog.value = false
//                },
//                modifier = Modifier,
//                properties = DialogProperties(
//                    dismissOnClickOutside = false
//                ),
//                content = {
//                    Column(
//                        modifier = Modifier
//                            .padding(horizontal = 16.dp)
//                            .fillMaxWidth()
//                            .background(
//                                color = Color.White,
//                                RoundedCornerShape(12.dp)
//                            )
//                            .padding(vertical = 50.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.dialog_ic),
//                            contentDescription = "",
//                            modifier = Modifier.size(80.dp)
//                        )
//                        Spacer(modifier = Modifier.height(24.dp))
//                        Text(
//                            text = "Plan Purchased Successful",
//                            fontSize = 18.sp,
//                            color = Color(0xffCC000000),
//                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
//                        )
//                    }
//                }
//            )
        }
    }
}


enum class PlanType(
    val title: String,
    val price: String
) {
    GOLD(
        title = "GOLD Plan",
        price = "AED 200 / Month"
    ),
    PLATINUM(
        title = "Platinum Plan",
        price = "AED 200 / Month"
    )
}

@Composable
fun GoldPlan(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Card(
        modifier = modifier,
        shape = if (compact) RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 20.dp
        ) else RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (compact) 2.dp else 8.dp),
        colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface)
    ) {
        if (compact) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFBA55),
                                Color(0xFFFFF1DA),
                                Color(0xFFFFCF80)
                            )
                        )
                    )
                    .padding(top = 20.dp, start = 8.dp)
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.no_plan),
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                        color = Color(0xFF590988),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier,
                    )
                }
            }
        } else {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFFFBA55),
                                    Color(0xFFFFF1DA),
                                    Color(0xFFFFCF80),
                                    Color(0xFFFFDEAD),
                                    Color(0xFFFFDF9C),
                                )
                            ),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.gold),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                            color = Color(0xFF5A2D9C),
                            modifier = Modifier.padding(top = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = " / Month",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = Color(0xFF000000)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Plan Detailsx",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color(0xFF6D6D6D)
                    )

                    Spacer(modifier = Modifier.height(16.dp))



                    PlanBullet3("Chat feature enabled for matched users.")
                    Spacer(modifier = Modifier.height(16.dp))

                    PlanBullet3("Includes 1 profile boost per month to enhance visibility in search results.")
                    Spacer(modifier = Modifier.height(16.dp))

                    PlanBullet3("10 Super likes.")
                }
            }
        }
    }
}



@Composable
fun PlatinumPlanCard(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = if (compact) RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ) else RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (compact) 2.dp else 10.dp),
        colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface)
    ) {
        if (compact) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFE6E6E6),
                                Color(0xFFEAE9E9),
                                Color(0xFFFFFFFF),
                                Color(0xFF808080),
                            )
                        )
                    )
                    .padding(top = 20.dp, start = 8.dp)
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Platinum Plan",
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                        color = Color(0xFF590988),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier,
                    )
                }
            }
        } else {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFFE4E4E4), Color(0xFFF8F8F8), Color(0xFFC0C0C0))
                            ),
                            shape = RoundedCornerShape(topEnd = 16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Platinum Plan",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                            color = Color(0xFF7D47C5),
                            modifier = Modifier.padding(top = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "AED 200 / Month",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = Color(0xFF000000)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Plan Details",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color(0xFF6D6D6D)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PlanBullet3("Unlimited profile likes with full discovery access.")
                    Spacer(modifier = Modifier.height(16.dp))

                    PlanBullet3("Chat feature enabled for matched users.")
                    Spacer(modifier = Modifier.height(16.dp))

                    PlanBullet3("Includes 1 profile boost per month to enhance visibility in search results.")
                    Spacer(modifier = Modifier.height(16.dp))

                    PlanBullet3("10 Super likes.")
                }
            }
        }
    }
}


@Composable
fun PlanBullet3(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier
                .size(14.dp)
                .padding(top = 2.dp),
            tint = Color(0xFFFFC107)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
            color = Color(0xFF000000),
            lineHeight = 18.sp
        )
    }
}



