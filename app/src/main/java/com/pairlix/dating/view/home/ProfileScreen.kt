package com.pairlix.dating.view.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.ThemeManager.LocalThemeManager
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.M4.FilterEditType
import com.pairlix.dating.view.profileDetails.MaritalStatusBottomSheet
import com.pairlix.dating.view.updragePlan.ActivePlanObserver
import com.pairlix.dating.view.updragePlan.getDaysLeft
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.graphicsLayer
fun shareApp(context: Context) {

    val shareIntent = Intent(Intent.ACTION_SEND).apply {

        type = "text/plain"

        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this amazing app: https://play.google.com/store/apps/details?id=${context.packageName}"
        )
    }

    context.startActivity(
        Intent.createChooser(shareIntent, "Share App")
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel,m4ViewModel: M4ViewModel,socketViewModel: SocketViewModel= hiltViewModel()) {

    val context = LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current
    val themeManager = LocalThemeManager.current // Get ThemeManager
    var showBottomSheet by remember { mutableStateOf(false) }
    var themeIndex by remember {
        mutableIntStateOf(themeManager.themeMode) // Use ThemeManager's current theme
    }

    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl


    if(showBottomSheet){

        ModalBottomSheet(onDismissRequest = {showBottomSheet=false}, dragHandle = null) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF14590988))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(R.string.theme),
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                        modifier = Modifier.weight(1f)
                    )

                    Image(
                        painter = painterResource(R.drawable.cross_pruple_ic),
                        contentDescription = "close",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() })
                            {
                              showBottomSheet=false}

                    )


                }


            MaritalStatusBottomSheet(
                item = listOf<String>(stringResource(R.string.light_mode), stringResource(R.string.dark_mode),stringResource(R.string.as_per_os)),
                selectedIndex = themeIndex,
                onItemSelected = {
                    themeIndex=it
                },
                allowUnselect = false,
                onDone = {
                    SharedPreference.get(context).themeMode = themeIndex

                    themeManager.themeMode = themeIndex

                    showBottomSheet = false
                }

            )

        }

        }



    }




    LaunchedEffect(Unit){

    viewModel.hitGetActivePlan(
        access_token = SharedPreference.get(context).accessToken
    )
    ActivePlanObserver(
        viewModel = viewModel,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            it.let {
                viewModel.activePlanData.value = it
            }


        }
    )
}
    LaunchedEffect(key1=Unit) {
        viewModel.hitGetHomeProfile(
            access_token = SharedPreference.get(context).accessToken
        )
    }

    HomePageObserver(
        viewModel = viewModel,
        m4ViewModel = m4ViewModel,
        context = context,
        lifecycleOwner = lifecycleOwner,
        navController = navController,socketViewModel
    )
    val data = viewModel.activePlanData.value
    val homeData = viewModel.getHomePageData.value
    val rawExpiry = data?.expiredOn?.toString() ?: ""
    val daysLeft = getDaysLeft(rawExpiry)
    val expireText = when {
        daysLeft < 0 -> {
            stringResource(R.string.expired)
        }
        daysLeft == 0L -> {
            stringResource(R.string.expire_today)
        }
        else -> {
            stringResource(R.string.expire_in_days, daysLeft)
        }
    }


    val rowList = listOf(
        SideScreenRow(R.drawable.eye_icon, stringResource(R.string.visibility_control)) {
            navController.navigate(Screen.VisibilityControlScreen.route)
        },

        SideScreenRow(R.drawable.language_ic, stringResource(R.string.language)) {
            navController.navigate(Screen.SettingLanguageScreen.route)
        },

        SideScreenRow(R.drawable.theme_ic, stringResource(R.string.theme)) {
            showBottomSheet = true
        },

        SideScreenRow(R.drawable.blocked_profile_ic, stringResource(R.string.blocked_profile)) {
            SingletonObject.isComeFromBlockedProfile = true
            navController.navigate(Screen.BlockedProfileScreen.route)
        },

        SideScreenRow(R.drawable.ticket_ic, stringResource(R.string.ticket)) {
            navController.navigate(Screen.TicketScreen.route)
        },

        SideScreenRow(R.drawable.terms_ic, stringResource(R.string.terms_conditions)) {
            navController.navigate(Screen.TermsAndConditionScreen.route)
        },

        SideScreenRow(R.drawable.privacy_ic, stringResource(R.string.privacy_policy)) {
            navController.navigate(Screen.PrivacyAndPolicyScreen.route)
        },

        SideScreenRow(R.drawable.faq_ic, stringResource(R.string.faq)) {
            navController.navigate(Screen.FaqScreen.route)
        },

        SideScreenRow(R.drawable.help_ic, stringResource(R.string.help)) {
            navController.navigate(Screen.HelpScreen.route)
        },

        SideScreenRow(R.drawable.share_app_ic, stringResource(R.string.share_app)){
            shareApp(context)
        },

        SideScreenRow(R.drawable.rate_app_ic, stringResource(R.string.rate_app)),

        SideScreenRow(R.drawable.settings_ic, stringResource(R.string.settings)) {
            navController.navigate(Screen.SettingScreen.route)
        },

        SideScreenRow(R.drawable.safety_support_ic, stringResource(R.string.safety_support)) {
            navController.navigate(Screen.SafetyAndSupportScreen.route)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
           .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .appGradientBackground()

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {

                Row {
                    // back arrow
                    Image(
                        modifier = Modifier.size(40.dp).clickable{
                            SingletonObject.isComeFromBlockedProfile = false
                            m4ViewModel.showBottomActions = m4ViewModel.selectedChipIndex.value

                            navController.popBackStack()},
                        painter = painterResource(R.drawable.arrow_back_circle),
                        contentDescription = "back"
                    )

                    Spacer(modifier = Modifier.weight(1f))

                   /* Image(
                        painter = painterResource(R.drawable.baseline_logout_24),
                        contentDescription = "Logout",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(40.dp)
                            .clickable {
                            SharedPreference.get(context).isLogin = false
                            SharedPreference.get(context).accessToken = ""
                            SharedPreference.get(context).deviceToken = ""
                            SharedPreference.get(context).userID = ""
                            viewModel.resetLivenessState()
                            m4ViewModel.selectedMainScreenIndex.value=0
                            viewModel.resetHeightState()
                            viewModel.clearUserSession()
                            socketViewModel.sendOffline()
                            SingletonObject.isFromEditProfile = false
                            SingletonObject.isCreateFlowInitialized = false
                            SingletonObject.isComeFromRegister = false
                            navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(0)
                            }
                        }
                    )*/
                }


                verticalSpace(16)

                // profile row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Box() {
                        Box(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .border(2.dp, Color.White, shape = CircleShape)
                                .size(60.dp)
                                .clip(shape = CircleShape), contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(homeData?.profileImages?.firstOrNull()?:""   )
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.size(120.dp).clip(CircleShape)) }


                        Text(
                            modifier = Modifier
                                .offset(y = 10.dp)
                                .padding(top = 40.dp)
                                .align(alignment = Alignment.BottomCenter)
                                .background(
                                    shape = RoundedCornerShape(12.dp),
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFE5D4FF),  // light lavender
                                            Color(0xFFEBD9FF),  // soft lilac
                                            Color(0xFFF2E3FF),  // pale pink-lilac
                                            Color(0xFFF7EAFF)   // very soft blush
                                        )
                                    )
                                )
                                .padding(horizontal = 5.sdp, vertical = (3.sdp)),
                            text = "${homeData?.personalDetails?.profileCompletionPercentage ?: 0}%",
                            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                            fontSize = 10.ssp,
                            color = Color(0xFF590988)
                        )

                    }

                    horizontalSpace(10)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(
                            text = buildString {
                                val fullName = "${homeData?.firstName.orEmpty()} ${homeData?.lastName.orEmpty()}".trim()
                                append(fullName.take(20))
                                if (fullName.length > 20) append("...") },
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            fontSize = 12.ssp,
                            color = Color.White,
                            modifier = Modifier

                        )
                        verticalSpace(4)

                        Text(
                            text = "${homeData?.countryCode ?: "N/A"} ${homeData?.phoneNumber ?: "N/A"}",
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            fontSize = 10.ssp,
                            color = Color.White,
                            modifier = Modifier
                        )

                    }

                    Text(
                        text = stringResource(R.string.manage_profile),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 12.ssp,
                        color =  Color(0xFF8B5DF6),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .background(
                                Color.White, shape = RoundedCornerShape(12.dp)
                            ).clickable{navController.navigate(Screen.ViewProfileScreen.route)}
                            .padding( vertical = 5.sdp, horizontal = 8.sdp),
                    )
                }

                verticalSpace(20)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth().wrapContentHeight()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    navController.navigate(
                                        Screen.PlanScreen.route
                                    )
                                },
                        ) {

                            Image(
                                painter = painterResource(
                                    if(data?.planType==3) R.drawable.platinum_card
                                    else if (data?.planType==2) R.drawable.gold
                                    else R.drawable.active_plan_background
                                ),
                                contentDescription = "active plan",
                                modifier = Modifier
                                    .matchParentSize()
                                    .graphicsLayer {
                                        scaleX = if (isRtl) -1f else 1f
                                    },
                                contentScale = ContentScale.FillWidth
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding( 12.dp )
                            ) {
                                Text(
                                    text = stringResource(R.string.active_plan),
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                    fontSize = 10.ssp,
                                    color = if(data?.planType==1) Color.Black else Color(0xFF530386),
                                    modifier = Modifier.padding(top=5.dp)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically){
                                Text(
                                    text = if (data?.planType == 1) stringResource( R.string.free_plan) else if (data?.planType == 2) stringResource( R.string.gold_plan) else if(data?.planType==3) stringResource( R.string.platinum_plan ) else " ",
                                    fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                                    fontSize = 12.ssp,
                                    color = if(data?.planType==1) Color.Black else Color(0xFF530386),
                                    modifier = Modifier
                                )
                                    horizontalSpace(5)

                                    Image(painter = painterResource(R.drawable.side_arrow),
                                        contentDescription = "null",
                                        colorFilter = ColorFilter.tint(if(data?.planType==1) Color.Black else Color(0xFF530386)),
                                        modifier = Modifier.size(18.dp))

                                }

                                verticalSpace(5)

                                if(data?.planType==1){
                                Text(
                                    stringResource( R.string.upgrade_plan),
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(50.dp))
                                        .border(
                                            1.dp,
                                            Color(0xFF590988),
                                            shape = RoundedCornerShape(50.dp))
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFFE5D4FF),  // light lavender
                                                    Color(0xFFEBD9FF),  // soft lilac
                                                    Color(0xFFF2E3FF),  // pale pink-lilac
                                                    Color(0xFFF7EAFF)
                                                )
                                            )
                                        )
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                )}
                                else{
                                    Text(
                                        text = if(data?.planType==1) "" else expireText,
                                        fontSize = 10.ssp,
                                        color = Color(0xFFEB0031),
                                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                    )
                                }

                            }
                        }







                verticalSpace(10)
            }
        }


        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -10.sdp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                rowList.forEachIndexed { index, item ->
                    SideScreenUi(icon = item.icon, text = item.text,onClick = item.route)
                    if (index != rowList.lastIndex) {
                        verticalSpace(20)
                    }
                }
                verticalSpace(24)

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp
                )

                verticalSpace(16)

                // Follow Us section (simple version)
                Text(
                    text = stringResource(R.string.follow_us),
                            fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                verticalSpace(12)

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.facebook_ic),
                        contentDescription = "Facebook",
                        modifier = Modifier.size(40.dp).clickable{
                            socialLink(
                                url = "https://www.facebook.com/people/Pairlix/61581605795863/?mibextid=wwXIfr",
                                context = context
                            )
                        }
                    )
                    Image(
                        painter = painterResource(R.drawable.tiktok_logo),
                        contentDescription = "TikTok",
                        modifier = Modifier.size(40.dp).clickable{
                            socialLink(
                                url = "https://www.tiktok.com/@pairlix?_r=1&_t=ZN-95XAcLxj0vW",
                                context = context
                            )
                        }
                    )
                    Image(
                        painter = painterResource(R.drawable.x_twitter_ic),
                        contentDescription = "X",
                        modifier = Modifier.size(40.dp).clickable{
                            socialLink(
                                url = "https://x.com/pairlix?s=21",
                                context = context
                            )
                        }
                    )
                }

                verticalSpace(12)

                Text(
                    text = stringResource(R.string.app_version),
                    fontSize = 10.sp,
                    color = Color(0xFFAAAAAA),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// -------------------- ROW ITEM --------------------

@Composable
fun SideScreenUi(icon: Int, text: String,onClick:()->Unit={}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = icon), contentDescription = "icon"
        )

        horizontalSpace(8)

        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier,
            painter = painterResource(R.drawable.side_arrow),
            contentDescription = "arrow"
        )
    }
}

fun socialLink(url: String,context: Context){
    val url = url

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }

    context.startActivity(intent)
}

data class SideScreenRow(val icon: Int, val text: String, val route: ()->Unit = {})

