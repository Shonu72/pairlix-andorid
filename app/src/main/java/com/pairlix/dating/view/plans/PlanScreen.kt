package com.pairlix.dating.view.updragePlan


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import com.pairlix.dating.ThemeManager.isAppInDarkTheme
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController

import androidx.navigation.NavHostController
import com.pairlix.dating.MainActivity

import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference

import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.response.ActivePlanResponse
import com.pairlix.dating.response.GetPlansResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.plans.SubscriptionScreen
import com.pairlix.dating.view.plans.defaultPrices
import com.pairlix.dating.view.plans.goldPriceTable
import com.pairlix.dating.view.plans.platinumPriceTable
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.AuthViewModel.LocationViewModel
import ir.kaaveh.sdpcompose.sdp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts


import android.app.Activity
import android.content.IntentSender
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class GpsUtils(private val activity: Activity) {

    private val settingsClient = LocationServices.getSettingsClient(activity)

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 1000
    ).build()

    private val settingsRequest =
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(true)   // ⭐ VERY IMPORTANT → System dialog force show
            .build()

    fun enableGPS(launcher: ActivityResultLauncher<IntentSenderRequest>) {

        settingsClient.checkLocationSettings(settingsRequest).addOnSuccessListener {
                // GPS already ON
            }.addOnFailureListener { exception ->

                if (exception is ResolvableApiException) {
                    try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.resolution).build()

                        launcher.launch(intentSenderRequest)

                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
    }
}


@Composable
fun RequestLocationAndGps(
    context: Context, onPermissionGranted: () -> Unit = {}, onGpsEnabled: () -> Unit = {}
) {

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->

        val fine = result[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = result[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fine || coarse) {
            onPermissionGranted()

            // After permission → check GPS
            if (isGpsEnabled(context)) {
                onGpsEnabled()
            } else {
                Toast.makeText(context,
                    context.getString(R.string.gps_is_off_opaening_settings), Toast.LENGTH_SHORT)
                    .show()
                openGpsSettings(context)
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.location_permission_denied),
                Toast.LENGTH_SHORT
            ).show()        }
    }

    // Trigger automatically when composable is called
    LaunchedEffect(Unit) {
        permissionLauncher.launch(locationPermissions)
    }
}


fun isGpsEnabled(context: Context): Boolean {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun openGpsSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
)

fun getExpiryDateISO(context: Context, duration: String): String {
    val today = LocalDate.now() // device local date
    val expiry = when (duration) {
        context.getString(R.string.plan_1_month)  -> today.plusMonths(1)
        context.getString(R.string.plan_3_months) -> today.plusMonths(3)
        context.getString(R.string.plan_6_months) -> today.plusMonths(6)
        else -> today
    }
    // Return ISO format e.g. "2026-04-30"
    return expiry.toString()
}





fun getDaysLeft(expiryDateString: String?): Long {
    if (expiryDateString.isNullOrBlank()) return -1

    // Extract only the date part from ISO timestamp
    // e.g. "2026-03-23T00:00:00.000Z" → "2026-03-23"
    val datePart = expiryDateString.trim().take(10)

    return try {
        val expiryDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val today = LocalDate.now() // ✅ Use device local timezone, NOT UTC

        ChronoUnit.DAYS.between(today, expiryDate) // negative if expired
    } catch (e: Exception) {
        -1 // treat parse failure as expired
    }
}

fun ActivePlanObserver(
    viewModel: AuthViewModel,
    context: MainActivity,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    onSuccess: (ActivePlanResponse.Data?) -> Unit = {}
) {

    viewModel.getActivePlan.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
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

                    onSuccess(state.value.data)

                    state.value.success = false
                }
            }

            else -> {
                // no-op
            }
        }
    }


}


@Composable
fun PlanScreen(navController: NavHostController, viewModel: AuthViewModel) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {

        viewModel.hitGetActivePlan(
            access_token = SharedPreference.get(context).accessToken
        )
        ActivePlanObserver(
            viewModel = viewModel,
            context = context as MainActivity,
            lifecycleOwner = lifecycleOwner,
            navController = navController,
            onSuccess = { it ->
                it.let {
                    viewModel.activePlanData.value = it
                }


            })
    }
    val locationVM = remember { LocationViewModel() }

    var currentRegion by remember { mutableStateOf("International") }
    var currentCountry by remember { mutableStateOf("Unknown") }


    LaunchedEffect(Unit) {
        locationVM.loadLocation(context) {
            currentCountry = locationVM.country
            currentRegion = locationVM.region
        }
    }


    val data = viewModel.activePlanData.value
    val rawExpiry = data?.expiredOn?.toString() ?: ""
    val daysLeft = getDaysLeft(rawExpiry)
    val expireText = when {
        daysLeft < 0 -> {
            stringResource(R.string.expired)
        }
        daysLeft == 0L -> {
            stringResource(R.string.expire_today)
        }
        else -> {
            stringResource(R.string.expire_in_days, daysLeft)
        }
    }

    val goldBenefits = stringArrayResource(R.array.gold_benefits).toList()

    val platinumBenefits = stringArrayResource(R.array.platinum_benefits).toList()

    val freeBenefits = stringArrayResource(R.array.free_benefits).toList()

    val gold1MonthPrice =
        goldPriceTable[currentRegion]?.price1Display ?: defaultPrices.price1Display
    val platinum1MonthPrice =
        platinumPriceTable[currentRegion]?.price1Display ?: defaultPrices.price1Display




    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        val max = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
               .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)


        ) {
            TopBackBtnHeading(navController, text = stringResource(R.string.plans))

            verticalSpace(18)

            LazyColumn(modifier = Modifier.fillMaxSize()) {


                item {
                    val context = LocalContext.current
                    val viewModel = remember { LocationViewModel() }

                    /*    var currentCountry by remember { mutableStateOf("Loading...") }
                        var currentRegion by remember { mutableStateOf("Loading...") }

                        // Permission Check
                        val permissionGranted =
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                        LaunchedEffect(permissionGranted) {
                            if (permissionGranted) {
                                viewModel.loadLocation(context) {
                                    currentCountry = viewModel.country
                                    currentRegion = viewModel.region
                                }
                            }
                        }*/

                    /*    Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text("Current Country: $currentCountry", )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Region: $currentRegion", )
                        }*/


                    Text(
                        text = stringResource(R.string.plan_to_upgrade),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold))
                    )


                    verticalSpace(20)

                    GoldPlanCard(priceText ="", onClick = {
                        SingletonObject.isComeFromGoldPlan = true
                        SingletonObject.isComeFromPlatinumPlan = false
                        navController.navigate(Screen.PlanUpgradeScreen.route)
                    })


                    verticalSpace(20)

                    PlatinumPlanCard(priceText = "", onClick = {
                        SingletonObject.isComeFromPlatinumPlan = true
                        SingletonObject.isComeFromGoldPlan = false
                        navController.navigate(Screen.PlanUpgradeScreen.route)
                    })



                    verticalSpace(20)


                    Text(
                        text = stringResource(R.string.your_current_plan),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold))
                    )
                    verticalSpace(20)


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {

                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column {

                            // ---------- TOP GRADIENT HEADER ----------
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            if (data?.planType == 2) listOf(
                                                Color(0xFFFFBA55),
                                                Color(0xFFFFF1DA),
                                                Color(0xFFFFCF80),
                                                Color(0xFFFFDEAD),
                                                Color(0xFFFFDF9C),

                                                )
                                            else if (data?.planType == 3) listOf(
                                                Color(0xFFD9D9D9),  // light silver start
                                                Color(0xFFF2F2F2),  // thin highlight (white reduced)
                                                Color(0xFFE0E0E0),  // mid silver
                                                Color(0xFFB3B3B3)
                                            )
                                            else {
                                                listOf(
                                                    Color(0xFF8B5DF6),
                                                    Color(0xFF8B5DF6).copy(alpha = 0.8f),
                                                    Color(0xFFF6A6D6).copy(alpha = 0.95f),  // Pink → very small + soft
// Middle light purple
                                                    // Full purple
                                                )


                                            }
                                        ),
                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 10.dp, top = 20.dp, bottom = 10.dp, end = 10.dp
                                        )
                                ) {

                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = if (data?.planType == 1) stringResource(R.string.free_plan) else if (data?.planType == 2) stringResource(R.string.gold_plan) else stringResource(R.string.platinum_plan),
                                                fontSize = 22.sp,
                                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                                color = (if (data?.planType == 1) Color.White else if (data?.planType == 2) Color.Black else Color(
                                                    0xFF4A148C
                                                ))
                                            )

                                            Text(
                                                text = if (data?.planType == 1) "" else expireText,
                                                fontSize = 12.sp,
                                                color = Color(0xFFEB0031),
                                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                            )/* Text(
                                                 text = if (data?.planType == 1) "" else data?.price.toString(),
                                                 fontSize = 20.sp,
                                                 fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                                 color = _root_ide_package_.androidx.compose.ui.res.MaterialTheme.colorScheme.onBackground
                                             )*/
                                        }

                                    }
                                    Spacer(modifier = Modifier.height(20.dp))


                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = if (data?.planType == 1) stringResource(R.string.basic_matching_connection) else if (data?.planType == 2) stringResource(R.string.visibility_ultimate_connections) else stringResource(R.string.ultimate_dating_experience),
                                                fontSize = 16.sp,
                                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                                color = if (data?.planType == 1) Color.White else Color.Black
                                            )
                                        }

                                    }

                                }
                            }
                            // ---------- BODY ----------
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Text(
                                    text = stringResource(R.string.plan_details),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                    color = Color(0xFF6D6D6D)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                if (data?.planType == 1) {
                                    freeBenefits.forEach { item ->
                                        PlanBullet1(text = item)
                                    }
                                } else if (data?.planType == 2) {

                                    goldBenefits.forEach { item ->
                                        PlanBullet1(text = item)
                                    }
                                } else {
                                    platinumBenefits.forEach { benefit ->
                                        PlanBullet1(text = benefit)
                                    }
                                }


                            }
                        }
                    }


                    /* FreePlanCard(
                         modifier = Modifier, onClick = {

                             navController.navigate(Screen.CurrentPlaDetailsScreen.route)
                         }
                     )*/

                    /* GoldPlanCardActive(
                         modifier = Modifier, onClick = {

                             navController.navigate(Screen.CurrentPlaDetailsScreen.route)
                         }

                         )*/


                  /*  Text(
                        text = "Expired Plan(s)",
                        color = _root_ide_package_.androidx.compose.ui.res.MaterialTheme.colorScheme.onBackground
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold))
                    )*/

                    verticalSpace(20)

                    Text(
                        stringResource(R.string.note_now_you_can_only_explore_plan_once_your_plan_expire_then_you_can_purchase_upgrade),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF1A8B5DF6), shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(
                            Font(R.font.axiforma_regular)
                        ),
                        lineHeight = 15.sp
                    )
                    verticalSpace(20)
                }
            }/* Column(
                 modifier = Modifier
                     .fillMaxSize()
                     .verticalScroll(scrollState)
             ) {




                 GoldPlanCard1()
                 Spacer(Modifier.height(20.dp))


                 Text(
                     "NOTE: Now you can only explore plan once your plan expire then you can purchase/upgrade",
                     modifier = Modifier
                         .fillMaxWidth()
                         .background(
                             color = Color(0xFF1A8B5DF6),
                             shape = RoundedCornerShape(12.dp)
                         )
                         .padding(12.dp),
                     color = Color(0xff000000),
                     fontSize = 10.sp,
                     fontFamily = FontFamily(
                         Font(R.font.axiforma_regular)
                     ),
                     lineHeight = 15.sp
                 )
                 Spacer(Modifier.height(80.sdp))


             }*/


        }

        /*  AppButton(
              modifier = Modifier
                 .background(MaterialTheme.colorScheme.background)
                  .padding(horizontal = 16.dp)
                  .align(Alignment.BottomCenter),
              text = "Upgrade Plan",
              onClick = {
                  navController.navigate(Screen.SelectPlanScreen.route)
              }
          )*/


    }
}

@Composable
fun GoldPlanCardActive(
    modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {

    val goldBenefits = stringArrayResource(R.array.gold_benefits).toList()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFBA55),
                                Color(0xFFFFF1DA),
                                Color(0xFFFFCF80),
                                Color(0xFFFFDEAD),
                                Color(0xFFFFDF9C),
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 20.dp, bottom = 10.dp, end = 10.dp)
                ) {

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.gold_plan),
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                color = Color(0xFF000000)
                            )
                            Text(
                                text = stringResource(R.string.expire_in_days, 4),
                                fontSize = 12.sp,
                                color = Color(0xFFEB0031),
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.visibility_and_ultimate_connections),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = Color(0xFF000000)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.plan_details),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    color = Color(0xFF6D6D6D)
                )

                Spacer(modifier = Modifier.height(10.dp))

                goldBenefits.forEach {
                    PlanBullet1(it)
                }

            }
        }
    }
}


@Composable
fun GoldPlanCard(
    modifier: Modifier = Modifier, priceText: String = "", onClick: () -> Unit = {}
) {

    val goldBenefits = stringArrayResource(R.array.gold_benefits).toList()

    val isDark = isAppInDarkTheme()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            if (isDark) listOf(
                                Color(0xFF4A3B18),
                                Color(0xFF6B5320),
                                Color(0xFF52401B)
                            ) else listOf(
                                Color(0xFFFFBA55),
                                Color(0xFFFFF1DA),
                                Color(0xFFFFCF80),
                                Color(0xFFFFDEAD),
                                Color(0xFFFFDF9C),
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 20.dp, bottom = 10.dp, end = 10.dp)
                ) {

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.gold_plan),
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                color = if (isDark) Color.White else Color(0xFF000000)
                            )
                            Text(
                                text = priceText,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                color = if (isDark) Color.White else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.visibility_and_ultimate_connections),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = if (isDark) Color.White.copy(alpha = 0.9f) else Color(0xFF000000)
                            )
                        }
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.plan_details),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    color = Color(0xFF6D6D6D)
                )

                Spacer(modifier = Modifier.height(10.dp))

                goldBenefits.forEach {
                    PlanBullet1(it)
                }

            }
        }
    }
}

@Composable
fun GoldPlanCardCurrent(
    modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {

    val goldBenefits = stringArrayResource(R.array.gold_benefits).toList()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {

            // ---------- TOP GRADIENT HEADER ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFBA55),
                                Color(0xFFFFF1DA),
                                Color(0xFFFFCF80),
                                Color(0xFFFFDEAD),
                                Color(0xFFFFDF9C),
                            )
                        ), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 20.dp, bottom = 10.dp, end = 10.dp)
                ) {

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.gold_plan),
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                color = Color(0xFF000000)
                            )
                            Text(
                                text = stringResource(R.string.expire_in_days, 4),
                                fontSize = 12.sp,
                                color = Color(0xFFEB0031),
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.visibility_and_ultimate_connections),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = Color(0xFF000000)
                            )
                        }

                    }

                }
            }

            // ---------- BODY ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.plan_details),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    color = Color(0xFF6D6D6D)
                )

                Spacer(modifier = Modifier.height(10.dp))

                goldBenefits.forEach {
                    PlanBullet1(it)
                }

                verticalSpace(20) /*   SubscriptionScreen(
                       modifier = Modifier.padding(horizontal = 8.dp),
                       regionPrices = ,
                       onPlanSelected =
                   )*/

            }
        }
    }
}
@Composable
fun PlatinumPlanCard(
    modifier: Modifier = Modifier,
    priceText: String = "",
    onClick: () -> Unit = {},
) {

    val platinumBenefits = stringArrayResource(R.array.platinum_benefits).toList()

    val isDarkPlatinum = isAppInDarkTheme()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            if (isDarkPlatinum) listOf(
                                Color(0xFF2B213A),
                                Color(0xFF3D2B54),
                                Color(0xFF2B213A)
                            ) else listOf(
                                Color(0xFFD9D9D9),
                                Color(0xFFF2F2F2),
                                Color(0xFFE0E0E0),
                                Color(0xFFB3B3B3)
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 20.dp, bottom = 10.dp, end = 10.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = stringResource(R.string.platinum_plan),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                            color = if (isDarkPlatinum) Color.White else Color(0xFF4A148C)
                        )

                        Text(
                            text = priceText,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                            color = if (isDarkPlatinum) Color.White else Color(0xFF4A148C)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.ultimate_dating_experience),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = if (isDarkPlatinum) Color.White.copy(alpha = 0.9f) else Color(0xFF000000)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.plan_details),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    color = Color(0xFF6D6D6D)
                )

                Spacer(modifier = Modifier.height(8.dp))

                platinumBenefits.forEach {
                    PlanBullet1(it)
                }

            }
        }
    }
}
@Composable
fun PlanBullet1(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(R.drawable.gold_star_ic),
            contentDescription = null,
            modifier = Modifier.size(14.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun FreePlanCard(
    modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {

    val freeBenefits = stringArrayResource(R.array.free_benefits).toList()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .appGradientBackground()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 20.dp, bottom = 10.dp, end = 10.dp)
                ) {

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.free_plan),
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                color = Color(0xFF000000)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.basic_matching_connection),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = Color(0xFF000000)
                            )
                        }
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.plan_details),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    color = Color(0xFF6D6D6D)
                )

                Spacer(modifier = Modifier.height(10.dp))

                freeBenefits.forEach {
                    PlanBullet1(it)
                }

            }
        }
    }
}