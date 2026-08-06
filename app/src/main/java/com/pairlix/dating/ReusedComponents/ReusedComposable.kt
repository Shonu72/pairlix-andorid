package com.pairlix.dating.ReusedComponents

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.ui.text.intl.LocaleList
import android.telephony.TelephonyManager

import android.util.Log
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.abi.simplecountrypicker.DialogCountryPicker
import com.abi.simplecountrypicker.data.CountryData
import com.pairlix.dating.R
import com.pairlix.dating.response.GetCountryCodeResponse
import com.pairlix.dating.view.allLoginScreen.AppLanguage
import com.pairlix.dating.view.newAccountRegistrationScreen.FaithItem
import ir.kaaveh.sdpcompose.sdp
import java.io.ByteArrayOutputStream
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex

import java.util.Locale
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.abi.simplecountrypicker.CountryPickerViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pairlix.dating.ReusedComponents.items
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.formatMillis
import com.pairlix.dating.helper.isVideoUrl
import com.pairlix.dating.helper.prioritizeSearch
import com.pairlix.dating.requests.ActionRequest
import com.pairlix.dating.response.GetMatchResponse
import com.pairlix.dating.utils.saveBitmapToUri
import com.pairlix.dating.utils.uriToFile
import com.pairlix.dating.view.M5.ActionItem
import com.pairlix.dating.view.M6.PrivacyToggle
import com.pairlix.dating.view.M6.ProfileStatus
import com.pairlix.dating.view.home.MatchUser
import com.pairlix.dating.view.newAccountRegistrationScreen.saveBitmapToCache
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.ChatAudioViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.forEachIndexed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat.setLayoutDirection
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.android.billingclient.api.ProductDetails
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.ErrorUtil.showErrorDialog
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.response.GetNotificationResponse
import com.pairlix.dating.utils.GlideEngine
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.M4.timeAgo
import com.pairlix.dating.view.newAccountRegistrationScreen.Country
import com.pairlix.dating.view.newAccountRegistrationScreen.hasCameraPermission
import com.pairlix.dating.view.newAccountRegistrationScreen.hitCityApi
import com.pairlix.dating.view.newAccountRegistrationScreen.normalizeForSearch
import com.pairlix.dating.view.plans.defaultPrices
import com.pairlix.dating.view.plans.defaultPricesSAR
import com.pairlix.dating.view.plans.getCurrentCountryAndRegion
import com.pairlix.dating.view.plans.goldPriceTable
import com.pairlix.dating.view.plans.platinumPriceTable
import com.pairlix.dating.view.updragePlan.PlanBullet1
import com.pairlix.dating.viewModel.PurchaseViewModel
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt




@Composable
fun PlanPopUp( onDismiss: () -> Unit, navController: NavController,model: PurchaseViewModel= hiltViewModel()) {

    val context = LocalContext.current


    var country by remember { mutableStateOf("Unknown") }
    var region by remember { mutableStateOf("International") }
/*
    var goldPrice by remember { mutableStateOf(defaultPrices.price1Display) }
    var platinumPrice by remember { mutableStateOf(defaultPrices.price1Display) }
*/

    val goldPlan=model.products.find { it.productId=="gold_plan" }
    val platinumPlan=model.products.find { it.productId=="platinum_plan" }


    var goldPrice by remember {
        mutableStateOf(
            goldPriceTable["Saudi Arabia"]?.price1Display ?: defaultPricesSAR.price1Display
        )
    }

    var platinumPrice by remember {
        mutableStateOf(
            platinumPriceTable["Saudi Arabia"]?.price1Display ?: defaultPricesSAR.price1Display
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentCountryAndRegion(context) { c, r ->
                country = c
                region = r
            }
        }
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED

        if (granted) {
            getCurrentCountryAndRegion(context) { c, r ->
                country = c
                region = r
            }
        } else {
            permissionLauncher.launch(permission)
        }
    }

   /* LaunchedEffect(region) {
        goldPrice = (goldPriceTable[region] ?: defaultPrices).price1Display
        platinumPrice = (platinumPriceTable[region] ?: defaultPrices).price1Display
    }*/

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 1.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        painter = painterResource(R.drawable.cross_pruple_ic),
                        contentDescription = "ring",
                        modifier = Modifier
                            .size(20.sdp)
                            .clickable { onDismiss() }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(Color(0xFF1A8B5DF6))
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ring),
                        contentDescription = "ring",
                        modifier = Modifier.size(40.dp)
                    )
                    horizontalSpace(5)
                    Text(
                        text = stringResource(R.string.pairlix_premium),
                        fontSize = 24.sp,
                        color = Color(0xFF590988),
                        fontFamily = FontFamily(Font(R.font.axiforma_bold))
                    )
                }

                verticalSpace(15)

                Text(
                    text = stringResource(R.string.get_the_premium_plans_to_get_more_matches_enhance_visibility_nd_unlock_premium_features),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    lineHeight = 18.sp
                )

                verticalSpace(15)

                val goldBenefits = stringArrayResource(R.array.gold_benefits).toList()
                val popupBenefits = stringArrayResource(R.array.popup_benefits).toList()
                val platinumBenefits = stringArrayResource(R.array.platinum_benefits).toList()

                var selectedPlan by remember { mutableIntStateOf(1) }

                popupBenefits.forEach {
                    PlanBullet1(it)
                }

                verticalSpace(10)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    PlanCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        planType = PlanType.GOLD,
                        title = stringResource(R.string.gold),
                        price = goldPrice,
                        offerDetail = goldPlan?.subscriptionOfferDetails?.find { it.basePlanId=="monthly" },
                        billingText = stringResource(R.string.billed_montly),
                        discountText = stringResource(R.string.save_30),
                        isSelected = selectedPlan == 0,
                        onClick = { selectedPlan = 0 }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    PlanCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        planType = PlanType.PLATINUM,
                        title = stringResource(R.string.platinum),
                        price = platinumPrice,
                        billingText = stringResource(R.string.billed_montly),
                        discountText = stringResource(R.string.save_50),
                        offerDetail = platinumPlan?.subscriptionOfferDetails?.find { it.basePlanId=="montly" },
                        isBestValue = true,
                        isSelected = selectedPlan == 1,
                        onClick = { selectedPlan = 1 }
                    )
                }

                verticalSpace(40)

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = if (selectedPlan == 0) {
                                    listOf(
                                        Color(0xFFF2D380),
                                        Color(0xFFFFE4AA),
                                        Color(0xFFFFBA55),
                                        Color(0xFFB35803)
                                    )
                                } else {
                                    listOf(
                                        Color(0xFFE6E6E6),
                                        Color(0xFFA6A6A6),
                                        Color(0xFFB3B3B3),
                                        Color(0xFF808080)
                                    )
                                }
                            )
                        )
                        .clickable {
                            if (selectedPlan == 0) {
                                SingletonObject.isComeFromGoldPlan = true
                                SingletonObject.isComeFromPlatinumPlan = false
                                ErrorUtil.clearError()
                                onDismiss()
                                navController.navigate(Screen.PlanUpgradeScreen.route)
                            } else {
                                SingletonObject.isComeFromPlatinumPlan = true
                                SingletonObject.isComeFromGoldPlan = false
                                ErrorUtil.clearError()
                                onDismiss()
                                navController.navigate(Screen.PlanUpgradeScreen.route)
                            }
                        }
                        .padding(vertical = 12.sdp),
                    text = if (selectedPlan == 0) stringResource(R.string.upgrade_to_gold)
                    else stringResource(R.string.upgrade_to_platinum),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 12.ssp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                )
            }
        }
    }
}



@Composable
fun GradientTimeProgress(
    progress: Float,        // 0f to 1f
    timeText: String,       // "30\nmin"
    size: Dp = 40.dp,
    strokeWidth: Dp = 6.dp
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.size(size)) {

            // 1️⃣ Gray background ring
            drawArc(
                color = Color(0xFF33000000),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                ),
                size = Size(size.toPx(), size.toPx())
            )

            val gradient = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFF6A6D6),
                    Color(0xFF8B5DF6),
                    Color(0xFFF6A6D6)
                )
            )

            val stroke = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )

            // 2️⃣ Gradient progress ring
            rotate(-90f) {
                drawArc(
                    brush = gradient,
                    startAngle = 0f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = stroke,
                    size = Size(size.toPx(), size.toPx())
                )
            }
        }

        Text(
            text = stringResource(R.string.min, timeText),
            fontSize = 9.sp,
            color = Color(0xFF590988),
            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
            textAlign = TextAlign.Center,
            lineHeight = 10.sp
        )
    }
}

@Composable
fun MatchProgressCircle(
    percentage: Int,
    size: Dp = 44.dp,
    strokeWidth: Dp = 8.dp
) {
    val progress = percentage / 100f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {

        Canvas(modifier = Modifier.size(size)) {

            // Background Circle
            drawArc(
                color = Color(0x4DFFFFFF), // light purple
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // Progress Arc
            drawArc(
                color = Color(0xFF590988),
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        Text(
            text = "$percentage%",
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            color = Color.White,//0xFF590988
        )
    }
}

@Composable
fun AppButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit = {}) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(50.dp))
            .appGradientBackground()
            .clickable { onClick() }
            .padding(vertical = 12.sdp),
        text = text,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
    )
}


fun countryCodeToFlagEmoji(countryCode: String): String {
    return countryCode.uppercase().map { char ->
        Character.toChars(char.code + 0x1F1E6 - 'A'.code)
    }.joinToString("") { String(it) }
}

fun countryNameToIsoCode(countryName: String): String? {
    val iso = Locale.getISOCountries().map { code -> Locale("", code) }
        .firstOrNull { it.displayCountry.equals(countryName, true) }?.country
    return countryCodeToFlagEmoji(iso ?: "IN")

}

@Composable
fun verticalSpace(height: Int, useSdp: Boolean = false) {
    Spacer(
        modifier = Modifier.height(
            if (useSdp) height.sdp else height.dp
        )
    )
}


@Composable
fun horizontalSpace(width: Int, useSdp: Boolean = false) {
    Spacer(
        modifier = Modifier.width(
            if (useSdp) width.sdp else width.dp
        )
    )
}

fun noInitialSpace(input: String): String {
    return input.replaceFirst("^\\s+".toRegex(), "")
}


@Composable
fun LanguageRow(
    item: AppLanguage, isSelected: Boolean, onClick: (Int) -> Unit
) {

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF8B5DF6), Color(0xFFF6A6D6))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                brush = if (isSelected) gradient else Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable { onClick(item.id) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Image(
            painter = painterResource(id = item.flagRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )

        Text(
            text = item.name,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 12.dp),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
        )
    }
}


@Composable
fun showTagImageTextBlackBg(text: String) {

    Row(
        modifier = Modifier
            .background(
                Color(0xFF40000000), shape = RoundedCornerShape(20.dp)
            )
            .padding(vertical = 5.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier.size(10.sdp),
            colorFilter = ColorFilter.tint(Color.White),
            painter = painterResource(R.drawable.profile_ic),
            contentDescription = "img"
        )
        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 8.ssp,
            maxLines = 1,
            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
        )

    }
}
@Composable
fun showTagImageTextBlackBgEducaion(text: String) {

    Row(
        modifier = Modifier
            .background(
                Color(0xFF40000000), shape = RoundedCornerShape(20.dp)
            )
            .padding(vertical = 5.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier.size(10.sdp),
            colorFilter = ColorFilter.tint(Color.White),
            painter = painterResource(R.drawable.educaton_book),
            contentDescription = "img"
        )
        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 8.ssp,
            maxLines = 1,
            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
        )

    }
}


/*fun Modifier.appGradientBackground(): Modifier {
    return this.background(
        Brush.horizontalGradient(
            listOf(
                Color(0xFF8B5DF6),
                Color(0xFFF6A6D6)
            )
        )
    )
}*/


fun Modifier.appGradientBackground(): Modifier = this.drawBehind {

    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFF6A6D6).copy(alpha = 0.95f),
                Color(0xFF8B5DF6).copy(alpha = 0.8f),
                Color(0xFF8B5DF6)),
            start = Offset(size.width, 0f),
            end = Offset(0f, size.height)
        )
    )
}

@Composable
fun PagerIndicator(
    totalDots: Int, selectedIndex: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(8.dp)
                    .background(
                        color = if (index == selectedIndex) Color(0xFF8B5DF6) else Color(0xFFE6E6E6),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun MyCountryPickerDialog(
    isFlagVisible: Boolean, onCountrySelected: (CountryData) -> Unit
) {
    CompositionLocalProvider(
        LocalTextStyle provides TextStyle(
            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
            fontSize = 10.sp,
        )
    ) {
        DialogCountryPicker(
            pickedCountry = { country ->
                onCountrySelected(country)
            },
            isCountryFlagVisible = isFlagVisible,
            isCircleShapeFlag = true,
            textColor = Color.Black, // text color for country names
            countryCodeTextColorAndIconColor = Color.Black,
            backgroundColor = Color.White
        )
    }
}

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

    val path = MediaStore.Images.Media.insertImage(
        context.contentResolver, bitmap, "IMG_${System.currentTimeMillis()}", null
    )

    return path.toUri()
}


/*
@Composable
fun TopBackBtnHeading(
    navController: NavController,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit={}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
    ) {
        Image(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .size(35.dp)
                .clip(shape = RoundedCornerShape(50.dp))
                .clickable { navController.popBackStack()
                           onClick()},
            painter = painterResource(R.drawable.back_icon),
            contentDescription = "back_ic"
        )

        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = text,
            color = Color(0xFF000000),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.axiforma_medium))
        )

    }

}
*/


@Composable
fun TopBackBtnHeading(
    navController: NavController,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    var isBackClicked by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 25.dp),
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(35.dp)
                .clip(RoundedCornerShape(50.dp))
                // ✅ Click only once
                .clickable(enabled = !isBackClicked) {

                    // Lock click
                    isBackClicked = true

                    // Optional callback before back
                    onClick()

                    // Navigate back only once
                    navController.popBackStack()
                },

            painter = painterResource(R.drawable.back_icon), contentDescription = "back_ic"
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.axiforma_medium))
        )

    }
}


@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    heading: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    maxLines: Int = 1,
    minLines: Int = 1,
    singleLine: Boolean = true,
    headingFont: FontFamily = FontFamily(Font(R.font.axiforma_regular)),
    enabled: Boolean = true
) {
    val axiformaRegular = FontFamily(Font(R.font.axiforma_regular))

    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = heading, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontFamily = headingFont
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp)
                ), verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = value, onValueChange = {

                    if (enabled) onValueChange(it)     // 🔒 BLOCK CHANGE
                }, enabled = enabled,                     // 🔥 IMPORTANT
                readOnly = !enabled,

                placeholder = {
                    Text(
                        placeholder, style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            fontSize = 14.sp,
                        ), color = Color(0xFF6D6D6D)
                    )
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType, imeAction = imeAction
                ),

                // ⬇️ Controlled by user
                singleLine = singleLine, maxLines = maxLines, minLines = minLines,

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground
                ), textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ), modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun GradientExpandableCardIndex(
    title: String, items: List<String>, selectedIndex: Int, onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f),
                        )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 18.sp,   // <<--- space between wrapped lines
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // DROPDOWN
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelected(index)   // Select item
                                expanded = false        // <── AUTO CLOSE
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = item,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (index == selectedIndex) Color(0xFF8B5DF6)
                            else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )

                        if (index == selectedIndex) {
                            Icon(
                                painter = painterResource(id = R.drawable.tick_icon),
                                contentDescription = null,
                                tint = Color(0xFF8B5DF6)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GradientExpandableCardIndexVisibilityControl(
    title: String,
    subHeading: String,
    list: List<ProfileStatus>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp, MaterialTheme.colorScheme.outline,
                RoundedCornerShape(20.dp)
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f),
                        )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = title,
                fontSize = 16.sp,
                lineHeight = 20.sp,   // <<--- space between wrapped lines
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // DROPDOWN
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Text(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 14.dp),
                    text = subHeading,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    color =  MaterialTheme.colorScheme.outlineVariant,

                    thickness = 1.dp,
                )


                list.forEachIndexed { index, item ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 10.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                onItemSelected(index)   // Select item
                                //expanded = false        // <── AUTO CLOSE
                            },
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.heading,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = if (index == selectedIndex) Color(0xFF8B5DF6)
                                else MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )

                            if (index == selectedIndex) {
                                Icon(
                                    painter = painterResource(id = R.drawable.tick_icon),
                                    contentDescription = null,
                                    tint = Color(0xFF8B5DF6)
                                )
                            }
                        }

                        if (item.subHeading.isNotEmpty()) {
                            verticalSpace(5)

                            Text(
                                text = item.subHeading,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = Color.Gray,
                                modifier = Modifier.padding()
                            )
                        }


                    }
                }
            }
        }
    }

}

@Composable
fun VisibilityCard(
    heading: String, list: List<ProfileStatus>, selectedIndex: Int, onItemSelected: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .heightIn(max = 300.dp)
            .verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Color(0x1A000000), RoundedCornerShape(20.dp)),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.padding(16.dp),
            text = heading,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily(Font(R.font.axiforma_regular))
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            color = Color(0xFF1A000000), thickness = 1.dp,
        )


        list.forEachIndexed { index, item ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onItemSelected(index)   // Select item
                        //expanded = false        // <── AUTO CLOSE
                    },
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.heading,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = if (index == selectedIndex) Color(0xFF8B5DF6)
                        else Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    if (index == selectedIndex) {
                        Icon(
                            painter = painterResource(id = R.drawable.tick_icon),
                            contentDescription = null,
                            tint = Color(0xFF8B5DF6)
                        )
                    }
                }

                if (item.subHeading.isNotEmpty()) {
                    verticalSpace(5)
                    Text(
                        text = item.subHeading,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = Color.Gray,
                        modifier = Modifier.padding()
                    )
                }


            }
        }
    }


}


@Composable
fun SettingNotificationCard(
    heading: String, list: List<ProfileStatus>, selectedIndex: Int, onItemSelected: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .heightIn(max = 300.dp)
            .verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp)),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.padding(16.dp),
            text = heading,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily(Font(R.font.axiforma_regular))
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            color =  MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp,
        )


        list.forEachIndexed { index, item ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onItemSelected(index + 1)    // Select item
                        //expanded = false        // <── AUTO CLOSE
                    },
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.heading,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = if (index == selectedIndex) Color(0xFF8B5DF6)
                        else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )

                    if (index == selectedIndex) {
                        Icon(
                            painter = painterResource(id = R.drawable.tick_icon),
                            contentDescription = null,
                            tint = Color(0xFF8B5DF6)
                        )
                    }
                }

                if (item.subHeading.isNotEmpty()) {
                    verticalSpace(5)
                    Text(
                        text = item.subHeading,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = Color.Gray,
                        modifier = Modifier.padding()
                    )
                }


            }
        }
    }


}

@Composable
fun NotificationCard(
    data: GetNotificationResponse.Data
) {
var isRead by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { isRead = true }
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {


        Image(
            painter = painterResource(if (isRead) R.drawable.read_notification_ic  else  R.drawable.new_notification_unread_ic),
            contentDescription = "bell",
            modifier = Modifier.size(30.dp)
        )

        horizontalSpace(8)
        Column(modifier = Modifier.fillMaxWidth()) {

            val languageManager = LocalLanguageManager.current

            Text(
                text = if (languageManager.currentLanguage == "ar") {
                    data?.titleAr ?: data?.titleEn.orEmpty()
                } else {
                    data?.titleEn.orEmpty()
                },
                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            verticalSpace(10)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(   modifier = Modifier.weight(1f),
                    text = if (languageManager.currentLanguage == "ar") {
                        data?.descriptionAr ?: data?.descriptionEn.orEmpty()
                    } else {
                        data?.descriptionEn.orEmpty()
                    },
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier,
                    text = timeAgo(data?.timestamp),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 12.sp,
                    color = Color(0xFF6D6D6D)
                )
            }


        }

    }


}


@Composable
fun VisibilityCardToggle(
    heading: String,
    list: List<PrivacyToggle>,
    onToggleChanged: (index: Int, value: Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            //.heightIn(max = 300.dp)
            //.verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
    ) {

        Text(
            modifier = Modifier.padding(16.dp),
            text = heading,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily(Font(R.font.axiforma_regular))
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            color =  MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp,

            )

        list.forEachIndexed { index, item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    if (item.description.isNotEmpty()) {
                        verticalSpace(4)
                        Text(
                            text = item.description,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = Color.Gray
                        )
                    }
                }

                GradientSwitch(
                    checked = item.enabled, onCheckedChange = { isChecked ->
                        onToggleChanged(index, isChecked)
                    })

            }
        }
    }
}

@Composable
fun GradientSwitch(
    checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    val trackWidth = 44.dp
    val trackHeight = 24.dp
    val padding = 2.dp
    val thumbSize = trackHeight - (padding * 2)

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - (padding * 2)
        else 0.dp, label = ""
    )

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(1000.dp))
            .background(
                if (checked) Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF8B5DF6), // purple
                        Color(0xFFF6A6D6)  // pink
                    )
                )
                else SolidColor(Color(0xFFE6E6E6))
            )
            .clickable { onCheckedChange(!checked) }
            .padding(padding)) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}


@Composable
fun GradientExpandableCardMultiIdFaith(
    title: String, faithItems: List<FaithItem>,          // id + title
    selectedIds: List<String>,             // 🔥 API friendly
    onSelectionChanged: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color(0x1A000000), RoundedCornerShape(20.dp))
    ) {

        // 🔹 HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f)
                        )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // 🔹 EXPANDABLE LIST (MULTI SELECT, ID BASED)
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                faithItems.forEach { item ->
                    val isSelected = selectedIds.contains(item.id)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val updated = if (isSelected) selectedIds - item.id
                                else selectedIds + item.id

                                onSelectionChanged(updated)
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = item.title,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (isSelected) Color(0xFF8B5DF6)
                            else Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        if (isSelected) {
                            Icon(
                                painter = painterResource(id = R.drawable.tick_icon),
                                contentDescription = null,
                                tint = Color(0xFF8B5DF6)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GradientExpandableCard(
    title: String, items: List<String>, selectedItem: String?, onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp, Color(0x1A000000), RoundedCornerShape(20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f),

                            )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // ---- DROPDOWN CONTENT ----
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)               // prevents overflow crash
                    .verticalScroll(rememberScrollState()) // allows internal scroll
                   .background(MaterialTheme.colorScheme.background)
            ) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelected(item)
                                // expanded = false
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = item,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (item == selectedItem) Color(0xFF8B5DF6)
                            else Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        if (item == selectedItem) {
                            Icon(
                                painter = painterResource(id = R.drawable.tick_icon),
                                contentDescription = null,
                                tint = Color(0xFF8B5DF6)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FormProgressBar(
    currentPage: Double, totalPages: Int = 7, percentage: String, modifier: Modifier = Modifier
) {
    val progress = currentPage.toFloat() / totalPages
    // val percentage = (progress * 100).toInt()

    val animatedProgress by animateFloatAsState(
        targetValue = progress, animationSpec = tween(600), label = ""
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // Get actual width of progress bar
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val barWidthPx = with(LocalDensity.current) { maxWidth.toPx() }

            // Bubble offset based on current progress
            val bubbleOffsetPx = animatedProgress * barWidthPx

            Column(
                modifier = Modifier
                    .offset(x = with(LocalDensity.current) { bubbleOffsetPx.toDp() - 20.dp }   // center adjust
                    )
                    .align(Alignment.TopStart),
                horizontalAlignment = Alignment.CenterHorizontally) {

                // Bubble Box
                Box(
                    modifier = Modifier
                        .padding(start = if (currentPage == 0.0) 16.dp else 0.dp)
                        .background(Color(0xFF590988), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$percentage%", color = Color.White, fontSize = 12.sp
                    )
                }

                // Triangle Pointer
                Canvas(
                    modifier = Modifier
                        .padding(start = if (currentPage == 0.0) 16.dp else 0.dp)
                        .size(16.dp, 10.dp)
                ) {
                    val path = Path().apply {
                        moveTo(size.width / 2, size.height)
                        lineTo(0f, 0f)
                        lineTo(size.width, 0f)
                        close()
                    }
                    drawPath(path, Color(0xFF590988))
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        // PROGRESS BAR (full width)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE9E9E9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF590988))
            )
        }
    }
}

@Composable
fun ProfileProgressBar(
    percentage: String, modifier: Modifier = Modifier
) {
    val progressValue = remember(percentage) {
        percentage.replace("%", "").toFloatOrNull()?.coerceIn(0f, 100f) ?: 0f
    }

    val progress = progressValue / 100f

    val animatedProgress by animateFloatAsState(
        targetValue = progress, animationSpec = tween(600), label = ""
    )

    val bubbleWidth = 40.dp   // approx bubble width

    Column(modifier = modifier.fillMaxWidth()) {

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {

            val barWidthDp = maxWidth

            // 🔥 CLAMPED OFFSET
            val bubbleOffsetDp = remember(animatedProgress) {
                val rawOffset = barWidthDp * animatedProgress
                rawOffset.minus(bubbleWidth / 2).coerceIn(0.dp, barWidthDp - bubbleWidth)
            }

            Column(
                modifier = Modifier
                    .offset(x = bubbleOffsetDp)
                    .align(Alignment.TopStart),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔵 Bubble
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFF590988), RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${progressValue.toInt()}%", color = Color.White, fontSize = 12.sp
                    )
                }

                // 🔻 Pointer
                Canvas(modifier = Modifier.size(16.dp, 10.dp)) {
                    val path = Path().apply {
                        moveTo(size.width / 2, size.height)
                        lineTo(0f, 0f)
                        lineTo(size.width, 0f)
                        close()
                    }
                    drawPath(path, Color(0xFF590988))
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        // 🟣 Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE9E9E9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF590988))
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownIndex(
    modifier: Modifier = Modifier,
    placeholder: String = "Select Option",
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(1f),
                text = if (selectedIndex == -1) placeholder else items[selectedIndex],
                color = if (selectedIndex == -1) Color(0xFF6D6D6D) else Color.Black,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )
            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )

//            Image(modifier= Modifier.rotate(),
//                painter =painterResource(R.drawable.arrow_top_ic),
//                contentDescription = ""
//            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {

            items.forEachIndexed { index, item ->
                DropdownMenuItem(modifier = Modifier.fillMaxWidth(), text = {
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                }, onClick = {
                    expanded = false
                    onItemSelected(index)   // 👈 store index
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ApiDropdown(
    modifier: Modifier = Modifier,
    placeholder: String = "Select Option",
    items: List<T>,
    selectedItem: T?,
    labelExtractor: (T) -> String,     // How to show text from API model
    onItemSelected: (T) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(1f),
                text = selectedItem?.let { labelExtractor(it) } ?: placeholder,
                color = if (selectedItem == null) Color(0xFF6D6D6D) else Color.Black,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)))

            Image(
                painter = painterResource(R.drawable.arrow_top_ic), contentDescription = ""
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
                    containerColor = MaterialTheme.colorScheme.surface
        ) {

            items.forEach { item ->

                DropdownMenuItem(text = {
                    Text(
                        text = labelExtractor(item),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                }, onClick = {
                    expanded = false
                    onItemSelected(item)
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
    modifier: Modifier = Modifier,
    placeholder: String = "Select City",
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {

        // MAIN DROPDOWN FIELD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()   // 👈 IMPORTANT
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(1f),
                text = selectedItem ?: placeholder,
                color = if (selectedItem == null) Color(0xFF6D6D6D) else Color.Black,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )


            Image(
                painter = painterResource(R.drawable.arrow_top_ic), contentDescription = ""
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
                    containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {

            items.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background), text = {
                        Text(
                            text = item,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                    }, onClick = {
                        expanded = false
                        onItemSelected(item)
                    })
            }
        }
    }
}


@Composable
fun CustomRadioButton(
    selected: Boolean,
    label: String,
    selectedColor: Color = Color(0xFF8378E2),
    unselectedColor: Color? = null,  // Make it nullable
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val actualUnselectedColor = unselectedColor ?: MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(vertical = 6.dp)) {

        Box(
            modifier = Modifier
                .size(22.dp)
                .border(
                    width = 2.dp,
                    color = if (selected) selectedColor else actualUnselectedColor,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {

            if (selected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(selectedColor, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    var showSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isArabic = SharedPreference.get(context).language == "ar"

    val displayDate = remember(selectedDate, isArabic) {
        if (selectedDate.isEmpty()) ""
        else forceEnglishDigits(selectedDate) // ← Always sanitize digits
    }


    if (showSheet) {
        val isArabic = SharedPreference.get(context).language == "ar"
        val arabicMonths = listOf(
            "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
            "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
        )

        val englishMonths = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val forcedLocale = if (isArabic) {
            Locale.forLanguageTag("ar-u-nu-latn")
        } else {
            Locale.ENGLISH
        }

        DisposableEffect(Unit) {
            val original = Locale.getDefault()
            Locale.setDefault(forcedLocale)
            onDispose {
                Locale.setDefault(original)
            }
        }

        val forcedConfiguration = android.content.res.Configuration(
            context.resources.configuration
        ).apply {
            setLocale(forcedLocale)
        }

        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Ltr,
            LocalConfiguration provides forcedConfiguration
        ) {
            val datePickerState = rememberDatePickerState(
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis <= System.currentTimeMillis()
                    }
                }
            )
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            ModalBottomSheet(
                dragHandle = null,
                sheetState = sheetState,
                onDismissRequest = { showSheet = false }
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),

                    headline = {
                        val millis = datePickerState.selectedDateMillis
                        val headlineText = if (millis != null) {
                            val cal = Calendar.getInstance(Locale.ENGLISH)
                            cal.timeInMillis = millis
                            val day = String.format(Locale.ENGLISH, "%02d", cal.get(Calendar.DAY_OF_MONTH))
                            val year = String.format(Locale.ENGLISH, "%04d", cal.get(Calendar.YEAR))
                            val monthIndex = cal.get(Calendar.MONTH)
                            val monthLabel = if (isArabic) arabicMonths[monthIndex] else englishMonths[monthIndex]
                            if (isArabic) "$day $monthLabel $year" else "$day $monthLabel $year"
                        } else {
                            if (isArabic) "اختر التاريخ" else "Select date"
                        }

                        Text(
                            text = buildAnnotatedString {
                                headlineText.forEach { char ->
                                    if (char.isDigit()) {
                                        withStyle(
                                            SpanStyle(
                                            localeList = LocaleList("en-US")
                                        )
                                        ) { append(char) }
                                    } else {
                                        append(char)
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
                            fontSize = 28.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },

                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        headlineContentColor = MaterialTheme.colorScheme.onBackground,
                        weekdayContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        navigationContentColor = MaterialTheme.colorScheme.onBackground,
                        dayContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledDayContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        selectedDayContentColor = Color.White,
                        selectedDayContainerColor = Color(0xFF590988),
                        todayContentColor = Color(0xFF590988),
                        todayDateBorderColor = Color(0xFF590988),
                        selectedYearContentColor = Color.White,
                        selectedYearContainerColor = Color(0xFF590988),
                        dividerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    text = if (isArabic) "تأكيد" else "Confirm",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val calendar = Calendar.getInstance(Locale.ENGLISH)
                            calendar.timeInMillis = millis
                            val day = calendar.get(Calendar.DAY_OF_MONTH)
                            val month = calendar.get(Calendar.MONTH) + 1
                            val year = calendar.get(Calendar.YEAR)
                            val formatted = String.format(
                                Locale.ENGLISH,
                                "%02d/%02d/%04d",
                                day, month, year
                            )
                            onDateSelected(formatted)
                            showSheet = false
                        }
                    }
                )
            }
        }
    }
    // ✅ YOUR SAME UI (UNCHANGED)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (enabled) showSheet = true
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (displayDate.isEmpty())
                    stringResource(R.string.dd_mm_yyyy)
                else displayDate,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(R.drawable.calendar_icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}


fun forceEnglishDigits(input: String): String {
    return input.map { char ->
        if (char in '\u0660'..'\u0669') { // Arabic-Indic digits ٠١٢٣٤٥٦٧٨٩
            '0' + (char - '\u0660')
        } else {
            char
        }
    }.joinToString("")
}

fun wrapWithLTR(text: String): String {
    return "\u202A$text\u202C" // LTR embedding marks around the text
}


/*
@Composable
fun CustomDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    enabled: Boolean = true          // 🔥 NEW PARAM
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // ✔ Dialog only launches once
    if (showDatePicker && enabled) {
        LaunchedEffect(Unit) {
            val datePicker = DatePickerDialog(
                context, { _, year, month, dayOfMonth ->
                    val formatted = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                    onDateSelected(formatted)
                    showDatePicker = false
                }, 2000, 0, 1
            )

            datePicker.datePicker.maxDate = System.currentTimeMillis()

            datePicker.setOnDismissListener {
                showDatePicker = false
            }

            datePicker.show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp, Color(0xFFE0E0E0), // 🔥 grey when disabled
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                enabled = enabled,                 // 🔒 BLOCK CLICK
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                if (enabled) showDatePicker = true
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = if (selectedDate.isEmpty()) stringResource(R.string.dd_mm_yyyy) else selectedDate,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )

            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(R.drawable.calendar_icon),
                contentDescription = null,
                colorFilter =  ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)

            )
        }
    }
}
*/

fun showDatePicker(
    context: Context,
    minDate: Long? = null,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance(Locale.ENGLISH) // ✅ Force English locale

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance(Locale.ENGLISH)
            selectedCalendar.set(year, month, dayOfMonth)

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = format.format(selectedCalendar.time)

            // ✅ Double ensure English digits (covers all Arabic variants)
            val englishDate = date.toEnglishDigits()
            Log.d("DatePicker", "Selected: '$date' -> English: '$englishDate'")

            onDateSelected(englishDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // ✅ FIXED: No setLocale() - Use these instead:
    minDate?.let {
        datePicker.datePicker.minDate = it
    }

    // ✅ Optional: Show spinners for better English number display
    datePicker.datePicker.spinnersShown = true
    datePicker.datePicker.firstDayOfWeek = Calendar.SUNDAY // English week start

    datePicker.show()
}


fun convertDateForApii(date: String?): String {
    if (date.isNullOrBlank()) return ""

    return try {
        // ✅ Always convert to English digits first
        val englishDate = date.toEnglishDigits()
        Log.d("convertDateForApi", "Input: '$date' -> English: '$englishDate'")

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        val parsedDate = inputFormat.parse(englishDate)
        parsedDate?.let {
            val result = outputFormat.format(it)
            Log.d("convertDateForApi", "Output: '$result'")
            result
        } ?: ""
    } catch (e: Exception) {
        Log.e("convertDateForApi", "Error: ${e.message}")
        ""
    }
}

fun String.toEnglishDigits(): String {
    val westernArabic = "٠١٢٣٤٥٦٧٨٩"      // Western Arabic
    val easternArabic = "۰۱۲۳۴۵۶۷۸۹"       // Eastern Arabic (Pakistan, India)
    val persian = "۰۱۲۳۴۵۶۷۸۹"            // Persian variant
    val english = "0123456789"

    var result = this

    // Replace all Arabic digit variants
    for (i in english.indices) {
        result = result.replace(westernArabic[i], english[i])
        result = result.replace(easternArabic[i], english[i])
        result = result.replace(persian[i], english[i])
    }

    return result
}
@Composable
fun GradientExpandableCardMultipleSelect(
    title: String, items: List<String>,

    // 🔥 single string → convert into multiple selected list
    selectedItems: List<String>, onSelectionChange: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp, Color(0x1A000000), RoundedCornerShape(20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f),
                        )
                    )
                )
                .clickable { expanded = !expanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // ---- DROPDOWN CONTENT ----
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)               // prevents crash
                    .verticalScroll(rememberScrollState()) // allows inside scroll
                   .background(MaterialTheme.colorScheme.background)
            ) {

                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()

                            .clickable {

                                val current = selectedItems.toMutableList()

                                if (current.contains(item)) {
                                    // unselect
                                    current.remove(item)
                                } else {
                                    // max 10 limit
                                    if (current.size < 10) {
                                        current.add(item)
                                    }
                                }

                                onSelectionChange(current)
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = item,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (selectedItems.contains(item)) Color(0xFF8B5DF6)
                            else Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        // 🔥 MULTIPLE ticks allowed now
                        if (selectedItems.contains(item)) {
                            Icon(
                                painter = painterResource(id = R.drawable.tick_icon),
                                contentDescription = null,
                                tint = Color(0xFF8B5DF6)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Chips(
    text: String, iconUrl: String = "", onClose: () -> Unit,    // this tells parent "remove me"
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = Color.White, RoundedCornerShape(40.dp))
            .border(
                color = Color(0xff590988), width = 1.dp, shape = RoundedCornerShape(40.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClose() }

    ) {

        // Cross icon
        Image(
            painter = painterResource(id = R.drawable.cross_ic),
            contentDescription = "cross",
            modifier = Modifier
                .size(16.dp)
                .padding(end = 8.dp, top = 8.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier
                .padding(start = 7.dp, end = 7.dp, top = 7.dp, bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {

            if (!iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = iconUrl, contentDescription = null, modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(3.dp))
            }

            // Chip text
            Text(
                text = text,
                modifier = Modifier.padding(end = 12.dp),
                color = Color(0xff590988),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )
        }
    }
}

@Composable
fun CommonSelection(
    text: String,
    iconUrl: String = "",
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Color(0xff590988) else Color(0xFFBFBFBF)
    val textColor = if (selected) Color(0xff590988) else Color(0xFF4A4A4A)

    Box(
        modifier = modifier
            .wrapContentSize()
            .border(
                color = borderColor, width = 1.dp, shape = RoundedCornerShape(50.dp)
            )
            .background(Color.White, RoundedCornerShape(50.dp))

            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onToggle() }) {
        // Cross icon now toggles highlight, doesn't remove the chip
        // Cross icon spacing fix
        if (selected) {

            Image(
                painter = painterResource(id = R.drawable.cross_ic),
                contentDescription = "cross",
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 9.dp, top = 8.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.FillBounds
            )
        }

        Row(
            modifier = Modifier.padding(start = 7.dp, end = 7.dp, bottom = 7.dp, top = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(3.dp))
            }

            Text(
                modifier = Modifier.padding(end = if (selected) 13.dp else 0.dp),
                text = text,
                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )
        }
    }
}


/*
@Composable
fun CommonSelection(
    text: String,
    iconUrl: String = "",
    selected: Boolean, onToggle: () -> Unit,     // now it's a toggle, not delete
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Color(0xff590988) else Color(0xFFBFBFBF)
    val textColor = if (selected) Color(0xff590988) else Color(0xFF4A4A4A)

    Box(
        modifier = modifier
            .wrapContentSize()
            .background(Color.White, RoundedCornerShape(40.dp))
            .border(
                color = borderColor, width = 1.dp, shape = RoundedCornerShape(40.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onToggle() }) {

        // Cross icon now toggles highlight, doesn't remove the chip
        if (selected) {
            Image(
                painter = painterResource(id = R.drawable.cross_ic),
                contentDescription = "cross",
                modifier = Modifier
                    .size(14.dp)
                    .padding(end = 10.dp, top = 10.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.FillBounds
            )
        }

        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(20.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = text,
                modifier = Modifier
                    .padding(  bottom = if(selected)2.dp else 0.dp, top =if(selected)2.dp else 0.dp , end= if(selected)2.dp else 2.dp)
                // .padding(vertical = 16.dp, horizontal = 20.dp)
                ,

                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )
        }
    }
}
*/




@Composable
fun SearchBar(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                color = Color(0xFF7C7C7C),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                // light gray like your image
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF7C7C7C)
            )
        },
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = Color(0xFFE0E0E0),
            cursorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.sdp)
    )
}


@Composable
fun GradientExpandableCardFaithSingle(
    title: String,
    faithItems: List<FaithItem>,
    selectedIds: List<String>,       // still list for API
    onSelectionChanged: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f)
                        )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // EXPANDABLE LIST (SINGLE SELECT)
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                faithItems.forEach { item ->
                    val isSelected = selectedIds.firstOrNull() == item.id

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val updated = if (isSelected) emptyList()
                                else listOf(item.id)   // ✅ ONLY ONE ID
                                onSelectionChanged(updated)
                                expanded = false
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = item.title,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (isSelected) Color(0xFF8B5DF6)
                            else  MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )

                        if (isSelected) {
                            Icon(
                                painter = painterResource(id = R.drawable.tick_icon),
                                contentDescription = null,
                                tint = Color(0xFF8B5DF6)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GradientExpandableCardWithLayout(
    title: String, heightValue: String,                 // 👈 user-typed height
    unitValue: String,                   // 👈 "CM" or "FT"
    onHeightChange: (String) -> Unit,    // 👈 send back height
    onUnitChange: (String) -> Unit       // 👈 send back unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f),
                        )
                    )
                )
                .clickable { expanded = !expanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 18.sp,   // <<--- space between wrapped lines

                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // CONTENT
        AnimatedVisibility(expanded) {
            Column {

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Height Text Field
                    OutlinedTextField(
                        value = heightValue,
                        onValueChange = { onHeightChange(it) },   // 👈 send to parent
                        placeholder = { Text(stringResource(R.string.enter_height)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFE0E0E0),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    UnitChip(
                        text = stringResource(R.string.cm),
                        selected = unitValue == "CM",
                        onClick = { onUnitChange("CM") }       // 👈 send to parent
                    )

                    UnitChip(
                        text = stringResource(R.string.ft),
                        selected = unitValue == "FT",
                        onClick = { onUnitChange("FT") }       // 👈 send to parent
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun GradientExpandableCardWithEditText(
    title: String, value: String, onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f),
                        )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 18.sp,   // <<--- space between wrapped lines

                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // Expandable Section
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                OutlinedTextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.write_here),
                            color =  MaterialTheme.colorScheme.onBackground
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFDDDDDD),
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        cursorColor = MaterialTheme.colorScheme.onBackground
                    ),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    )
                )
            }
        }
    }
}

val items = listOf(
    "Poetry",
    "Music",
    "Lorem",
    "Dance",
    "Travel",
    "Food",
    "Gaming",
    "Art",
    "Travel",
    "Food",
    "Gaming",
    "Art"
)

@Composable
fun GradientExpandableCardWithMultipleSelect(
    title: String, selectedItems: List<String>, onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color(0x1A000000), RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f)
                        )
                    )
                )
                .clickable { expanded = !expanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        AnimatedVisibility(expanded) {
            Column {
                Spacer(Modifier.height(12.dp))

                FlowRow(
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(25.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items.forEach { label ->
                        CommonSelection(
                            text = label,
                            selected = selectedItems.contains(label),
                            onToggle = { onItemSelected(label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun UnitChip(
    text: String, selected: Boolean, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(42.dp))
            .background(
                if (selected) Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF7D4DFF), Color(0xFFEB8BD3)
                    )
                )
                else Brush.verticalGradient(
                    listOf(Color.White, Color.White)
                )
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)) {
        Text(
            text = text, color = if (selected) Color.White else Color.Black, fontSize = 14.sp
        )
    }
}

fun getUserCountry(context: Context): String {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    // SIM-based country code
    val simCountry = tm.simCountryIso
    if (!simCountry.isNullOrEmpty()) {
        return simCountry.uppercase()
    }

    // Network-based country code
    val networkCountry = tm.networkCountryIso
    if (!networkCountry.isNullOrEmpty()) {
        return networkCountry.uppercase()
    }

    // Fallback to device locale
    return Locale.getDefault().country.uppercase()
}
fun getUserCountryLogin(context: Context): String {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val simCountry = tm.simCountryIso
    if (!simCountry.isNullOrEmpty()) return simCountry.lowercase()

    val networkCountry = tm.networkCountryIso
    if (!networkCountry.isNullOrEmpty()) return networkCountry.lowercase()

    return Locale.getDefault().country.lowercase()
}

@Composable
fun CustomDialog(
    id: Int, text1: String? = null, text2: String? = null, onDismiss: () -> Unit = {}, appBtn:Boolean=false,btnText:String=""
) {

    Dialog(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                .padding(vertical = 40.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = text1 ?: "",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )
if(!text2.isNullOrEmpty()) {
    Spacer(modifier = Modifier.height(12.dp))
}
            Text(
                text = text2 ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            if(appBtn){
                verticalSpace(25)
            AppButton(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp), text = btnText, onClick = {onDismiss()})}
        }
    }
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}


@Composable
fun GradientExpandableCardMultipleSelectIndex(
    title: String, items: List<String>,

    // 🔥 List of SELECTED INDEXES as STRING, e.g. ["0","2","5"]
    selectedIndexes: List<String>, onSelectionChange: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color(0x1A000000), RoundedCornerShape(20.dp))
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f)
                        )
                    )
                )
                .clickable { expanded = !expanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // DROPDOWN CONTENT
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                items.forEachIndexed { index, item ->

                    val indexStr = index.toString()   // 👈 convert int → string

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                val current = selectedIndexes.toMutableList()

                                if (current.contains(indexStr)) {
                                    // unselect
                                    current.remove(indexStr)
                                } else {
                                    // max 10 limit
                                    if (current.size < 10) {
                                        current.add(indexStr)
                                    }
                                }

                                onSelectionChange(current)
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = item,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (selectedIndexes.contains(indexStr)) Color(0xFF8B5DF6)
                            else Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        if (selectedIndexes.contains(indexStr)) {
                            Icon(
                                painter = painterResource(id = R.drawable.tick_icon),
                                contentDescription = null,
                                tint = Color(0xFF8B5DF6)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GradientExpandableCardWithMultipleSelectApi(
    title: String,
    faithItems: List<FaithItem>,
    selectedIds: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color(0x1A000000), RoundedCornerShape(20.dp))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f)
                        )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                lineHeight = 18.sp,   // <<--- space between wrapped lines

                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        AnimatedVisibility(expanded) {
            Column {
                Spacer(Modifier.height(12.dp))

                FlowRow(
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    faithItems.forEach { item ->
                        val selected = selectedIds.contains(item.id)

                        CommonSelection(
                            text = item.title, selected = selected, onToggle = {
                                val updated = if (selected) selectedIds - item.id
                                else selectedIds + item.id

                                onSelectionChanged(updated)
                            })
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun GradientExpandableCardMultipleSelectIndexSearch(
    title: String, items: List<String>,
    // List of SELECTED INDEXES as STRING, e.g. ["0","2","5"]
    selectedIndexes: List<String>, onSelectionChange: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8B5DF6).copy(alpha = 0.2f),
                            Color(0xFFF6A6D6).copy(alpha = 0.2f)
                        )
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 18.sp,   // <<--- space between wrapped lines
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_top_ic),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.rotate(if (expanded) 0f else 180f)
            )
        }

        // DROPDOWN CONTENT
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                var search by rememberSaveable { mutableStateOf("") }

                SearchBar(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
                )

                // pre-compute last index ONCE
                val lastIndex = items.lastIndex

                val filteredItems = remember(search, items) {
                    val indexedItems = items.withIndex().toList()

                    if (search.isBlank()) {
                        indexedItems
                    } else {
                        prioritizeSearch(
                            indexedItems, search
                        ) { it.value }
                    }
                }


                if (filteredItems.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.language_not_found),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color =  Color(0xFF6D6D6D)
                        )
                    }

                } else {

                    filteredItems.forEach { indexedValue ->
                        val index = indexedValue.index
                        val item = indexedValue.value
                        val indexStr = index.toString()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val current = selectedIndexes.toMutableList()

                                    if (current.contains(indexStr)) {
                                        current.remove(indexStr)
                                    } else {
                                        if (current.size < 10) {
                                            current.add(indexStr)
                                        }
                                    }

                                    onSelectionChange(current)
                                }
                                .padding(horizontal = 15.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                text = item,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = if (selectedIndexes.contains(indexStr)) Color(0xFF8B5DF6)
                                else MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )

                            if (selectedIndexes.contains(indexStr)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.tick_icon),
                                    contentDescription = null,
                                    tint = Color(0xFF8B5DF6)
                                )
                            }
                        }
                    }
                }
                // 🔥 Show the extra TextField BELOW the list
                // when the LAST item is selected
//                if (selectedIndexes.contains(items.lastIndex.toString())) {
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    TextField(
//                        value = language,
//                        onValueChange = { language = it },
//                        placeholder = {
//                            Text(
//                                "Enter Other Language",
//                                style = TextStyle(
//                                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
//                                    fontSize = 14.sp,
//                                ),
//                                color = Color(0xFF6D6D6D),
//                                textAlign = TextAlign.Start
//                            )
//                        },
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Text,
//                            imeAction = ImeAction.Done
//                        ),
//                        singleLine = true,
//                        shape = RoundedCornerShape(14.dp),
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.Transparent,
//                            unfocusedContainerColor = Color.Transparent,
//                            disabledContainerColor = Color.Transparent,
//                            errorContainerColor = Color.Transparent,
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent,
//                            disabledIndicatorColor = Color.Transparent,
//                            errorIndicatorColor = Color.Transparent,
//                            cursorColor = colorResource(R.color.black)
//                        ),
//                        textStyle = TextStyle(
//                            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
//                            fontSize = 14.sp,
//                            color = colorResource(R.color.black)
//                        ),
//                        modifier = Modifier
//                            .padding(horizontal = 15.dp, vertical = 8.dp)
//                            .fillMaxWidth()
//                            .background(color = Color.Transparent, RoundedCornerShape(14.dp))
//                            .border(width = 1.dp, color = Color(0xff33000000), shape = RoundedCornerShape(14.dp),)
//                    )
//                }
            }
        }
    }
}

fun showImagePickerDialog(
    context: Context, onCamera: () -> Unit, onGallery: () -> Unit
) {
    val options = arrayOf("Camera", "Gallery")
    AlertDialog.Builder(context).setTitle("Select Image From").setItems(options) { _, which ->
        when (which) {
            0 -> onCamera()
            1 -> onGallery()
        }
    }.show()
}


data class ChatItem(val index: Int, val text: String)

val chatList = listOf(
    ChatItem(0, "Chat"), ChatItem(1, "Profile Views")
)

@Composable
fun ChatStickyChips(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onChipClick: (Int) -> Unit,
    chatList: List<ChatItem>
) {
    val interactionSource = remember { MutableInteractionSource() }
    val axiformaFont = remember {
        FontFamily(Font(R.font.axiforma_medium))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        chatList.forEach { item ->

            val isSelected = item.index == selectedIndex

            Text(
                text = item.text,
                fontFamily = axiformaFont,
                textAlign = TextAlign.Center,
                color = if (isSelected) Color.White else Color(0xFF590988),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(42.dp))
                    .clickable(
                        indication = null, interactionSource = interactionSource
                    ) {
                        if (selectedIndex != item.index) {
                            onChipClick(item.index)
                        }
                    }
                    .background(
                        brush = if (isSelected) Brush.linearGradient(
                            listOf(Color(0xFF8B5DF6), Color(0xFFF6A6D6))
                        )
                        else Brush.linearGradient(
                            listOf(Color.White, Color.White)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 15.dp))
        }
    }
}


@Composable
fun VoiceMessageItem(
    profileImage: Int, duration: String, time: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .background(
                    color = Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {

            // 🔹 Profile Image
            AsyncImage(
                model = profileImage,
                contentDescription = "profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🔹 Play Button
            Icon(
                painter = painterResource(R.drawable.play_ic),
                contentDescription = "play",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            // 🔹 Duration
            Text(
                text = duration, fontSize = 12.sp, color = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🔹 Fake Waveform (placeholder)
            Image(
                painter = painterResource(R.drawable.audio_line),
                contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .width(120.dp)
            )
        }

        // 🔹 Custom Time (Outside Bubble)
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = time, // ⬅️ CUSTOM TIME
            fontSize = 12.sp, color = Color(0xFF9CA3AF), modifier = Modifier.padding(start = 44.dp)
        )
    }
}

/*
@Composable
fun FullScreenImagePreview(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Image Preview",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

*/

@Composable
fun ActionRowItem(
    item: ActionItem, isSelected: Boolean, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp, color = if (isSelected) Color.Red
                else Color(0xFFB388FF), shape = RoundedCornerShape(12.dp)
            )

            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {

        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            modifier = Modifier.size(16.dp)
        )

        horizontalSpace(7)
        Text(
            text = item.title,
            fontSize = 14.sp,
            color = Color(0xFF8B5DF6),
            fontFamily = FontFamily(Font(R.font.axiforma_medium))
        )
    }
}

@Composable
fun SenderMessage(img: String?, msg: String, time: String, maxWidth: Dp) {
    Row(
        modifier = Modifier.width(maxWidth * 0.8f)
    ) {
        AsyncImage(
            model = img,
            contentDescription = "profile pic",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        horizontalSpace(5)

        Column() {
            Text(
                modifier = Modifier
                    .background(
                        color = Color(0xFFF5F5F5), shape = RoundedCornerShape(
                            topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
                        )
                    )
                    .padding(12.dp),
                text = msg,
                color = Color(0xFF262324),
                fontSize = 14.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )

            verticalSpace(8)
            Text(
                modifier = Modifier,
                text = time,
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )

        }


    }
}

@Composable
fun ReceiverMessage(
    msg: String,
    time: String,
    maxWidth: Dp,
    isRead: String?,
    onDeleteClick: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val deleteButtonWidth = 50.dp
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ✅ Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                coroutineScope.launch { offsetX.animateTo(0f) }
            },
                    containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = stringResource(R.string.delete_message),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 16.sp,
                    color = Color(0xFF262324),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.are_you_sure_you_want_to_delete_this_message_for_everyone),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color(0xFF6B7280),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        // Delete button revealed on swipe
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            val showDelete = offsetX.value < -10f
            if (showDelete) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFF4444), shape = CircleShape)
                        .clickable {
                            showDeleteDialog = true // ✅ show dialog instead of direct delete
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Message bubble with drag
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -deleteButtonWidthPx / 2) {
                                    offsetX.animateTo(
                                        -deleteButtonWidthPx,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                } else {
                                    offsetX.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newValue = (offsetX.value + dragAmount)
                                    .coerceIn(-deleteButtonWidthPx, 0f)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                }
        ) {
            Box(
                modifier = Modifier.width(maxWidth * 0.8f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = msg,
                        color = Color(0xFF262324),
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )
                    if (!isRead.isNullOrEmpty() && isRead != "null") {
                        Image(
                            painter = painterResource(R.drawable.double_tick_green_ic),
                            contentDescription = null,
                            modifier = Modifier.size(width = 15.dp, height = 8.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.tick_icon),
                            contentDescription = null,
                            modifier = Modifier.size(width = 15.dp, height = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = time,
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )
        }
    }
}

@Composable
fun SenderImage(
    img: String, time: String, onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {

        AsyncImage(
            model = img,
            contentDescription = "sent image",
            placeholder = ColorPainter(Color.Gray),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .width(198.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() })

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = time, color = Color(0xFF9CA3AF), fontSize = 12.sp
        )
    }
}

@Composable
fun ReceiverImage(
    img: String,
    time: String,
    isRead: String?,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val deleteButtonWidth = 50.dp
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ✅ Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                coroutineScope.launch { offsetX.animateTo(0f) }
            },
                    containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = stringResource(R.string.delete_message),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 16.sp,
                    color = Color(0xFF262324),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.are_you_sure_you_want_to_delete_this_message_for_everyone),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color(0xFF6B7280),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        // ✅ Delete dustbin button revealed on swipe
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (offsetX.value < -10f) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFF4444), shape = CircleShape)
                        .clickable {
                            showDeleteDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // ✅ Image bubble with drag gesture
        Column(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -deleteButtonWidthPx / 2) {
                                    offsetX.animateTo(
                                        -deleteButtonWidthPx,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                } else {
                                    offsetX.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newValue = (offsetX.value + dragAmount)
                                    .coerceIn(-deleteButtonWidthPx, 0f)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                },
            horizontalAlignment = Alignment.End
        ) {
            AsyncImage(
                model = img,
                contentDescription = "received image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .width(198.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onClick() },
                placeholder = ColorPainter(Color.Gray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = time,
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )

                horizontalSpace(5)
                if (!isRead.isNullOrEmpty() && isRead != "null") {
                    Image(
                        painter = painterResource(R.drawable.double_tick_green_ic),
                        contentDescription = null,
                        modifier = Modifier.size(width = 15.dp, height = 8.dp)
                    )
                } else {

                    Image(
                        painter = painterResource(R.drawable.tick_icon),
                        contentDescription = null,
                        modifier = Modifier.size(width = 15.dp, height = 8.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeFromUtc(createdAt: String): String {
    val instant = Instant.parse(createdAt)

    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
        .withZone(ZoneId.systemDefault())

    return formatter.format(instant)
}

fun formatSeconds(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return String.format("%02d:%02d", min, sec)
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AudioWave(
    modifier: Modifier,
    recorder: MediaRecorder?,
    isRecording: Boolean
) {
    val bars = remember { mutableStateListOf<Int>() }

    LaunchedEffect(isRecording, recorder) {
        if (isRecording && recorder != null) {
            delay(200)
            while (isRecording) {
                val amp = try {
                    recorder.maxAmplitude
                } catch (e: IllegalStateException) { 0 }

                // Normalize to canvas height range (4dp min, 30dp max)
                val normalized = if (amp > 0) {
                    val ratio = (amp / 32767f).coerceIn(0f, 1f)
                    // Apply sqrt for more natural-looking wave (boosts quiet sounds)
                    val boosted = Math.sqrt(ratio.toDouble()).toFloat()
                    (boosted * 30f).toInt().coerceAtLeast(4)
                } else {
                    4 // flat bar when silent
                }

                bars.add(normalized)
                if (bars.size > 80) bars.removeAt(0)
                delay(80)
            }
        }
        bars.clear()
    }

    val barColor = Color(0xFFF6A6D6)
    val barWidth = 4.dp
    val barSpacing = 2.dp

    Canvas(modifier = modifier
        .height(40.dp)
        .fillMaxWidth()) {
        val barWidthPx = barWidth.toPx()
        val spacingPx = barSpacing.toPx()
        val stepPx = barWidthPx + spacingPx
        val centerY = size.height / 2f

        // Calculate how many bars fit in the available canvas width
        val maxBars = (size.width / stepPx).toInt()

        // Take only the latest bars that fit
        val visibleBars = if (bars.size > maxBars) bars.takeLast(maxBars) else bars

        visibleBars.forEachIndexed { index, heightDp ->
            val left = index * stepPx
            val halfH = (heightDp.dp.toPx() / 2f).coerceAtMost(size.height / 2f)

            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, centerY - halfH),
                size = Size(barWidthPx, halfH * 2f),
                cornerRadius = CornerRadius(2.dp.toPx())
            )
        }
    }



}


@Composable
fun AudioMessagePlayerSender(
    audioUrl: String,
    audioVM: ChatAudioViewModel,
    modifier: Modifier = Modifier,
    duration: Long,
    img: String?
) {
    val isThisPlaying = audioVM.currentUrl == audioUrl && audioVM.isPlaying

    val progress =
        if (audioVM.duration > 0 && audioVM.currentUrl == audioUrl) audioVM.position / audioVM.duration.toFloat()
        else 0f

    LaunchedEffect(isThisPlaying) {
        while (isThisPlaying) {
            audioVM.updateProgress()
            delay(300)
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start
    ) {
        Row {


            Row(
                modifier = Modifier
            ) {
                AsyncImage(
                    model = img,
                    contentDescription = "profile pic",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                horizontalSpace(5)

            }
            Row(
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(
                        if (isThisPlaying) R.drawable.cross_pruple_ic
                        else R.drawable.play_ic
                    ), contentDescription = null, modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            audioVM.toggle(audioUrl)
                        })

                Spacer(Modifier.width(12.dp))

                AudioWaveProgress(
                    progress = progress, modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = if (audioVM.currentUrl == audioUrl) "${formatMillis(audioVM.position)} / ${
                        formatMillis(
                            duration
                        )
                    }"
                    else {
                        formatMillis(duration)
                    }, fontSize = 12.sp, color = Color.Gray
                )

            }

        }


    }
}

@Composable
fun AudioMessagePlayerReceiver(
    audioUrl: String,
    audioVM: ChatAudioViewModel,
    modifier: Modifier = Modifier,
    duration: Long,
    time:String,
    isRead:String?,
    onDeleteClick: () -> Unit
) {
    val isThisPlaying = audioVM.currentUrl == audioUrl && audioVM.isPlaying

    val progress =
        if (audioVM.duration > 0 && audioVM.currentUrl == audioUrl) audioVM.position / audioVM.duration.toFloat()
        else 0f

    LaunchedEffect(isThisPlaying) {
        while (isThisPlaying) {
            audioVM.updateProgress()
            delay(300)
        }
    }

    val offsetX = remember { Animatable(0f) }
    val deleteButtonWidth = 50.dp
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ✅ Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                coroutineScope.launch { offsetX.animateTo(0f) }
            },
                    containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = stringResource(R.string.delete_message),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 16.sp,
                    color = Color(0xFF262324),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.are_you_sure_you_want_to_delete_this_message_for_everyone),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color(0xFF6B7280),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            }
        )
    }


    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            // ✅ Delete dustbin button revealed on swipe
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp, bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                if (offsetX.value < -10f) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFF4444), shape = CircleShape)
                            .clickable {
                                showDeleteDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // ✅ Audio bubble with drag gesture
            // ✅ Audio bubble with drag gesture
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    if (offsetX.value < -deleteButtonWidthPx / 2) {
                                        offsetX.animateTo(
                                            -deleteButtonWidthPx,
                                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                        )
                                    } else {
                                        offsetX.animateTo(
                                            0f,
                                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                        )
                                    }
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                coroutineScope.launch {
                                    val newValue = (offsetX.value + dragAmount)
                                        .coerceIn(-deleteButtonWidthPx, 0f)
                                    offsetX.snapTo(newValue)
                                }
                            }
                        )
                    },
                horizontalAlignment = Alignment.End
            ) {

                // 🔊 Audio player row
                Row(
                    modifier = modifier
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            if (isThisPlaying) R.drawable.cross_pruple_ic
                            else R.drawable.play_ic
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                audioVM.toggle(audioUrl)
                            }
                    )

                    Spacer(Modifier.width(8.dp))

                    AudioWaveProgress(
                        progress = progress,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = if (audioVM.currentUrl == audioUrl)
                            "${formatMillis(audioVM.position)} / ${formatMillis(duration)}"
                        else formatMillis(duration),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // ✅ Time + Tick below bubble (same as ReceiverImage)
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text = time,
                        color = Color(0xFF9CA3AF),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )

                    horizontalSpace(5)

                    if (!isRead.isNullOrEmpty() && isRead != "null") {
                        Image(
                            painter = painterResource(R.drawable.double_tick_green_ic),
                            contentDescription = null,
                            modifier = Modifier.size(width = 15.dp, height = 8.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.tick_icon),
                            contentDescription = null,
                            modifier = Modifier.size(width = 15.dp, height = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioWaveProgress(
    progress: Float, modifier: Modifier = Modifier
) {
    val bars = remember { List(40) { (6..30).random().toFloat() } }

    Canvas(
        modifier = modifier.height(32.dp)
    ) {
        val totalBars = bars.size
        val barWidth = (size.width / totalBars) * 0.6f   // bar width
        val gapWidth = (size.width / totalBars) * 0.4f   // gap between bars
        val centerY = size.height / 2

        bars.forEachIndexed { index, barHeight ->
            val played = index / totalBars.toFloat() <= progress
            val barHeightPx = (barHeight / 30f) * size.height  // normalize to canvas height

            val x = index * (barWidth + gapWidth) + gapWidth / 2

            drawRoundRect(
                color = if (played) Color(0xFF8B5DF6) else Color.LightGray,
                topLeft = Offset(x, centerY - barHeightPx / 2),
                size = Size(barWidth, barHeightPx),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

@Composable
fun SenderVideo(
    videoUrl: String, time: String, context: Context, onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .width(198.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }) {
            // Thumbnail or first frame
            AsyncImage(
                model = ImageRequest.Builder(context).data(videoUrl)
                    .decoderFactory(VideoFrameDecoder.Factory()).build(),

                contentDescription = "sent video",
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.Gray),

                modifier = Modifier.fillMaxSize()
            )

            // Play icon overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.play_ic),
                    contentDescription = "play video",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = time,
            color = Color(0xFF9CA3AF),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_regular))
        )
    }
}

@Composable
fun ReceiverVideo(
    videoUrl: String,
    time: String,
    context: Context,
    isRead:String?,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val deleteButtonWidth = 50.dp
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ✅ Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                coroutineScope.launch { offsetX.animateTo(0f) }
            },
                    containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = stringResource(R.string.delete_message),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 16.sp,
                    color = Color(0xFF262324),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.are_you_sure_you_want_to_delete_this_message_for_everyone),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color(0xFF6B7280),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        // ✅ Delete dustbin button revealed on swipe
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (offsetX.value < -10f) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFF4444), shape = CircleShape)
                        .clickable {
                            showDeleteDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // ✅ Video bubble with drag gesture
        Column(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -deleteButtonWidthPx / 2) {
                                    offsetX.animateTo(
                                        -deleteButtonWidthPx,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                } else {
                                    offsetX.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newValue = (offsetX.value + dragAmount)
                                    .coerceIn(-deleteButtonWidthPx, 0f)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                },
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .width(198.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onClick() }
            ) {
                // Thumbnail / first frame
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(videoUrl)
                        .decoderFactory(VideoFrameDecoder.Factory())
                        .build(),
                    contentDescription = "received video",
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color.Gray),
                    modifier = Modifier.fillMaxSize()
                )

                // Play icon overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.play_ic),
                        contentDescription = "play video",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
    Text(
        text = time,
        color = Color(0xFF9CA3AF),
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular))
    )

    horizontalSpace(5)

    if (!isRead.isNullOrEmpty() && isRead != "null") {
        Image(
            painter = painterResource(R.drawable.double_tick_green_ic),
            contentDescription = null,
            modifier = Modifier.size(width = 15.dp, height = 8.dp)
        )
    } else {
        Image(
            painter = painterResource(R.drawable.tick_icon),
            contentDescription = null,
            modifier = Modifier.size(width = 15.dp, height = 8.dp)
        )
    }

}




        }
    }
}

@Composable
fun FullScreenVideoPlayer(
    videoUrl: String, onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { /* Prevent click-through */ }) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                }
            }, modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        // Close button
        Image(
            painter = painterResource(R.drawable.cross_pruple_ic),
            contentDescription = "close",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 16.dp, vertical = (40.dp))
                .size(32.dp)
                .clickable {
                    exoPlayer.pause()
                    onDismiss()
                })
    }

    LaunchedEffect(Unit) {
        exoPlayer.play()
    }
}


@Composable
fun FullScreenImagePreview(
    imageUrl: String, onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "full screen image",
            contentScale = ContentScale.Fit,
            placeholder = ColorPainter(Color.Gray),

            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)

                        if (scale > 1f) {
                            offsetX += pan.x
                            offsetY += pan.y
                        } else {
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (scale > 1f) {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                            } else {
                                scale = 2f
                            }
                        })
                }
                .graphicsLayer(
                    scaleX = scale, scaleY = scale, translationX = offsetX, translationY = offsetY
                ))

        // Close button
        Image(
            painter = painterResource(R.drawable.cross_pruple_ic),
            contentDescription = "close",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 16.dp, vertical = (40.dp))
                .size(32.dp)
                .clickable { onDismiss() })
    }
}


@Composable
fun SenderMultipleMedia(
    mediaUrls: List<String>, time: String, onClick: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        MultipleMediaGrid(
            mediaUrls = mediaUrls, onClick = onClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = time,
            color = Color(0xFF9CA3AF),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_regular))
        )
    }
}

@Composable
fun ReceiverMultipleMedia(
    mediaUrls: List<String>,
    time: String,
    onClick: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val deleteButtonWidth = 50.dp
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ✅ Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                coroutineScope.launch { offsetX.animateTo(0f) }
            },
                    containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = stringResource(R.string.delete_message),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 16.sp,
                    color = Color(0xFF262324),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.are_you_sure_you_want_to_delete_this_message_for_everyone),
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch { offsetX.animateTo(0f) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color(0xFF6B7280),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        fontSize = 14.sp
                    )
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        // ✅ Delete dustbin button revealed on swipe
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            val showDelete = offsetX.value < -10f
            if (showDelete) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFF4444), shape = CircleShape)
                        .clickable {
                            showDeleteDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // ✅ Media bubble with drag gesture
        Column(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -deleteButtonWidthPx / 2) {
                                    offsetX.animateTo(
                                        -deleteButtonWidthPx,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                } else {
                                    offsetX.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newValue = (offsetX.value + dragAmount)
                                    .coerceIn(-deleteButtonWidthPx, 0f)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                },
            horizontalAlignment = Alignment.End
        ) {
            MultipleMediaGrid(
                mediaUrls = mediaUrls,
                onClick = onClick
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = time,
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )
        }
    }
}

@Composable
fun MultipleMediaGrid(
    mediaUrls: List<String>, onClick: (Int) -> Unit
) {
    val maxVisible = 4
    val visibleUrls = mediaUrls.take(maxVisible)
    val extraCount = mediaUrls.size - maxVisible

    Row(
        modifier = Modifier
            .height(100.dp)
            .width(200.dp)  // Adjust width as needed
            .clip(RoundedCornerShape(12.dp)), horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        visibleUrls.forEachIndexed { index, url ->
            val isLastVisible = index == visibleUrls.lastIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onClick(index) }) {
                AsyncImage(
                    model = url,
                    contentDescription = "media thumbnail",
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color.Gray),

                    modifier = Modifier.fillMaxSize()
                )
                if (isVideoUrl(url)) {
                    // Play icon for videos
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.play_ic),
                            contentDescription = "play",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                if (isLastVisible && extraCount > 0) {
                    // Overlay for extra count
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+$extraCount",
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MultipleMediaPreview(
    mediaUrls: List<String>, startIndex: Int, onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = startIndex) { mediaUrls.size }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            val url = mediaUrls[page]
            if (isVideoUrl(url)) {
                // Video preview (reuse your logic)
                FullScreenVideoPlayer(videoUrl = url, onDismiss = {})  // Disable inner dismiss
            } else {
                // Image preview (reuse your logic)
                FullScreenImagePreview(imageUrl = url, onDismiss = {})  // Disable inner dismiss
            }
        }

        // Close button
        Image(
            painter = painterResource(R.drawable.cross_pruple_ic),
            contentDescription = "close",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 16.dp, vertical = 40.dp)
                .size(32.dp)
                .clickable { onDismiss() })

        // Page indicator (optional)
        Text(
            text = "${pagerState.currentPage + 1}/${mediaUrls.size}",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}


@Composable
fun MatchDialog(
    user1: MatchUser,
    user2: MatchUser,
    buttonText: String = stringResource(R.string.say_hello),
    topHeart: Int,
    bottomHeart: Int,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(25.dp))
                .wrapContentHeight().background(color = MaterialTheme.colorScheme.background)
        ) {

            // 🔥 1. FULL BACKGROUND RING (FULL SIZE)
            Image(
                painter = painterResource(R.drawable.match_pop_up_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )


            // 🎉 2. CELEBRATION BG (TOP ONLY)
            Image(
                painter = painterResource(R.drawable.match_dialog_celebration_bg),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            // 🧾 3. MAIN CONTENT SURFACE (WHITE CARD LOOK)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(24.dp))

            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // 🔥 STACKED IMAGES (UNCHANGED)
                    MatchStackedProfiles(
                        user1,
                        user2,
                        topHeart = topHeart,
                        bottomHeart = bottomHeart
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Image(painter = painterResource(R.drawable.ring), contentDescription = null,modifier=Modifier.size(100.dp))

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.alhamdulillah),
                        fontSize = 14.sp,
                        color = Color(0xFF530386),
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.it_s_a_matchh),
                        fontSize = 20.sp,
                        color = Color(0xFF000000),
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(modifier = Modifier.fillMaxWidth(),
                        text = buildString {
                            append(stringResource(R.string.you_both_showed_interest))
                            append(stringResource(R.string.begin_with_respect_and_intention))
                        },
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF6D6D6D),
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    AppButton(
                        text = stringResource(R.string.say_salam),
                        onClick = { onClick() }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(52.dp))
                            .border(1.dp, Color(0xFF590988), shape = RoundedCornerShape(52.dp))
                            .clickable {
                                onDismiss()
                            }
                            .padding(vertical = 16.dp),
                        text = stringResource(R.string.keep_exploring),
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color(0xFF590988),
                        textAlign = TextAlign.Center
                    )
verticalSpace(10)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.and_among_his_signs_he_created_for_you),
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color(0xFF590988),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }}


@Composable
fun MatchStackedProfiles(
    user1: MatchUser, user2: MatchUser, topHeart: Int, bottomHeart: Int
) {
    val layoutDirection = LocalLayoutDirection.current
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(top = 25.sdp),
        contentAlignment = Alignment.TopCenter
    ) {

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

            PolaroidCard(
                user = user1,
                rotation = -15f,
                modifier = Modifier
                    .offset(x = (-48).dp, y = 12.dp)
                    .zIndex(1f),
                showHeartBottom = true,
                topHeart = topHeart,
                bottomHeart = bottomHeart
            )

            PolaroidCard(
                user = user2,
                rotation = 15f,
                modifier = Modifier
                    .offset(x = 50.dp, y = 12.dp)
                    .zIndex(0f),
                showHeart = true,
                topHeart = topHeart,
                bottomHeart = bottomHeart
            )
        }
    }
}

@Composable
fun PolaroidCard(
    user: MatchUser,
    rotation: Float,
    modifier: Modifier = Modifier,
    topHeart: Int,
    bottomHeart: Int,
    showHeart: Boolean = false,
    showHeartBottom: Boolean = false
) {
    Box(
        modifier = modifier
            .size(width = 110.dp, height = 142.dp)
            .rotate(rotation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(user.imageUrl)
                    .crossfade(true).build(),
                contentDescription = user.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.no_dp_icon),
                error = painterResource(R.drawable.no_dp_icon),
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
            )


            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = user.name, fontSize = 14.sp, color = Color(0xFF6A1B9A), maxLines = 1
            )
        }

        // ❤️ Bottom heart
        /*if (showHeartBottom) {
            Image(
                painter = painterResource(bottomHeart),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-18).dp, y = 20.dp)
            )
        }

        // ❤️ Top heart
        if (showHeart) {
            Image(
                painter = painterResource(topHeart),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (-18).dp, y = (-15).dp)
            )
        }*/
    }
}


@Composable
fun MatchDialogPhotoOnly(
    modifier: Modifier = Modifier,
    user1: MatchUser,
    user2: MatchUser,
    topHeart: Int,
    bottomHeart: Int,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFAF3FB))
                .padding(20.dp)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(10.dp))

                // 🔥 STACKED IMAGES
                MatchStackedProfiles(
                    user1, user2, topHeart = topHeart, bottomHeart = bottomHeart
                )


            }
        }
    }
}


@Composable
fun ShutterWithGifAnd70Logics(
    viewModel: M4ViewModel, authViewModel: AuthViewModel, data: GetMatchResponse.Data?
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var apiCalled by remember { mutableStateOf(false) }

    val imageLoader = remember {
        ImageLoader.Builder(context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    }

    val screenHeight = configuration.screenHeightDp.dp
    val screenHeightPx = with(LocalDensity.current) { screenHeight.toPx() }

    val shutterHeight = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    var isDragging by remember { mutableStateOf(false) }

    var showGif by remember { mutableStateOf(false) }
    var gifPlayed by remember { mutableStateOf(false) }

    val GIF_DURATION = 2000L
    val threshold = screenHeightPx * 0.20f

    LaunchedEffect(showGif) {
        if (showGif && !apiCalled) {
            apiCalled = true

            viewModel.hitAction(
                access_token = SharedPreference.get(context).accessToken, request = ActionRequest(
                    action = "reject", toUserId = data?.userId
                )
            )

            authViewModel.hitGetMatch(
                accessToken = SharedPreference.get(context).accessToken,
                filter = viewModel.currentFilterRequest.value
            )
        }
    }

    LaunchedEffect(showGif) {
        if (showGif) {
            delay(GIF_DURATION)
            showGif = false
            gifPlayed = false
            apiCalled = false
            shutterHeight.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.TopCenter)
                .pointerInput(Unit) {

                    detectVerticalDragGestures(

                        onDragStart = {
                            isDragging = true
                        },

                        onVerticalDrag = { change, dragAmount ->
                            change.consume()

                            val updated =
                                (shutterHeight.value + dragAmount).coerceIn(0f, screenHeightPx)

                            scope.launch {
                                shutterHeight.snapTo(updated)
                            }
                        },

                        onDragEnd = {
                            isDragging = false

                            val current = shutterHeight.value

                            if (current < threshold) {

                                scope.launch {
                                    shutterHeight.animateTo(
                                        0f, animationSpec = tween(350, easing = FastOutSlowInEasing)
                                    )
                                }
                            } else {

                                scope.launch {
                                    shutterHeight.animateTo(
                                        screenHeightPx,
                                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                                    )
                                }

                                if (!gifPlayed) {
                                    gifPlayed = true
                                    showGif = true
                                }
                            }
                        })
                })

        // 🔹 Shutter Layer (same as before)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { shutterHeight.value.toDp() })
                .background(Color.Black.copy(alpha = 0.85f))
        )

        // 🔹 GIF overlay (same as before)
        if (showGif) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {

                /*AnimatedPreloaderReject(
                    modifier = Modifier.size(1000.dp)

                )*//*   authViewModel.hitGetMatch(
                       accessToken = SharedPreference.get(context).accessToken,
                       filter = viewModel.currentFilterRequest.value
                   )

                   viewModel.hitAction(
                       access_token = SharedPreference.get(context).accessToken,
                       request = ActionRequest(
                           action = "reject",
                           toUserId = data?.userId
                       )
                   )*/


                /* Image(
                     painter = rememberAsyncImagePainter(
                         model = R.raw.cross, imageLoader = imageLoader
                     ), contentDescription = null, modifier = Modifier.size(180.dp)
                 )*/


            }
        }
    }
}


@Composable
fun AnimatedPreloaderReject(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.cross
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition, iterations = LottieConstants.IterateForever, isPlaying = true
    )

    LottieAnimation(
        composition = preloaderLottieComposition, progress = preloaderProgress, modifier = modifier
    )
}

@Composable
fun AnimatedPreloaderSuperLike(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.star
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition, iterations = LottieConstants.IterateForever, isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition, progress = preloaderProgress, modifier = modifier
    )
}


@Composable
fun AnimatedPreloaderLike(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.heart
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition, iterations = LottieConstants.IterateForever, isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition, progress = preloaderProgress, modifier = modifier
    )
}


//for moderate both

@Composable
fun MultiImagePicker(
    viewModel: AuthViewModel, maxImages: Int = 5, onImagesSelected: (List<Uri>) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // -------------------------------------------------------
    // 1️⃣ Multi Image Picker (Gallery)
    // -------------------------------------------------------
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxImages), onResult = { uris ->

            selectedImages = uris
            onImagesSelected(uris)

            uris.forEach { uri ->
                val imageFile = uriToFile(context, uri)

                imageFile?.let { file ->

                    val maxFileSize = 10 * 1024 * 1024 // 10MB

                    if (file.length() > maxFileSize) {
                        ErrorUtil.showErrorDialog(
                            context, "Image size exceeds 10MB. Please select a smaller image."
                        )
                        return@forEach
                    }

                    if (viewModel.checkInternetConnection()) {

                        val fileWithExtension = File(file.parent, file.name + ".jpg")

                        file.copyTo(fileWithExtension, overwrite = true)

                        val requestFile =
                            fileWithExtension.asRequestBody("image/jpeg".toMediaTypeOrNull())

                        val multipartBody = MultipartBody.Part.createFormData(
                            "upload_file",  // 🔥 updated key
                            fileWithExtension.name, requestFile
                        )
                        val multipartList = mutableListOf<MultipartBody.Part>()
                        multipartList.add(multipartBody)
                        viewModel.uploadImageFile(
                            token = SharedPreference.get(context).accessToken, multipartList
                        )

                    } else {
                        ErrorUtil.showErrorDialog(context, "No Internet Connection. Please try again.")
                    }
                }
            }
        })

    // -------------------------------------------------------
    // 2️⃣ Camera Capture Launcher
    // -------------------------------------------------------
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(), onResult = { bitmap ->

            bitmap?.let {
                val uri = saveBitmapToCache(context, it)
                uri?.let { finalUri ->

                    val imageFile = uriToFile(context, finalUri)

                    imageFile?.let { file ->
                        val maxFileSize = 10 * 1024 * 1024

                        if (file.length() > maxFileSize) {
                            ErrorUtil.showErrorDialog(
                                context, "Image size exceeds 10MB. Please select a smaller image."
                            )
                            return@let
                        }

                        if (viewModel.checkInternetConnection()) {

                            val fileWithExtension = File(file.parent, file.name + ".jpg")
                            file.copyTo(fileWithExtension, overwrite = true)

                            val requestFile =
                                fileWithExtension.asRequestBody("image/jpeg".toMediaTypeOrNull())

                            val multipartBody = MultipartBody.Part.createFormData(
                                "upload_file",  // 🔥 updated key
                                fileWithExtension.name, requestFile
                            )
                            val multipartList = mutableListOf<MultipartBody.Part>()
                            multipartList.add(multipartBody)

                            viewModel.uploadImageFile(
                                token = SharedPreference.get(context).accessToken, multipartList
                            )

                        } else {
                            ErrorUtil.showErrorDialog(context, "No Internet Connection. Please try again.")
                        }
                    }
                }
            }
        })

    // -------------------------------------------------------
    // 3️⃣ Camera Permission Launcher
    // -------------------------------------------------------
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) cameraLauncher.launch(null)
        else Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    // -------------------------------------------------------
    // 4️⃣ Only Buttons (NO LazyRow Preview)
    // -------------------------------------------------------
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

        // 📸 CAMERA BUTTON
        Button(onClick = {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
            Text("Open Camera")
        }

        // 🖼️ GALLERY BUTTON
        Button(onClick = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text("Pick Images ($maxImages max)")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun convertToWebp(context: Context, uri: Uri): File {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.webp")

    val outputStream = FileOutputStream(file)

    bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, outputStream)

    outputStream.flush()
    outputStream.close()

    return file
}

//for moderate
@Composable
fun SingleImagePicker(
    context: Context,
    viewModel: AuthViewModel,
    navHostController: NavHostController,
    onClick: (Boolean) -> Unit
): (onImageSelected: (Uri) -> Unit) -> Unit {

    // 🔥 HELPER FUNCTION (ADDED)
    suspend fun convertToWebp(context: Context, uri: Uri): File {
        return withContext(Dispatchers.IO) {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.webp")
            val outputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, outputStream)

            outputStream.flush()
            outputStream.close()

            file
        }
    }
    var onImageSelected by remember { mutableStateOf<(Uri) -> Unit>({}) }
    var showDialog by remember { mutableStateOf(false) }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { onImageSelected(it) }

            val imageFile = uri?.let { uriToFile(context, it) }

            imageFile?.let {
                val maxFileSizeInBytes = 10 * 1024 * 1024
                val fileSizeInBytes = it.length()

                if (fileSizeInBytes > maxFileSizeInBytes) {
                    ErrorUtil.showErrorDialog(
                        context,
                        "Image resolution is too low. Please upload a higher-quality image."
                    )
                } else {
                    if (viewModel.checkInternetConnection()) {


                        CoroutineScope(Dispatchers.Main).launch {
                            val webpFile = withContext(Dispatchers.IO) {
                                convertToWebp(context, uri!!)
                            }

                            val requestFile =
                                webpFile.asRequestBody("image/webp".toMediaTypeOrNull())

                            val multipartBody = MultipartBody.Part.createFormData(
                                "upload_file",
                                webpFile.name,
                                requestFile
                            )

                            val multipartList = mutableListOf<MultipartBody.Part>()
                            multipartList.add(multipartBody)

                            viewModel.uploadImageFile(
                                token = SharedPreference.get(context).accessToken,
                                multipartList
                            )
                        }

                    }
                }
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->

            bitmap?.let {
                val uri = saveBitmapToUri(context, it)
                uri?.let { onImageSelected(it) }

                val imageFile = uri?.let { uriToFile(context, it) }
                Log.d("TAG", "rememberImagePicker: Camera-------$imageFile")

                imageFile?.let { file ->

                    val maxFileSizeInBytes = 10 * 1024 * 1024
                    val fileSizeInBytes = file.length()

                    if (fileSizeInBytes > maxFileSizeInBytes) {
                        ErrorUtil.showErrorDialog(
                            context,
                            "Image resolution is too low. Please upload a higher-quality image."
                        )
                    } else {
                        if (viewModel.checkInternetConnection()) {

                            CoroutineScope(Dispatchers.Main).launch {
                                val webpFile = withContext(Dispatchers.IO) {
                                    convertToWebp(context, uri!!)
                                }

                                val requestFile =
                                    webpFile.asRequestBody("image/webp".toMediaTypeOrNull())

                                val multipartBody = MultipartBody.Part.createFormData(
                                    "upload_file",
                                    webpFile.name,
                                    requestFile
                                )

                                val multipartList = mutableListOf<MultipartBody.Part>()
                                multipartList.add(multipartBody)

                                viewModel.uploadImageFile(
                                    token = SharedPreference.get(context).accessToken,
                                    multipartList
                                )
                            }



                        }
                    }
                }
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val pdfLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val imageFile = uriToFile(context, it)

                imageFile?.let { file ->
                    val maxFileSizeInBytes = 10 * 1024 * 1024
                    val fileSizeInBytes = file.length()

                    if (fileSizeInBytes > maxFileSizeInBytes) {
                        ErrorUtil.showErrorDialog(
                            context,
                            "Pdf resolution is too low. Please upload a higher-quality image."
                        )
                    } else {
                        if (viewModel.checkInternetConnection()) {
                            onImageSelected(it)

                            val fileWithExtension = File(file.parent, file.name + ".pdf")
                            file.copyTo(fileWithExtension, overwrite = true)

                            val requestFile =
                                fileWithExtension.asRequestBody("application/pdf".toMediaTypeOrNull())

                            val multipartBody = MultipartBody.Part.createFormData(
                                "upload_file",
                                fileWithExtension.name,
                                requestFile
                            )

                            val multipartList = mutableListOf<MultipartBody.Part>()
                            multipartList.add(multipartBody)

                            viewModel.uploadImageFile(
                                token = SharedPreference.get(context).accessToken,
                                multipartList
                            )
                        }
                    }
                }
            }
        }

    val imagePicker: (onImageSelected: (Uri) -> Unit) -> Unit = { callback ->
        onImageSelected = callback
        showDialog = true
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.camera_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.select_image)) },
            text = { Text(stringResource(R.string.choose_image_source)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }) {
                    Text(stringResource(R.string.camera))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        galleryLauncher.launch("image/*")
                    }) {
                    Text(stringResource(R.string.gallery))
                }
            })
    }

    return imagePicker
}

@Composable
fun AttachmentUploadSection(
    viewModel: AuthViewModel,
    pendingIndices: MutableList<Int>,
    activity: Activity,
    onUrlsUpdated: (List<String>) -> Unit  // ✅ add this callback
) {
// Add this inside AttachmentUploadSection, after other remember blocks
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.uploadImageFile.observe(lifecycleOwner) { state ->
            if (state is EmpResource.Success) {
                val urls = when (val data = state.value.data) {
                    is List<*> -> data.filterIsInstance<String>()
                    else -> emptyList()
                }
                urls.forEachIndexed { i, url ->
                    val idx = if (pendingIndices.isNotEmpty()) pendingIndices.removeAt(0) else -1
                    if (idx in 0 until 9 && url.isNotEmpty()) {
                        viewModel.uploadedImageUrls[idx] = url
                    }
                }
                // ✅ Notify parent with latest URLs
                onUrlsUpdated(viewModel.uploadedImageUrls.filter { it.isNotEmpty() })
            }
        }
    }

    val context = LocalContext.current
    var images by rememberSaveable(
        stateSaver = listSaver(
            save = { it.map { uri -> uri?.toString() } },
            restore = { it.map { s -> s?.toUri() } })
    ) { mutableStateOf(listOf<Uri?>()) }

    var showPickerDialog by remember { mutableStateOf(false) }

    val maxImages = 9

    // Gallery launcher using PictureSelector (same as your existing code)
    fun openGallery(activity: Activity) {
        val remainingSlots = maxImages - images.size
        if (remainingSlots <= 0) {
            context.showToast("Maximum $maxImages images allowed")
            return
        }
        PictureSelector.create(activity)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine())
            .setSelectionMode(SelectModeConfig.MULTIPLE)
            .setMaxSelectNum(remainingSlots)
            .isPreviewImage(true)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    result?.let { mediaList ->
                        val newUris = mediaList.map { Uri.parse(it.availablePath) }
                        val combined = (images.filterNotNull() + newUris).take(maxImages)
                        images = combined

                        // Upload all new files at once
                        val parts = mutableListOf<MultipartBody.Part>()
                        val startIndex = images.size - newUris.size

                        newUris.forEachIndexed { i, uri ->
                            val file = uriToFile(context, uri)
                            file?.let {
                                parts.add(
                                    MultipartBody.Part.createFormData(
                                        "upload_file",
                                        it.name,
                                        it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                    )
                                )
                            }
                            pendingIndices.add(startIndex + i)
                        }
                        if (parts.isNotEmpty()) {
                            viewModel.uploadImageFile(
                                SharedPreference.get(context).accessToken,
                                parts
                            )
                        }
                    }
                }
                override fun onCancel() {}
            })
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null && images.size < maxImages) {
            val file = File(context.cacheDir, "cam_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            val uri = file.toUri()
            val newIndex = images.size
            images = images + uri

            val part = MultipartBody.Part.createFormData(
                "upload_file", file.name,
                file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            pendingIndices.add(newIndex)
            viewModel.uploadImageFile(SharedPreference.get(context).accessToken, listOf(part))
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) cameraLauncher.launch(null)
        else context.showToast("Camera permission required")
    }

// ─── UI ───────────────────────────────────────────
    Text(
        text = stringResource(R.string.attachment_if_any),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular))
    )

    verticalSpace(10)

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 2000.dp)
    ) {
        itemsIndexed(images) { index, uri ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                if (uri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = "Attachment",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .padding(3.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(50))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            val updated = images.toMutableList()
                            updated.removeAt(index)
                            images = updated
                            if (viewModel.uploadedImageUrls.getOrNull(index)
                                    ?.isNotEmpty() == true
                            ) {
                                viewModel.uploadedImageUrls[index] = ""
                            }
                            pendingIndices.remove(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.cross_pruple_ic),
                        contentDescription = "close",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ✅ Single upload placeholder — always last in grid
        if (images.size < maxImages) {
            item {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showPickerDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.upload_image_border),
                        contentDescription = "Upload",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    verticalSpace(20)

// Picker dialog (Camera / Gallery)
    if (showPickerDialog) {
        AlertDialog(
            onDismissRequest = { showPickerDialog = false },
            title = { Text(stringResource(R.string.select_option)) },
            text = { Text("") },
            confirmButton = {
                TextButton(onClick = {
                    showPickerDialog = false
                    if (hasCameraPermission(context)) cameraLauncher.launch(null)
                    else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }) { Text(stringResource(R.string.camera)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPickerDialog = false
                    openGallery(activity)
                }) { Text(stringResource(R.string.gallery)) }
            }
        )
    }
}


@Composable
fun GlobalErrorDialog() {

    val message = ErrorUtil.errorMessage.value

    message?.let {

        Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(30.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {

                Image(painter = painterResource(R.drawable.temp_icon), contentDescription = null,modifier=Modifier
                    .size(45.dp)
                    .clip(shape = RoundedCornerShape(25.dp)))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = it,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                   // color = if (isSystemInDarkTheme()) Color.White else Color(0xFF262324),
                    color =  Color(0xFF262324),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF7B5CFF),
                                        Color(0xFFD46DFF)
                                    )
                                )
                            )
                            .clickable {
                                ErrorUtil.errorMessage.value = null
                            },
                        contentAlignment = Alignment.Center
                    ) {


                        Text(modifier = Modifier.padding(vertical = 16.dp),
                            text = stringResource(R.string.ok),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                }
            }
        }}


    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryBottomSheetSelector(
    countryList: List<Country>,
    selectedCountry: Country?,
    onCountrySelected: (Country) -> Unit
) {
    val languageManager = LocalLanguageManager.current
    val isArabic = languageManager.currentLanguage == "ar"
    var showSheet by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredCountries = remember(search, countryList) {
        if (search.isBlank()) countryList
        else countryList.filter {
            val name = if (isArabic) it.nameAr else it.name
            name.contains(search, ignoreCase = true)
        }
    }

    // Trigger Row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { showSheet = true }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedCountry?.let { if (isArabic) it.nameAr else it.name }
                ?: stringResource(R.string.select_country),
            color = if (selectedCountry == null) Color(0xFF6D6D6D)
            else MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(R.drawable.arrow_top_ic),
            modifier = Modifier.rotate(180f),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            contentDescription = null
        )
    }

    // Bottom Sheet
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false; search = "" },
            sheetState = sheetState,
            sheetGesturesEnabled = false,
            dragHandle = null,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF14590988))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.select_country),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
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
                                interactionSource = remember { MutableInteractionSource() }
                            ) { showSheet = false; search = "" }
                    )
                }

                // Search
                SearchBar(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                // List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                        .navigationBarsPadding()
                ) {
                    if (filteredCountries.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.no_city_found),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(filteredCountries) { country ->
                            val displayName = if (isArabic) country.nameAr else country.name
                            val isSelected = selectedCountry?.iso == country.iso

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        onCountrySelected(country)
                                        showSheet = false
                                        search = ""
                                    }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                        else Color.Transparent
                                    )
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = displayName,
                                    fontSize = 14.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onBackground,
                                    fontFamily = FontFamily(
                                        Font(
                                            if (isSelected) R.font.axiforma_semi_bold
                                            else R.font.axiforma_medium
                                        )
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Icon(
                                        painter = painterResource(R.drawable.tick_icon),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityBottomSheetSelector(
    viewModel: AuthViewModel,
    navController: NavController,
    country: String,
    selectedCity: GetCountryCodeResponse.Data.City?,
    selectedCityName: String?,
    searchQuery: String,
    onCitySelected: (GetCountryCodeResponse.Data.City?) -> Unit
) {
    val context = LocalContext.current
    val languageManager = LocalLanguageManager.current
    val isArabic = languageManager.currentLanguage == "ar"
    val isTranslationPending by viewModel.isTranslationPending
    var showSheet by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isLoadingCities by remember { mutableStateOf(false) }

    // Restore city in edit mode
    LaunchedEffect(selectedCityName, viewModel.cityList1.size) {
        if (selectedCityName.isNullOrBlank() || selectedCity != null) return@LaunchedEffect
        val trimmed = selectedCityName.trim()
        val restored = viewModel.cityList1.firstOrNull { city ->
            city?.nameEn?.trim().equals(trimmed, ignoreCase = true) == true
                    || city?.nameAr?.trim().equals(trimmed, ignoreCase = true) == true
        }
        if (restored != null) {
            onCitySelected(restored)
            Log.d("CitySelector", "City pre-selected: ${restored.nameEn}")
        }
    }

    // Translation pending refresh
    LaunchedEffect(isTranslationPending, country) {
        if (isTranslationPending && country.isNotBlank()) {
            delay(30_000L)
            hitCityApi(viewModel, context, country, 1, 100, languageManager.currentLanguage)
        }
    }

    // Sheet open — always triggers a fresh load
    LaunchedEffect(showSheet) {
        if (showSheet && country.isNotBlank()) {
            hitCityApi(
                viewModel, context, country, 1, 100,
                languageManager.currentLanguage,
                null
            )
        }
    }

    // Search debounce — only non-blank queries, sheet-open load handled above
    LaunchedEffect(search) {
        if (!showSheet) return@LaunchedEffect
        if (search.isNotBlank()) {
            delay(300)
            isLoadingCities = true
            viewModel.currentCityPage = 1
            hitCityApi(
                viewModel, context, country, 1, 100,
                languageManager.currentLanguage,
                search.normalizeForSearch()
            )
        }
    }

    // ✅ FIXED: only turn off loader when list is non-empty (real data arrived)
    val cityListSize = viewModel.cityList1.size
    LaunchedEffect(cityListSize) {
        if (isLoadingCities && cityListSize > 0) {
            isLoadingCities = false
        }
    }

    // ✅ Safety net: if API returns empty result (country has no cities),
    // wait briefly then clear loader so "No city found" can show
    LaunchedEffect(isLoadingCities) {
        if (isLoadingCities) {
            delay(8_000L)
            isLoadingCities = false
        }
    }

    val displayLabel = selectedCity?.let {
        if (isArabic) it.nameAr?.takeIf { n -> n.isNotBlank() } ?: it.nameEn
        else it.nameEn ?: ""
    }

    // Trigger Row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (country.isBlank()) {
                    context.showToast(context.getString(R.string.please_choose_country_first))
                } else {
                    search = ""
                    viewModel.cityList1.clear()
                    viewModel.currentCityPage = 1
                    isLoadingCities = true      // ← set TRUE before sheet opens, same frame
                    showSheet = true
                }
            }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayLabel ?: stringResource(R.string.select_city),
            color = if (selectedCity == null) Color(0xFF6D6D6D)
            else MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(R.drawable.arrow_top_ic),
            modifier = Modifier.rotate(180f),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            contentDescription = null
        )
    }

    // Bottom Sheet
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false; search = "" },
            sheetState = sheetState,
            sheetGesturesEnabled = false,
            dragHandle = null,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF14590988))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.select_city),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
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
                                interactionSource = remember { MutableInteractionSource() }
                            ) { showSheet = false; search = "" }
                    )
                }

                // Search
                SearchBar(
                    value = search,
                    onValueChange = { newText ->
                        search = newText.replace(Regex("[^\\p{L}0-9 '''ʼ`\\-]"), "")                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                val lazyListState = androidx.compose.foundation.lazy.rememberLazyListState()
                val shouldLoadMore by remember {
                    derivedStateOf {
                        val lastVisible = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                        val total = lazyListState.layoutInfo.totalItemsCount
                        viewModel.currentCityPage <= viewModel.totalCityPages
                                && total > 0
                                && lastVisible != null
                                && lastVisible.index >= total - 3
                    }
                }
                LaunchedEffect(shouldLoadMore) {
                    if (shouldLoadMore) {
                        hitCityApi(
                            viewModel, context, country,
                            viewModel.currentCityPage, 100,
                            languageManager.currentLanguage,
                            search.ifBlank { null }
                        )
                    }
                }

                val sortedCities = remember(viewModel.cityList1.toList(), search) {
                    val query = search.normalizeForSearch()  // already normalizes apostrophes
                    if (query.isEmpty()) {
                        viewModel.cityList1.toList()
                    } else {
                        val startsWith = mutableListOf<GetCountryCodeResponse.Data.City?>()
                        val contains = mutableListOf<GetCountryCodeResponse.Data.City?>()
                        viewModel.cityList1.forEach { city ->
                            val rawLabel = (if (isArabic)
                                city?.nameAr?.takeIf { it.isNotBlank() } ?: city?.nameEn
                            else city?.nameEn).orEmpty()

                            val label = rawLabel.normalizeForSearch()  // normalizes apostrophes, accents, lowercase

                            // Strip ALL apostrophe variants for a secondary fuzzy compare
                            val labelStripped = label.replace(Regex("['''ʼ`]"), "")
                            val queryStripped = query.replace(Regex("['''ʼ`]"), "")

                            when {
                                label.startsWith(query) || labelStripped.startsWith(queryStripped) -> startsWith.add(city)
                                label.contains(query) || labelStripped.contains(queryStripped) -> contains.add(city)
                            }
                        }
                        startsWith + contains
                    }
                }
                // ✅ Fixed min-height so sheet never auto-dismisses while loading
                androidx.compose.foundation.lazy.LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 450.dp)   // ← min keeps sheet open
                        .navigationBarsPadding()
                ) {
                    when {
                        isLoadingCities -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),   // ← explicit height, not just padding
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(28.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        sortedCities.isEmpty() -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.no_city_found),
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        else -> {
                            items(sortedCities) { city ->
                                val displayName = if (isArabic)
                                    city?.nameAr?.takeIf { it.isNotBlank() } ?: city?.nameEn ?: ""
                                else
                                    city?.nameEn ?: ""
                                val isSelected = selectedCity?.nameEn == city?.nameEn

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            onCitySelected(city)
                                            showSheet = false
                                            search = ""
                                        }
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.08f
                                            )
                                            else Color.Transparent
                                        )
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = displayName,
                                        fontSize = 14.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onBackground,
                                        fontFamily = FontFamily(
                                            Font(
                                                if (isSelected) R.font.axiforma_semi_bold
                                                else R.font.axiforma_medium
                                            )
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            painter = painterResource(R.drawable.tick_icon),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            if (viewModel.currentCityPage <= viewModel.totalCityPages) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PlanCard(modifier: Modifier=Modifier,
    planType: PlanType,
    title: String,
    price: String,
    billingText: String,
             offerDetail: ProductDetails.SubscriptionOfferDetails?,

    discountText: String? = null,
    isBestValue: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {

    val pricingPhase = offerDetail?.pricingPhases?.pricingPhaseList?.first()

    val priceMicros = pricingPhase?.priceAmountMicros
    val currency = pricingPhase?.priceCurrencyCode

    val priceValue = ((priceMicros?:0) / 1_000_000).toInt()
    val displayPrice = pricingPhase?.formattedPrice

    val borderColor = when (planType) {PlanType.GOLD -> Color(0xFFFFBA55)
        PlanType.PLATINUM -> Color(0xFFE6E6E6)}

    val backgroundColor = when (planType) {
        PlanType.GOLD -> Color(0xFFFFF3E0)
        PlanType.PLATINUM -> Color(0xFF919191).copy(0.1f)}

    val titleColor = when (planType) {
        PlanType.GOLD -> Color(0xFFFFBA55)
        PlanType.PLATINUM -> Color.Black
    }
    val saveBackground = when (planType) {
        PlanType.GOLD -> Color(0xFFFFBA55).copy(alpha = 7f)
        PlanType.PLATINUM ->Color(0xFF919191).copy(0.5f)
    }

    Box(
        modifier = modifier
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) borderColor else Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = if(isSelected) backgroundColor else Color.White),
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onClick() }
        ) {

            Column(  modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween) {

                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {

                    RadioButton(
                        selected = isSelected,
                        onClick = { onClick() },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = borderColor
                        )
                    )

                    Text(
                        text = title,
                        color = titleColor,
                        fontSize = 18.sp
                    )
                }

                if (!discountText.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = discountText,
                        modifier = Modifier
                            .background(
                                if (isSelected)
                                    saveBackground else Color(0xFFF9F9F9),
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text =displayPrice?:"" ,
                    fontSize = 28.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                )

                Text(
                    text = billingText,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        // ⭐ Best Value Tag
        if (isBestValue) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp)
                    .offset(y = (-10).dp)
                    .background(
                        Color(0xFFEDE7F6),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.best_valued),
                    fontSize = 8.ssp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                )
            }
        }

///*
//        AppButton(modifier=Modifier.align(Alignment.BottomCenter) )
//*/
    }
}



enum class PlanType {
    GOLD,
    PLATINUM
}

