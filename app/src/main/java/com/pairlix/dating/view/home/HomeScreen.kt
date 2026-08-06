package com.pairlix.dating.view.home

import android.content.Context
import android.graphics.Paint.Align
import android.os.Build
import androidx.compose.material3.MaterialTheme
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AnimatedPreloaderLike
import com.pairlix.dating.ReusedComponents.AnimatedPreloaderReject
import com.pairlix.dating.ReusedComponents.AnimatedPreloaderSuperLike
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomDialog
import com.pairlix.dating.ReusedComponents.GradientTimeProgress
import com.pairlix.dating.ReusedComponents.MatchProgressCircle
import com.pairlix.dating.ReusedComponents.PlanCard
import com.pairlix.dating.ReusedComponents.PlanPopUp
import com.pairlix.dating.ReusedComponents.PlanType
import com.pairlix.dating.ReusedComponents.ShutterWithGifAnd70Logics
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.countryCodeToFlagEmoji
import com.pairlix.dating.ReusedComponents.countryNameToIsoCode
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.showTagImageTextBlackBg
import com.pairlix.dating.ReusedComponents.showTagImageTextBlackBgEducaion
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.CountryListHelper
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SavedProfilesManager
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.formatTime
import com.pairlix.dating.helper.getRemainingSecondsFromUtc
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.ActionRequest
import com.pairlix.dating.requests.BoostProfileRequest
import com.pairlix.dating.requests.GetMatchFilterRequest
import com.pairlix.dating.requests.ProfileViewActionRequest
import com.pairlix.dating.response.ActionResponse
import com.pairlix.dating.response.ActivePlanResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.MatchPopupResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.utils.SocketManager
import com.pairlix.dating.utils.SocketState
import com.pairlix.dating.utils.formatText
import com.pairlix.dating.view.M4.timeAgo
import com.pairlix.dating.view.plans.defaultPrices
import com.pairlix.dating.view.plans.getCurrentCountryAndRegion
import com.pairlix.dating.view.plans.goldPriceTable
import com.pairlix.dating.view.plans.platinumPriceTable
import com.pairlix.dating.view.profileDetails.PreviewProfileObserver
import com.pairlix.dating.view.updragePlan.GpsUtils
import com.pairlix.dating.view.updragePlan.PlanBullet1
import com.pairlix.dating.view.updragePlan.RequestLocationAndGps
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import com.pairlix.dating.viewModel.M6ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    viewModelM5: M5ViewModel,
    viewModelM6: M6ViewModel,
    socketViewModel: SocketViewModel
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    //var selectedIndex by remember { mutableIntStateOf(0) }
    // val data = viewModel.getMatchList.getOrNull(selectedIndex)
    val data by remember { derivedStateOf { viewModel.getMatchList.firstOrNull() } }
    val images = data?.personalDetails?.images ?: emptyList()
    val filterChnage by viewModelM4.isFilterChange.collectAsState()
    val maxScroll = 2000f
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val activity = context as MainActivity
    var showDialogRejected by remember { mutableStateOf(false) }
    var showDialogLike by remember { mutableStateOf(false) }
    var showDialogSuperLike by remember { mutableStateOf(false) }
    val isMatchListEmpty = viewModel.getMatchList.isEmpty()
    var showDoubleTapLike by remember { mutableStateOf(false) }

    var doubleTapEvent by remember { mutableIntStateOf(0) }
    // val isLastItem = viewModel.getMatchList.size == 1
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val profileViewAction by viewModelM5.profileViewAction.collectAsState()
    val boostProfile by viewModelM6.boostProfile.collectAsState()
    var remainingSeconds by remember { mutableStateOf(0L) }
    val totalTime = 30 * 60 // 30 minutes in seconds
    val languageManager = LocalLanguageManager.current
    val userId = data?.userId ?: ""
    var planPopUp by remember { mutableStateOf(false) }
    val imagePages by viewModel.imagePages.collectAsState()
    val currentPage = imagePages[userId] ?: 0
    var isClickLocked by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { data?.personalDetails?.images?.size ?: 0 }
    )
    val actionResult by viewModelM4.actionResult.collectAsStateWithLifecycle()

    val isActionInProgress by remember {
        derivedStateOf { isClickLocked || showDialogLike || showDialogRejected || showDialogSuperLike || showDoubleTapLike }
    }
    var hasLoadedOnce by remember { mutableStateOf(false) }
    var isFirstLoadDone by remember { mutableStateOf(false) }




    LaunchedEffect(viewModel.getMatchList.size) {
        if (viewModel.getMatchList.isNotEmpty()) {
            hasLoadedOnce = true
        }
    }


    LaunchedEffect(actionResult) {
        when (val result = actionResult) {
            is M4ViewModel.ActionResult.Success -> {
                isClickLocked = false

                when (result.action) {
                    "like" -> showDialogLike = true
                    "superlike" -> showDialogSuperLike = true
                    "reject" -> showDialogRejected = true
                }

                viewModel.hitGetMatch(

                    accessToken = SharedPreference.get(context).accessToken,
                    filter = viewModelM4.currentFilterRequest.value
                )
                viewModelM4.resetActionResult()
            }

            is M4ViewModel.ActionResult.Error -> {
                isClickLocked = false
                    planPopUp = true

                viewModelM4.resetActionResult()
            }

            is M4ViewModel.ActionResult.LimitReached -> {
              /*  planPopUp = true
                viewModelM4.resetActionResult()*/

            }
            M4ViewModel.ActionResult.Idle -> {}
        }
    }

    LaunchedEffect(Unit) {
        socketViewModel.emitHomePageStatus(true)
    }

    DisposableEffect(Unit) {
        onDispose {
            socketViewModel.emitHomePageStatus(false)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            ErrorUtil.clearError()
        }
    }

// Sync pager when external state changes (e.g. navigated from other screen)
    LaunchedEffect(currentPage) {
        if (pagerState.currentPage != currentPage) {
            pagerState.scrollToPage(currentPage) // or animateScrollToPage
        }
    }
    LaunchedEffect(data?.userId) {
        if (images.isNotEmpty()) {
            pagerState.scrollToPage(0)
        }
    }
    val imageLoader = context.imageLoader
    LaunchedEffect(data?.personalDetails?.images) {
        data?.personalDetails?.images?.forEach { url ->
            imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .data(url)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build()
            )
        }
    }

// Update state when user swipes
    LaunchedEffect(pagerState.currentPage) {
        viewModel.updateImagePage(userId, pagerState.currentPage)
    }

    val progress by animateFloatAsState(
        targetValue = if (remainingSeconds > 0)
            (totalTime - remainingSeconds) / totalTime.toFloat()
        else 0f,
        label = ""
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.hitPreviewProfile(
            access_token = SharedPreference.get(context).accessToken
        )
    }

    PreviewProfileObserver(
        viewModel = viewModel,
        viewModelM4=viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            SharedPreference.get(context).userID = it?.id.toString()
            SharedPreference.get(context).profileImage = it?.profileImages.toString()
            socketViewModel.connectSocket(it?.id.toString())
            viewModel.getPreviewProfileData.value = it
        }
    )

    val userData = viewModel.getPreviewProfileData.value

    LaunchedEffect(userData) {
        if (userData?.activePlanType == 1 && !SingletonObject.hasPlanPopupShownThisSession) {
            Log.e("popup", "HomeScreenfirst: ${SingletonObject.hasPlanPopupShownThisSession}", )
            if (!SingletonObject.isComeFromUploadIdPage) {
                planPopUp = true
                SingletonObject.hasPlanPopupShownThisSession = true
            }
            Log.e("popup", "HomeScreenafter: ${SingletonObject.hasPlanPopupShownThisSession}", )

        }

       }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
    }

    LaunchedEffect(viewModel.getPreviewProfileData.value) {

        val profile = viewModel.getPreviewProfileData.value
        val endTime = profile?.boostEndTime
        val isActive = profile?.isBoostActive == true
        if (isActive && !endTime.isNullOrEmpty()) {

            val seconds = getRemainingSecondsFromUtc(endTime)

            remainingSeconds = if (seconds > 0) seconds else 0

        } else {
            remainingSeconds = 0
        }
    }


    LaunchedEffect(profileViewAction) {
        profileViewAction.let {
            if (it is EmpResource.Success) {
                CustomLoader.hideLoader()
                // context.showToast(it.value.message?:"")
            } else if (it is EmpResource.Failure) {
                CustomLoader.hideLoader()
                it.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            } else if (it is EmpResource.Loading) {
                // CustomLoader.showLoader(context as MainActivity)
            }
        }
    }

    LaunchedEffect(boostProfile) {
        boostProfile.let {
            if (it is EmpResource.Success) {
                // CustomLoader.hideLoader()
                context.showToast(it.value.message ?: "")
                val endTime = it.value.data?.boostEndTime
                remainingSeconds = getRemainingSecondsFromUtc(endTime)
                viewModelM6.resetBoostProfile()
            } else if (it is EmpResource.Failure) {
                Log.e("boostfailer", "HomeScreen: true", )
                CustomLoader.hideLoader()
                planPopUp = true
                viewModelM6.resetBoostProfile()
            } else if (it is EmpResource.Loading) {
                // CustomLoader.showLoader(context as MainActivity)
            }
        }
    }

    var showDialog by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        val gps = GpsUtils(activity)
        gps.enableGPS(activity.gpsResolutionLauncher)
    }
    val socketState by socketViewModel.socketState.collectAsState()

    LaunchedEffect(socketState) { Log.e("Socket", "${socketState} ") }

    LaunchedEffect(isMatchListEmpty) {
        if (isMatchListEmpty) {
            showDialogLike = false
            showDialogRejected = false
            showDialogSuperLike = false
            showDoubleTapLike = false
        }
    }

    val filterRequest by viewModelM4.currentFilterRequest

    LaunchedEffect(Unit) {
        // ✅ Always hit on first entry — covers fresh account, fresh install, logout+login
        if (!viewModel.isMatchApiCalledOnce) {
            viewModel.hitGetMatch(
                accessToken = SharedPreference.get(context).accessToken,
                filter = filterRequest
            )
        }
    }

    LaunchedEffect(filterChnage) {
        if (filterRequest == null) return@LaunchedEffect
        // ✅ Only hit when filter actually changes AND api was already called once
        if (filterChnage) {
            viewModel.hitGetMatch(
                accessToken = SharedPreference.get(context).accessToken,
                filter = filterRequest
            )
            viewModelM4.updateFilter(false)
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
            // ✅ Don't clear first — replace atomically to avoid empty flash
            if (list.isNotEmpty()) {
                viewModel.getMatchList.clear()
                viewModel.getMatchList.addAll(list)
            } else {
                // Only clear when list is truly empty from server
                viewModel.getMatchList.clear()
            }
            viewModel.isMatchApiCalledOnce = true
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

    val documentVerified = viewModel.getHomePageData.value?.personalDetails?.isDocumentVerified
    val matchList = viewModel.getMatchList


    LaunchedEffect(showDialogRejected) {
        if (showDialogRejected) {
            delay(500)
            showDialogRejected = false
        }
    }

    LaunchedEffect(showDialogLike) {
        if (showDialogLike) {
            delay(1000)
            showDialogLike = false
        }
    }

    LaunchedEffect(showDialogSuperLike) {
        if (showDialogSuperLike) {
            delay(1000)
            showDialogSuperLike = false
        }
    }

    if (!viewModel.isMatchApiCalledOnce) {
        HomeSkeletonScreen()
    } else {
        DiscoverHomeScreenLayout(
            navController = navController,
            viewModel = viewModel,
            viewModelM4 = viewModelM4,
            viewModelM5 = viewModelM5,
            viewModelM6 = viewModelM6,
            context = context,
            remainingSeconds = remainingSeconds,
            progress = progress
        )
    }

        if (SingletonObject.isComeFromUploadIdPage ) {

            Dialog(onDismissRequest = {
               SingletonObject.isComeFromUploadIdPage = false
                    if (userData?.activePlanType == 1 ) {
                        planPopUp = true
                        SingletonObject.hasPlanPopupShownThisSession = true
                    }

            }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(25.dp))
                ) {


                    Image(
                        painter = painterResource(R.drawable.match_pop_up_bg),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp, horizontal = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ring),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(25.dp))
                        Image(
                            painter = painterResource(R.drawable.two_person),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(200.dp)
                        )
                        Spacer(modifier = Modifier.height(25.dp))

                        Text(
                            text = stringResource(R.string.assalaamu_alaikum_welcome_to_pairlix),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold))
                        )
verticalSpace(20)
                        Text(
                            text = stringResource(R.string.a_community_of_verified_muslims) + stringResource(
                                R.string.serious_about_halal_marriage),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                        )


                        verticalSpace(25)
                        AppButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            text = stringResource(R.string.begin_your_journey),
                            onClick = {
                                SingletonObject.isComeFromUploadIdPage = false
                                if (userData?.activePlanType == 1) {
                                    planPopUp = true
                                    SingletonObject.hasPlanPopupShownThisSession = true
                                }
                            })
                    }

                }
            }
        }

        if (planPopUp) {

            PlanPopUp(onDismiss ={ planPopUp = false}, navController)

        }

    }

fun HomePageObserver(
    viewModel: AuthViewModel,
    m4ViewModel: M4ViewModel,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    socketViewModel: SocketViewModel
) {

    viewModel.getHomeProfile.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                //  CustomLoader.showLoader(context)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                //SharedPreference.get(context).userID = state.value.data?.id ?: ""
                //  socketViewModel.connectSocket()

                if (state.value.success == true) {
                    viewModel.getHomePageData.value = state.value.data
                    state.value.success = false
                }
            }

            else -> {
                // no-op
            }
        }
    }


}

fun GetMatchObserver(
    viewModel: AuthViewModel,
    m4ViewModel: M4ViewModel,
    context: MainActivity,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    onSuccess: (List<GetMatchResponse.Data?>?) -> Unit,
    onActionSuccess: (ActionResponse.Data?) -> Unit,
    onPopUpSuccess: (List<MatchPopupResponse.Data?>?) -> Unit
) {

    viewModel.getMatch.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                // CustomLoader.showLoader(context)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (state.value.success == true) {

                    onSuccess(state.value.data)

                    state.value.success = false
                }
            }

            else -> {
                // no-op
            }
        }
    }

    m4ViewModel.action.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                // CustomLoader.showLoader(context)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (state.value.success == true) {

                    if (state.value.success == true) {

                        //context.showToast(state.value.message.toString())
                        onActionSuccess(state.value.data)


                        state.value.success = false
                    }

                }
            }

            else -> {
                // no-op
            }
        }
    }




}


data class MatchUser(
    val name: String, val imageUrl: String
)

@Composable
fun DiscoverHomeScreenLayout(
    navController: NavController,
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    viewModelM5: M5ViewModel,
    viewModelM6: M6ViewModel,
    context: Context,
    remainingSeconds: Long,
    progress: Float
) {
    var selectedChipIndex by remember { mutableIntStateOf(0) }
    val matchList = viewModel.getMatchList

    val displayedProfiles = remember(selectedChipIndex, matchList.toList()) {
        val nonNullList = matchList.filterNotNull()
        when (selectedChipIndex) {
            1 -> nonNullList.sortedByDescending { it.createdAt ?: it.matchCreatedAt ?: "" }
            2 -> nonNullList.filter { it.isOnline == true }
            else -> nonNullList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FB))
            .statusBarsPadding()
    ) {
        // 1. Header (Discover Title + Subtitle + Notification / Filter Icons)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Discover",
                    fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                    fontSize = 26.sp,
                    color = Color(0xFF1F1035)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Find your compatible match",
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Notification Button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4EFFF))
                        .clickable { navController.navigate(Screen.NotificationScreen.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.notification_bell_ic),
                        contentDescription = "notification",
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Filter Button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE5E5E5), CircleShape)
                        .clickable { navController.navigate(Screen.FilterScreen.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.filter_ic),
                        contentDescription = "filter",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // 2. Verified Profiles Banner Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6EFFE)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE6D6FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.blue1),
                        contentDescription = "verified_shield",
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Verified Profiles Only",
                        fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                        fontSize = 14.sp,
                        color = Color(0xFF1F1035)
                    )
                    Text(
                        text = "Every profile is manually verified",
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 11.sp,
                        color = Color(0xFF757575)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "chevron",
                    tint = Color(0xFF7330DB),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Filter Chips Bar (LazyRow)
        val filterTitles = listOf("All Matches", "New", "Online •")
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(filterTitles) { index, title ->
                val isSelected = index == selectedChipIndex
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFF7330DB) else Color.White)
                        .then(
                            if (!isSelected) Modifier.border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(20.dp))
                            else Modifier
                        )
                        .clickable { selectedChipIndex = index }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                        fontSize = 13.sp,
                        color = if (isSelected) Color.White else Color(0xFF444444)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Profiles Vertical Cards List (LazyColumn)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(displayedProfiles) { _, profile ->
                MatchProfileCardItem(
                    matchData = profile,
                    onViewProfileClick = {
                        viewModel.setData(profile)
                        SingletonObject.isComeFromBlockedProfile = false
                        SingletonObject.isComeFromHomePage = true
                        SingletonObject.isFromProfileView = false
                        viewModelM4.showBottomActions = 4
                        navController.navigate(Screen.HomeScreenDetailScreen.route)

                        viewModelM5.hitProfileViewAction(
                            access_token = SharedPreference.get(context).accessToken,
                            request = ProfileViewActionRequest(
                                isFullProfileView = true,
                                viewDuration = 1,
                                toUserId = profile.userId ?: ""
                            )
                        )
                    },
                    onConnectClick = {
                        viewModelM4.hitAction(
                            access_token = SharedPreference.get(context).accessToken,
                            request = ActionRequest(action = "like", toUserId = profile.userId ?: "")
                        )
                    },
                    onBookmarkClick = {
                        SavedProfilesManager.saveProfile(context, profile)
                        context.showToast("Saved to bookmarks")
                    }
                )
            }
        }
    }
}

@Composable
fun MatchProfileCardItem(
    matchData: GetMatchResponse.Data,
    onViewProfileClick: () -> Unit,
    onConnectClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onViewProfileClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Left Profile Image Container
            Box(
                modifier = Modifier
                    .width(125.dp)
                    .height(165.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                val imageUrl = matchData.personalDetails?.images?.firstOrNull()
                    ?: matchData.profileImages?.firstOrNull()
                AsyncImage(
                    model = imageUrl ?: "",
                    contentDescription = "profile_img",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Right Bookmark Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { onBookmarkClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "bookmark",
                        tint = Color(0xFF333333),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Bottom Left Online Badge
                if (matchData.isOnline == true) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xE60D3D22))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Online",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right Info Details Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Name
                    val name = matchData.firstName ?: ""
                    Text(
                        text = name,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                        fontSize = 17.sp,
                        color = Color(0xFF1F1035),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Age, Location
                    val age = matchData.age ?: 0
                    val city = matchData.city ?: ""
                    val country = matchData.countryName ?: ""
                    val location = buildString {
                        if (age > 0) append("$age")
                        if (city.isNotEmpty()) {
                            if (isNotEmpty()) append(", ")
                            append(city)
                        }
                        if (country.isNotEmpty()) {
                            if (isNotEmpty()) append(", ")
                            append(country)
                        }
                    }
                    Text(
                        text = location,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 12.sp,
                        color = Color(0xFF757575),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Verified Pill Badge
                    val isVerified = matchData.personalDetails?.isFaceVerified == true ||
                            matchData.personalDetails?.isDocumentVerified == true
                    if (isVerified) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF0E7FE))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.blue1),
                                contentDescription = "verified",
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Verified",
                                color = Color(0xFF7330DB),
                                fontSize = 10.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    // Profession
                    val profession = matchData.personalDetails?.currentProfession
                        ?.ifEmpty { null }
                        ?: matchData.personalDetails?.jobTitle
                        ?.ifEmpty { null }
                        ?: "Marketing"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            
                            imageVector = Icons.Outlined.Work,
                            contentDescription = "profession",
                            tint = Color(0xFF656565),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = profession,
                            color = Color(0xFF555555),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Compatibility Score
                    val rawScore = matchData.finalRankScore ?: matchData.matchScore ?: 33.0
                    val score = if (rawScore <= 0) 33 else rawScore.toInt()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.like_purple_ic),
                            contentDescription = "heart",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$score% Compatibility",
                            color = Color(0xFF7330DB),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // View Profile Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF4EFFF))
                            .clickable { onViewProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "View Profile",
                            color = Color(0xFF7330DB),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                        )
                    }

                    // Connect Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.2.dp, Color(0xFF7330DB), RoundedCornerShape(10.dp))
                            .clickable { onConnectClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Connect",
                            color = Color(0xFF7330DB),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                        )
                    }
                }
            }
        }
    }
}

