package com.pairlix.dating.view.M4

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pairlix.dating.LanguageManager.AppLanguageManager
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.MainActivity
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.PlanPopUp
import com.pairlix.dating.ReusedComponents.countryCodeToFlagEmoji
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.firbase.NotificationBus
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.CountryListHelper
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SavedProfilesManager
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.response.ActivePlanResponse
import com.pairlix.dating.response.GetMatchResponse

import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.profileDetails.PreviewProfileObserver
import com.pairlix.dating.view.profileDetails.PreviewStickyChips
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel

import ir.kaaveh.sdpcompose.sdp


import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
fun timeAgo(apiTime: String?): String {

    if (apiTime.isNullOrEmpty()) return ""

    return try {

        // ✅ App language fix
        val locale = Locale(AppLanguageManager.currentLanguage)

        val context = MyApplication.appContext.createConfigurationContext(
            Configuration().apply {
                setLocale(locale)
            }
        )

        // ✅ Always parse in English (API format)
        val sdf = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.ENGLISH
        )
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val past = sdf.parse(apiTime) ?: return ""
        val now = Date()

        val diffMillis = now.time - past.time

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

        when {
            minutes < 1 ->
                context.getString(R.string.just_now)

            minutes < 60 ->
                context.getString(R.string.minutes_ago, minutes)

            hours < 24 ->
                context.getString(R.string.hours_ago, hours)

            days < 7 ->
                context.getString(R.string.days_ago, days)

            else ->
                SimpleDateFormat("dd MMM yyyy", locale)
                    .format(past)
        }

    } catch (e: Exception) {
        ""
    }
}

/*@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActivityScreen(
    navController: NavController,
    viewModelM4: M4ViewModel,
    authViewModel: AuthViewModel
) {
    val chipList = stringArrayResource(R.array.chip_list).toList()
    val context = LocalContext.current
    val getPreviewProfile by authViewModel.getPreviewProfile.observeAsState()
    var planType by remember { mutableStateOf(0) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedIndex by remember { mutableStateOf(0) }
    val hasLoadedOnce = remember { mutableStateMapOf(0 to false, 1 to false, 2 to false, 3 to false) }
    var previewProfileLoaded by remember { mutableStateOf(false) }

    // ✅ StateFlow collect karo - Compose inhe track karega
    val userLikeList by viewModelM4.getUserLike.collectAsState()
    val linkSendList by viewModelM4.likeSent.collectAsState()
    val matchesList by viewModelM4.matches.collectAsState()
    val rejectedList by viewModelM4.rejected.collectAsState()

    // ✅ Chip ke hisab se correct list - Compose reactively update karega
    val selectedChip = viewModelM4.selectedChipIndex.value
    val userList = when (selectedChip) {
        0 -> userLikeList
        1 -> linkSendList
        2 -> matchesList
        else -> rejectedList
    }


    LaunchedEffect(Unit) {
         viewModelM4.resetAllList()
        viewModelM4.hitUserActivity(
            access_token = SharedPreference.get(context).accessToken,
            actionType = when (selectedChip) {
                0 -> "likes"
                1 -> "sentLikes"
                2 -> "matches"
                else -> "rejected"
            }
        )
    }


    LaunchedEffect(Unit) {
        if (!previewProfileLoaded) {
            authViewModel.hitPreviewProfile(
                access_token = SharedPreference.get(context).accessToken
            )
            previewProfileLoaded = true
        }
    }

    LaunchedEffect(getPreviewProfile) {
        getPreviewProfile?.let {
            if (it is EmpResource.Success) {
                planType = it.value.data?.activePlanType ?: 0
            }
        }
    }

    // ✅ Chip change par sirf UserActivity call karo
    LaunchedEffect(selectedChip) {
        selectedIndex=selectedChip
        if ((selectedIndex==0&&userLikeList.isEmpty())||(selectedIndex==1&&linkSendList.isEmpty())||(selectedIndex==2&&matchesList.isEmpty())||(selectedIndex==3&&rejectedList.isEmpty()))
        viewModelM4.hitUserActivity(
            access_token = SharedPreference.get(context).accessToken,
            actionType = when (selectedChip) {
                0 -> "likes"
                1 -> "sentLikes"
                2 -> "matches"
                else -> "rejected"
            }
        )
    }



    LaunchedEffect(Unit) {
        NotificationBus.events.collect {
            viewModelM4.hitUserActivity(
                access_token = SharedPreference.get(context).accessToken,
                actionType = when (viewModelM4.selectedChipIndex.value) {
                    0 -> "likes"
                    1 -> "sentLikes"
                    2 -> "matches"
                    else -> "rejected"
                }
            )
        }
    }

    // ✅ Broadcast receiver for push notifications
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val type = intent?.getStringExtra("type") ?: ""
                Log.d("ActivityScreen", "Broadcast received: type=$type, chip=${viewModelM4.selectedChipIndex.value}")

                val actionType = when (type) {
                    "like", "superlike" -> "likes"
                    "match" -> "matches"
                    else -> null
                }

                actionType?.let {
                    viewModelM4.hitUserActivity(
                        access_token = SharedPreference.get(context).accessToken,
                        actionType = when (viewModelM4.selectedChipIndex.value) {
                            0 -> "likes"
                            1 -> "sentLikes"
                            2 -> "matches"
                            else -> "rejected"
                        }
                    )
                }
            }
        }

        val filter = IntentFilter("com.pairlix.NOTIFICATION_RECEIVED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        onDispose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: Exception) {
                Log.e("ActivityScreen", "Receiver already unregistered: ${e.message}")
            }
        }
    }

    // ✅ Observer - updateData call karega jo StateFlow update karega
    getUserActivityObserver(
        viewModel = viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { list ->
            viewModelM4.updateData(list ?: emptyList())
            viewModelM4.reset()
        }
    )

    // ✅ hasLoadedOnce - userList se track karo
    LaunchedEffect(userList.size) {
        if (userList.isNotEmpty()) {
            hasLoadedOnce[selectedChip] = true
        }
    }

// Loading logic mein per-tab flag use karo

    PreviewProfileObserver(
        viewModel = authViewModel,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { previewData ->
            planType = previewData?.activePlanType ?: 0
            Log.e("ActivityScreen", "planType: ${previewData?.activePlanType}")
        }
    )

    // ✅ Matches tab ke liye filtered lists
    val aiWeeklyList = matchesList.filter { it?.matchSource == "AI_WEEKLY" }
    val algorithmList = matchesList.filter { it?.matchSource == "ALGORITHM" }
    val otherList = matchesList.filter {
        it?.matchSource != "AI_WEEKLY" && it?.matchSource != "ALGORITHM"
    }

    val state = viewModelM4.getUserActivity.value
    val isLoading = state is EmpResource.Loading
    val isError = state is EmpResource.Failure

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
        ) {

            // ✅ Title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.activity),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                }
            }

            // ✅ Sticky chips
            stickyHeader {
                PreviewStickyChips(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 10.dp),
                    list = chipList,
                    selectedIndex = selectedChip,
                    onChipClick = {
                        viewModelM4.selectedChipIndex.value = it
                        viewModelM4.showBottomActions = it
                    }
                )
            }

            // ✅ Loading - sirf pehli baar skeleton dikhao
            val currentTabLoaded = hasLoadedOnce[selectedChip] == true

            if (!currentTabLoaded && isLoading) {
                item { SkeletonGrid() }
            } else if (isError && userList.isEmpty()) {
                item { SkeletonGrid() }
            } else if (userList.isEmpty() && !isLoading) {
                // ✅ Empty state
                item {
                    when (selectedChip) {
                        3 -> {
                            Image(
                                modifier = Modifier.fillMaxWidth().height(300.dp),
                                painter = painterResource(R.drawable.nodata_image),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                text = stringResource(R.string.profiles_you_skip_will_appear_here_for_reference),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )
                        }
                        0 -> {
                            Image(
                                modifier = Modifier.fillMaxWidth().height(300.dp),
                                painter = painterResource(R.drawable.nodata_image),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.complete_your_profile_and_upload_great_photos_to_get_noticed),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )
                        }
                        1 -> {
                            Image(
                                modifier = Modifier.fillMaxWidth().height(300.dp),
                                painter = painterResource(R.drawable.nodata_image),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.start_exploring_profiles_and_send_your_first_like),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )
                        }
                        2 -> {
                            Image(
                                modifier = Modifier.fillMaxWidth().height(300.dp),
                                painter = painterResource(R.drawable.shaikh_match_img),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.keep_swiping_the_right_connection_takes_time),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )
                        }
                    }
                }

            } else {

                if (selectedIndex == 2) {
                    // ✅ Matches tab - 3 sections

                    if (aiWeeklyList.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.ai_weekly_matches),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(aiWeeklyList.chunked(2)) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { item ->
                                    GridItem(
                                        modifier = Modifier.weight(1f),
                                        navController = navController as NavHostController,
                                        data = item!!,
                                        viewModel = viewModelM4,
                                        authViewModel = authViewModel,
                                        planType = planType
                                    )
                                }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    if (algorithmList.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.ai_random_matches),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(algorithmList.chunked(2)) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { item ->
                                    GridItem(
                                        modifier = Modifier.weight(1f),
                                        navController = navController as NavHostController,
                                        data = item!!,
                                        viewModel = viewModelM4,
                                        authViewModel = authViewModel,
                                        planType = planType
                                    )
                                }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    if (otherList.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.matches),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(otherList.chunked(2)) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { item ->
                                    GridItem(
                                        modifier = Modifier.weight(1f),
                                        navController = navController as NavHostController,
                                        data = item!!,
                                        viewModel = viewModelM4,
                                        authViewModel = authViewModel,
                                        planType = planType
                                    )
                                }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                } else {
                    // ✅ Likes / SentLikes / Rejected - userList directly use karo
                    items(if (selectedIndex==0)userLikeList.chunked(2) else if (selectedIndex==1)linkSendList.chunked(2) else rejectedList.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { item ->
                                GridItem(
                                    modifier = Modifier.weight(1f),
                                    navController = navController as NavHostController,
                                    data = item!!,
                                    viewModel = viewModelM4,
                                    authViewModel = authViewModel,
                                    planType = planType
                                )
                            }
                            if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item { verticalSpace(100) }
        }
    }
}*/


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActivityScreen(
    navController: NavController,
    viewModelM4: M4ViewModel,
    authViewModel: AuthViewModel
) {
    val chipList = stringArrayResource(R.array.chip_list).toList()
    val context = LocalContext.current
    val getPreviewProfile by authViewModel.getPreviewProfile.observeAsState()
    var planType by remember { mutableStateOf(0) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedIndex by remember { mutableStateOf(0) }
    var previewProfileLoaded by remember { mutableStateOf(false) }
    var showPlanPopUp by remember { mutableStateOf(false) }
    val userLikeList by viewModelM4.getUserLike.collectAsState()
    val linkSendList by viewModelM4.likeSent.collectAsState()
    val matchesList by viewModelM4.matches.collectAsState()
    val rejectedList by viewModelM4.rejected.collectAsState()
    val selectedChip = viewModelM4.selectedChipIndex.value
    val savedProfilesList = remember(selectedChip) { SavedProfilesManager.getSavedProfiles(context) }
    val userList = when (selectedChip) {
        0 -> userLikeList
        1 -> linkSendList
        2 -> matchesList
        3 -> rejectedList
        else -> savedProfilesList
    }
    val state by viewModelM4.getUserActivity.observeAsState(EmpResource.Idle)
    val isLoading = state is EmpResource.Loading

    LaunchedEffect(Unit) {
        viewModelM4.resetAllList()
        viewModelM4.hitUserActivity(
            access_token = SharedPreference.get(context).accessToken,
            actionType = when (selectedChip) {
                0 -> "likes"
                1 -> "sentLikes"
                2 -> "matches"
                else -> "rejected"
            }
        )
    }
    LaunchedEffect(Unit) {
        if (!previewProfileLoaded) {
            authViewModel.hitPreviewProfile(
                access_token = SharedPreference.get(context).accessToken
            )
            previewProfileLoaded = true
        }
    }
    LaunchedEffect(getPreviewProfile) {
        getPreviewProfile?.let {
            if (it is EmpResource.Success) {
                planType = it.value.data?.activePlanType ?: 0
            }
        }
    }
    LaunchedEffect(selectedChip) {
        selectedIndex = selectedChip
        if (userList.isEmpty()) {
            viewModelM4.hitUserActivity(
                access_token = SharedPreference.get(context).accessToken,
                actionType = when (selectedChip) {
                    0 -> "likes"
                    1 -> "sentLikes"
                    2 -> "matches"
                    else -> "rejected"
                }
            )
        }
    }
    LaunchedEffect(Unit) {
        NotificationBus.events.collect {
            viewModelM4.hitUserActivity(
                access_token = SharedPreference.get(context).accessToken,
                actionType = when (viewModelM4.selectedChipIndex.value) {
                    0 -> "likes"
                    1 -> "sentLikes"
                    2 -> "matches"
                    else -> "rejected"
                }
            )
        }
    }
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val type = intent?.getStringExtra("type") ?: ""
                val actionType = when (type) {
                    "like", "superlike" -> "likes"
                    "match" -> "matches"
                    else -> null
                }
                actionType?.let {
                    viewModelM4.hitUserActivity(
                        access_token = SharedPreference.get(context).accessToken,
                        actionType = when (viewModelM4.selectedChipIndex.value) {
                            0 -> "likes"
                            1 -> "sentLikes"
                            2 -> "matches"
                            else -> "rejected"
                        }
                    )
                }
            }
        }
        val filter = IntentFilter("com.pairlix.NOTIFICATION_RECEIVED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
        }
        onDispose {
            try { context.unregisterReceiver(receiver) } catch (e: Exception) { }
        }
    }

    getUserActivityObserver(
        viewModel = viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { list -> viewModelM4.updateData(list ?: emptyList())
            viewModelM4.reset() }
    )

    PreviewProfileObserver(
        viewModel = authViewModel,
        viewModelM4=viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { previewData ->
            authViewModel.getPreviewProfileData.value = previewData
            planType = previewData?.activePlanType ?: 0
        }
    )

    val aiWeeklyList = matchesList.filter { it?.matchSource == "AI_WEEKLY" }
   // val algorithmList = matchesList.filter { it?.matchSource == "ALGORITHM" }
    val otherList = matchesList.filter {
        it?.matchSource != "AI_WEEKLY"
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.activity),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                }
            }

            stickyHeader {
                PreviewStickyChips(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 10.dp),
                    list = chipList,
                    selectedIndex = selectedChip,
                    onChipClick = {
                        viewModelM4.selectedChipIndex.value = it
                        viewModelM4.showBottomActions = it
                    }
                )
            }

            // ✅ Simple: skeleton when loading, empty state or content when done
            if (isLoading) {
                item { SkeletonGrid() }
            } else if (userList.isEmpty()) {
                item {
                    when (selectedChip) {
                        0 -> {
                            Image(modifier = Modifier.fillMaxWidth().height(300.dp), painter = painterResource(R.drawable.nodata_image), contentDescription = "img")
                            verticalSpace(20)
                            Text(modifier = Modifier.fillMaxWidth(), text = stringResource(R.string.complete_your_profile_and_upload_great_photos_to_get_noticed), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.axiforma_bold)))
                        }
                        1 -> {
                            Image(modifier = Modifier.fillMaxWidth().height(300.dp), painter = painterResource(R.drawable.nodata_image), contentDescription = "img")
                            verticalSpace(20)
                            Text(modifier = Modifier.fillMaxWidth(), text = stringResource(R.string.start_exploring_profiles_and_send_your_first_like), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.axiforma_bold)))
                        }
                        2 -> {
                            Image(modifier = Modifier.fillMaxWidth().height(300.dp), painter = painterResource(R.drawable.shaikh_match_img), contentDescription = "img")
                            verticalSpace(20)
                            Text(modifier = Modifier.fillMaxWidth(), text = stringResource(R.string.keep_swiping_the_right_connection_takes_time), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.axiforma_bold)))
                        }
                        3 -> {
                            Image(modifier = Modifier.fillMaxWidth().height(300.dp), painter = painterResource(R.drawable.nodata_image), contentDescription = "img")
                            verticalSpace(20)
                            Text(text = stringResource(R.string.profiles_you_skip_will_appear_here_for_reference), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.axiforma_bold)))
                        }
                        else -> {
                            Image(modifier = Modifier.fillMaxWidth().height(300.dp), painter = painterResource(R.drawable.nodata_image), contentDescription = "img")
                            verticalSpace(20)
                            Text(text = "No saved profiles yet. Profiles you save will appear here.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.axiforma_bold)))
                        }
                    }
                }

            } else {
                if (selectedIndex == 2) {
                    if (aiWeeklyList.isNotEmpty()) {
                        item { Text(text = stringResource(R.string.ai_weekly_matches), fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontFamily = FontFamily(Font(R.font.axiforma_bold)), modifier = Modifier.padding(vertical = 8.dp)) }
                        items(aiWeeklyList.chunked(2)) { rowItems ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                rowItems.forEach { item -> GridItem(modifier = Modifier.weight(1f), navController = navController as NavHostController, data = item!!, viewModel = viewModelM4, authViewModel = authViewModel, planType = planType,showPlanPopUp = { showPlanPopUp = it }) }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                 /*   if (algorithmList.isNotEmpty()) {
                        item { Text(text = stringResource(R.string.ai_random_matches), fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontFamily = FontFamily(Font(R.font.axiforma_bold)), modifier = Modifier.padding(vertical = 8.dp)) }
                        items(algorithmList.chunked(2)) { rowItems ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                rowItems.forEach { item -> GridItem(modifier = Modifier.weight(1f), navController = navController as NavHostController, data = item!!, viewModel = viewModelM4, authViewModel = authViewModel, planType = planType,showPlanPopUp = { showPlanPopUp = it }) }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }*/

                    if (otherList.isNotEmpty()) {
                        item { Text(text = stringResource(R.string.matches), fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontFamily = FontFamily(Font(R.font.axiforma_bold)), modifier = Modifier.padding(vertical = 8.dp)) }
                        items(otherList.chunked(2)) { rowItems ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                rowItems.forEach { item -> GridItem(modifier = Modifier.weight(1f), navController = navController as NavHostController, data = item!!, viewModel = viewModelM4, authViewModel = authViewModel, planType = planType,showPlanPopUp = { showPlanPopUp = it }) }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    val activeList = when (selectedIndex) {
                        0 -> userLikeList
                        1 -> linkSendList
                        3 -> rejectedList
                        4 -> savedProfilesList
                        else -> emptyList()
                    }
                    items(activeList.chunked(2)) { rowItems ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowItems.forEach { item -> GridItem(modifier = Modifier.weight(1f), navController = navController as NavHostController, data = item!!, viewModel = viewModelM4, authViewModel = authViewModel, planType = planType,showPlanPopUp = { showPlanPopUp = it }) }
                            if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item { verticalSpace(100) }
        }
    }

        if(showPlanPopUp){
            PlanPopUp(onDismiss ={ showPlanPopUp = false}, navController = navController)
        }
}

@Composable
fun GridItem(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    data: GetMatchResponse.Data,
    viewModel: M4ViewModel,
    authViewModel: AuthViewModel,
    showPlanPopUp:(Boolean) -> Unit ,
    icon: Boolean = true, planType: Int
) {
    val languageManager = LocalLanguageManager.current
    val userData = authViewModel.getPreviewProfileData.value
    val shouldBlurByPlan = when (userData?.activePlanType) {
        3 -> false
        2 -> data.activePlanType == 3
        1 -> data.activePlanType in listOf(2, 3)
        else -> false
    }
    val isBlurred = data.blurProfile == true

    val shouldBlur = isBlurred || shouldBlurByPlan
    Box(
        modifier = modifier
            .clickable {
                authViewModel.setData(data)

                val chipIndex = viewModel.selectedChipIndex.value
                viewModel.showBottomActions = chipIndex

                when {
                    planType == 1 && chipIndex == 0 -> {
                        showPlanPopUp(true)
                    }
                    SingletonObject.isComeFromBlockedProfile == true -> {
                        viewModel.showBottomActions = 5
                        navController.navigate(Screen.HomeScreenDetailScreen.route)
                    }
                    else -> {
                        navController.navigate(Screen.HomeScreenDetailScreen.route)
                    }
                }
            }
            .height(160.sdp)
            .clip(RoundedCornerShape(12.dp))
        //.background(Color(0xFFD1C4E9))
        , contentAlignment = Alignment.Center
    ) {


        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val imageModel = ImageRequest.Builder(LocalContext.current)
                .data(data.profileImages?.firstOrNull())
             /*   .placeholder(R.drawable.no_dp_icon)
                .error(R.drawable.no_dp_icon)
                .fallback(R.drawable.no_dp_icon)*/
                .build()


            AsyncImage(
                model = imageModel,
                contentDescription = "pic",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            if (shouldBlur) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(18.dp)
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.15f))
                )
            }
        }
        if (icon) {

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart).padding(start = 10.dp, top=10.dp )

            ) {
                if (data?.activePlanType == 2 || data?.activePlanType == 3) {
                    val planIcon = when (data!!.activePlanType) {
                        2 -> if (languageManager.currentLanguage == "en") R.drawable.gold_new_en else R.drawable.gold_new_ar
                        3 -> if (languageManager.currentLanguage == "en") R.drawable.silver_new_en else R.drawable.silver_new_ar
                        else -> null

                    }

                    if (planIcon != null)
                        Image(
                            painter = painterResource(id = planIcon),
                            contentDescription = "premium",
                            modifier = Modifier
                                //.size(if (data.activePlanType == 2) 50.dp else 65.dp)
                                .clip(shape = RoundedCornerShape(50.dp))
                        )
                }
            }



            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                val icon = when (data.action) {
                    "superlike" -> R.drawable.star_pink_correct_dp_ic
                    "like" -> R.drawable.like_btn_ic
                    else -> null
                }
                icon?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = "like",
                        modifier = Modifier.size(30.dp)
                    )
                }

            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.35f))
                .align(Alignment.BottomCenter)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = if ((data.firstName?.length
                            ?: 0) < 8
                    ) "${data.firstName ?: ""}" else data.firstName?.substring(0, 7) + "...",
                    color = Color(0xFF590988),
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                    maxLines = 1
                )

                if (data?.age != null) {
                    Text(
                        text = ",${data?.age ?: ""}",
                        color = Color(0xFF590988),
                        fontSize = 10.sp,
                        maxLines = 1,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold))
                    )
                    Spacer(modifier = Modifier.width(4.dp))


                }
                // if(data?.usisFaceVerified==true) {

                Image(
                    modifier = Modifier.size(12.sdp),
                    painter = painterResource(R.drawable.blue1),
                    contentDescription = ""
                )

                //}

                Spacer(modifier = Modifier.width(4.dp))

                Spacer(Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .background(
                            Color(0xFF0000000).copy(alpha = 0.25f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                    text = timeAgo(data.updatedAt),
                    color = Color.White,
                    fontSize = 7.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )
            }
            verticalSpace(5)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                verticalAlignment = Alignment.CenterVertically
            ) {

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



                Spacer(modifier = Modifier.width(5.sdp))


                val languageManager = LocalLanguageManager.current
                val isArabic = languageManager.currentLanguage == "ar"
                val cityName = if (isArabic) {
                    if (!data.cityAr.isNullOrEmpty()) data.cityAr else data.city
                } else {
                    if (!data.city.isNullOrEmpty()) data.city else data.cityAr
                }


                val countryName = if (isArabic) {
                    if (!data.countryNameAr.isNullOrEmpty()) data.countryNameAr else data.countryName
                } else {
                    if (!data.countryName.isNullOrEmpty()) data.countryName else data.countryNameAr
                }


                val locationText = listOfNotNull(
                    cityName?.takeIf { it.isNotBlank() },
                    countryName?.takeIf { it.isNotBlank() }
                ).joinToString(", ")

                Text(
                    modifier = Modifier.weight(1f),
                    text = locationText,
                    color = Color(0xFF590988),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                )


            }


        }


    }

}


fun getUserActivityObserver(
    viewModel: M4ViewModel,
    context: MainActivity,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    onSuccess: (List<GetMatchResponse.Data?>?) -> Unit
) {
    viewModel.getUserActivity.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (state.value.success == true) {
                    state.let {
                        onSuccess(state.value.data)
                    }
                }
            }

            else -> {
                // no-op
            }
        }
    }


}

