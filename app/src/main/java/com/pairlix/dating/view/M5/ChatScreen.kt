package com.pairlix.dating.view.M5

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.ChatItem
import com.pairlix.dating.ReusedComponents.ChatStickyChips
import com.pairlix.dating.ReusedComponents.SearchBar
import com.pairlix.dating.ReusedComponents.countryNameToIsoCode
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.response.GetChatListResponse
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.response.Item
import com.pairlix.dating.response.ProfileViewResponse
import com.pairlix.dating.view.M4.FilterEditType
import com.pairlix.dating.view.M4.GridItem
import com.pairlix.dating.view.M4.timeAgo

import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.handleCoroutineException
import kotlin.collections.chunked
import kotlin.collections.forEach
import kotlin.collections.get
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pairlix.dating.agora.CallViewModel
import com.pairlix.dating.helper.formatChatTime
import com.pairlix.dating.helper.parseMediaUrls
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.viewModel.ChatViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.colorResource
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.firbase.NotificationBus
import com.pairlix.dating.utils.SocketManager
import com.pairlix.dating.utils.SocketState
import com.pairlix.dating.view.M4.ChatSkeletonScreen
import com.pairlix.dating.view.M4.SkeletonChatItem
import jakarta.inject.Inject
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    viewModelM4: M4ViewModel,
    viewModelM5: M5ViewModel,
    authViewModel: AuthViewModel,
    chatViewmodel: ChatViewModel = hiltViewModel(),

) {
    var searchText by remember { mutableStateOf("") }
    val chatListt = listOf(
        ChatItem(0, stringResource(R.string.chat)), ChatItem(
            1,
            stringResource(R.string.profile_views)
        )
    )
    val context = LocalContext.current
    val profileList = remember { mutableStateListOf<GetMatchResponse.Data?>() }
    val chatListViewModel by viewModelM5.getChatList.collectAsState()
    val chatList by chatViewmodel.chatList.collectAsStateWithLifecycle()
    val languageManager = LocalLanguageManager.current
    var isLoadingMore by remember { mutableStateOf(false) }
    var showPlanPopUp by remember { mutableStateOf(false) }
    var hasMorePages by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasApiResponded by remember { mutableStateOf(false) }
    val getPreviewProfile by authViewModel.getPreviewProfile.observeAsState()
    var planType by remember { mutableStateOf(0) }


    val isListLoading by chatViewmodel.chatListLoading.collectAsStateWithLifecycle()
    val socketState by chatViewmodel.socketState.collectAsStateWithLifecycle()

    var hasEverLoadedData by remember { mutableStateOf(chatList.isNotEmpty()) }
    var isRefreshing by remember { mutableStateOf(false) }

//    if (socketState==SocketState.DISCONNECTED) {
//        ChatSkeletonScreen()
//    }

    LaunchedEffect(getPreviewProfile) {
        getPreviewProfile.let {
            if (it is EmpResource.Success){
                planType=it.value.data?.activePlanType?:0
            }
        }

    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(3000)
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        NotificationBus.events.collect{
           // chatViewmodel.clearList()
            chatViewmodel.setupChatListListener()

            val myUserId = SharedPreference.get(context).userID
            chatViewmodel.setupMessageReadListener(myUserId)

            chatViewmodel.requestChatList(
                search = searchText,
                page = chatViewmodel.currentPage.value,
                size = 10
            )
        }
    }



    LaunchedEffect(socketState) {
        if (socketState == SocketState.CONNECTED) {
           // chatViewmodel.clearList()
            isRefreshing = true

            chatViewmodel.setupChatListListener()
            val myUserId = SharedPreference.get(context).userID
            chatViewmodel.setupMessageReadListener(myUserId)

            viewModelM5.hitProfileViewList(SharedPreference.get(context).accessToken)
            chatViewmodel.requestChatList(
                search = searchText,
                page = chatViewmodel.currentPage.value,
                size = 10
            )
        }
    }

  /*  LaunchedEffect(socketState) {
        if (socketState == SocketState.CONNECTED) {
            chatViewmodel.clearList()
            chatViewmodel.setupChatListListener()
            viewModelM5.hitProfileViewList(SharedPreference.get(context).accessToken)
            chatViewmodel.requestChatList(
                search = searchText,
                page = chatViewmodel.currentPage.value,
                size = 10
            )
        }
    }*/
    var isFirstLoad by remember { mutableStateOf(true) }

    var isReadyToShowEmpty by remember { mutableStateOf(false) }

    LaunchedEffect(isListLoading, chatList) {
        if (!isListLoading) {
            isFirstLoad = false
            hasApiResponded = true
            // Only allow empty state AFTER loading done + list confirmed empty
            if (chatList.isEmpty()) {
                delay(300) // wait 300ms to be sure list won't populate
                if (chatList.isEmpty()) {
                    isReadyToShowEmpty = true
                }
            } else {
                isReadyToShowEmpty = false
            }
        }
    }

    LaunchedEffect(chatList) {
        if (chatList.isNotEmpty()) {
            hasEverLoadedData = true
            isRefreshing = false
            isFirstLoad = false
            isReadyToShowEmpty = false
        }
    }


  /*  LaunchedEffect(Unit) {
        if (chatList.isNotEmpty()) {
            hasEverLoadedData = true
            return@LaunchedEffect  // already have data, skip delay entirely
        }
        delay(1000)
        hasEverLoadedData = true  // timeout reached, stop skeleton regardless
    }*/





    LaunchedEffect(chatViewmodel.currentPage.value) {
        chatViewmodel.requestChatList(
            search = searchText,
            page = chatViewmodel.currentPage.value,
            size = 10
        )
        /*  viewModelM5.hitGetChatList(SharedPreference.get(context).accessToken,
              page = 1, size = 10)*/
    }

    LaunchedEffect(Unit) {
        viewModelM5.profileViewList.collectLatest { it ->
            when (it) {
                is EmpResource.Loading -> {
                   // CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    profileList.clear()
                    profileList.addAll(it.value.data ?: emptyList())
                    viewModelM5.resetProfileViewList()
                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                    viewModelM5.resetProfileViewList()
                }
                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

    var showSkelton by  remember {   mutableStateOf(false)}

    LaunchedEffect(chatListViewModel) {
        chatListViewModel.let {

            when (it) {

                is EmpResource.Success -> {
                    showSkelton = false
                    val newItems = it.value.data?.list?.filterNotNull() ?: emptyList()

                    if (newItems.isEmpty()) {
                        hasMorePages = false
                    } else {
                        /* chatList.clear()
                         chatList.addAll(newItems)*/
                    }

                    isLoadingMore = false
                }

                is EmpResource.Loading -> {
                    showSkelton =true



                }

                else -> {}
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collectLatest { lastVisibleIndex ->

            val totalItems = listState.layoutInfo.totalItemsCount

            if (
                lastVisibleIndex != null &&
                lastVisibleIndex >= totalItems - 2 &&
                !isLoadingMore &&
                hasMorePages &&
                totalItems > 0  // ✅ Ensure there are items loaded
            ) {
                isLoadingMore = true
                chatViewmodel.currentPage.value++

                /*  viewModelM5.hitGetChatList(
                      SharedPreference.get(context).accessToken,
                      page = currentPage.value,
                      size = 10
                  )*/
            }
        }
    }



    // Replace your DisposableEffect(lifecycleOwner) with this:
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                chatViewmodel.currentPage.value = 1
                hasMorePages = true

                // ✅ Re-register listener and refresh list on resume
                chatViewmodel.setupChatListListener()
                val myUserId = SharedPreference.get(context).userID
                chatViewmodel.setupMessageReadListener(myUserId)
                chatViewmodel.requestChatList(
                    search = searchText,
                    page = 1,
                    size = 10
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            CustomLoader.hideLoader()
        }
    }

    val filteredChatList = remember(searchText, chatList) {

        chatList
            .filter { item ->

                val lastMsg = item.lastMessage?.trim()
                val matchDate = item.matchUpdatedAt?.trim()

                val isLastMessageEmpty =
                    lastMsg.isNullOrEmpty() || lastMsg.equals("null", true)

                val isMatchDateEmpty =
                    matchDate.isNullOrEmpty() || matchDate.equals("null", true)

                val isBothEmpty = isLastMessageEmpty && isMatchDateEmpty

                val isValidChat = !isBothEmpty

                if (searchText.isBlank()) {
                    isValidChat
                } else {
                    val fullName =
                        "${item.otherUserFirstName.orEmpty()} ${item.otherUserLastName.orEmpty()}"
                    isValidChat && fullName.contains(searchText, ignoreCase = true)
                }
            }

            // ✅ PURE TIME BASED (CORRECT FOR YOU)
            .sortedByDescending { item ->

                val lastMsg = item.lastMessage?.trim()
                val isLastMessageEmpty =
                    lastMsg.isNullOrEmpty() || lastMsg.equals("null", true)

                if (!isLastMessageEmpty) {
                    // ✅ message hai → use message time
                    item.lastMessageAt ?: ""
                } else {
                    // ✅ no message → use match time
                    item.matchUpdatedAt ?: ""
                }
            }
    }
Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {  }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.chat),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium))
        )
    }
    val showSkeleton = isFirstLoad && chatList.isEmpty()

    if (showSkeleton) {
        ChatSkeletonScreen()
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 16.dp, end = 16.dp, top = 20.dp)
                .navigationBarsPadding().imePadding()


        ) {

            stickyHeader {
                when (viewModelM5.selectedChipIndex.value) {
                    0 -> {
                        SingletonObject.isFromProfileView = false
                    }

                    1 -> {
                        SingletonObject.isFromProfileView = true
                    }

                }

                ChatStickyChips(
                    selectedIndex = viewModelM5.selectedChipIndex.value,
                    chatList = chatListt,
                    onChipClick = { viewModelM5.selectedChipIndex.value = it }
                )

            }


            if (viewModelM5.selectedChipIndex.value == 0) {

                item {
                    SearchBar(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)
                    )
                }

                itemsIndexed(filteredChatList) { index, item ->
                    ChatRowItem(navController, item)
                }
                if (isReadyToShowEmpty && filteredChatList.isEmpty()) {
                    item {
                    Image(
                            painterResource(R.drawable.no_chat_img),
                            contentDescription = "null",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp)
                                .height(300.dp)
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.no_chat_yet),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))

                        )


                    }
                }


            } else {
                if (profileList.isEmpty()) {
                    item {
                        Image(
                            painter = painterResource(R.drawable.no_profile_view_pic),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(300.dp)
                        )
                        verticalSpace(20)
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(R.string.no_profile_views_yet),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                } else

                    items(profileList.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            rowItems.forEach { item ->
                                GridItem(
                                    modifier = Modifier.weight(1f),
                                    navController = navController as NavHostController,
                                    data = item!!,
                                    viewModel = viewModelM4,
                                    authViewModel = authViewModel,
                                    icon = false, planType = planType, showPlanPopUp = { showPlanPopUp = it }
                                )
                            }

                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
            }

            item {
                verticalSpace(80)
            }
        }

       /* if(showPlanPopUp){
            PlanPopUp(onDismiss ={ showPlanPopUp = false}, navController)
        }*/

    }
}


@Composable
private fun ChatRowItem(
    navController: NavController,
    item: Item,
) {
    var context = LocalContext.current
    val profileImageUrl = item.otherUserProfileImage
    val languageManager = LocalLanguageManager.current

    val isMyMessage = item.lastMessageSenderId == SharedPreference.get(context).userID
    val hasMessage = !item.lastMessage.isNullOrBlank() && item.lastMessage != "null"
    val isUnreadFromOther =
        hasMessage &&
                !isMyMessage &&
                (item.readAt.isNullOrEmpty() || item.readAt == "null")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    Screen.ChatScreenOneToOne.passId(
                        id = item.otherUserId ?: "",
                        name = item.otherUserFirstName ?: "",
                        age = item.otherUserAge ?: "",
                        image = item.otherUserProfileImage ?: "",
                        isOnline = item.otherIsOnline ?: false,
                        isActive = item.isActive ?: false,
                        matchDate = item.matchUpdatedAt ?: "",
                        isDocument = item.otherUserDocumentVerified ?: false,
                        isFace = item.otherUserFaceVerified ?: false
                    )
                )
            }
    ) {

        AsyncImage(
            model = profileImageUrl ?: R.drawable.no_dp_icon,
            contentDescription = "profile pic",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(50.dp)
                .width(48.dp)
                .clip(shape = CircleShape)
        )

        horizontalSpace(10)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${item.otherUserFirstName ?: stringResource(R.string.no_name)} ${item.otherUserLastName ?: ""}, ${item.otherUserAge ?: ""}".trim(),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )
                    horizontalSpace(1)
                    if (item.otherUserFaceVerified == true) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(R.drawable.blue1),
                            contentDescription = ""
                        )
                    }
                    horizontalSpace(1)
                    if (item.otherUserDocumentVerified == true) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(R.drawable.blue2),
                            contentDescription = ""
                        )
                    }
                    horizontalSpace(3)
                    if (item.activePlanType == 2 || item.activePlanType == 3) {
                        val planIcon = when (item.activePlanType) {
                            2 -> if (languageManager.currentLanguage == "en") R.drawable.gold_new_en else R.drawable.gold_new_ar
                            3 -> if (languageManager.currentLanguage == "en") R.drawable.silver_new_en else R.drawable.silver_new_ar
                            else -> null
                        }
                        if (planIcon != null) {
                            Image(
                                painter = painterResource(id = planIcon),
                                contentDescription = "premium",
                                modifier = Modifier
                                    //.size(if (item.activePlanType == 2) 50.dp else 70.dp)
                                    .clip(shape = RoundedCornerShape(50.dp))
                            )
                        }
                    }
                }

                verticalSpace(6)
                // Message preview row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // ✅ Show tick only for MY sent messages
                    if (isMyMessage) {
                        val isRead = !item.readAt.isNullOrEmpty() && item.readAt != "null"
                        Image(
                            painter = painterResource(
                                if (isRead) R.drawable.double_tick_green_ic
                                else R.drawable.tick_icon
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(width = 15.dp, height = 8.dp)
                        )
                        horizontalSpace(5)
                    }

                    val lastMsgText = run {
                        val lastMsg = item.lastMessage?.trim().orEmpty()
                        val lowerMsg = lastMsg.lowercase()

                        fun isVideo(msg: String) = msg.endsWith(".mp4") || msg.endsWith(".mkv") ||
                                msg.endsWith(".mov") || msg.endsWith(".avi") || msg.endsWith(".webm")
                        fun isImage(msg: String) = msg.endsWith(".jpg") || msg.endsWith(".jpeg") ||
                                msg.endsWith(".png") || msg.endsWith(".gif") || msg.endsWith(".webp")
                        fun isAudio(msg: String) = msg.endsWith(".m4a") || msg.endsWith(".mp3") ||
                                msg.endsWith(".wav") || msg.endsWith(".aac") || msg.endsWith(".ogg")

                        when {
                            lastMsg.startsWith("[") && lastMsg.endsWith("]") -> {
                                val urls = parseMediaUrls(lastMsg)
                                when {
                                    urls.any { isVideo(it.lowercase()) } -> stringResource(R.string.video)
                                    urls.any { isImage(it.lowercase()) } -> stringResource(R.string.image)
                                    urls.any { isAudio(it.lowercase()) } -> stringResource(R.string.audio)
                                    else -> stringResource(R.string.media)
                                }
                            }
                            isVideo(lowerMsg) -> stringResource(R.string.video)
                            isImage(lowerMsg) -> stringResource(R.string.image)
                            isAudio(lowerMsg) -> stringResource(R.string.audio)
                            else -> lastMsg.ifEmpty { stringResource(R.string.no_message) }
                        }
                    }

                    Text(
                        text = lastMsgText,
                        // ✅ Bold + darker only for unread messages from other user
                        color = if (isUnreadFromOther) MaterialTheme.colorScheme.onBackground
                        else Color(0xFF6D6D6D),
                        fontSize = if (isUnreadFromOther) 13.sp else 12.sp,
                        fontFamily = if (isUnreadFromOther)
                            FontFamily(Font(R.font.axiforma_semi_bold))
                        else
                            FontFamily(Font(R.font.axiforma_regular)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                verticalSpace(6)

                Text(
                    text = formatChatTime(item.lastMessageAt ?: "", context),
                    color = Color(0xFF6D6D6D),
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )
            }

            // ✅ Unread dot on RIGHT side — only for messages from other user
            if (isUnreadFromOther) {
                horizontalSpace(8)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = Color(0xFF1D7721),
                            shape = CircleShape
                        )
                )
            }
        }
    }

    verticalSpace(10)

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        thickness = 1.dp,
        color = Color(0xFFEDF1F3)
    )
}