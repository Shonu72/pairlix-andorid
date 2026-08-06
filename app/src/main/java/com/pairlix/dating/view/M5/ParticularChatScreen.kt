package com.pairlix.dating.view.M5

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.ChatViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.lifecycle.compose.LocalLifecycleOwner

import com.pairlix.dating.MainActivity
import com.pairlix.dating.ReusedComponents.ActionRowItem
import com.pairlix.dating.ReusedComponents.AudioMessagePlayerReceiver
import com.pairlix.dating.ReusedComponents.AudioMessagePlayerSender
import com.pairlix.dating.ReusedComponents.AudioWave
import com.pairlix.dating.ReusedComponents.CustomDialog
import com.pairlix.dating.ReusedComponents.FullScreenImagePreview
import com.pairlix.dating.ReusedComponents.FullScreenVideoPlayer
import com.pairlix.dating.ReusedComponents.MultipleMediaPreview
import com.pairlix.dating.ReusedComponents.ReceiverImage
import com.pairlix.dating.ReusedComponents.ReceiverMessage
import com.pairlix.dating.ReusedComponents.ReceiverMultipleMedia
import com.pairlix.dating.ReusedComponents.ReceiverVideo
import com.pairlix.dating.ReusedComponents.SenderImage
import com.pairlix.dating.ReusedComponents.SenderMessage
import com.pairlix.dating.ReusedComponents.SenderMultipleMedia
import com.pairlix.dating.ReusedComponents.SenderVideo
import com.pairlix.dating.ReusedComponents.formatSeconds
import com.pairlix.dating.ReusedComponents.formatTimeFromUtc
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.ImagePicker
import com.pairlix.dating.helper.getMimeType
import com.pairlix.dating.helper.isVideoUrl
import com.pairlix.dating.requests.ActionRequest
import com.pairlix.dating.response.ModerateContentResponse
import com.pairlix.dating.view.home.ReasonOption
import com.pairlix.dating.agora.CallViewModel
import com.pairlix.dating.helper.formatMatchDate
import com.pairlix.dating.helper.parseMediaUrls
import com.pairlix.dating.utils.SocketState
import com.pairlix.dating.view.home.HomePageObserver
import com.pairlix.dating.viewModel.ChatAudioViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import io.agora.rtc2.Constants
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt
import kotlin.text.compareTo
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.pairlix.dating.ReusedComponents.PlanPopUp
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.getFileType
import com.pairlix.dating.helper.isVideoUrlSafe
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.response.MatchTimingData
import com.pairlix.dating.response.MatchTimingResponse
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

data class ActionItem(
    val id: Int, val title: String, val icon: Int
)

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenOneToOne(
    navController: NavController,
    m5ViewModel: M5ViewModel,
    authViewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    chatViewModel: ChatViewModel = hiltViewModel(),
    chatAudioViewModel: ChatAudioViewModel = hiltViewModel(),
    callViewModel: CallViewModel,
    socketViewModel: SocketViewModel,
    userId: String,
    name: String,
    age: String? = null,
    image: String,
    isOnline: Boolean? = false,
    isActive: Boolean? = false,
    matchDate: String?,
    isDocument: Boolean? = false,
    isFace: Boolean? = false,

    ) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val roomId by chatViewModel.roomId.collectAsState()
    val uploadState by authViewModel.uploadImageFile.observeAsState()
    val messageList by chatViewModel.messages.collectAsState()
    Log.e("messge", "${messageList}: ")
    var type by remember { mutableStateOf("") }
    val context = LocalContext.current
    var mid by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var chatText by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showReportBottomSheet by remember { mutableStateOf(false) }
    var blockAndReportDialog by remember { mutableStateOf(false) }
    var showImageDialog by remember { mutableStateOf(false) }
    var planPopUp by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetStateReport = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var mediaRecorder: MediaRecorder? by remember { mutableStateOf(null) }
    var previewImageUrl by remember { mutableStateOf<String?>(null) }
    var pendingCallType by remember { mutableStateOf<String?>(null) }
    val isMessagesLoading by chatViewModel.isMessagesLoading.collectAsState()
    val checkAbusive by m5ViewModel.checkAbusiveWord.collectAsState()

    fun handleCallNavigation(type: String) {
        val safeRoomId = roomId

        if (safeRoomId.isNullOrEmpty()) {
            chatViewModel.emitCallError(
                context.getString(R.string.connection_not_ready)
            )
            return
        }
        callViewModel.setCallerType("sender")
        callViewModel.resetCallEnd()
        callViewModel.updateCallType(type)
        callViewModel.updateCallerName(name)
        navController.navigate(
            Screen.VideoCallScreen.createRoute(
                safeRoomId,
                userId,
                image
            )
        )
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val micGranted = permissions[Manifest.permission.RECORD_AUDIO] == true
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true

        when (pendingCallType) {
            "audio" -> {
                if (micGranted) {
                    handleCallNavigation("audio")
                } else {
                    Toast.makeText(context, "Microphone permission required", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            "video" -> {
                if (micGranted && cameraGranted) {
                    handleCallNavigation("video")
                } else {
                    Toast.makeText(
                        context,
                        "Camera & Microphone permissions required",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /*
        var moderateData by remember { mutableStateOf<ModerateContentResponse.Data?>(null) }
    */


    var blockDialog by remember { mutableStateOf(false) }
    var showIncomingScreen by remember { mutableStateOf(false) }
    var unMatchedDialog by remember { mutableStateOf(false) }
    var previewVideoUrl by remember { mutableStateOf<String?>(null) }
    var previewMultipleMediaUrls by remember { mutableStateOf<List<String>?>(null) }
    var previewMultipleStartIndex by remember { mutableStateOf(0) }
    var bottomSheetIndex by remember { mutableStateOf(0) }
    var pendingUploadType by remember { mutableStateOf("") }
    val alreadyRead = remember { mutableSetOf<String>() }
    var isRecording by remember { mutableStateOf(false) }
    var recordingStarted by remember { mutableStateOf(false) }
    var recordingSeconds by remember { mutableIntStateOf(0) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var recordedAudioDuration by remember { mutableLongStateOf(0L) }  // ✅ Add this
    val socket by socketViewModel.socketState.collectAsState()
    /*
        val moderateContent by authViewModel.moderateContent.collectAsState()
    */
    val isTyping by chatViewModel.isOtherUserTyping.collectAsState()
    val isRecordingg by chatViewModel.isOtherUserRecording.collectAsState()
    val matchTime by m5ViewModel.matchingTiming.collectAsState()
    var matchData by remember { mutableStateOf<MatchTimingData?>(null) }


    val actionList = listOf(
        ActionItem(1, stringResource(R.string.block), R.drawable.block_ic_red),
        ActionItem(2, stringResource(R.string.block_report), R.drawable.block_ic),
        ActionItem(3, stringResource(R.string.clear_chat), R.drawable.clear_chat_ic),
        ActionItem(4, stringResource(R.string.unmatch), R.drawable.x_icon_cross)
    )

    val reasons = listOf(
        ReasonOption(1, stringResource(R.string.reason_wrong_age)),
        ReasonOption(2, stringResource(R.string.reason_married_pretending_single)),
        ReasonOption(3, stringResource(R.string.reason_harassment)),
        ReasonOption(3, stringResource(R.string.reason_harassment)),
        ReasonOption(4, stringResource(R.string.reason_religious_disrespect)),
        ReasonOption(5, stringResource(R.string.reason_sexual_content)),
        ReasonOption(6, stringResource(R.string.reason_scamming)),
        ReasonOption(7, stringResource(R.string.reason_spam)),
        ReasonOption(8, stringResource(R.string.reason_catfishing)),
        ReasonOption(9, stringResource(R.string.reason_extremist)),
        ReasonOption(10, stringResource(R.string.reason_bad_behavior)),
        ReasonOption(11, stringResource(R.string.reason_threats)),
        ReasonOption(12, stringResource(R.string.reason_financial_scam)),
        ReasonOption(13, stringResource(R.string.reason_other))
    )

    LaunchedEffect(socket) {

        if (socket == SocketState.CONNECTED && userId.isNotEmpty()) {
            chatViewModel.joinRoom(userId)
            chatViewModel.listenMessages(userId)
        }
    }

    BackHandler {
        roomId?.let {
            chatViewModel.leaveChat(it)
        }
        if (!navController.popBackStack()) {
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(0)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            showImageDialog = false

            chatViewModel.stopRecording(roomId ?: "")
            chatViewModel.stopTyping(roomId ?: "")
            roomId?.let {
                chatViewModel.leaveChat(it)
            }
        }
    }

    var isBackClicked by remember { mutableStateOf(false) }

    fun startRecording(
        context: Context, onFileReady: (File) -> Unit
    ) {
        val file = File(
            context.cacheDir, "audio_${System.currentTimeMillis()}.m4a"
        )

        try {
            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION") MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder
            onFileReady(file)

        } catch (e: Exception) {
            Log.e("AudioRecorder", "Failed to start recording", e)
            e.printStackTrace()
        }
    }

    fun safeStopAndUpload(
        context: Context, file: File, viewModel: AuthViewModel
    ) {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error stopping recorder", e)
            e.printStackTrace()
        } finally {
            mediaRecorder = null
        }

        // ✅ Upload the file
        val part = MultipartBody.Part.createFormData(
            "upload_file",
            file.name,
            file.asRequestBody("audio/m4a".toMediaTypeOrNull())
        )


        val multipartList = mutableListOf<MultipartBody.Part>()
        multipartList.add(part)

        viewModel.uploadImageFile(
            SharedPreference.get(context).accessToken,
            multipartList
        )

    }

    LaunchedEffect(Unit) {
        showImageDialog = false

        m5ViewModel.hitMatchingTiming(
            access_token = SharedPreference.get(context).accessToken,
            userId = userId ?: ""
        )
    }


    LaunchedEffect(checkAbusive) {

        checkAbusive.let{
            when (it){

                is EmpResource.Loading->{
                   //CustomLoader.showLoader(context as MainActivity)

                }

                is EmpResource.Success->{
                    //context.showToast(it.value.message?:"")

                    chatViewModel.sendMessage(
                        roomId = roomId ?: "", toUserId = userId,
                        text = m5ViewModel.pendingMessageText,
                        type = "text",
                        duration = "0")
                    chatViewModel.stopTyping(roomId ?: "")
                    m5ViewModel.pendingMessageText = ""
                    m5ViewModel.resetCheckAbusiveWord()
                }

                is EmpResource.Failure->{

                    CustomLoader.hideLoader()
                    it.throwable?.let {
                        ErrorUtil.handlerGeneralError(context, it)
                    }
                    m5ViewModel.resetCheckAbusiveWord()

                }

                else->{ }
            }

        }


    }


    HomePageObserver(
        viewModel = authViewModel,
        m4ViewModel = viewModelM4,
        context = context,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        socketViewModel = socketViewModel
    )

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

//    LaunchedEffect(checkAbusive) {
//
//        checkAbusive.let {  }
//    }

    LaunchedEffect(blockAndReportDialog) {
        if (blockAndReportDialog) {
            delay(2500)
            blockAndReportDialog = false
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }


// This ensures the upload state is reset after success to prevent reusing old data.

    LaunchedEffect(Unit) {
        snapshotFlow { uploadState }
            .collect { state ->
                state ?: return@collect

                if (state is EmpResource.Success) {
                    CustomLoader.hideLoader()

                    val uploadedFile = state.value.data?.firstOrNull() ?: return@collect
                    val fileUrl = uploadedFile.documentImageUrl ?: return@collect

                    val isVideo =
                        fileUrl.endsWith(".mp4", true) ||
                                fileUrl.endsWith(".mkv", true) ||
                                fileUrl.endsWith(".mov", true) ||
                                fileUrl.endsWith(".avi", true) ||
                                fileUrl.endsWith(".webm", true)

                    val isAudio =
                        fileUrl.endsWith(".m4a", true) ||
                                fileUrl.endsWith(".mp3", true) ||
                                fileUrl.endsWith(".aac", true) ||
                                fileUrl.endsWith(".wav", true) ||
                                fileUrl.endsWith(".ogg", true)

                    if (isAudio) {
                        chatViewModel.sendMessage(
                            roomId = roomId ?: "",
                            toUserId = userId,
                            text = fileUrl,
                            type = "audio",
                            duration = recordedAudioDuration.toString()
                        )
                        recordedAudioDuration = 0L
                        pendingUploadType = ""
                        authViewModel.resetUploadState()
                        return@collect
                    }

                    if (isVideo) {
                        chatViewModel.sendMessage(
                            roomId = roomId ?: "",
                            toUserId = userId,
                            text = Json.encodeToString(
                                ListSerializer(String.serializer()),
                                listOf(fileUrl)
                            ),
                            type = "media",
                            duration = "0"
                        )
                        pendingUploadType = ""
                        authViewModel.resetUploadState()
                        return@collect
                    }

                    // 🖼 IMAGE
                    chatViewModel.sendMessage(
                        roomId = roomId ?: "",
                        toUserId = userId,
                        text = Json.encodeToString(
                            ListSerializer(String.serializer()),
                            listOf(fileUrl)
                        ),
                        type = "media",
                        duration = "0"
                    )
                    pendingUploadType = ""
                    authViewModel.resetUploadState()
                }

                if (state is EmpResource.Loading) {
                    CustomLoader.showLoader(context as MainActivity)
                }

                if (state is EmpResource.Failure) {
                    CustomLoader.hideLoader()
                    state.throwable?.let {
                        ErrorUtil.handlerGeneralError(context, it)
                    }
                    authViewModel.resetUploadState()
                }
            }
    }

    val socketError by chatViewModel.socketError.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }


    LaunchedEffect(socketError) {
        socketError?.let { errorMsg ->
            dialogMessage = errorMsg
            planPopUp = true

            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()

            //  chatViewModel.clearSocketError()
        }
    }


    LaunchedEffect(matchTime) {
        matchTime.let { it ->
            when (it) {
                is EmpResource.Loading -> {
                    //   CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    // context.showToast( it.value.message?:"")

                    matchData = it.value.data
                    m5ViewModel.resetMatchingTiming()

                }

                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                    m5ViewModel.resetMatchingTiming()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }


    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startRecording(context) { file ->
                audioFile = file
                isRecording = true
                recordingStarted = true
            }
        } else {
            context.showToast(context.getString(R.string.microphone_permission_is_require_to_use_this_feature))
            recordingStarted = false
            isRecording = false
        }
    }

// ✅ Track recording duration in real-time
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingSeconds = 0
            while (isRecording) {
                delay(1000)
                recordingSeconds++
            }
        }
    }

    LaunchedEffect(userId) {
        mid = SharedPreference.get(context).userID

    }


    LaunchedEffect(roomId) {

        if (!roomId.isNullOrEmpty()) {

            chatViewModel.loadMessages(roomId.toString())

        }
    }



    LaunchedEffect(listState.firstVisibleItemIndex, messageList.size) {
        messageList.filter { msg ->
            msg.senderId == userId && msg.readAt == "null" && !alreadyRead.contains(msg._id)
        }.forEach { msg ->

            alreadyRead.add(msg._id.toString())

            chatViewModel.readMessage(
                roomId.toString(), msg._id.toString()
            )
        }
    }


    var previousSize by remember { mutableIntStateOf(0) }

    /*   LaunchedEffect(messageList.size) {
           if (messageList.size > previousSize) {
               // ✅ Only scroll when new message added, not on delete
               listState.animateScrollToItem(messageList.size - 1)
           }
           previousSize = messageList.size
       }*/


    /*
        LaunchedEffect(moderateContent) {
            moderateContent.let {

                when (it) {
                    is EmpResource.Loading -> {

                        // CustomLoader.showLoader(context as MainActivity)
                    }

                    is EmpResource.Success -> {
                        CustomLoader.hideLoader()

                        moderateData = it.value.data

                        m5ViewModel.resetModerateContent()
                    }

                    is EmpResource.Failure -> {
                        CustomLoader.hideLoader()
                        it.throwable?.let { err ->
                            ErrorUtil.handlerGeneralError(context, err)

                        }
                        m5ViewModel.resetProfileViewList()
                    }

                    EmpResource.Idle -> {
                        CustomLoader.hideLoader()
                    }
                }
            }
        }
    */



    if (showIncomingScreen) {
        CallPickUpScreen(
            navController = navController, model = callViewModel
        )
    } else {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {

            if (showDialog) {

                Dialog(onDismissRequest = {
                    showDialog = false
                    navController.popBackStack()
                    chatViewModel.clearSocketError()
                }) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 40.dp, horizontal = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(R.drawable.temp_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(25.dp))
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        Text(
                            text = dialogMessage,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                        verticalSpace(20)
                        AppButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            text = stringResource(R.string.upgrade_plan), onClick = {
                                showDialog = false
                                navController.navigate(Screen.PlanScreen.route) {
                                    popUpTo(Screen.ChatScreenOneToOne.route) {
                                        inclusive = false
                                    }
                                }
                            })

                    }


                }
            }

            val maxHeight = this.maxHeight
            val maxWidth = this.maxWidth

            if (showBottomSheet == true) {

                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                        selectedId = null
                    }, dragHandle = { null }, sheetState = sheetState
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF14590988))
                                .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(R.string.options),
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
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        showBottomSheet = false
                                        selectedId = null
                                    })

                        }

                        verticalSpace(10)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            actionList.forEach { item ->
                                ActionRowItem(
                                    item = item, isSelected = selectedId == item.id
                                ) {
                                    selectedId = item.id
                                    when (item.id) {
                                        1 -> {
                                            bottomSheetIndex = 0
                                        }

                                        2 -> {
                                            bottomSheetIndex = 1
                                        }

                                        3 -> {
                                            bottomSheetIndex = 2
                                        }

                                        4 -> {
                                            bottomSheetIndex = 3
                                        }
                                    }
                                }
                            }

                            verticalSpace(10)

                            AppButton(
                                modifier = Modifier
                                //.padding(horizontal=16.dp),
                                , text = stringResource(R.string.submit), onClick = {

                                    when (bottomSheetIndex) {
                                        0 -> {
                                            selectedId = null
                                            blockDialog = true
                                            chatViewModel.clearChat(roomId ?: "")
                                            viewModelM4.hitAction(
                                                access_token = SharedPreference.get(context).accessToken,
                                                request = ActionRequest(
                                                    action = "block", toUserId = userId
                                                )
                                            )
                                        }

                                        1 -> {
                                            selectedId = null
                                            chatViewModel.clearChat(roomId ?: "")
                                            showReportBottomSheet = true

                                        }

                                        2 -> {
                                            selectedId = null
                                            chatViewModel.clearChat(roomId ?: "")
                                        }

                                        3 -> {
                                            selectedId = null
                                            unMatchedDialog = true
                                            viewModelM4.hitAction(
                                                access_token = SharedPreference.get(context).accessToken,
                                                request = ActionRequest(
                                                    action = "unmatch", toUserId = userId
                                                )
                                            )
                                        }

                                        else -> {}

                                    }

                                    showBottomSheet = false

                                })

                        }

                    }
                }

                verticalSpace(100)
            }




            if (blockAndReportDialog) {
                CustomDialog(
                    id = R.drawable.red_alert_sign_ic,
                    text1 = stringResource(R.string.block_report_profile),
                    text2 = stringResource(R.string.report_submitted_sucessfully),
                    onDismiss = { })
            }

            if (unMatchedDialog) {
                CustomDialog(
                    id = R.drawable.green_tick,
                    text1 = stringResource(R.string.profile_unmatched),
                    text2 = "",
                    onDismiss = {
                        //unMatchedDialog = false
                    })
            }

            if (blockDialog) {
                CustomDialog(
                    id = R.drawable.red_alert_sign_ic,
                    text1 = stringResource(R.string.blocked),
                    text2 = stringResource(R.string.you_won_t_see_each_other_anymore),
                    onDismiss = { })
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(),
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .size(35.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .clickable(enabled = !isBackClicked) {
                                    isBackClicked = true
                                    if (!navController.popBackStack()) {
                                        navController.navigate(Screen.MainScreen.route) {
                                            popUpTo(0)
                                        }

                                    }
                                },

                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = "back_ic"
                        )

                        Text(
                            modifier = Modifier.align(alignment = Alignment.Center),
                            text = stringResource(R.string.chat),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )

                        Row(
                            modifier = Modifier.align(Alignment.TopEnd),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Replace your two call Image buttons with these:

// 🔊 Audio Call Button
                            // 🔊 Audio Call Button
                            Image(
                                modifier = Modifier
                                    .size(27.dp)
                                    .clip(shape = RoundedCornerShape(50.dp))
                                    .clickable {
                                        pendingCallType = "audio"
                                        permissionLauncher.launch(
                                            arrayOf(Manifest.permission.RECORD_AUDIO)
                                        )
                                    },
                                painter = painterResource(R.drawable.call_ic),
                                contentDescription = "audio call"
                            )

// 📹 Video Call Button
                            Image(
                                modifier = Modifier
                                    .size(27.dp)
                                    .clip(shape = RoundedCornerShape(50.dp))
                                    .clickable {
                                        pendingCallType = "video"
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.RECORD_AUDIO
                                            )
                                        )
                                    },
                                painter = painterResource(R.drawable.video_call_ic),
                                contentDescription = "video call"
                            )

                            Image(
                                modifier = Modifier
                                    .size(27.dp)
                                    .clip(shape = RoundedCornerShape(50.dp))
                                    .clickable { showBottomSheet = true },
                                painter = painterResource(R.drawable.message_ic),
                                contentDescription = "back_ic"
                            )

                        }


                    }

                }
                verticalSpace(15)


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = "profile pic",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    verticalSpace(10)

                    horizontalSpace(5)

                    Column() {
                        Row() {

                            Text(
                                modifier = Modifier,
                                text = "${name},${age}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                            )
                            horizontalSpace(1)
                            if (isFace == true) {
                                Image(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource(R.drawable.blue1),
                                    contentDescription = ""
                                )
                            }
                            horizontalSpace(1)
                            if (isDocument == true) {
                                Image(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource(R.drawable.blue2),
                                    contentDescription = ""
                                )
                            }


                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if (isActive == true) {
                                Image(
                                    painter = painterResource(R.drawable.active_greem_dot),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(if (isOnline == true) Color.Green else Color.Red),
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                )
                            }

                            horizontalSpace(5)

                            if (!matchDate.isNullOrEmpty() && matchData?.isMatch == true) {
                                Text(
                                    text = stringResource(
                                        id = R.string.matched_with,
                                        name, formatMatchDate(matchDate)
                                    ),
                                    color = Color(0xFF6D6D6D),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                )

                            }

                        }
                        if (isRecordingg) {
                            verticalSpace(2)

                            Text(
                                text = stringResource(R.string.audio_recording),
                                color = Color(0xFF6D6D6D),
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )
                        } else if (isTyping) {

                            verticalSpace(2)
                            Text(
                                text = stringResource(R.string.typing),
                                color = Color(0xFF6D6D6D),
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )
                            verticalSpace(4)

                        }

                    }

                }

                when {
                    isMessagesLoading -> {
                        // ✅ Loading — show nothing (or skeleton if you want)
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f))
                    }

                    messageList.isEmpty() -> {
                        // ✅ Confirmed empty AFTER load finished
                        Image(
                            painterResource(R.drawable.no_chat_img),
                            contentDescription = "null",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp)
                                .height(300.dp)
                        )
                        verticalSpace(10)
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.no_chat_yet),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(bottom = 50.dp),
                            reverseLayout = true
                        ) {


                            items(messageList.reversed()) { message ->
                                Log.e("msgList", "${messageList}: ")
                                verticalSpace(12)

                                val isSender = message.senderId == userId
                                val mediaUrls = parseMediaUrls(message.message)
                                val firstUrl = mediaUrls.firstOrNull() ?: message.message
                                when (getFileType(firstUrl)) {
                                    "text" -> {
                                        if (isSender) {
                                            SenderMessage(
                                                img = image,
                                                msg = message.message,
                                                time = formatTimeFromUtc(message.createdAt),
                                                maxWidth = maxWidth
                                            )
                                        } else {
                                            ReceiverMessage(
                                                msg = message.message,
                                                time = formatTimeFromUtc(message.createdAt),
                                                maxWidth = maxWidth,
                                                isRead = message.readAt,
                                                onDeleteClick = {
                                                    chatViewModel.deleteMessage(
                                                        roomId ?: "",
                                                        message._id ?: ""
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    "image" -> {

                                        val mediaUrls = parseMediaUrls(message.message)
                                        // ✅ Handle optimistic image message
                                        if (mediaUrls.size == 1) {
                                            val url = mediaUrls.first()
                                            if (isSender) {
                                                SenderImage(
                                                    img = url,
                                                    time = formatTimeFromUtc(message.createdAt)
                                                ) { previewImageUrl = url }
                                            } else {
                                                ReceiverImage(
                                                    img = url,
                                                    time = formatTimeFromUtc(message.createdAt),
                                                    onClick = { previewImageUrl = url },
                                                    isRead = message.readAt,
                                                    onDeleteClick = {
                                                        chatViewModel.deleteMessage(
                                                            roomId ?: "",
                                                            message._id ?: ""
                                                        )
                                                    }

                                                )
                                            }
                                        } else {
                                            if (isSender) {
                                                SenderMultipleMedia(
                                                    mediaUrls = mediaUrls,
                                                    time = formatTimeFromUtc(message.createdAt)
                                                ) {}
                                            } else {


                                                ReceiverMultipleMedia(
                                                    mediaUrls = mediaUrls,
                                                    time = formatTimeFromUtc(message.createdAt),
                                                    onClick = { index -> /* handle media click */ },
                                                    onDeleteClick = {
                                                        chatViewModel.deleteMessage(
                                                            roomId ?: "",
                                                            message._id ?: ""
                                                        )
                                                    }
                                                )

                                            }
                                        }

                                    }


                                    "audio" -> {

                                        if (isSender) {
                                            AudioMessagePlayerSender(
                                                audioUrl = message.message,
                                                audioVM = chatAudioViewModel,
                                                modifier = Modifier,
                                                duration = if (message.duration != "null") message.duration?.toLong()
                                                    ?: 0L else 0L,
                                                img = image
                                            )
                                        } else {
                                            AudioMessagePlayerReceiver(
                                                audioUrl = message.message,
                                                audioVM = chatAudioViewModel,
                                                modifier = Modifier,
                                                time = formatTimeFromUtc(message.createdAt),
                                                isRead = message?.readAt,
                                                duration = if (message.duration != "null") message.duration?.toLong()
                                                    ?: 0L else 0L,
                                                onDeleteClick = {
                                                    chatViewModel.deleteMessage(
                                                        roomId ?: "",
                                                        message._id ?: ""
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    "video" -> {
                                        val mediaUrls = parseMediaUrls(message.message)

                                        if (mediaUrls.isEmpty()) return@items
                                        Log.e("mediaaa", "${mediaUrls}: ")

                                        val isVideo = mediaUrls.any { isVideoUrlSafe(it) }

                                        when {
                                            mediaUrls.size == 1 -> {
                                                val url = mediaUrls.first()

                                                if (isVideo) {
                                                    if (isSender) {
                                                        SenderVideo(
                                                            videoUrl = url,
                                                            time = formatTimeFromUtc(message.createdAt),
                                                            context = context
                                                        ) { previewVideoUrl = url }
                                                    } else {
                                                        ReceiverVideo(
                                                            videoUrl = url,
                                                            time = formatTimeFromUtc(message.createdAt),
                                                            context = context,
                                                            onClick = { previewVideoUrl = url },
                                                            isRead = message.readAt,
                                                            onDeleteClick = {
                                                                chatViewModel.deleteMessage(
                                                                    roomId ?: "", message._id ?: ""
                                                                )


                                                            }
                                                        )
                                                    }
                                                } else {
                                                    if (isSender) {
                                                        SenderImage(
                                                            img = url,
                                                            time = formatTimeFromUtc(message.createdAt)
                                                        ) { previewImageUrl = url }
                                                    } else {
                                                        ReceiverImage(
                                                            img = url,
                                                            time = formatTimeFromUtc(message.createdAt),
                                                            onClick = { previewImageUrl = url },
                                                            isRead = message.readAt,
                                                            onDeleteClick = {
                                                                chatViewModel.deleteMessage(
                                                                    roomId ?: "", message._id ?: ""
                                                                )
                                                            }
                                                        )
                                                    }
                                                }
                                            }

                                            /*   mediaUrls.size > 1 -> {
                                                if (isSender) {
                                                    SenderMultipleMedia(
                                                        mediaUrls = mediaUrls,
                                                        time = formatTimeFromUtc(message.createdAt),
                                                        onClick = {
                                                            previewMultipleMediaUrls = mediaUrls
                                                            previewMultipleStartIndex = it
                                                        }
                                                    )
                                                } else {
                                                    ReceiverMultipleMedia(
                                                        mediaUrls = mediaUrls,
                                                        time = formatTimeFromUtc(message.createdAt),
                                                        onClick = {
                                                            previewMultipleMediaUrls = mediaUrls
                                                            previewMultipleStartIndex = it
                                                        },
                                                        onDeleteClick = {
                                                            chatViewModel.deleteMessage(
                                                                roomId ?: "",
                                                                message._id ?: ""
                                                            )
                                                        }
                                                    )
                                                }
                                            }*/
                                        }
                                    }
                                }

                                verticalSpace(6)
                            }

                            item { verticalSpace(10) }

                        }
                    }
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 16.dp, end = 16.dp, bottom = 5.dp)
                    .align(Alignment.BottomCenter), verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isRecording) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(12.dp)
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = chatText,
                            onValueChange = {
                                chatText = it
                                if (it.isNotEmpty()) chatViewModel.startTyping(roomId ?: "")
                                else chatViewModel.stopTyping(roomId ?: "")
                            },
                            placeholder = {
                                Text(
                                    stringResource(R.string.write_a_message), style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                        fontSize = 14.sp,
                                    ), color = Color(0xFF6D6D6D)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Default
                            ),
                            singleLine = false,
                            maxLines = 3,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.outline
                            ),
                            textStyle = TextStyle(
                                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)

                        )
                    }
                    horizontalSpace(7)
                }

                if (isRecording) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // ❌ Cross Icon → Cancel Recording (No Upload)
                        Image(
                            painter = painterResource(R.drawable.cross_pruple_ic),
                            contentDescription = "cancel audio",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {

                                    // Stop recording only (Cancel)
                                    isRecording = false
                                    recordingStarted = false
                                    recordingSeconds = 0
                                    recordedAudioDuration = 0L
                                    chatViewModel.stopRecording(roomId ?: "")

                                    try {
                                        mediaRecorder?.apply {
                                            stop()
                                            release()
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    } finally {
                                        mediaRecorder = null
                                    }

                                    // Delete file so nothing is sent
                                    audioFile?.delete()
                                    audioFile = null
                                })
                        Spacer(modifier = Modifier.width(8.dp)) // ✅ Space after icon


                        // 🌊 Audio Wave
                        AudioWave(
                            modifier = Modifier
                                .weight(1f)
                                .height(35.dp),
                            recorder = mediaRecorder!!,
                            isRecording = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))


                        // ⏱ Timer
                        Text(
                            text = " ${formatSeconds(recordingSeconds)}",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                if (chatText.isEmpty()) {

                    if (isRecording == false) {
                        Image(
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    pendingUploadType = "media"
                                    showImageDialog = true
                                },
                            painter = painterResource(R.drawable.camera_ic),
                            contentDescription = "back_ic",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }

                    horizontalSpace(7)

                    Image(
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {

                                if (!isRecording) {

                                    // 🎤 Mic Click → Start Recording Only
                                    pendingUploadType = "audio"
                                    type = "audio"
                                    recordingStarted = false
                                    micPermissionLauncher.launch(
                                        Manifest.permission.RECORD_AUDIO
                                    )
                                    chatViewModel.startRecording(roomId ?: "")

                                } else {

                                    // 📩 Send Click → Stop + Upload + Send Audio
                                    isRecording = false
                                    // Duration in milliseconds
                                    recordedAudioDuration = recordingSeconds * 1000L
                                    chatViewModel.stopRecording(roomId ?: "")
                                    audioFile?.let { file ->
                                        safeStopAndUpload(
                                            context = context,
                                            file = file,
                                            viewModel = authViewModel
                                        )
                                    }
                                }
                            },

                        painter = painterResource(
                            id = if (isRecording) R.drawable.send_icon else R.drawable.microphone_ic
                        ),
                        contentDescription = "audio record/send",
                        colorFilter = ColorFilter.tint(
                            color =
                                MaterialTheme.colorScheme.onBackground


                        )

                    )


                } else {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                val textToSend = chatText.trim()
                                if (textToSend.isEmpty()) return@clickable
                                chatText = ""
                                m5ViewModel.hitCheckAbusiveWord(
                                      SharedPreference.get(context).accessToken, word = textToSend)

                            },

                        painter = painterResource(R.drawable.send_icon),
                        contentDescription = "back_ic",
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colorScheme.onBackground

                        )
                    )

                }

            }


        }

        if (planPopUp) {

            PlanPopUp(onDismiss = {
                planPopUp = false
                callViewModel.clearError()
                navController.popBackStack() }
                , navController)


        }



        if (showReportBottomSheet) {
            ReportBottomSheet(
                reasons = reasons,
                userId = userId,
                viewModelM4 = viewModelM4,
                context = context,
                onDismiss = { showReportBottomSheet = false },
                onSuccess = {
                    blockAndReportDialog = true
                    showReportBottomSheet = false
                }
            )
        }

    }

    previewMultipleMediaUrls?.let { urls ->

        MultipleMediaPreview(
            mediaUrls = urls,
            startIndex = previewMultipleStartIndex,
            onDismiss = { previewMultipleMediaUrls = null })
    }


    previewImageUrl?.let { url ->
        FullScreenImagePreview(
            imageUrl = url, onDismiss = { previewImageUrl = null })
    }

    previewVideoUrl?.let { url ->
        FullScreenVideoPlayer(
            videoUrl = url, onDismiss = { previewVideoUrl = null })
    }

    //var showImageDialog by remember { mutableStateOf(false) }
    var isDismissed by remember { mutableStateOf(false) } // ← add this
    if (showImageDialog) {
        ImagePicker(
            showImageDialog,
            onMediaPicked = { data ->
                if (!data.file.exists() || data.file.length() == 0L) {
                    Toast.makeText(context, "File not found or empty", Toast.LENGTH_SHORT).show()
                    showImageDialog = false
                    isDismissed = false
                    return@ImagePicker
                }

                val mimeType = getMimeType(data.file.name)

                try {
                    val part = MultipartBody.Part.createFormData(
                        "upload_file",
                        data.file.name,
                        data.file.asRequestBody(mimeType.toMediaTypeOrNull())
                    )
                    authViewModel.uploadImageFile(
                        token = SharedPreference.get(context).accessToken,
                        mutableListOf(part)
                    )
                } catch (e: Exception) {
                    Log.e("ChatScreen", "Upload error: ${e.message}")
                    Toast.makeText(context, "Failed to prepare file", Toast.LENGTH_SHORT).show()
                }

                showImageDialog = false  // ✅ remove dialog AFTER result delivered
                isDismissed = false
            },
            onDismiss = {
                showImageDialog = false
                if (!isDismissed) {
                    isDismissed = true
                    showImageDialog = false
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    reasons: List<ReasonOption>,
    userId: String?,
    viewModelM4: M4ViewModel,
    context: Context,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {

    val sheetStateReport = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val sheetScrollConnection = rememberNestedScrollInteropConnection()

    // ✅ Same States
    var selectedId by remember { mutableStateOf(1) }
    var selectedText by remember { mutableStateOf("Wrong Age") }
    var otherText by remember { mutableStateOf("") }

    ModalBottomSheet(
        sheetState = sheetStateReport,
        sheetGesturesEnabled = false,
        dragHandle = null,
        onDismissRequest = onDismiss,

        // ❌ Removed align (causes top issue)
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 2000.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .imePadding()
                .nestedScroll(sheetScrollConnection)
        ) {

            // ✅ Header Row (Same UI)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF14590988))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.report),
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
                            interactionSource = remember { MutableInteractionSource() }) {
                            onDismiss()
                        })
            }

            // ✅ Layout Fix: Scroll + Submit Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 550.dp) // ✅ Prevent button hiding
            ) {

                // ✅ Reasons Scroll List (Same UI)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {

                    items(reasons) { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    selectedId = item.id
                                    selectedText = item.title
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = selectedId == item.id, onClick = {
                                    selectedId = item.id
                                    selectedText = item.title
                                }, colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF8378E2),
                                    unselectedColor = Color(0xFF4D000000)
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = item.title,
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = Color.Black
                            )
                        }

                        verticalSpace(5)
                    }

                    // ✅ Other TextField (Same UI)
                    if (selectedId == 13) {
                        item {

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
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    disabledContainerColor = Color(0xFFF2F2F2),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                textStyle = TextStyle(
                                    fontSize = 14.sp, color = Color.Black
                                )
                            )

                            verticalSpace(15)
                        }
                    }
                }

                // ✅ Submit Button (Same UI + Fixed Always Visible)
                AppButton(
                    text = stringResource(R.string.submit),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding(),
                    onClick = {

                        val reportReasonToSend = if (selectedId == 13) "other" else selectedText

                        val customReasonToSend = if (selectedId == 13) otherText else null

                        // ✅ API Logic SAME
                        viewModelM4.hitAction(
                            access_token = SharedPreference.get(context).accessToken,
                            request = ActionRequest(
                                action = "block",
                                toUserId = userId ?: "",
                                reportReason = reportReasonToSend,
                                customReason = customReasonToSend
                            )
                        )

                        onSuccess()
                    })

                verticalSpace(10)
            }
        }
    }
}

