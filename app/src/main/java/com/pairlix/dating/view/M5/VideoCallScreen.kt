package com.pairlix.dating.view.M5

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.media.AudioManager
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.PlanPopUp
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.agora.AgoraEventHandler
import com.pairlix.dating.agora.AgoraManager
import com.pairlix.dating.agora.CallViewModel
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.utils.CallManager
import com.pairlix.dating.utils.CallService
import com.pairlix.dating.viewModel.M5ViewModel
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration
import kotlinx.coroutines.delay

fun formatCallTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}


@Composable
fun VideoCallScreen(
    roomId: String,
    userId: String,
    image: String,
    navController: NavController, model: CallViewModel
) {

    val context = LocalContext.current
    val agoraData by model.agoraData.collectAsState()
    val remoteUsers by AgoraManager.remoteUsers.collectAsState()
    val type by model.type.collectAsStateWithLifecycle()
    val callType by model.callType.collectAsStateWithLifecycle()
    val callerName by model.callerName.collectAsStateWithLifecycle()
    val callStarted by model.callStarted.collectAsState()
    val token by model.token.collectAsStateWithLifecycle()
    val callDuration by model.callDuration.collectAsState()
    val callEnded by model.callEnded.collectAsState()
    var profileImageUrl by remember { mutableStateOf("") }
    val remoteUid = remoteUsers.firstOrNull()
    val errorMessage by model.socketError.collectAsState()
    var hasJoined by remember { mutableStateOf(false) }
    var isCallStarted by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var planPopUp by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }


    // Initialize engine once
    LaunchedEffect(errorMessage) {
        if (errorMessage?.isNotEmpty() == true){
            planPopUp = true
            dialogMessage=errorMessage?:""
        }




        model.listenCallEvents()
        profileImageUrl = SharedPreference.get(context).profileImage


        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.isSpeakerphoneOn = false

    }

    LaunchedEffect(callStarted) {
        if (callStarted){
            model.playRingtone(context)
        }
        else{
            model.stopRingtone()
        }
    }

    if (planPopUp) {

        PlanPopUp(onDismiss ={
            planPopUp = false
            model.clearError()
            if (navController.currentDestination?.route != Screen.PlanUpgradeScreen.route) {
                navController.popBackStack()
            }
                             }, navController)

    }

    if (showDialog) {

        Dialog (onDismissRequest = {showDialog=false
            model.clearError()
           navController.popBackStack()

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
                AppButton(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                    text = stringResource(R.string.upgrade_plan), onClick = {
                        showDialog=false
                        navController.navigate(Screen.PlanScreen.route){
                            popUpTo(Screen.ChatScreenOneToOne.route){
                                inclusive=false
                            }
                        } })

            }


        }
    }


    LaunchedEffect(remoteUid) {
        if (remoteUid != null) {
            model.startTimer()


        }


    }
    LaunchedEffect(callEnded) {
        if (callEnded == true) {
            val intent = Intent(context, CallService::class.java).apply {
                action = CallService.ACTION_END_CALL
            }
            model.resetState()

            context.startService(intent)
            model.endCall()

            navController.popBackStack()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            model.stopRingtone()
            model.resetCallEnd()
            model.callEnd()
            model.clearError()
        }
    }





    LaunchedEffect(type, callType) {
        if (type.isNotEmpty() && callType.isNotEmpty()) {
            if (type == "sender") {
                model.startCall(roomId, userId, callType)
            } else if (type == "receiver") {

                model.acceptCall(roomId)

            }
        }
    }



    // Join channel when data is ready
    LaunchedEffect(agoraData) {
        val data = agoraData ?: return@LaunchedEffect
        if (data.token.isBlank() || hasJoined) return@LaunchedEffect


        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.isSpeakerphoneOn = false

        AgoraManager.rtcEngine?.setEnableSpeakerphone(false)
        AgoraManager.speakerEnabled = false

        // ✅ Guard: RECORD_AUDIO must be granted before starting microphone FGS
        val audioGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        val cameraGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!audioGranted) {
            Log.e("AGORA_JOIN", "❌ RECORD_AUDIO not granted, cannot start call service")
            // Show permission rationale to user here
            return@LaunchedEffect
        }

        if (callType == "video" && !cameraGranted) {
            Log.e("AGORA_JOIN", "⚠️ CAMERA not granted, falling back to audio call")
            // Either return or downgrade to audio
        }

        val intent = Intent(context, CallService::class.java).apply {
            action = CallService.ACTION_START_CALL
            putExtra(CallService.EXTRA_TOKEN, data.token)
            putExtra(CallService.EXTRA_CHANNEL, data.channelName)
            putExtra(CallService.EXTRA_UID, data.uid)
            putExtra(CallService.Call_Type, callType)
        }

        ContextCompat.startForegroundService(context, intent)
        hasJoined = true
    }
    if (agoraData == null) {

        // 🔥 LOADING SCREEN (instead of white)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {

        }

    }
    else {
        if (agoraData?.token?.isNotEmpty() == true) {

            if (callType == "audio") {
                AgoraManager.rtcEngine?.setEnableSpeakerphone(false)
            }

            if (remoteUid != null) {


                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    val maxWidth = this.maxWidth

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0XFFF6A6D6),
                                        Color(0XFF8B5DF6)
                                    )
                                )
                            )
                            .zIndex(6f)
                    ) {
                        // ✅ VIDEO CALL UI
                        if (callType == "video") {
                            val remoteView = remember { SurfaceView(context) }

                            LaunchedEffect(remoteUid) {
                                AgoraManager.rtcEngine?.setupRemoteVideo(
                                    VideoCanvas(
                                        remoteView,
                                        VideoCanvas.RENDER_MODE_HIDDEN,
                                        remoteUid
                                    )
                                )
                            }

                            AndroidView(
                                factory = { remoteView },
                                modifier = Modifier.fillMaxSize()
                            )
                            // ✅ Waiting for user in VIDEO call
                        }

// ✅ AUDIO CALL UI
                        else {

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = profileImageUrl,
                                    contentDescription = "profile pic",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop

                                )

                                AsyncImage(
                                    model = image,
                                    contentDescription = "profile pic",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(200.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                            }


                            /*AsyncImage(
                        model = image,
                        contentDescription = "profile pic",
                        modifier = Modifier.align(Alignment.Center)
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )*/


                        }


                        // Show waiting message if no remote user
// Show waiting message if no remote user

                        if (callType == "video") {

                            AndroidView(
                                factory = {
                                    SurfaceView(context).apply {
                                        setZOrderMediaOverlay(true)

                                        AgoraManager.rtcEngine?.setupLocalVideo(
                                            VideoCanvas(this, VideoCanvas.RENDER_MODE_HIDDEN, 0)
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .size(120.dp, 160.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(top = 50.dp, end = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }


                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding()
                                    .padding(top = 40.dp, start = 16.dp),
                            ) {
                                /*Image(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .size(35.dp)
                                .clip(shape = RoundedCornerShape(50.dp))
                                .clickable { navController.popBackStack() },
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = "back_ic"
                        )*/

                                Column(Modifier.align(alignment = Alignment.Center)) {

                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = callerName,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                    )
                                    verticalSpace(5)

                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = formatCallTime(callDuration),
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                    )
                                }


                            }


                        }

                    }


                }

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                ) {
                    /* Image(
                     modifier = Modifier
                         .align(alignment = Alignment.TopStart)
                         .size(35.dp)
                         .clip(shape = RoundedCornerShape(50.dp))
                         .clickable { navController.popBackStack() },
                     painter = painterResource(R.drawable.back_icon),
                     contentDescription = "back_ic"
                 )*/

                    if (callType == "audio") {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "profile pic",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop

                        )

                        AsyncImage(
                            model = image,
                            contentDescription = "profile pic",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(200.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .padding(top = 60.dp)
                                .align(Alignment.TopCenter)
                        ) {


                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = callerName,
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            )
                            verticalSpace(5)

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.calling),
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            )
                        }


                    } else
                    //video call calling screen waiting for other user
                        Column(
                            modifier = Modifier
                                .padding(top = 60.dp)
                                .align(Alignment.TopCenter)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = callerName,
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            )
                            verticalSpace(5)

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.calling),
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            )

                        }
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color(0xFF4D373737))
                        .padding(horizontal = 16.dp, vertical = 30.dp)
                ) {
                    CallControls(

                        // 🔄 Switch Camera
                        onFlip = {
                            AgoraManager.switchCamera()
                            Log.e("CALL", "Camera Switched")
                        },

                        // 🎥 Video ON/OFF
                        onVideoToggle = { isOn ->

                            AgoraManager.rtcEngine?.muteLocalVideoStream(!isOn)

                            if (isOn) {
                                AgoraManager.rtcEngine?.startPreview()
                            } else {
                                AgoraManager.rtcEngine?.stopPreview()
                            }

                            Log.e("CALL", "Video Enabled: $isOn")
                        },


                        // 🎤 Mic Mute/Unmute
                        onMuteToggle = { isMuted ->
                            AgoraManager.muteMic(isMuted)

                            Log.e("CALL", "Mic Muted: $isMuted")
                        },


                        onEndCall = {
                            CallManager.stopRingtone()


                            // ✅ Emit endCall event so other user also ends

                            if (remoteUid != null) {
                                model.endCall(roomId)
                            } else {
                                model.cancelCall(roomId)
                                val intent = Intent(context, CallService::class.java).apply {
                                    action = CallService.ACTION_END_CALL
                                }
                                model.resetState()

                                context.startService(intent)
                                model.endCall()

                                navController.popBackStack()
                            }


                            Log.e("roomId", "${remoteUid}")

                            // navController.popBackStack()
                        },

                        onSpeaker = {
                            AgoraManager.toggleSpeaker(context)
                        },


                        model = model,
                        callType = callType


                    )


                }
            }
        }
    }


}


@Composable
fun CallControls(
    modifier: Modifier = Modifier,
    onFlip: () -> Unit,
    onVideoToggle: (Boolean) -> Unit,
    onMuteToggle: (Boolean) -> Unit,
    onEndCall: () -> Unit,
    onSpeaker: () -> Unit,
    model: CallViewModel,
    callType: String
) {
    val context = LocalContext.current

    var isVideoOn by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🎥 Only Video Call Shows Flip + Video Toggle
        if (callType == "video") {

            CallIconButton(
                icon = Icons.Default.Cached,
                backgroundColor = Color.Gray
            ) { onFlip() }

            CallIconButton(
                icon = if (isVideoOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                backgroundColor = Color.Gray
            ) {
                isVideoOn = !isVideoOn
                onVideoToggle(isVideoOn)
            }
        }

        // 🎤 Mic Toggle for Both
        CallIconButton(
            icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
            backgroundColor = Color.Gray
        ) {
            isMuted = !isMuted
            onMuteToggle(isMuted)
        }



        CallIconButton(
            icon = if (isSpeakerOn)
                Icons.Default.VolumeUp
            else
                Icons.Default.VolumeOff, // or VolumeOff icon if available
            backgroundColor = Color.Gray
        ) {
            isSpeakerOn = AgoraManager.toggleSpeaker(context)
        }

        // ❌ End Call
        CallIconButton(
            icon = Icons.Default.CallEnd,
            backgroundColor = Color.Red
        ) { onEndCall() }


    }
}

@Composable
fun CallIconButton(
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(backgroundColor, shape = CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}










