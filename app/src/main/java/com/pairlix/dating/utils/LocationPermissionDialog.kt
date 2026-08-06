package com.pairlix.dating.utils

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.viewModel.AuthViewModel

object LocationManagers {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LocationPermissionAndGpsBottomSheet(
        model: AuthViewModel,
        activity: Activity,

        content: @Composable () -> Unit = {}
    ) {

        val context = LocalContext.current

        val isPermissionGranted by model.permissionGranted.collectAsState()
        val isGpsEnabled by model.isGpsEnabled.collectAsState()
        var showSheet by remember { mutableStateOf(false) }

        // Function to check GPS state
        fun checkGps() {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            model.setGpsEnabled(gps || network) // << IMPORTANT UPDATE
        }

        // Permission launcher
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            model.setPermissionGranted(granted)
            if (granted) checkGps()
        }

        // Initial check
        LaunchedEffect(Unit) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            model.setPermissionGranted(granted)
            checkGps()
        }

        // GPS BroadcastReceiver
        DisposableEffect(Unit) {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(c: Context?, intent: Intent?) {
                    checkGps()
                }
            }
            context.registerReceiver(receiver, filter)

            onDispose { context.unregisterReceiver(receiver) }
        }

        // Show/Hide the bottom sheet
        LaunchedEffect(isPermissionGranted, isGpsEnabled) {
            showSheet = !(isPermissionGranted && isGpsEnabled)
        }
        val bottomSheetState=rememberModalBottomSheetState()
        // UI rendering
        Box(Modifier.fillMaxSize()) {
            content()

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = {showSheet=false},
                    sheetState = bottomSheetState
                ) {
                    when {
                        !isPermissionGranted -> {
                            PermissionSheetUI() {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }

                        !isGpsEnabled -> {
                            GpsSheetUI() {
                                activity.startActivity(
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }

    @Composable
    fun PermissionSheetUI(onEnablePermission: () -> Unit) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Location Permission Needed", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Get accurate and relevant restaurant options based on your current location.",
                fontSize = 16.sp
            )
            Spacer(Modifier.height(16.dp))

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.grant_permission),
                onClick = {
                    onEnablePermission()
                }
            )


        }
    }

    @Composable
    fun GpsSheetUI( onEnableGps: () -> Unit) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.gps_is_off), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.please_enable_your_gps_to_continue), fontSize = 16.sp)
            Spacer(Modifier.height(16.dp))


            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.turn_on_gps),
                onClick = {
                    onEnableGps()
                }
            )

        }
    }
}
