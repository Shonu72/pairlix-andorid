package com.pairlix.dating

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.core.Amplify
import com.gravito.waiter_.Localization.Const
import com.pairlix.dating.LanguageManager.AppLanguageManager
import com.pairlix.dating.helper.NetworkMonitor
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.getCountryIso
import com.pairlix.dating.utils.CallManager
import com.pairlix.dating.utils.SocketManager
import com.pairlix.dating.viewModel.SocketViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    private lateinit var networkMonitor: NetworkMonitor
    private val TAG = "Pairlix Dating"



    @Inject
    lateinit var socketManager: SocketManager

    private var disconnectJob: Job? = null
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    companion object {
        lateinit var appContext: Context
            private set

        // Delay before disconnecting socket when app goes to background
        private const val DISCONNECT_DELAY_MS = 30000L // 30 seconds
    }

    override fun onCreate() {
        super.onCreate()
        Const.countryCode=getCountryIso(this)
        appContext = this
        setupLifecycleObserver()
        initializeAmplify()
        setupNetworkListener()
        AppLanguageManager.initialize(this)
        val languageCode = AppLanguageManager.currentLanguage
        AppLanguageManager.applyLanguage(this, languageCode)
    }

    override fun attachBaseContext(base: Context) {
        // CRITICAL: Apply language before anything else
        val languageCode = AppLanguageManager.getLanguage(base)
        val context = AppLanguageManager.applyLanguage(base, languageCode)
        super.attachBaseContext(context)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Reapply language when configuration changes
        val languageCode = AppLanguageManager.getLanguage(this)
        AppLanguageManager.applyLanguage(this, languageCode)
    }
    private fun setupLifecycleObserver() {
        try {
            ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {

                override fun onStop(owner: LifecycleOwner) {
                    // App moved to background
                    Log.d(TAG, "App moved to background")
                    scheduleSocketDisconnect()
                }

                override fun onStart(owner: LifecycleOwner) {
                    // App came to foreground
                    Log.d(TAG, "App returned to foreground")
                    cancelScheduledDisconnect()

                    val userId = getUserId()

                    if (!userId.isNullOrEmpty()) {
                        try {
                            Log.d(TAG, "Reconnecting socket on foreground")

                            if (::socketManager.isInitialized) {
                                socketManager.init(userId)
                            } else {
                                Log.w(TAG, "SocketManager not initialized on foreground")
                            }

                        } catch (e: Exception) {
                            Log.e(TAG, "Error reconnecting socket", e)
                        }
                    }
                }
            })
            Log.d(TAG, "Lifecycle observer setup complete")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to setup lifecycle observer", e)
        }
    }

    private fun initializeAmplify() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Amplify.Auth.fetchAuthSession(
                { session ->
                    val cognitoSession = session as? AWSCognitoAuthSession
                    if (cognitoSession?.isSignedIn == true) {
                        Log.d(TAG, "User is signed in")
                    } else {
                        Log.d(TAG, "User is not signed in")
                    }
                },
                { error ->
                    Log.e(TAG, "Failed to fetch auth session", error)
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Amplify initialization failed", e)
        }
    }

    private fun scheduleSocketDisconnect() {
        // Cancel any existing scheduled disconnect
        cancelScheduledDisconnect()

        disconnectJob = appScope.launch {
            try {
                Log.d(TAG, "Scheduling socket disconnect in ${DISCONNECT_DELAY_MS / 1000} seconds")
                delay(DISCONNECT_DELAY_MS)

                // After delay, disconnect socket
                withContext(Dispatchers.IO) {
                    disconnectSocket()
                }
            } catch (e: CancellationException) {
                Log.d(TAG, "Socket disconnect cancelled - user returned to app")
            } catch (e: Exception) {
                Log.e(TAG, "Error during scheduled disconnect", e)
            }
        }
    }

    private fun cancelScheduledDisconnect() {
        disconnectJob?.let { job ->
            if (job.isActive) {
                job.cancel()
                Log.d(TAG, "Cancelled scheduled socket disconnect")
            }
        }
        disconnectJob = null
    }

    private fun disconnectSocket() {
        try {
            val userId = getUserId()

            if (userId.isNullOrEmpty()) {
                Log.w(TAG, "Cannot disconnect socket - userId is empty")
                return
            }

            if (::socketManager.isInitialized) {
                socketManager.disconnect(userId)
                Log.d(TAG, "Socket disconnected successfully for user: $userId")
            } else {
                Log.w(TAG, "SocketManager not initialized")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting socket", e)
        }
    }

    private fun getUserId(): String? {
        return try {
            SharedPreference.get(this).userID.takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting userId from SharedPreference", e)
            null
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        cleanup()
        CallManager.stopRingtone()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(TAG, "Low memory warning received")
    }

    private fun cleanup() {
        try {
            Log.d(TAG, "Cleaning up application resources")

            // Cancel any pending disconnect jobs
            cancelScheduledDisconnect()

            // Disconnect socket immediately
            disconnectSocket()

            // Cancel coroutine scope
            appScope.cancel()

            Log.d(TAG, "Cleanup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error during cleanup", e)
        }
    }

    private fun setupNetworkListener() {

        networkMonitor = NetworkMonitor(
            context = this,

            onConnected = {
                val userId = getUserId()
                if (!userId.isNullOrEmpty()) {
                    Log.d(TAG, "Internet back → reconnect socket")
                    socketManager.init(userId)
                }
            },

            onDisconnected = {
                val userId = getUserId()
                if (!userId.isNullOrEmpty()) {
                    Log.d(TAG, "Internet lost → disconnect socket")
                    socketManager.disconnect(userId)
                }
            }
        )

        networkMonitor.start()
    }
}