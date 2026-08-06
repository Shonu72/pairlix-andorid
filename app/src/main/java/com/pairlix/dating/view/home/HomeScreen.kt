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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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

    //////dfdf///


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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            /* Box(
             modifier = Modifier
                 .fillMaxSize()
                 .zIndex(10f)
         ) {
             // if (documentVerified == true) {
             if (!isMatchListEmpty) {
                 ShutterWithGifAnd70Logics(viewModelM4, viewModel, data)
             }
             //}
         }*/

            val maxHeight = this.maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(onVerticalDrag = { _, dragDistance ->
                            scope.launch {
                                offsetY.snapTo(offsetY.value + dragDistance)
                            }
                        }, onDragEnd = {
                            if (!viewModel.getMatchList.isEmpty()) {
                                scope.launch {

                                    if (offsetY.value < -100f) {
                                        data?.let { safeData ->
                                            viewModel.setData(viewModel.getMatchList.firstOrNull()!!)
                                            SingletonObject.isComeFromBlockedProfile = false
                                            SingletonObject.isComeFromHomePage = true
                                            SingletonObject.isFromProfileView = false
                                            viewModelM4.showBottomActions = 4
                                            navController.navigate(Screen.HomeScreenDetailScreen.route)
                                        }
                                        viewModelM5.hitProfileViewAction(
                                            access_token = SharedPreference.get(context).accessToken,
                                            request = ProfileViewActionRequest(
                                                isFullProfileView = true,
                                                viewDuration = 1,
                                                toUserId = data?.userId
                                            )
                                        )
                                    }

                                    offsetY.animateTo(
                                        targetValue = 0f, animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy
                                        )
                                    )
                                }
                            }
                        })
                    }
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 14.sdp, end = 14.sdp, bottom = 10.dp)
                ) {
                    var isClicked by remember { mutableStateOf(false) }

                    Image(
                        modifier = Modifier
                            .clip(RoundedCornerShape(40.dp))
                            .clickable {
                                if (!isClicked) {
                                    isClicked = true
                                    navController.navigate(Screen.FilterScreen.route)
                                    // reset after delay
                                    CoroutineScope(Dispatchers.Main).launch {
                                        delay(500)
                                        isClicked = false
                                    }
                                }
                            },
                        painter = painterResource(R.drawable.filter_ic),
                        contentDescription = "filter"
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    // Progress Circle
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val isBoostActive = remainingSeconds > 0

                        if (!isBoostActive) {
                            Image(
                                painter = painterResource(id = R.drawable.boost_btn),
                                contentDescription = "Boost",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        viewModelM6.hitBoostProfile(
                                            access_token = SharedPreference.get(context).accessToken,
                                            request = BoostProfileRequest(
                                                isBoostActive = true
                                            )
                                        )
                                    }

                            )
                        } else {
                            GradientTimeProgress(
                                progress = progress,
                                timeText = formatTime(remainingSeconds.toInt())
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.sdp))



                    Image(
                        modifier = Modifier
                            .clip(RoundedCornerShape(40.dp))
                            .clickable { navController.navigate(Screen.NotificationScreen.route) },
                        painter = painterResource(R.drawable.notification_bell_ic),
                        contentDescription = "notification"
                    )
                }


                if (showDialogRejected) {
                    Dialog(onDismissRequest = { /* no-op */ }) {
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
                    Dialog(onDismissRequest = { /* no-op */ }) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            AnimatedPreloaderLike(
                                modifier = Modifier.size(1000.dp)
                            )
                        }
                    }
                }

                if (showDoubleTapLike) {
                    Dialog(onDismissRequest = { /* no-op */ }) {
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
                    Dialog(onDismissRequest = { /* no-op */ }) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            AnimatedPreloaderSuperLike(
                                modifier = Modifier.size(1000.dp)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            //.height(screenHeight * 0.75f)
                            .background(MaterialTheme.colorScheme.background)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        if (!isMatchListEmpty && !isActionInProgress) {
                                            // showDialogLike = true
                                            viewModelM4.hitAction(
                                                access_token = SharedPreference.get(context).accessToken,
                                                request = ActionRequest(
                                                    action = "like", toUserId = data?.userId
                                                )
                                            )
                                        }

                                    })
                            })
                    {
                        HorizontalPager(
                            state = pagerState,
                            beyondViewportPageCount = 2,
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        ) { page ->

                            val isBlurred = data?.blurProfile == true
                            val imageUrl = data?.personalDetails?.images?.getOrNull(page)
                            val shouldBlurByPlan = when (userData?.activePlanType) {
                                3 -> false // Platinum user: never blur
                                2 -> data?.activePlanType == 3 // Gold user: blur only Platinum profiles
                                1 -> data?.activePlanType in listOf(2, 3) // Free user: blur Gold & Platinum profiles
                                else -> false
                            }

                            val shouldBlur = isBlurred || shouldBlurByPlan
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    // .height(screenHeight * 0.75f)
                                    .clip(RoundedCornerShape(28.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        // .height(screenHeight * 0.75f)
                                        .padding(horizontal = 16.dp)
                                        .background(MaterialTheme.colorScheme.background)
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 28.dp,
                                                topEnd = 28.dp,
                                                bottomStart = 28.dp,
                                                bottomEnd = 28.dp
                                            )
                                        )
                                ) {

                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(imageUrl ?: "")
                                            .crossfade(true)
                                            .size(1080)
                                            .memoryCachePolicy(CachePolicy.ENABLED)
                                            .diskCachePolicy(CachePolicy.ENABLED)
                                            .build(),
                                        contentDescription = "pic",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .then(
                                                if (shouldBlur) Modifier.blur(
                                                    20.dp
                                                ) else Modifier
                                            )
                                    )

                                    if (shouldBlur) {
                                        Box(
                                            modifier = Modifier
                                                .matchParentSize()
                                                .background(Color.White.copy(alpha = 0.2f))
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    colorStops = arrayOf(
                                                        0.0f to Color.Transparent,
                                                        0.4f to Color.Transparent,
                                                        0.6f to Color.Transparent,
                                                        0.75f to Color(0xFF3C0060).copy(alpha = 0.1f),
                                                        0.9f to Color(0xFF3C0060).copy(alpha = 0.2f),
                                                        1.0f to Color(0xFF3C0060).copy(alpha = 0.3f)
                                                    )
                                                )
                                            )
                                    )
                                }

                            }
                        }

                    }

                    if (viewModel.getMatchList.isEmpty() && !isActionInProgress) {
                        val isFilterActive = viewModelM4.isFilterApplied()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isFilterActive) {
                                // Filter applied but no results
                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp),
                                    text = stringResource(R.string.no_filter_matches),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                                )
                                verticalSpace(10)
                                Text(
                                    modifier = Modifier
                                        .padding(top = 30.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .appGradientBackground()
                                        .clickable {
                                            viewModelM4.clearAllFilters()
                                            // context.showToast(context.getString(R.string.filter_cleared_successfully))
                                            viewModel.hitGetMatch(
                                                accessToken = SharedPreference.get(context).accessToken,
                                                filter = filterRequest

                                            )

                                        }

                                        .padding(horizontal = 15.dp, vertical = 10.dp),
                                    textAlign = TextAlign.Center,
                                    text = stringResource(R.string.clear_filter),
                                    color = MaterialTheme.colorScheme.onBackground,

                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                                )

                                Spacer(modifier = Modifier.weight(1f))

                            } else {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(230.sdp),
                                    painter = painterResource(R.drawable.nodata_image),
                                    contentDescription = "img"
                                )

                                verticalSpace(30, true)

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.you_ve_seen_the_all_the_profiles),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                                )
                                verticalSpace(5)

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.update_your),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                                )

                                verticalSpace(10)

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.interests_to_explore_more),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                                )

                                verticalSpace(20)

                                AppButton(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp),
                                    text = stringResource(R.string.update),
                                    onClick = {
                                        navController.navigate(Screen.ViewProfileScreen.route) {
                                            popUpTo(Screen.MainScreen.route) { inclusive = false }
                                        }
                                    }
                                )
                            }
                        }
                    } else {


                        if (data?.isActive == true) {

                            Text(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(start = 24.dp, top = 10.dp)
                                    .background(
                                        color = if (data?.isOnline == true) Color(
                                            0xFFCCE1EFE0
                                        ) else Color(0xFFD03A43).copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(48.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 7.dp)
                                    .align(alignment = Alignment.TopStart),
                                text = if (data?.isOnline == true) {
                                    stringResource(R.string.active)
                                } else if (data?.lastOnline != null && data?.isOnline == false) {
                                    "${stringResource(R.string.active)} ${timeAgo(data?.lastOnline.toString())}"
                                } else {
                                    stringResource(R.string.inactive)
                                },
                                color = if (data?.isOnline == true) Color(0xFF128807) else Color(
                                    0xFFEE404C),
                                maxLines = 1,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                            )
                        }


///-10 column
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 35.dp, start = 24.dp, end = 24.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                // horizontal Pager dots
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
                                                .background(
                                                    if (isSelected) Color(0xFF530386)
                                                    else Color.White
                                                )
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .clip(RoundedCornerShape(48.dp))
                                ) {
                                    MatchProgressCircle(
                                        percentage = data?.finalRankScore?.toInt() ?: 0
                                    )

                                }


                            }

                            // verticalSpace(5)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                // .padding(20.dp)

                            ) {


                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (data?.planType == 2 || data?.planType == 3) {
                                        val planIcon = when (data!!.planType) {
                                            2 -> if (languageManager.currentLanguage == "en") R.drawable.gold_new_en else R.drawable.gold_new_ar
                                            3 -> if (languageManager.currentLanguage == "en") R.drawable.silver_new_en else R.drawable.silver_new_ar
                                            else -> null

                                        }

                                        if (planIcon != null)
                                            Image(
                                                painter = painterResource(id = planIcon),
                                                contentDescription = "premium",
                                                modifier = Modifier
                                                    .padding(start = 3.dp)
                                                    .size(if (data?.planType == 3) 60.sdp else 50.sdp)
                                                    .clip(shape = RoundedCornerShape(50.dp))
                                                    .clickable {}
                                            )
                                    }
                                }

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
                                        color = Color.White,
                                        fontSize = 16.ssp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                        maxLines = 1,
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        if (data?.age != null) {

                                            Text(
                                                text = ", ${data?.age ?: 0}",
                                                color = Color.White,
                                                fontSize = 16.ssp,
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
                                                modifier = Modifier.size(21.sdp),
                                                painter = painterResource(R.drawable.blue2),
                                                contentDescription = ""
                                            )
                                        }

                                    }


                                    Spacer(modifier = Modifier.weight(1f))


                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!data?.countryName.isNullOrEmpty()) {
                                        val context = LocalContext.current
                                        val countryNamesEn =
                                            remember {
                                                CountryListHelper.getEnglishCountryNames(
                                                    context
                                                )
                                            }
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


                                    Text(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 3.dp),
                                        text = buildString {

                                            if (data?.locationSetting == true) {
                                                // City
                                                if (!data?.city.isNullOrEmpty()) {
                                                    if (isNotEmpty()) append(", ")
                                                    append(
                                                        if (languageManager.currentLanguage == "en") data?.city
                                                            ?: "" else data?.cityAr ?: data?.city
                                                    )
                                                }
                                                // Country
                                                if (!data?.countryName.isNullOrEmpty()) {
                                                    if (isNotEmpty()) append(", ")
                                                    append(
                                                        if (languageManager.currentLanguage == "en") data?.countryName
                                                            ?: "" else data?.countryNameAr
                                                            ?: data?.countryName
                                                    )
                                                }
                                            }
                                        },
                                        color = Color(0xFFFAFAFA),
                                        fontSize = 10.ssp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                    )


                                    val distanceText = if (data?.distanceSetting == true) {
                                        data?.distanceAway?.let { distance ->
                                            if (distance < 5) {
                                                stringResource(R.string._5_km_away)

                                            } else {
                                                stringResource(R.string.km_away, distance.toInt())
                                            }
                                        }
                                    } else null


                                    distanceText?.let {
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    color = Color(0xFF40000000),
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.location_ic),
                                                contentDescription = "null",
                                                colorFilter = ColorFilter.tint(Color.White),
                                                modifier = Modifier.size(12.sdp)
                                            )
                                            horizontalSpace(5)

                                            Text(
                                                modifier = Modifier,
                                                text = it,
                                                textAlign = TextAlign.Center,
                                                color = Color.White,
                                                fontSize = 8.ssp,
                                                maxLines = 1,
                                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.sdp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 15.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        showTagImageTextBlackBg(
                                            text = data?.personalDetails?.currentProfession?.formatText()
                                                ?: ""
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        showTagImageTextBlackBgEducaion(
                                            text = data?.personalDetails?.educationLevel?.formatText()
                                                ?: ""
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .offset(y = 30.dp)
                                .padding(horizontal = 25.sdp),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(45.sdp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .clickable(enabled = !isActionInProgress) {
                                        isClickLocked = true
                                        viewModelM4.hitAction(
                                            access_token = SharedPreference.get(context).accessToken,
                                            request = ActionRequest(
                                                action = "reject", toUserId = data?.userId
                                            )
                                        )
                                    }
                                    .background(

                                        MaterialTheme.colorScheme.onTertiaryFixed,
                                        shape = RoundedCornerShape(50.dp)
                                    ), contentAlignment = Alignment.Center)
                            {

                                Image(
                                    painter = painterResource(R.drawable.cross_red_ic),
                                    contentDescription = null,
                                    modifier = Modifier.size(15.sdp)
                                )
                            }

                            horizontalSpace(25)

                            Box(
                                modifier = Modifier
                                    .size(45.sdp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .clickable(enabled = !isActionInProgress) {
                                        isClickLocked = true
                                        viewModelM4.hitAction(
                                            access_token = SharedPreference.get(context).accessToken,
                                            request = ActionRequest(
                                                action = "like", toUserId = data?.userId
                                            ))
                                    }
                                    .background(
                                        MaterialTheme.colorScheme.onTertiaryFixed,
                                        shape = RoundedCornerShape(50.dp)
                                    ), contentAlignment = Alignment.Center)
                            {

                                Image(
                                    painterResource(R.drawable.like_purple_ic),
                                    contentDescription = "like",
                                    modifier = Modifier
                                       /* .clickable(enabled = !isActionInProgress) {
                                            isClickLocked = true
                                            viewModelM4.hitAction(
                                                access_token = SharedPreference.get(context).accessToken,
                                                request = ActionRequest(
                                                    action = "like", toUserId = data?.userId
                                                ))
                                        }*/
                                        .size(25.sdp),
                                )

                            }

                            horizontalSpace(25)
                            Box(
                                modifier = Modifier
                                    .size(45.sdp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .clickable(enabled = !isActionInProgress) {
                                        isClickLocked = true
                                        viewModelM4.hitAction(
                                            access_token = SharedPreference.get(context).accessToken,
                                            request = ActionRequest(
                                                action = "superlike", toUserId = data?.userId
                                            )
                                        )
                                    }
                                    .background(
                                        // Color.Black,
                                        MaterialTheme.colorScheme.onTertiaryFixed,
                                        shape = RoundedCornerShape(50.dp)
                                    ), contentAlignment = Alignment.Center)
                            {
                                Image(
                                    painter = painterResource(R.drawable.pink_super_like_ic),
                                    contentDescription = null,
                                    modifier = Modifier.size(25.sdp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(50.sdp))
            }
            Spacer(modifier = Modifier.height(15.sdp))

            Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),) {
                if (!viewModel.getMatchList.isEmpty()) {
                } else {
                }

            }

            if (isActionInProgress) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(100f)
                        /*.background(Color.Black.copy(alpha = 0.2f)) // optional dim*/
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                }
                            }
                        }
                )
            }

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
