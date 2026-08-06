package com.pairlix.dating.view.M5

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.agora.CallViewModel
import com.pairlix.dating.firbase.MyFirebaseMessagingService
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.utils.CallManager
import com.pairlix.dating.view.M4.timeAgo
import com.pairlix.dating.viewModel.M5ViewModel


@Composable
fun CallPickUpScreen(navController: NavController,model: CallViewModel) {

    val callEnded by model.callEnded.collectAsState()
    val roomId by model.roomId.collectAsState()
    var callerName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    val context= LocalContext.current
    val callType by model.callType.collectAsStateWithLifecycle()
    val incomingData by model.incomingRequest.collectAsStateWithLifecycle()


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val micGranted = permissions[Manifest.permission.RECORD_AUDIO] == true
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true

        if (callType == "video") {
            if (micGranted && cameraGranted) {
                // ✅ Video call permission granted
                model.setCallerType("receiver")
                navController.popBackStack()
                navController.navigate(
                    Screen.VideoCallScreen.createRoute(
                        roomId = roomId,
                        callerName,
                        image
                    )
                )
            } else {
                context.showToast("Camera & Microphone permissions are required for video call")
            }
        } else { // audio call
            if (micGranted) {
                // ✅ Audio call permission granted
                model.setCallerType("receiver")
                navController.popBackStack()
                navController.navigate(
                    Screen.VideoCallScreen.createRoute(
                        roomId = roomId,
                        callerName,
                        image
                    )
                )
            } else {
                context.showToast("Microphone permission is required for audio call")
            }
        }
    }



    LaunchedEffect(incomingData) {
      incomingData.let {
          callerName=it.firstName+ " ${it.lastName},${it.age}"
          age=it.age?:""
          image=it.profileImages?:""
          CallManager.playRingtone(context)
      }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            model.setCallerType("receiver")
            navController.popBackStack()

            navController.navigate(
                Screen.VideoCallScreen.createRoute(
                    roomId = model.roomId.value,callerName,image
                )
            )
        } else {
            context.showToast("Microphone permission is require to use this feature")

        }
    }


    LaunchedEffect(callEnded) {
        if (callEnded==true){
            navController.popBackStack()


           /* val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.cancel(MyFirebaseMessagingService.CALL_NOTIFICATION_ID)*/
            CallManager.stopRingtone()
            model.callEnd()
            model.resetState()
            Log.e("pickScreen", "📤 ${callEnded}:")


        }
    }

    DisposableEffect(Unit) {
        onDispose {
            model.callEnd()
            model.resetCallEnd()
            CallManager.stopRingtone()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .navigationBarsPadding()
    ) {
        val maxHeight = this.maxHeight

        AsyncImage(
            model = image,
            contentDescription = "profile pic",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .statusBarsPadding()
        ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top=50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                        Text(
                            text = callerName, color = Color.White,
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                        )

                        Text(
                            text = callType +" Call",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                        )



                }


        }

        if (roomId.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column() {
                    Image(
                        painter = painterResource(R.drawable.call__decline),
                        contentDescription = "",
                        modifier = Modifier.size(75.dp).clickable {
                            CallManager.stopRingtone()
                            model.rejectCall(roomId = roomId)
                            navController.popBackStack()
                            model.resetCall()


                        }
                    )

                    /*  Text(
                    text = "Decline",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )*/

                }

                Column() {
                    Image(
                        painter = painterResource(R.drawable.call__accept),
                        contentDescription = "",
                        modifier = Modifier.size(75.dp).clickable {
                            CallManager.stopRingtone()
                          /*  val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            nm.cancel(MyFirebaseMessagingService.CALL_NOTIFICATION_ID)*/
                            val permissionsToRequest = if (callType == "video") {
                                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                            } else {
                                arrayOf(Manifest.permission.RECORD_AUDIO)
                            }

                            permissionLauncher.launch(permissionsToRequest)
                        }
                    )





                    /* Text(
                    text = "Accept",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )*/

                }


            }
        }


    }


}



/* Image(
     painter = painterResource(R.drawable.call__accept),
     contentDescription = "",
     modifier = Modifier.size(75.dp).clickable {
         micPermissionLauncher.launch( Manifest.permission.RECORD_AUDIO)
         model.setCallerType("receiver")
         navController.popBackStack()
         navController.navigate(
             Screen.VideoCallScreen.createRoute(
                 roomId = model.roomId.value,
                 callerName,image
             )
         )

     }
 )
*/
