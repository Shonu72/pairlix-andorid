package com.pairlix.dating.view.home


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.view.profileDetails.AboutSection
import com.pairlix.dating.view.profileDetails.EducationSection
import com.pairlix.dating.view.profileDetails.PersonalitySection
import com.pairlix.dating.view.profileDetails.PreviewStickyChips
import ir.kaaveh.sdpcompose.sdp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.MainActivity
import com.pairlix.dating.ReusedComponents.AnimatedPreloaderLike
import com.pairlix.dating.ReusedComponents.AnimatedPreloaderReject
import com.pairlix.dating.ReusedComponents.AnimatedPreloaderSuperLike
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomDialog
import com.pairlix.dating.ReusedComponents.MatchDialog
import com.pairlix.dating.ReusedComponents.MatchDialogPhotoOnly
import com.pairlix.dating.ReusedComponents.MatchProgressCircle
import com.pairlix.dating.ReusedComponents.PlanPopUp
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.countryCodeToFlagEmoji
import com.pairlix.dating.ReusedComponents.countryNameToIsoCode
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.CountryListHelper
import com.pairlix.dating.helper.SavedProfilesManager
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.ActionRequest
import com.pairlix.dating.requests.ProfileViewActionRequest
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.utils.formatText
import com.pairlix.dating.view.M4.timeAgo
import com.pairlix.dating.view.newAccountRegistrationScreen.ChipItem
import com.pairlix.dating.view.newAccountRegistrationScreen.Items
import com.pairlix.dating.view.newAccountRegistrationScreen.TextHeading
import com.pairlix.dating.view.profileDetails.AboutEditType
import com.pairlix.dating.view.profileDetails.CategoryEditCard
import com.pairlix.dating.view.profileDetails.PreviewProfileObserver
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenDetailScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    viewModelM5: M5ViewModel,
    socketViewModel: SocketViewModel

) {
    val context = LocalContext.current
    val data by viewModel.selectedProfile.collectAsStateWithLifecycle()
    Log.e("proglr", "${data}")
    val languageManager = LocalLanguageManager.current
    val activityData = viewModelM4.selectedActivity
    var showDialogRejected by remember { mutableStateOf(false) }
    var showDialogLike by remember { mutableStateOf(false) }
    var showDialogSuperLike by remember { mutableStateOf(false) }
    val mainListState = rememberLazyListState()
    var hasTriggeredBack by remember { mutableStateOf(false) }
    var isAtTop by remember { mutableStateOf(true) }
    var showPlanPopUp by remember { mutableStateOf(false) }
    var pullOffset by remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset, source: NestedScrollSource
            ): Offset {
                isAtTop = mainListState.firstVisibleItemIndex == 0 && mainListState.firstVisibleItemScrollOffset == 0
                if (isAtTop && available.y > 0 && !hasTriggeredBack) {
                    pullOffset += available.y
                    hasTriggeredBack = true
                    navController.popBackStack()
                    // example guard (avoid infinite trigger)
                    if (pullOffset > 120f) {
                        pullOffset = 0f
                    }
                }

                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset, available: Offset, source: NestedScrollSource
            ): Offset {
                if (!isAtTop) pullOffset = 0f
                return Offset.Zero
            }
        }
    }
    val chips = stringArrayResource(R.array.profile_chips).toList()
    val interestedIn = stringResource(R.string.interested_in)
    val relocationAfterMarriage = stringResource(R.string.relocation_after_marriage)
    val languageSpoken = stringResource(R.string.language_spoken)
    val sect = stringResource(R.string.sect)
    val maritalStatus = stringResource(R.string.marital_status)
    val religionPractice = stringResource(R.string.religion_practice)
    val childrenStatus = stringResource(R.string.children_status)
    val educationLevel = stringResource(R.string.education_level)
    val currentProfession = stringResource(R.string.current_profession)
    val actionResult by viewModelM4.actionResult.collectAsStateWithLifecycle()
    val userId = data?.userId ?: ""
    val imagePages by viewModel.imagePages.collectAsState()
    val currentPage = imagePages[userId] ?: 0
    val pagerState = rememberPagerState(initialPage = currentPage, pageCount = { data?.personalDetails?.images?.size ?: 0 })
    val coroutineScope = rememberCoroutineScope()
    var selectedPreviewChip by remember { mutableStateOf(0) }
    var showReportBottomSheet by remember { mutableStateOf(false) }
    var showMatchDialog by remember { mutableStateOf(false) }
    var showPhotoOnlyDialog by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val sectionIndexMap = mapOf(
        0 to 2, // About
        1 to 3, //
        2 to 4, // Faith
        3 to 5, // Personality
        4 to 6, // Education
        5 to 7  // Lifestyle
    )
    val isActionInFlight by remember {
        derivedStateOf { actionResult !is M4ViewModel.ActionResult.Idle }
    }
    val stickyHeaderHeightPx = with(LocalDensity.current) { 64.dp.toPx().toInt() }
    var blockDialog by remember { mutableStateOf(false) }
    var blockAndReportDialog by remember { mutableStateOf(false) }
    var unMatchedDialog by remember { mutableStateOf(false) }
    var unBlockDialog by remember { mutableStateOf(false) }
    val userDocumentVerified = viewModel.getPreviewProfileData?.value?.personalDetails?.isDocumentVerified
    val userData = viewModel.getPreviewProfileData.value
    val userProfile = viewModel.getPreviewProfileData.value
    var secondsSpent by remember { mutableStateOf(0) }
    val startTime = remember { System.currentTimeMillis() }
    val lifecycleOwner= LocalLifecycleOwner.current
    val isFreeUser = userData?.activePlanType == 1
    var isSaved by remember(data?.userId) {
        mutableStateOf(SavedProfilesManager.isProfileSaved(context, data?.userId ?: ""))
    }

    LaunchedEffect(Unit) {
        viewModelM4.resetActionResult()
    }
    LaunchedEffect(currentPage) {
        if (pagerState.currentPage != currentPage) {
            pagerState.scrollToPage(currentPage) // or animateScrollToPage
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        viewModel.updateImagePage(userId, pagerState.currentPage)
    }
    LaunchedEffect(actionResult) {
        when (val result = actionResult) {
            is M4ViewModel.ActionResult.Success -> {
                when (result.action) {
                    "like" -> {
                        when {
                            // showBottomActions == 4 (home page) → like gif directly (no match dialog)
                            viewModelM4.showBottomActions == 4 -> showDialogLike = true

                            // showBottomActions == 3 (rejected section) → like gif
                            viewModelM4.showBottomActions == 3 -> showDialogLike = true

                            // showBottomActions == 0 (liked you list)
                            // → match dialog if NOT from chat, like gif if from chat
                            viewModelM4.showBottomActions == 0 && SingletonObject.isComeFromChat -> showDialogLike = true
                            viewModelM4.showBottomActions == 0 && !SingletonObject.isComeFromChat -> showMatchDialog = true

                            // isFromProfileView
                            // → match dialog if NOT from chat, like gif if from chat
                            SingletonObject.isFromProfileView && SingletonObject.isComeFromChat -> showDialogLike = true
                            SingletonObject.isFromProfileView && !SingletonObject.isComeFromChat -> showMatchDialog = true

                            else -> showDialogLike = true
                        }
                    }
                    "superlike" -> showDialogSuperLike = true
                    "reject"    -> showDialogRejected = true
                }

                viewModel.hitGetMatch(
                    accessToken = SharedPreference.get(context).accessToken,
                    filter = viewModelM4.currentFilterRequest.value
                )


                viewModelM4.resetActionResult()
            }
            is M4ViewModel.ActionResult.Error -> {
                showPlanPopUp=true
                //Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                viewModelM4.resetActionResult()
            }

            is  M4ViewModel.ActionResult.LimitReached -> {
                showPlanPopUp = true
                viewModelM4.resetActionResult()
            }
            M4ViewModel.ActionResult.Idle -> { }
        }
    }
    LaunchedEffect(showDialogLike) {
        if (showDialogLike) {
            delay(1000)
            showDialogLike = false
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(navController.graph.startDestinationId) {

                    //popUpTo(Screen.HomeScreenDetailScreen.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }

        }
    }
    LaunchedEffect(showDialogRejected) {
        if (showDialogRejected) {
            delay(1000)
            showDialogRejected = false
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(showDialogSuperLike) {
        if (showDialogSuperLike) {
            delay(1000)
            showDialogSuperLike = false
            navController.navigate(Screen.MainScreen.route) {
                //popUpTo(Screen.HomeScreenDetailScreen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    if (unMatchedDialog) {
        CustomDialog(
            id = R.drawable.green_tick, text1 = stringResource(R.string.profile_unmatched), text2 = "", onDismiss = {
                //unMatchedDialog = false
            })
    }
    if (unBlockDialog) {
        CustomDialog(
            id = R.drawable.green_tick,
            text1 = stringResource(R.string.unblock_successfully),
            text2 = "",
            onDismiss = {
                //unMatchedDialog = false
            })
    }
    LaunchedEffect(unMatchedDialog) {
        if (unMatchedDialog) {
            delay(1500)
            unMatchedDialog = false
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(unBlockDialog) {
        if (unBlockDialog) {
            delay(1500)
            unBlockDialog = false
            navController.popBackStack()
        }
    }
    if (blockDialog) {
        CustomDialog(
            id = R.drawable.red_alert_sign_ic,
            text1 = stringResource(R.string.blocked),
            text2 = stringResource(R.string.you_won_t_see_each_other_anymore),
            onDismiss = { })
    }
    if (blockAndReportDialog) {
        CustomDialog(
            id = R.drawable.red_alert_sign_ic,
            text1 =  stringResource(R.string.block_report_profile),
            text2 =  stringResource(R.string.report_submitted_sucessfully),
            onDismiss = { })
    }
    LaunchedEffect(blockDialog) {
        if (blockDialog) {
            delay(1500)
            blockDialog = false
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(blockAndReportDialog) {
        if (blockAndReportDialog) {
            delay(2500)
            blockAndReportDialog = false
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    GetMatchObserver(
        viewModel = viewModel,
        m4ViewModel = viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            val list = it ?: emptyList()
            it.let {
                viewModel.getMatchList.clear()
                viewModel.getMatchList.addAll(list)
            }
        },
        onActionSuccess = { it ->
            it.let {
                viewModelM4.actionData.value = it
            }
        },
        onPopUpSuccess = { it ->

            val list = it ?: emptyList()
            it.let {
                viewModelM4.popUpDataList.clear()
                viewModelM4.popUpDataList.addAll(list)
            }
        })


    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            secondsSpent++
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            SingletonObject.isComeFromHomePage = false
            val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()

            if (SingletonObject.isComeFromBlockedProfile == false) {
                viewModelM5.hitProfileViewAction(
                    access_token = SharedPreference.get(context).accessToken,
                    request = ProfileViewActionRequest(
                        isFullProfileView = true,
                        viewDuration = duration,
                        toUserId = data?.userId ?: ""
                    )
                )
            }

        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        val maxHeight = this.maxHeight

        LazyColumn(

            state = mainListState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
                .nestedScroll(nestedScrollConnection)
        ) {
            item {

                Box(modifier = Modifier.fillMaxWidth()) {
                    val imageUrl = data?.personalDetails?.images

                  /*  if (imageUrl.isNullOrEmpty()) {

                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp),
                            model = R.drawable.no_dp_icon,
                            contentDescription = "pic",
                            contentScale = ContentScale.Crop
                        )
                    } else {*/

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                        ) { page ->

                            val isBlurred = data?.blurProfile == true
                            val shouldBlurByPlan = when (userData?.activePlanType) {
                                3 -> false // Platinum user: never blur
                                2 -> data?.activePlanType == 3 // Gold user: blur only Platinum profiles
                                1 -> data?.activePlanType in listOf(2, 3) // Free user: blur Gold & Platinum profiles
                                else -> false
                            }

                            val shouldBlur = isBlurred || shouldBlurByPlan

                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(
                                            data?.personalDetails?.images?.get(page))
                                        .build(),
                                    contentDescription = "pic",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(450.dp)
                                        .then(
                                            if (shouldBlur)
                                                Modifier.blur(20.dp)
                                            else
                                                Modifier
                                        )
                                )

                                // White overlay if blurred
                                if (shouldBlur) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(Color.White.copy(alpha = 0.2f))
                                    )
                                }
                            }
                        }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 14.sdp, end = 14.sdp)
                            .align(Alignment.TopCenter)
                    ) {

                        var isBackClicked by remember { mutableStateOf(false) }

                        if (SingletonObject.isComeFromHomePage == false) {
                            Image(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(40.dp))
                                    .clickable(enabled = !isBackClicked) {
                                        isBackClicked = true
                                        navController.popBackStack()
                                    },
                                painter = painterResource(R.drawable.back_icon),
                                contentDescription = "back"
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        if (SingletonObject.isComeFromBlockedProfile == false) {

                            val messageFilter = data?.messageFilter ?: 0

                            if (
                                messageFilter == 0 || (messageFilter == 1 && userData?.isUserVerified == 1) || (messageFilter == 2 && data?.canMessage == true)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(40.dp))

                                        .clickable {
                                            if (userData?.activePlanType == 1 ?: 0) {
                                                showPlanPopUp = true

                                            } else {
                                                navController.navigate(
                                                    Screen.ChatScreenOneToOne.passId(
                                                        data?.userId ?: "",
                                                        name = data?.firstName ?: "",
                                                        /*
                                                    age = data?.age.takeIf { it!=null },
*/
                                                        age = data?.age?.toString() ?: "",
                                                        image = data?.profileImages?.firstOrNull()
                                                            ?: "",
                                                        isOnline = data?.isOnline ?: false,
                                                        isActive = data?.isActive ?: false,
                                                        matchDate = data?.updatedAt ?: "",
                                                        isDocument = data?.personalDetails?.isDocumentVerified
                                                            ?: false,
                                                        isFace = data?.personalDetails?.isFaceVerified
                                                            ?: false

                                                    )
                                                )
                                            }
                                            SharedPreference.get(context).userID =
                                                userProfile?.id ?: ""
                                            // socketViewModel.connectSocket()
                                        },
                                    painter = painterResource(R.drawable.message_icon_purple),
                                    contentDescription = "notification"
                                )

                            }
                        }
                    }


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.sdp)
                            .align(Alignment.BottomCenter)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                        ) {

                            if (data?.planType == 2 || data?.planType == 3) {
                                val planIcon = when (data?.planType) {
                                    2 ->  if(languageManager.currentLanguage=="en") R.drawable.gold_new_en else R.drawable.gold_new_ar
                                    3 -> if(languageManager.currentLanguage=="en") R.drawable.silver_new_en else R.drawable.silver_new_ar
                                    else -> null
                                }

                                Row( modifier = Modifier
                                    .align(alignment = Alignment.TopStart),verticalAlignment = Alignment.CenterVertically) {

                                    if (planIcon != null) Image(
                                        painter = painterResource(id = planIcon),
                                        contentDescription = "premium",
                                        modifier = Modifier
                                            //.size(if (data?.planType == 2) 50.sdp else 70.sdp )
                                            .padding(start = 15.dp)
                                    )

                                  /*  horizontalSpace(5)
                                    Text(
                                        text = if (data?.planType == 2) stringResource(R.string.gold) else if (data?.planType == 3) stringResource(
                                            R.string.platinum
                                        ) else " ",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                        color = Color.White

                                    )*/
                                }
                            }
                            val images = data?.personalDetails?.images ?: emptyList()
                            Row(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(images.size) { index ->
                                    val isSelected = pagerState.currentPage == index
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .size(if (isSelected) 10.dp else 8.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) Color(0xFF530386) else Color.White)
                                    )
                                }
                            }
                        }
                    }

                }
                Column(
                    modifier = Modifier
                        .offset(y = -10.sdp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(

                            text = buildString {
                                val fullName =
                                    "${data?.firstName.orEmpty()} ${data?.lastName.orEmpty()}".trim()
                                append(fullName.take(20))
                                if (fullName.length > 20) append("...")
                            },
                           // text = "${data?.firstName.orEmpty()} ${data?.lastName.orEmpty()}",
                            color = Color(0xFF590988),
                            fontSize = 14.ssp,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                            maxLines = 1,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (data?.age != null) {
                                Text(
                                    text = ", ${data?.age ?: 0}",
                                    color = Color(0xFF590988),
                                    fontSize = 14.ssp,
                                    maxLines = 1,
                                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }



                            if (data?.personalDetails?.isFaceVerified == true) {
                                Image(
                                    modifier = Modifier.size(20.sdp),
                                    painter = painterResource(R.drawable.blue1),
                                    contentDescription = ""

                                )

                            }

                            Spacer(modifier = Modifier.width(4.dp))


                            if (data?.personalDetails?.isDocumentVerified == true) {
                                Image(
                                    modifier = Modifier.size(20.sdp),
                                    painter = painterResource(R.drawable.blue2),
                                    contentDescription = ""
                                )

                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        if (data?.isActive == true) {
                            Text(
                                modifier = Modifier
                                    .background(
                                        color = if (data?.isOnline == true) Color(0xFFCCE1EFE0)
                                        else Color(0xFFD03A43).copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .padding(horizontal = 5.dp, vertical = 5.dp),
                                text = if (data?.isOnline == true) {
                                    stringResource(R.string.active)
                                } else if (data?.lastOnline != null && data?.isOnline == false) {
                                    "${stringResource(R.string.active)} ${timeAgo(data?.lastOnline.toString())}"
                                } else {
                                    stringResource(R.string.inactive)
                                },
                                color = if (data?.isOnline == true) Color(0xFF128807)
                                else Color(0xFFEE404C),
                                fontSize = 12.sp,
                                maxLines = 1,
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (!data?.countryName.isNullOrEmpty()) {


                            if (!data?.countryName.isNullOrEmpty()) {
                                val context = LocalContext.current
                                val countryNamesEn =
                                    remember { CountryListHelper.getEnglishCountryNames(context) }
                                val isoCodes = CommonResource().countryIsoCodes

                                // Find index using English name (since API returns English)
                                val index = countryNamesEn.indexOfFirst {
                                    it.equals(data?.countryName ?: "", ignoreCase = true)
                                }

                                if (index >= 0 && index < isoCodes.size) {
                                    Text(
                                        text = countryCodeToFlagEmoji(isoCodes[index]),
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }

                            /* Text(
                                 text = countryNameToIsoCode(data?.countryName ?: "") ?: "",
                                 fontSize = 15.sp
                             )*/

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                modifier = Modifier
                                    .padding(end = 5.dp),
                                text=  "${ if(languageManager.currentLanguage=="en") data?.city ?: "" else data?.cityAr ?:data?.city} ,${if(languageManager.currentLanguage=="en")data?.countryName ?: "" else data?.countryNameAr?:data?.countryName}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color(0xFF590988),
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                            )

                        }
                        Spacer(modifier = Modifier.weight(1f))


                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(48.dp))
                        ) {

                            // 🔥 Blur Background Layer
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .blur(20.dp)
                                    .background(
                                        Color(0x804D590988),
                                        shape = RoundedCornerShape(48.dp)
                                    )
                            )

                            Row(
                                modifier = Modifier
                                    .background(
                                        Color(0x664D590988),
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                MatchProgressCircle(
                                    percentage = data?.finalRankScore?.toInt() ?: 0,
                                    size = 32.dp,
                                    strokeWidth = 4.dp
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = stringResource(R.string.match),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                )
                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(10.sdp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.sdp)
                    ) {

                        if (data?.personalDetails?.isFaceVerified == true) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        Color(0xFF590988).copy(alpha = 0.7f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 8.sdp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.smile_face_ic),
                                    contentDescription = "",
                                    modifier = Modifier.size(12.sdp),
                                    colorFilter = ColorFilter.tint(
                                        color= Color.White,

                                            //MaterialTheme.colorScheme.onBackground

                                    )
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = stringResource(R.string.face_verified),
                                    //color= MaterialTheme.colorScheme.onBackground,
                                    color= Color.White,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }


                        if (data?.personalDetails?.isDocumentVerified == true) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        Color(0xFF590988).copy(alpha = 0.7f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 8.sdp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.smile_face_ic),
                                    contentDescription = "",
                                    modifier = Modifier.size(12.sdp),
                                    colorFilter =  ColorFilter.tint(color= Color.White,
                                        //MaterialTheme.colorScheme.onBackground)
                                ))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = stringResource(R.string.id_verified),
                                   // color= MaterialTheme.colorScheme.onBackground,
                                    color= Color.White,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                            maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        if (data?.personalDetails?.isDocumentVerified == true) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        Color(0xFF590988).copy(alpha = 0.7f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 8.sdp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.tick_verified_black),
                                    contentDescription = "",
                                    modifier = Modifier.size(12.sdp),
                                    colorFilter = ColorFilter.tint(

                                            color = Color.White,

                                            //MaterialTheme.colorScheme.onBackground

                                    )
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = stringResource(R.string.age_verified),
                                    color= Color.White,
                                   // color= MaterialTheme.colorScheme.onBackground,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }


                        }

                    }


                    Spacer(modifier = Modifier.height(10.sdp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color =  MaterialTheme.colorScheme.outlineVariant

                    )

                    Spacer(modifier = Modifier.height(10.sdp))


                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(8.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                painter = painterResource(R.drawable.gender_ic),
                                contentDescription = "",
                                modifier = Modifier.size(20.sdp),
                                colorFilter = ColorFilter.tint(
                                    color = MaterialTheme.colorScheme.onBackground

                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val genderText = when (data?.gender?.lowercase()) {
                                "male" -> stringResource(R.string.male)
                                "female" -> stringResource(R.string.female)
                                else -> ""
                            }
                            Text(
                                text =  genderText,
                                color= MaterialTheme.colorScheme.onBackground,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            )
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(8.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                painter = painterResource(R.drawable.height_ic),
                                contentDescription = "",
                                modifier = Modifier.size(20.sdp),
                                colorFilter = ColorFilter.tint(
                                    color =
                                        MaterialTheme.colorScheme.onBackground

                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "${data?.personalDetails?.height ?: 0} ${if (data?.personalDetails?.heightType == "0") stringResource(
                                    R.string.cm
                                ) else stringResource(R.string.ft)
                                }",
                                color= MaterialTheme.colorScheme.onBackground,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(10.sdp))

                    Row(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(R.drawable.two_heart_ic),
                            contentDescription = "",
                            modifier = Modifier.size(20.sdp),
                            colorFilter = ColorFilter.tint(
                                color =
                                    MaterialTheme.colorScheme.onBackground

                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(
                                R.string.compatibility_score,
                                data?.finalRankScore?.toInt() ?: 0
                            ),
                            color= MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                    }

                    verticalSpace(15)

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            stickyHeader(key = "preview_chips") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    PreviewStickyChips(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(
                                top = 35.dp, start = 16.dp, end = 16.dp, bottom = 10.dp
                            ),
                        list = chips,
                        selectedIndex = selectedPreviewChip,
                        onChipClick = { index ->
                            selectedPreviewChip = index
                            coroutineScope.launch {
                                mainListState.animateScrollToItem(
                                    index = sectionIndexMap[index] ?: 0,
                                    scrollOffset = -stickyHeaderHeightPx
                                )
                            }
                        })
                }
            }

            item {
                if (SingletonObject.isComeFromBlockedProfile == true) {
                    verticalSpace(10)
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = (16.dp))
                            .clip(shape = RoundedCornerShape(52.dp))
                            .border(
                                1.dp, Color(0xFFEE404C), shape = RoundedCornerShape(52.dp)
                            )
                            .background(
                                Color(0xFF1AEE404C), shape = RoundedCornerShape(52.dp)
                            )
                            .clickable {
                                unBlockDialog = true
                                viewModelM4.hitAction(
                                    access_token = SharedPreference.get(context).accessToken,
                                    request = ActionRequest(
                                        action = "unblock", toUserId = data?.userId
                                    )
                                )

                            }
                            .padding(vertical = 16.dp),
                        text = stringResource(R.string.unblock_profile),
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color(0xFFEE404C),
                        textAlign = TextAlign.Center)
                }

                val aboutList = remember(data) {
                    listOf(
                        Items(
                            interestedIn,
                            data?.personalDetails?.interestedIn?.formatText().orEmpty()
                        ),
                        Items(
                            languageSpoken,
                            data?.personalDetails?.spokenLanguages?.joinToString(",")?.formatText()
                                .orEmpty()
                        ),
                        Items(sect, data?.personalDetails?.sect?.formatText().orEmpty()),
                        Items(
                            maritalStatus,
                            data?.personalDetails?.maritalStatus?.formatText().orEmpty()
                        ),
                        Items(
                            religionPractice,
                            data?.personalDetails?.religionPractice?.formatText().orEmpty()
                        ),
                        Items(
                            childrenStatus,
                            data?.personalDetails?.haveChildren?.formatText().orEmpty()
                        ),
                        Items(
                            relocationAfterMarriage,
                            data?.personalDetails?.aboardAfterMarriage?.formatText().orEmpty()
                        )
                    )
                }
                verticalSpace(10)
                AboutSection(list = aboutList)
            }

            item {
                val educationListPreview = listOf<Items>(
                    Items(educationLevel, data?.personalDetails?.educationLevel ?: "-"),
                    // Items("School Name ", data?.schoolName ?: "-"),
                    // Items("Job Title", data?.personalDetails?.jobTitle ?: "-"),
                    Items(currentProfession, data?.personalDetails?.currentProfession ?: "-"),
                    //Items("Company Name", data?.companyName ?: "-"),
                )
                Spacer(Modifier.height(10.dp))
                EducationSection(educationListPreview)
            }


            item {
                Column(
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 12.dp, top = 15.dp, bottom = 12.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                TextHeading(text = stringResource(R.string.habits))

                            }
                            verticalSpace(10)

                            Row(modifier = Modifier.fillMaxWidth()) {


                                Row(modifier = Modifier.weight(1f)) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        Color(0xFFD9C8FF),
                                                        Color(0xFFEFD8FF),
                                                        Color(0xFFFFEFF8)
                                                    )
                                                ), shape = RoundedCornerShape(
                                                    12.dp
                                                )
                                            )
                                            .padding(
                                                horizontal = 12.dp, vertical = 8.dp
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.smoking_im),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                        )

                                        Spacer(Modifier.height(8.dp))

                                        Text(
                                            text = stringResource(R.string.smokimg),
                                            color = Color(0xff6D6D6D),
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                        )

                                        Spacer(Modifier.height(8.dp))

                                        Text(
                                            text = when (data?.personalDetails?.howOftenSmoke) {
                                                "0" -> stringResource(R.string.casual)
                                                "1" -> stringResource(R.string.smoker)
                                                "2" -> stringResource(R.string.trying_to_quit)
                                                "3" -> stringResource(R.string.smoking_when_drinking)
                                                "4" -> stringResource(R.string.never)
                                                else -> data?.personalDetails?.howOftenSmoke?.formatText()
                                                    ?: "-"
                                            },
                                            color = Color(0xff590988),
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                        )
                                    }

                                }
                                horizontalSpace(15)
                                Row(modifier = Modifier.weight(1f)) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        Color(0xFFD9C8FF),  // lavender
                                                        Color(0xFFEFD8FF),  // soft pink-purple
                                                        Color(0xFFFFEFF8)   // light peach
                                                    )
                                                ), shape = RoundedCornerShape(
                                                    12.dp
                                                )
                                            )
                                            .padding(
                                                horizontal = 12.dp, vertical = 8.dp
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.glass_ic),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                        )

                                        Spacer(Modifier.height(8.dp))

                                        Text(
                                            text = stringResource(R.string.drinking),
                                            color = Color(0xff6D6D6D),
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                        )

                                        Spacer(Modifier.height(8.dp))

                                        Text(
                                            text = when (data?.personalDetails?.howOftenDrink) {
                                                "0" -> stringResource(R.string.never)
                                                "1" -> stringResource(R.string.occasional)
                                                "2" -> stringResource(R.string.regular)
                                                else -> data?.personalDetails?.howOftenDrink?.formatText()
                                                    ?: "-"
                                            },
                                            color = Color(0xff590988),
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                        )
                                    }

                                }
                            }



                            verticalSpace(20)
                            Row() {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFFD9C8FF),  // lavender
                                                    Color(0xFFEFD8FF),  // soft pink-purple
                                                    Color(0xFFFFEFF8)   // light peach
                                                )
                                            ), shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(
                                            horizontal = 12.dp, vertical = 8.dp
                                        )
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.dumble_ic),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = stringResource(R.string.workout),
                                        color = Color(0xff6D6D6D),
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = when (data?.personalDetails?.workOut) {
                                            "0" -> stringResource(R.string.everyday)
                                            "1" -> stringResource(R.string.often)
                                            "2" -> stringResource(R.string.sometimes)
                                            "3" -> stringResource(R.string.never)
                                            else -> data?.personalDetails?.workOut?.formatText()
                                                ?: "-"
                                        },
                                        color = Color(0xff590988),
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                    )
                                }
                                verticalSpace(20)
                            }
                        }
                    }
                }
            }
            item {

                val languageManager = LocalLanguageManager.current

                val interestChips =
                    data?.personalDetails?.interests?.filterNotNull()?.flatMap { interest ->
                        interest.tags?.filterNotNull()?.map { tag ->

                            val displayName =
                                if (languageManager.currentLanguage == "ar") {
                                    tag.tagNameAr ?: tag.tagNameEn.orEmpty()
                                } else {
                                    tag.tagNameEn.orEmpty()
                                }

                            ChipItem(
                                tagId = tag.id.orEmpty(),
                                tagName = displayName,
                                iconUrl = tag.iconImage
                            )
                        } ?: emptyList()
                    } ?: emptyList()


                CategoryEditCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    heading = stringResource(R.string.interests),
                    chips = interestChips,
                    isEditable = false,
                    onClick = {})

                verticalSpace(20)

            }


            item {

                val languageManager = LocalLanguageManager.current

                CategoryEditCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    heading = stringResource(R.string.faith_identity),
                    chips = data?.personalDetails?.faith?.filterNotNull()?.map {

                        val displayName =
                            if (languageManager.currentLanguage == "ar") {
                                it.faithNameAr ?: it.faithNameEn.orEmpty()
                            } else {
                                it.faithNameEn.orEmpty()
                            }

                        ChipItem(
                            tagId = it.id.orEmpty(),
                            tagName = displayName,
                            iconUrl = ""
                        )
                    } ?: emptyList(),

                    isEditable = false,
                    onClick = {}
                )



                verticalSpace(20)

            }
            item { PersonalitySection(text = data?.personalDetails?.description ?: "") }
            item {
                if (viewModelM4.showBottomActions == 2) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(52.dp))
                                .border(1.dp, Color(0xFF590988), shape = RoundedCornerShape(52.dp))
                                .background(Color.White, shape = RoundedCornerShape(52.dp))
                                .clickable {
                                    unMatchedDialog = true
                                    viewModelM4.hitAction(
                                        access_token = SharedPreference.get(context).accessToken,
                                        request = ActionRequest(
                                            action = "unmatch", toUserId = data?.userId
                                        )
                                    )
                                }
                                .padding(vertical = 14.dp),
                            text = stringResource(R.string.unmatch),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                            color = Color(0xFF590988),
                            textAlign = TextAlign.Center)

                    }

                    verticalSpace(20)

                }

                if (viewModelM4.showBottomActions != 5) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(52.dp))
                                .border(
                                    1.dp, Color(0xFF590988), shape = RoundedCornerShape(52.dp)
                                )
                                .background(
                                    Color.White, shape = RoundedCornerShape(52.dp)
                                )
                                .clickable {
                                    blockDialog = true
                                    viewModelM4.hitAction(
                                        access_token = SharedPreference.get(context).accessToken,
                                        request = ActionRequest(
                                            action = "block", toUserId = data?.userId
                                        )
                                    )

                                }
                                .padding(vertical = 14.dp),
                            text = stringResource(R.string.block),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                            color = Color(0xFF590988),
                            textAlign = TextAlign.Center)


                        horizontalSpace(10)
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(52.dp))
                                .border(
                                    1.dp, Color.Transparent, shape = RoundedCornerShape(52.dp)
                                )
                                .appGradientBackground()
                                .clickable { showReportBottomSheet = true }
                                .padding(vertical = 14.dp),
                            text = stringResource(R.string.report_block),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                            color = Color.White,
                            textAlign = TextAlign.Center)


                    }

                }


                /* if (viewModelM4.showBottomActions != 2 ||  viewModelM4.showBottomActions == 1 && data?.action == "like" ) {

                     verticalSpace(150)

                 } else {
                     verticalSpace(30)


                 }*/


                val isLikeCase = viewModelM4.showBottomActions == 1 && data?.action == "superlike"

                if (viewModelM4.showBottomActions != 2 && !isLikeCase && viewModelM4.showBottomActions != 5) {
                    verticalSpace(150)
                } else {
                    verticalSpace(30)
                }

            }

        }




        if (showDialogRejected) {
            Dialog(onDismissRequest = { showDialogRejected = false }) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {

                    AnimatedPreloaderReject(
                        modifier = Modifier.size(1000.dp)
                    )

                }


            }
        }
        if (showDialogLike) {


            Dialog(onDismissRequest = { showDialogLike = false }) {


                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {

                    AnimatedPreloaderLike(
                        modifier = Modifier.size(1000.dp)

                    )

                }

            }
        }
        if (showDialogSuperLike) {


            Dialog(onDismissRequest = { showDialogSuperLike = false }) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {

                    AnimatedPreloaderSuperLike(
                        modifier = Modifier.size(1000.dp)

                    )

                }
            }
        }
        if (showReportBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showReportBottomSheet = false },
                dragHandle = null,
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .imePadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF14590988))
                            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(R.string.report),
                            fontSize = 18.sp,
                            color= MaterialTheme.colorScheme.onBackground,
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
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    showReportBottomSheet = false
                                })
                    }
                    val reasons = listOf(
                        ReasonOption(1, stringResource(R.string.reason_wrong_age)),
                        ReasonOption(2, stringResource(R.string.reason_married_pretending_single)),
                        ReasonOption(3, stringResource(R.string.reason_harassment)),
                        ReasonOption(4, stringResource(R.string.reason_religious_disrespect)),
                        ReasonOption(5, stringResource(R.string.reason_sexual_content)),
                        ReasonOption(6, stringResource(R.string.reason_scamming_fraud)),
                        ReasonOption(7, stringResource(R.string.reason_spam)),
                        ReasonOption(8, stringResource(R.string.reason_catfishing)),
                        ReasonOption(9, stringResource(R.string.reason_extremist_political)),
                        ReasonOption(10, stringResource(R.string.reason_bad_behavior_chat_call)),
                        ReasonOption(11, stringResource(R.string.reason_threats_bullying)),
                        ReasonOption(12, stringResource(R.string.reason_financial_scam)),
                        ReasonOption(13, stringResource(R.string.reason_other))
                    )


                    var selectedId by remember { mutableStateOf(1) }
                    var selectedText by remember { mutableStateOf("Lorem ipsum dolor sit amet, consectetur") }
                    var otherText by remember { mutableStateOf("") }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        reasons.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource()
                                    ) {
                                        selectedId = item.id
                                        selectedText = item.title
                                    }, verticalAlignment = Alignment.CenterVertically
                            ) {

                                RadioButton(
                                    selected = selectedId == item.id, onClick = {
                                        selectedId = item.id
                                        selectedText = item.title
                                    }, colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF8378E2),   // change as per brand
                                        unselectedColor =  MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    lineHeight = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                    color= MaterialTheme.colorScheme.onBackground,

                                    )
                            }
                            verticalSpace(5)
                        }

                        if (selectedId == 13) {
                            TextField(
                                value = otherText,
                                onValueChange = { otherText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp, Color(0xFF33000000), RoundedCornerShape(12.dp)
                                    ),
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.write_a_reason_here),
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                },
                                minLines = 3,
                                maxLines = 5,
                                singleLine = false,
                                enabled = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.background,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                    disabledContainerColor = Color(0xFFF2F2F2),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                textStyle = TextStyle(
                                    fontSize = 14.sp, color =MaterialTheme.colorScheme.onBackground
                                )
                            )

                        }
                        verticalSpace(15)

                        AppButton(
                            text = stringResource(R.string.submit),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onClick = {

                                val reportReasonToSend =
                                    if (selectedId == 13) "other" else selectedText

                                val customReasonToSend = if (selectedId == 13) otherText else null

                                viewModelM4.hitAction(
                                    access_token = SharedPreference.get(context).accessToken,
                                    request = ActionRequest(
                                        action = "block",
                                        toUserId = data?.userId,
                                        reportReason = reportReasonToSend,
                                        customReason = customReasonToSend
                                    )
                                )
                                blockAndReportDialog = true
                                //showReportBottomSheet = false
                            })

                    }
                }
            }
        }

        if (showPhotoOnlyDialog) {
            MatchDialogPhotoOnly(
                modifier = Modifier,
                user1 = MatchUser(
                    name = data?.firstName ?: "",
                    imageUrl = data?.profileImages?.firstOrNull().toString()
                ),
                user2 = MatchUser(
                    name = userData?.firstName ?: "",
                    imageUrl = userData?.profileImages?.firstOrNull().toString(),

                    ),
                topHeart = R.drawable.red_star_ic,
                bottomHeart = R.drawable.red_star_ic,
                onDismiss = {
                    showPhotoOnlyDialog = false
                })
        }


        RedesignedFloatingActionBar(
            showBottomActions = viewModelM4.showBottomActions,
            isActionInFlight = isActionInFlight,
            isSaved = isSaved,
            onSkipClick = {
                viewModelM4.hitAction(
                    access_token = SharedPreference.get(context).accessToken,
                    request = ActionRequest(action = "reject", toUserId = data?.userId)
                )
            },
            onConnectClick = {
                if (isFreeUser && viewModelM4.showBottomActions == 3) {
                    showPlanPopUp = true
                } else {
                    viewModelM4.hitAction(
                        access_token = SharedPreference.get(context).accessToken,
                        request = ActionRequest(action = "like", toUserId = data?.userId)
                    )
                }
            },
            onSaveClick = {
                data?.let { profile ->
                    val targetId = profile.userId ?: ""
                    if (isSaved) {
                        SavedProfilesManager.removeProfile(context, targetId)
                        isSaved = false
                        Toast.makeText(context, "Removed from bookmarks", Toast.LENGTH_SHORT).show()
                    } else {
                        SavedProfilesManager.saveProfile(context, profile)
                        isSaved = true
                        Toast.makeText(context, "Saved to bookmarks", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }


    if(showPlanPopUp){
        PlanPopUp(onDismiss ={ showPlanPopUp = false}, navController)
    }
}

data class ReasonOption(
    val id: Int, val title: String
)

@Composable
fun RedesignedFloatingActionBar(
    showBottomActions: Int,
    isActionInFlight: Boolean,
    isSaved: Boolean = false,
    onSkipClick: () -> Unit,
    onConnectClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (showBottomActions == 2 || showBottomActions == 5) return

    val containerWidth = when (showBottomActions) {
        1 -> 230.dp
        3, 4 -> 210.dp
        else -> 290.dp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .width(containerWidth)
                .height(68.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFFF8F5FA))
                .border(1.dp, Color(0xFFEAEAEA), RoundedCornerShape(26.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // A. SKIP BUTTON (Home Feed only: showBottomActions == 0)
            if (showBottomActions == 0) {
                Box(
                    modifier = Modifier
                        .width(58.dp)
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE6E6E6), RoundedCornerShape(14.dp))
                        .clickable(enabled = !isActionInFlight) { onSkipClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "skip",
                            tint = Color(0xFFF24040),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Skip",
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            fontSize = 11.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
            }

            // B. MIDDLE ACTION BUTTON
            when (showBottomActions) {
                1 -> {
                    // Sent Tab: Disabled Request Sent Badge
                    Box(
                        modifier = Modifier
                            .width(142.dp)
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF8C66C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "sent",
                                tint = Color(0xFFF2F2F2),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Request Sent",
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                                fontSize = 13.sp,
                                color = Color(0xFFF2F2F2)
                            )
                        }
                    }
                }
                3, 4 -> {
                    // Archived / Saved Tab: Enabled Connect Button (Width 124dp)
                    Box(
                        modifier = Modifier
                            .width(124.dp)
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF7331D9))
                            .clickable(enabled = !isActionInFlight) { onConnectClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "connect",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Connect",
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                }
                else -> {
                    // Standard Connect Button (Width 134dp)
                    Box(
                        modifier = Modifier
                            .width(134.dp)
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF7331D9))
                            .clickable(enabled = !isActionInFlight) { onConnectClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "connect",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Connect",
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // C. SAVE / UNSAVE BUTTON
            Box(
                modifier = Modifier
                    .width(58.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE6E6E6), RoundedCornerShape(14.dp))
                    .clickable { onSaveClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (isSaved) "unsave" else "save",
                        tint = if (isSaved) Color(0xFFF24040) else Color(0xFF7331D9),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isSaved) "Unsave" else "Save",
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                        fontSize = 11.sp,
                        color = if (isSaved) Color(0xFFF24040) else Color(0xFF333333)
                    )
                }
            }
        }
    }
}

