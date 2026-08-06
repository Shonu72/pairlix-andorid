package com.pairlix.dating.view.plans

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.android.billingclient.api.ProductDetails
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomDialog
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.requests.PurchasedPlanRequest
import com.pairlix.dating.response.GetPlansResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.updragePlan.ActivePlanObserver
import com.pairlix.dating.view.updragePlan.GoldPlanCard
import com.pairlix.dating.view.updragePlan.PlanBullet1
import com.pairlix.dating.view.updragePlan.PlatinumPlanCard
import com.pairlix.dating.view.updragePlan.getDaysLeft
import com.pairlix.dating.view.updragePlan.getExpiryDateISO
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.PurchaseViewModel
import java.util.Locale
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId

/*
fun getTodayDate(): String {
    val today = LocalDate.now()
    return today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}
*/

fun getTodayDate(): String {
    val today = LocalDate.now()
    return today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}



fun getExpiryDate(duration: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val today = LocalDate.now()

    val expiryDate = when (duration) {
        "1 Month" -> today.plusMonths(1)
        "3 Months" -> today.plusMonths(3)
        "6 Months" -> today.plusMonths(6)
        else -> today
    }

    return expiryDate.format(formatter)
}

// ----------------------- PRICE DATA CLASSES -----------------------

data class RegionPrices(
    val price1Display: String,
    val price3Display: String,
    val price6Display: String,
    val price1Value: Int,
    val price3Value: Int,
    val price6Value: Int
)

// Region -> Prices mapping
val goldPriceTable: Map<String, RegionPrices> = mapOf(
    "Pakistan" to RegionPrices(
        price1Display = "1,400 PKR",
        price3Display = "3,060 PKR",
        price6Display = "4,875 PKR",
        price1Value = 1400,
        price3Value = 3060,
        price6Value = 4875
    ),

    "Saudi Arabia" to RegionPrices(
        price1Display = "28 SAR",
        price3Display = "61 SAR",
        price6Display = "98 SAR",
        price1Value = 28,
        price3Value = 61,
        price6Value = 98
    ),

    "Europe" to RegionPrices(
        price1Display = "€11",
        price3Display = "€26",
        price6Display = "€41",
        price1Value = 11,
        price3Value = 26,
        price6Value = 41
    ),

    "USA / International" to RegionPrices(
        price1Display = "$13",
        price3Display = "$30",
        price6Display = "$47",
        price1Value = 13,
        price3Value = 30,
        price6Value = 47
    ),

    "India" to RegionPrices(
        price1Display = "₹120",
        price3Display = "₹300",
        price6Display = "₹450",
        price1Value = 120,
        price3Value = 300,
        price6Value = 450
    )
)

val platinumPriceTable = mapOf(
    "Pakistan" to RegionPrices("2,200 PKR", "4,250 PKR", "6,750 PKR", 2200, 4250, 6750),
    "Saudi Arabia" to RegionPrices("42 SAR", "89 SAR", "139 SAR", 42, 89, 139),
    "Europe" to RegionPrices("€22", "€44", "€71", 22, 44, 71),
    "USA / International" to RegionPrices("$27", "$54", "$86", 27, 54, 86),
    "India" to RegionPrices("₹220", "₹425", "₹675", 220, 425, 675)  // OPTIONAL (you can change)
)



// Default AED prices – used when country not in above 5 regions
val defaultPrices = RegionPrices(
    price1Display = "AED 120",
    price3Display = "AED 160",
    price6Display = "AED 250",
    price1Value = 120,
    price3Value = 160,
    price6Value = 250
)

val defaultPricesSAR = RegionPrices(
    price1Display = "SAR 120",
    price3Display = "SAR 160",
    price6Display = "SAR 250",
    price1Value = 120,
    price3Value = 160,
    price6Value = 250
)


// ----------------------- MAIN SCREEN -----------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanUpgradeScreen(navController: NavController, viewModel: AuthViewModel,purchaseViewModel: PurchaseViewModel= hiltViewModel()) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity =context as Activity


    var autoRenew by remember { mutableStateOf(true) }



    // location related state
   // var country by remember { mutableStateOf("Unknown") }
    var country = "Saudi Arabia"
    var region by remember { mutableStateOf("International") }

    // selected plan info (for API + button text)
    var selectedDuration by remember { mutableStateOf("3 Months") }
    var selectedPriceValue by remember { mutableStateOf(defaultPrices.price3Value) }
    var selectedDisplayPrice by remember { mutableStateOf(defaultPrices.price3Display) }


   // var regionPrices by remember { mutableStateOf(defaultPrices) }
// ✅ Static SAR prices
    var regionPrices by remember {
        mutableStateOf(
            when {
                SingletonObject.isComeFromGoldPlan -> goldPriceTable["Saudi Arabia"] ?: defaultPricesSAR
                SingletonObject.isComeFromPlatinumPlan -> platinumPriceTable["Saudi Arabia"] ?: defaultPricesSAR
                else -> defaultPricesSAR
            }
        )
    }
    val goldBenefits = stringArrayResource(R.array.gold_benefits).toList()
    val platinumBenefits = stringArrayResource(R.array.platinum_benefits).toList()


   // old correct
 /*   val regionPrices = when {
        SingletonObject.isComeFromGoldPlan -> {
            goldPriceTable[region] ?: defaultPrices
        }
        SingletonObject.isComeFromPlatinumPlan -> {
            platinumPriceTable[region] ?: defaultPrices
        }
        else -> defaultPrices
    }
*/

    //new in test




  /*  LaunchedEffect(region) {
        regionPrices = when {
            SingletonObject.isComeFromGoldPlan -> goldPriceTable[region] ?: defaultPrices
            SingletonObject.isComeFromPlatinumPlan -> platinumPriceTable[region] ?: defaultPrices
            else -> defaultPrices
        }
    }*/







    // -------- Permission launcher for location ----------





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


            }
        )
    }

    val data = viewModel.activePlanData.value
    val rawExpiry = data?.expiredOn?.toString() ?: ""
    val daysLeft = getDaysLeft(rawExpiry)
    var showDialog  by remember { mutableStateOf(false) }

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



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            TopBackBtnHeading(
                navController,
                text = stringResource(R.string.your_current_plan),
            )
            verticalSpace(15)

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if(showDialog){
                    CustomDialog(
                        id = R.drawable.green_tick_ic,
                        text1 = stringResource(R.string.plan_purchased_sucessfully) ,
                        text2 ="" ,
                        onDismiss = {showDialog=false}
                    )

                }

                if (SingletonObject.isComeFromPlatinumPlan) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column {
                            // ---------- TOP GRADIENT HEADER ----------
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFFD9D9D9),  // light silver start
                                                Color(0xFFF2F2F2),  // thin highlight (white reduced)
                                                Color(0xFFE0E0E0),  // mid silver
                                                Color(0xFFB3B3B3)
                                            )
                                        ),
                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                    )
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 10.dp,
                                            top = 20.dp,
                                            bottom = 10.dp,
                                            end = 10.dp
                                        )
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Text(
                                            text = stringResource(R.string.platinum_plan),                                            fontSize = 22.sp,
                                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                            color = Color(0xFF4A148C)  // Purple from screenshot
                                        )
                                        if(data?.planType==3) {
                                            Text(
                                                text = expireText,
                                                fontSize = 12.sp,
                                                color = Color(0xFFEB0031),
                                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = stringResource(R.string.ultimate_dating_experience),
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                        color = Color(0xFF000000)
                                    )
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

                                Spacer(modifier = Modifier.height(8.dp))
                                platinumBenefits.forEach{PlanBullet1(it)}

                                verticalSpace(20)
                                val product=purchaseViewModel.products.find { it.productId=="platinum_plan" }

                                SubscriptionScreen(
                                    regionPrices = regionPrices,
                                    activeDuration = data?.duration,
                                    planType = data?.planType,
                                    planStatus = data?.status,
                                    isActive = data?.planType==3 && data.status=="active",

                                    offerDetails = product?.subscriptionOfferDetails?:arrayListOf(),
                                    onPlanSelected = {
                                        Log.e("purchase", "${product}: \n base----${it} ", )
                                        purchaseViewModel.buy(activity,product!!,it)
                                    }
                                )


                            }
                        }
                    }







                  /*  PlatinumPlanCard(
                        modifier = Modifier,

                        onClick = {}
                    )*/
                } else {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                          ,
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface)
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
                                        ),
                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 10.dp,
                                            top = 20.dp,
                                            bottom = 10.dp,
                                            end = 10.dp
                                        )
                                ) {

                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.gold_plan),                                                fontSize = 22.sp,
                                                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                                color = Color(0xFF000000)
                                            )
                                            if(data?.planType==2){
                                            Text(
                                                text =  expireText ,
                                                fontSize = 12.sp,
                                                color = Color(0xFFEB0031),
                                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                            )
                                            }
                                        }

                                    }
                                    Spacer(modifier = Modifier.height(20.dp))


                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = stringResource(R.string.visibility_and_ultimate_connections),                                                fontSize = 16.sp,
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

                                goldBenefits.forEach{
                                    PlanBullet1(it)
                                }

                                verticalSpace(20)
                                val product=purchaseViewModel.products.find { it.productId=="gold_plan" }

                                SubscriptionScreen(
                                    regionPrices = regionPrices,
                                    activeDuration = data?.duration,
                                    planType = data?.planType,
                                    planStatus = data?.status,
                                    offerDetails = product?.subscriptionOfferDetails?:arrayListOf(),
                                    isActive = data?.planType==2 && data.status=="active",
                                    onPlanSelected = {
                                        Log.e("purchase", "${product}: \n base----${it} ", )
                                        purchaseViewModel.buy(activity,product!!,it)
                                    }
                                )




                            }
                        }
                    }











/*
                    GoldPlanCard(modifier = Modifier, onClick = {})
*/
                }


                // --------- Subscription plans with regional prices ---------

                Spacer(Modifier.height(25.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.auto_renewal),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                          color = MaterialTheme.colorScheme.onBackground,

                        )
                    Box(modifier = Modifier.scale(0.8f)) {
                        Switch(
                            modifier = Modifier,
                            checked = autoRenew,
                            onCheckedChange = { autoRenew = it }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        stringResource(R.string.turn_on_auto_renew_to_keep_your_subscription_active_without_any_hassle),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = Color(0xFF6D6D6D),
                        lineHeight = 16.sp
                    )
                }

                Spacer(Modifier.height(20.dp))
                val planTypeToSend = if (SingletonObject.isComeFromGoldPlan) 2 else 3

                // ------------- UPGRADE BUTTON -------------
                AppButton(
                    modifier = Modifier,
                    text = stringResource(R.string.upgrade_price, selectedDisplayPrice) ,
                    onClick = {
                        val purchaseDate = getTodayDate()
                        val expireDate = getExpiryDateISO(context,selectedDuration)

                       /* viewModel.hitPurchasedPlans(
                            access_token = SharedPreference.get(context).accessToken,
                            request = PurchasedPlanRequest(
                                countryName = country,
                                duration = selectedDuration,
                                expiredOn = expireDate,
                                planType = planTypeToSend,
                                price = selectedPriceValue,
                                purchasedOn = purchaseDate,
                                paymentStatus = 0 // work in the future

                            )
                        )*/

                        PlanPurchaseObserver(
                            viewModel = viewModel,
                            context = context,
                            lifecycleOwner = lifecycleOwner,
                            navController = navController,
                            purchaseViewModel,
                            onShowDialog={showDialog=true}
                        )
                    }
                )
                verticalSpace(10)


                Text(
                    stringResource(R.string.subscription_terms),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        Font(R.font.axiforma_regular)
                    ),
                    lineHeight = 18.sp
                )
                verticalSpace(10)

              /*  if (SingletonObject.isComeFromPlatinumPlan&& data?.planType==3){
                Text(
                    modifier = Modifier
                        .fillMaxWidth().clip(shape = RoundedCornerShape(52.dp))
                        .border(1.dp, Color(0xFFEF505F),
                            shape = RoundedCornerShape(52.dp))
                        .background(Color.White,
                            shape = RoundedCornerShape(52.dp)).padding(vertical = 18.dp),
                    text = "Cancel Subscription",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    color = Color(0xFFEF505F),
                    textAlign = TextAlign.Center
                )}
                if (SingletonObject.isComeFromGoldPlan&& data?.planType==2){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth().clip(shape = RoundedCornerShape(52.dp))
                            .border(1.dp, Color(0xFFEF505F),
                                shape = RoundedCornerShape(52.dp))
                            .background(Color.White,
                                shape = RoundedCornerShape(52.dp)).padding(vertical = 18.dp),
                        text = "Cancel Subscription",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color(0xFFEF505F),
                        textAlign = TextAlign.Center
                    )}

*/
            }
        }
    }
}

// ---------------------- SUBSCRIPTION LIST ----------------------

data class Plan(
    val displayPrice: String,
    val id: String,
    val priceValue: Int,
    val duration: String,
)

@Composable
fun SubscriptionScreen(
    regionPrices: RegionPrices,
    activeDuration: String?,
    planType: Int?,
    planStatus: String?,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    offerDetails: List<ProductDetails.SubscriptionOfferDetails>,
    onPlanSelected: (offerToken: String) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }



    val plans = offerDetails.mapIndexed { index, offer ->


        val pricingPhase = offer.pricingPhases.pricingPhaseList.first()

        val priceMicros = pricingPhase.priceAmountMicros
        val currency = pricingPhase.priceCurrencyCode

        val priceValue = (priceMicros / 1_000_000).toInt()
        val displayPrice = pricingPhase.formattedPrice

        Plan(
            displayPrice = displayPrice,
            id = offer.offerToken, // or map to "1 month / 3 month"
            priceValue = priceValue,
            duration = if (index==0)"1 Month" else if (index==1)"3 Months" else "6 Months"
        )
    }


    LaunchedEffect(activeDuration) {
        if (activeDuration == null) {
            // user has no active plan → select 3 months
            selectedIndex = 0
        } else {

            val activeIndex = plans.indexOfFirst { it.id == activeDuration }

            selectedIndex = if (activeIndex == 1) {
                // IF active plan = 3 Months (index 1)
                // then select 6 Months (index 2)
                2
            } else {
                // else select 3 months (index 1) always
                1
            }
        }

        // send selected plan info to parent

    }




    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = stringResource(R.string.select_duration),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            color = Color(0xFF6D6D6D)
        )

        Spacer(Modifier.height(16.dp))

        plans.forEachIndexed { index, plan ->





            SubscriptionItem(
                price = plan.displayPrice,
                duration = activeDuration?:"",
                selected = selectedIndex == index,
                isActive =isActive&&activeDuration==plan.duration,
                onClick = {
                    onPlanSelected(plan.id)
                   /* if (!isActive) {
                        selectedIndex = index
                        onPlanSelected(plan.duration, plan.priceValue, plan.displayPrice)
                    }*/
                }
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun SubscriptionItem(
    price: String,
    duration: String,
    selected: Boolean,
    isActive: Boolean = false,   // 👈 ADD
    onClick: () -> Unit
) {
    val gradient = if (selected) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF8B5DF6).copy(alpha = 0.8f),
                Color(0xFFF6A6D6).copy(alpha = 0.9f)
            )
        )
    }
    else if(isActive){
        Brush.horizontalGradient(
            listOf(
                Color(0xFF6D6D6D).copy(alpha = 0.4f),
                Color(0xFF6D6D6D).copy(alpha = 0.30f),

            ))
    }

    else {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF8B5DF6).copy(alpha = 0.20f),
                Color(0xFFF6A6D6).copy(alpha = 0.20f)
            )
        )
    }


    val textColor = if (selected) Color.White else Color(0xFF590988)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(brush = gradient)
                .padding(horizontal = 12.dp, vertical = 20.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = price,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                    color = if (selected) Color.White else Color.Black)


                Text(
                    text = duration,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    color = if (selected) Color.White else Color(0xFF590988)
                )}
            verticalSpace(5)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                    if (isActive) {
                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.currently_active),
                            fontSize = 12.sp,
                            color = Color(0xFF026C34),
                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                        )

                    }
                }
            }
        }
    }

// ---------------------- OBSERVER ----------------------

fun PlanPurchaseObserver(
    viewModel: AuthViewModel,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    purchaseViewModel: PurchaseViewModel,
    onShowDialog :()-> Unit
) {
    purchaseViewModel.purchasePlan.observe(lifecycleOwner) { state ->
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
                   onShowDialog()
                    context.showToast(state.value.message?:"")
                    state.value.success = false
                }
            }

            else -> {
                // no-op
            }
        }
    }
}

// ---------------------- LOCATION HELPERS ----------------------

fun mapCountryToRegion(country: String): String {
    return when (country) {
        "Pakistan" -> "Pakistan"
        "Saudi Arabia" -> "Saudi Arabia"
        "United States", "Canada" -> "USA / International"
        "India" -> "India"
        "United Kingdom", "Germany", "France", "Italy", "Spain",
        "Portugal", "Netherlands", "Sweden", "Norway", "Finland" -> "Europe"
        else -> "International"
    }
}

fun getCountryFromLatLng(
    context: Context,
    lat: Double,
    lng: Double,
    onResult: (country: String, region: String) -> Unit
) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val result = geocoder.getFromLocation(lat, lng, 1)

        val country = result?.firstOrNull()?.countryName ?: "Unknown"
        val region = mapCountryToRegion(country)
        onResult(country, region)

    } catch (e: Exception) {
        onResult("Unknown", "International")
    }
}

@SuppressLint("MissingPermission")
fun getCurrentCountryAndRegion(
    context: Context,
    onResult: (country: String, region: String) -> Unit
) {
    val fused = LocationServices.getFusedLocationProviderClient(context)

    // Force fresh GPS location every time, skip cache
    fused.getCurrentLocation(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        null
    ).addOnSuccessListener { location ->
        if (location != null) {
            getCountryFromLatLng(context, location.latitude, location.longitude, onResult)
        } else {
            // Fallback to lastLocation if fresh fetch returns null
            fused.lastLocation.addOnSuccessListener { lastLoc ->
                if (lastLoc != null) {
                    getCountryFromLatLng(context, lastLoc.latitude, lastLoc.longitude, onResult)
                } else {
                    onResult("Unknown", "International")
                }
            }
        }
    }.addOnFailureListener {
        onResult("Unknown", "International")
    }
}