package com.pairlix.dating

import android.app.Activity
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pairlix.dating.LanguageManager.AppLanguageManager
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.LanguageManager.LanguageManager
import com.pairlix.dating.ReusedComponents.GlobalErrorDialog
import com.pairlix.dating.ThemeManager.LocalThemeManager
import com.pairlix.dating.ThemeManager.ThemeManager
import com.pairlix.dating.agora.CallViewModel
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.AppNavigation
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.ui.theme.PairlixTheme
import com.pairlix.dating.utils.SocketState
import com.pairlix.dating.view.splash.SplashScreen
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.ChatViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import com.pairlix.dating.viewModel.M6ViewModel
import com.pairlix.dating.viewModel.M7ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private val viewModelM4: M4ViewModel by viewModels()
    private val viewModelM5: M5ViewModel by viewModels()
    private val viewModelM6: M6ViewModel by viewModels()
    private val viewModelM7: M7ViewModel by viewModels()
    private val socketViewModel: SocketViewModel by viewModels()
    private val callViewModel: CallViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var cameraExecutor : ExecutorService

    val gpsResolutionLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
            // GPS TURNED ON
            } else {
                // USER CANCELLED
            }
        }
    override fun attachBaseContext(newBase: Context) {
        // CRITICAL: Apply saved language BEFORE activity is created
        val languageCode = AppLanguageManager.getLanguage(newBase)
        val context = AppLanguageManager.applyLanguage(newBase, languageCode)
        super.attachBaseContext(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PairlixDating)
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(socketViewModel))
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        cameraExecutor = Executors.newSingleThreadExecutor()
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {

            val themeManager = remember { ThemeManager(SharedPreference.get(this).themeMode) }
            val languageManager = remember { LanguageManager(AppLanguageManager.currentLanguage) }
            // Provide managers to all composables
            CompositionLocalProvider(
                LocalThemeManager provides themeManager,
                LocalLanguageManager provides languageManager
            ) {
                // Set layout direction based on language
                val layoutDirection = if (AppLanguageManager.isRTL()) {
                    LayoutDirection.Rtl
                } else {
                    LayoutDirection.Ltr
                }

                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    PairlixTheme(
                        themeMode = themeManager.themeMode
                    ) {
                        val socketState by socketViewModel.socketState.collectAsStateWithLifecycle()

                        LaunchedEffect(socketState) {
                            if (socketState == SocketState.CONNECTED) {
                                Log.e("MAIN", "  Socket Connected → Start Call Listening")
                                callViewModel.listenCallEvents()
                            }
                        }

                        val navController = rememberNavController()
                        if (viewModel.showSplash && !intent.hasExtra("noti")) {
                            SplashScreen { viewModel.showSplash = false }
                        } else {

                            var data=intent?.extras
                            AppNavigation(
                                cameraExecutor,
                                navController,
                                viewModel,
                                viewModelM4,
                                viewModelM5,
                                viewModelM6,
                                viewModelM7,
                                socketViewModel,
                                chatViewModel,
                                callViewModel = callViewModel,intent.hasExtra("noti"),data
                            )

                            GlobalErrorDialog()
                        }
                    }
                }
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val languageCode = AppLanguageManager.getLanguage(this)
            AppLanguageManager.applyLanguage(this, languageCode)
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
       //     socketViewModel.sendOffline()
        }
    }

    override fun onStop() {
        super.onStop()
        // socketViewModel.sendOffline()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    /*fun initView(navController: NavController){
        if (intent.hasExtra("noti")){
            intent?.extras?.let { bundle ->

                val type = bundle.getString("type")
                val roomId = bundle.getString("roomId")
                val senderName = bundle.getString("senderName")
                val senderImage = bundle.getString("senderImage")
                val age = bundle.getString("senderImage")
                val isOnline = bundle.getString("senderImage")
                val isActive = bundle.getString("senderImage")
                val isFace = bundle.getString("senderImage")
                

                Log.d("NOTIFICATION", "Type: $type")
                Log.d("NOTIFICATION", "RoomId: $roomId")
                navController.navigate(
                    Screen.ChatScreenOneToOne.passId(

                        name = "",
                        age = "12",
                        image =   "dd",
                        isOnline = true,
                        isActive = true,
                        matchDate = "fd",
                        isDocument = true,
                        isFace = true,
                        id = ""
                    )
                )

                when (type) {

                    "CHAT_MSG" -> {

                     //   navController.navigate("chat_screen/$roomId")
                    }

                    else -> {
                     //   navController.navigate("home")
                    }
                }
            }
        }
    }*/
}