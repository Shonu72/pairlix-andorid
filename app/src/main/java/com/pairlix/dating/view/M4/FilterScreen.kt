package com.pairlix.dating.view.M4

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.Chips
import com.pairlix.dating.ReusedComponents.CommonSelection
import com.pairlix.dating.ReusedComponents.SearchBar
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.appGradientBackground
import com.pairlix.dating.ReusedComponents.countryNameToIsoCode
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.items
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.CountryListHelper
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.prioritizeSearch
import com.pairlix.dating.requests.GetMatchFilterRequest
import com.pairlix.dating.response.GetAllCategoriesResponse
import com.pairlix.dating.response.RecentSearchResponse
import com.pairlix.dating.view.M4.ProfessionBottomSheet
import com.pairlix.dating.view.M4.UserTypeBottomSheet
import com.pairlix.dating.view.newAccountRegistrationScreen.ChipItem
import com.pairlix.dating.view.newAccountRegistrationScreen.SelectedInterest
import com.pairlix.dating.view.newAccountRegistrationScreen.createAccountStep6Observer
import com.pairlix.dating.view.profileDetails.AboutEditType
import com.pairlix.dating.view.profileDetails.InterestBottomSheet
import com.pairlix.dating.view.profileDetails.InterestedInBottomSheet
import com.pairlix.dating.view.profileDetails.LanguageBottomSheet
import com.pairlix.dating.view.profileDetails.MaritalStatusBottomSheet
import com.pairlix.dating.view.profileDetails.SectBottomSheet
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import kotlin.collections.any
import kotlin.collections.forEach
import kotlin.collections.orEmpty


enum class FilterEditType {
    COUNTRY,
    USER_TYPE,
    CHILDREN,
    // INTERESTED_IN,
    INTERESTS,
    SECT,
    PROFESSION,
    LANGUAGE,
    DRINKING,
    SMOKING,
    MARITAL_STATUS
}

data class UserType(val title: String, val icon: Int)
//data class Country(val flag:String, val name:String)

data class Country(
    val isoCode: String,
    val nameEn: String,
    val nameAr: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController, viewModel: M4ViewModel,authViewModel: AuthViewModel) {

    var selectedType by remember { mutableStateOf<FilterEditType?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var hasBio by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val lifecycleOwner=LocalLifecycleOwner.current
    val categoryList = authViewModel.categoryBackup
    val minAgeLimit = 18f
    val maxAgeLimit = 80f
    val distance = viewModel.distance.floatValue
    val minAge = viewModel.minAge.floatValue
    val maxAge = viewModel.maxAge.floatValue

    var text by rememberSaveable() { mutableStateOf("") }

    var customText by rememberSaveable() { mutableStateOf("") }

    val selectedInterests = rememberSaveable {
        mutableStateOf(
            listOf<SelectedInterest>()
        )
    }
    var recentTagList by rememberSaveable {
        mutableStateOf(listOf<RecentSearchResponse.Data>())
    }
    val categoryData = authViewModel.getCategoryList
    val originalList = authViewModel.categoryBackup


    /* Text(
         text = countryNameToIsoCode(homeData?.countryName ?: "") ?: "",
         fontSize = 15.sp
     )
     */

    var filteredCategoryList by remember {
        mutableStateOf(listOf<GetAllCategoriesResponse.Data?>())
    }
    val selectedTagsText by viewModel.selectedInterestNames

    val languageManager = LocalLanguageManager.current

// ✅ Load both English and Arabic arrays explicitly
    val countryNamesEn = remember { CountryListHelper.getEnglishCountryNames(context) }
    val countryNamesAr = remember { CountryListHelper.getArabicCountryNames(context) }

    val countryListIso = remember(countryNamesEn, countryNamesAr) {
        CommonResource().countryIsoCodes.mapIndexed { index, code ->
            Country(
                isoCode = code,
                nameEn = countryNamesEn.getOrNull(index) ?: "",
                nameAr = countryNamesAr.getOrNull(index) ?: ""
            )
        }
    }


    LaunchedEffect(Unit) {
        if (categoryList.isEmpty()) {
            authViewModel.hitGetAllCategoriesStep6(
                SharedPreference.get(context).accessToken
            )
        }
    }


    createAccountStep6Observer(
        context = context as MainActivity,
        viewModel = authViewModel,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController,
        onSuccess = { it ->

            val list = it ?: emptyList()

            // Backup in ViewModel (persistent across navigation)
            authViewModel.categoryBackup.clear()
            authViewModel.categoryBackup.addAll(list)

            // Show full list initially
            filteredCategoryList = list
            viewModel.setCategories(list) // 🔥 MOST IMPORTANT LINE

            authViewModel.getCategoryList.clear()
            authViewModel.getCategoryList.addAll(list)
        },

        onSuccessRecentTags = { tagList ->

            if (text.isEmpty()) {
                filteredCategoryList = originalList.toList()
            } else {

                val searched = tagList?.filterNotNull() ?: emptyList()

                val newFiltered = originalList.mapNotNull { category ->

                    if (category == null) return@mapNotNull null

                    val matchedTags = category.tags?.filter { tag ->
                        searched.any { it.tagNameEn == tag?.tagName?.en }
                    }

                    if (!matchedTags.isNullOrEmpty()) {
                        category.copy(tags = matchedTags)
                    } else null
                }

                filteredCategoryList = newFiltered
            }
        }


    )


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()

    ) {
        val max = this.maxHeight
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)


        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


                TopBackBtnHeading(navController,

                    text = stringResource(R.string.filter), modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .appGradientBackground()
                        .clickable {
                            viewModel.clearAllFilters()

                            context.showToast(context.getString(R.string.filter_cleared_successfully))
                        }
                        .padding(10.dp),
                    text = stringResource(R.string.clear_filter),
                    color = MaterialTheme.colorScheme.onBackground,

                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                )


            }
            verticalSpace(30)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.maximum_distance),
                        color = MaterialTheme.colorScheme.onBackground,

                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )

                    Text(
                        text = distance.toInt().toString()+" "+ stringResource(R.string.km) ,
                        color = MaterialTheme.colorScheme.onBackground,

                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )
                }


                DistanceProgress(
                    distance = distance,
                    onDistanceChange = { viewModel.updateDistance(it) }
                )

                verticalSpace(20)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.age_range),
                        color = MaterialTheme.colorScheme.onBackground,

                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )

                    Text(
                        text = "${minAge.toInt()} - ${maxAge.toInt()}",
                        color = MaterialTheme.colorScheme.onBackground,

                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )


                }
                verticalSpace(20)


                AgeRangeProgress(
                    startValue = minAge,
                    endValue = maxAge,
                    onRangeChange = { start, end ->
                        viewModel.updateAgeRange(
                            start = start,
                            end = end
                        )
                    }
                )

                verticalSpace(20)
                AboutArrowItem(stringResource(R.string.country)) {
                    selectedType = FilterEditType.COUNTRY
                    showBottomSheet = true
                }

                AboutArrowItem(stringResource(R.string.user_type)) {
                    selectedType = FilterEditType.USER_TYPE
                    showBottomSheet = true
                }

                AboutArrowItem(stringResource(R.string.children)) {
                    selectedType = FilterEditType.CHILDREN
                    showBottomSheet = true
                }

                /*AboutArrowItem("Interested In") {
                    selectedType = FilterEditType.INTERESTED_IN
                    showBottomSheet = true
                }*/

                verticalSpace(20)


                InterestRow(
                    interests = selectedTagsText,
                    onClick = {
                        viewModel.saveInterestState()
                        selectedType = FilterEditType.INTERESTS
                        showBottomSheet = true
                    },
                    onRemove = {
                        viewModel.removeInterestByName(it)
                    }
                )

                verticalSpace(5)

                AboutArrowItem(stringResource(R.string.sect)) {
                    selectedType = FilterEditType.SECT
                    showBottomSheet = true
                }

                AboutArrowItem(stringResource(R.string.profession)) {
                    selectedType = FilterEditType.PROFESSION
                    showBottomSheet = true
                }

                AboutArrowItem(stringResource(R.string.language_spoken)) {
                    selectedType = FilterEditType.LANGUAGE
                    showBottomSheet = true
                }

                AboutArrowItem(stringResource(R.string.drinking)) {
                    selectedType = FilterEditType.DRINKING
                    showBottomSheet = true
                }

                AboutArrowItem(stringResource(R.string.smoking)) {
                    selectedType = FilterEditType.SMOKING
                    showBottomSheet = true
                }

                AboutArrowItem(stringResource(R.string.marital_status)) {
                    selectedType = FilterEditType.MARITAL_STATUS
                    showBottomSheet = true
                }

                verticalSpace(25)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(52.dp))
                            .border(
                                1.dp, Color(0xFF590988), shape = RoundedCornerShape(52.dp)
                            )
                            .background(
                                Color.White, shape = RoundedCornerShape(52.dp)
                            )
                            .clickable {
                                navController.popBackStack()
                                viewModel.restoreAgeRange()
                                viewModel.restoreDistance()

                            }
                            .padding(vertical = 14.dp),
                        text = stringResource(R.string.cancel),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color(0xFF590988),
                        textAlign = TextAlign.Center
                    )


                    horizontalSpace(10)
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(52.dp))
                            .border(
                                1.dp, Color.Transparent, shape = RoundedCornerShape(52.dp)
                            )
                            .appGradientBackground()
                            .clickable {
                                // 🔥 FINAL COMMIT (ONLY PLACE)
                                viewModel.commitCountry(countryListIso)
                                viewModel.commitLanguageSelection()
                                viewModel.commitPlanType()
                                viewModel.commitInterestedIn()
                                viewModel.commitSect()
                                viewModel.commitProfession()
                                viewModel.commitChildren()
                                viewModel.commitMaritalStatus()
                                viewModel.commitDrinkStatus()
                                viewModel.commitSmokeStatus()
                                viewModel.submitInterestUpdate(context)
                                val request = viewModel.buildFilterRequest()
                                viewModel.currentFilterRequest.value = request
                                viewModel.updateFilter(true)

                                /* authViewModel.hitGetMatch(
                                     accessToken = SharedPreference.get(context).accessToken,
                                     filter = request
                                 )*/

                                navController.popBackStack()
                            }

                            /*
                                                        .clickable{

                                                            val request = viewModel.buildFilterRequest()
                                                            viewModel.currentFilterRequest.value = request

                                                            authViewModel.hitGetMatch(
                                                                accessToken = SharedPreference.get(context).accessToken,
                                                                filter = request
                                                            )

                                                            navController.popBackStack()
                                                        }
                            */
                            .padding(vertical = 14.dp),
                        text = stringResource(R.string.done),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }}

            // 🔥 ONE COMMON BOTTOM SHEET
            if (showBottomSheet && selectedType != null) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    dragHandle = null,
                    onDismissRequest = {
                        when (selectedType) {

                            FilterEditType.COUNTRY -> {
                                viewModel.restoreCountry(countryListIso)
                                // country temp restore handled by keeping temp list
                            }
                            FilterEditType.INTERESTS -> viewModel.restoreInterestState()
                            FilterEditType.USER_TYPE -> viewModel.restorePlanType()

                            //  FilterEditType.INTERESTED_IN -> viewModel.restoreInterestedIn()
                            FilterEditType.SECT -> viewModel.restoreSect()
                            FilterEditType.CHILDREN -> viewModel.restoreChildren()
                            FilterEditType.PROFESSION -> viewModel.restoreProfession()
                            FilterEditType.LANGUAGE -> viewModel.restoreLanguageSelection()
                            FilterEditType.DRINKING -> viewModel.restoreDrinkStatus()
                            FilterEditType.SMOKING -> viewModel.restoreSmokeStatus()
                            FilterEditType.MARITAL_STATUS -> viewModel.restoreMaritalStatus()

                            else -> {}
                        }

                        showBottomSheet = false
                        selectedType = null
                    }
                    ,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ) {
                    AboutEditBottomSheet(
                        viewModel = viewModel,
                        authViewModel=authViewModel,
                        context = context,
                        type = selectedType!!,
                        onClose = {
                            showBottomSheet = false
                            selectedType = null
                        }
                    )
                }

            }

        }

    }
}

@Composable
fun DistanceProgress(
    distance: Float,
    onDistanceChange: (Float) -> Unit
) {
    val maxValue = 1000f
    val trackHeight = 10.dp
    val thumbSize = 26.dp
    val languageManager = LocalLanguageManager.current
    val isRtl = languageManager.currentLanguage == "ar"

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val widthPx = constraints.maxWidth.toFloat()
        val thumbRadiusPx = with(density) { thumbSize.toPx() / 2 }
        val usableWidth = widthPx - thumbRadiusPx * 2
        val safeDistance = distance.coerceAtLeast(5f)

        var thumbPx by remember {
            mutableFloatStateOf(
                if (isRtl) {
                    (1f - safeDistance / maxValue) * usableWidth + thumbRadiusPx
                } else {
                    (safeDistance / maxValue) * usableWidth + thumbRadiusPx
                }
            )
        }

        LaunchedEffect(safeDistance, isRtl) {
            thumbPx = if (isRtl) {
                (1f - safeDistance / maxValue) * usableWidth + thumbRadiusPx
            } else {
                (safeDistance / maxValue) * usableWidth + thumbRadiusPx
            }
        }

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                // Background track
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth()
                        .height(trackHeight)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFE0E0E0))
                )

                // Active progress
                if (isRtl) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset { IntOffset((thumbPx - thumbRadiusPx).toInt(), 0) }
                            .height(trackHeight)
                            .width(with(density) { (widthPx - thumbPx + thumbRadiusPx).toDp() })
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF530386))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .height(trackHeight)
                            .width(with(density) { thumbPx.toDp() })
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF530386))
                    )
                }

                // Thumb
                Box(
                    modifier = Modifier
                        .offset { IntOffset((thumbPx - thumbRadiusPx).toInt(), 0) }
                        .align(Alignment.CenterStart)
                        .size(thumbSize)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFF6A6D6), Color(0xFF8B5DF6))
                            )
                        )
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                // 🔥 Negate delta for RTL so drag direction feels natural
                                val effectiveDelta = if (isRtl) -delta else delta
                                val newThumbPx = (thumbPx + effectiveDelta)
                                    .coerceIn(thumbRadiusPx, widthPx - thumbRadiusPx)
                                thumbPx = newThumbPx

                                val ratio = (newThumbPx - thumbRadiusPx) / usableWidth
                                val newDistance = if (isRtl) {
                                    (1f - ratio) * maxValue
                                } else {
                                    ratio * maxValue
                                }
                                onDistanceChange(newDistance.coerceAtLeast(5f))
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun AgeRangeProgress(
    startValue: Float,
    endValue: Float,
    onRangeChange: (Float, Float) -> Unit
) {
    val minAgeLimit = 18f
    val maxAgeLimit = 80f
    val trackHeight = 10.dp
    val thumbSize = 26.dp

    // ✅ FIX: Use system RTL instead of manual language check
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val widthPx = constraints.maxWidth.toFloat()
        val thumbRadiusPx = with(density) { thumbSize.toPx() / 2 }
        val usableWidth = widthPx - thumbRadiusPx * 2

        fun ageToPx(age: Float): Float {
            val ratio = (age - minAgeLimit) / (maxAgeLimit - minAgeLimit)
            return if (isRtl) {
                (1f - ratio) * usableWidth + thumbRadiusPx
            } else {
                ratio * usableWidth + thumbRadiusPx
            }
        }

        fun pxToAge(px: Float): Float {
            val ratio = (px - thumbRadiusPx) / usableWidth
            return if (isRtl) {
                ((1f - ratio) * (maxAgeLimit - minAgeLimit) + minAgeLimit)
                    .coerceIn(minAgeLimit, maxAgeLimit)
            } else {
                (ratio * (maxAgeLimit - minAgeLimit) + minAgeLimit)
                    .coerceIn(minAgeLimit, maxAgeLimit)
            }
        }

        var startPx by remember { mutableFloatStateOf(ageToPx(startValue)) }
        var endPx by remember { mutableFloatStateOf(ageToPx(endValue)) }

        LaunchedEffect(startValue, endValue) {
            startPx = ageToPx(startValue)
            endPx = ageToPx(endValue)
        }

        val leftPx = minOf(startPx, endPx)
        val rightPx = maxOf(startPx, endPx)

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {

                // Track
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth()
                        .height(trackHeight)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFE0E0E0))
                )

                // Selected Range
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset { IntOffset((leftPx - thumbRadiusPx).toInt(), 0) }
                        .height(trackHeight)
                        .width(with(density) { (rightPx - leftPx).coerceAtLeast(0f).toDp() })
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF530386))
                )

                // ✅ Start Thumb (LOW AGE)
                RangeThumb(
                    xPx = startPx,
                    thumbRadiusPx = thumbRadiusPx,
                    onDrag = { delta ->

                        // 🔥 FIX: reverse delta only
                        val adjustedDelta = if (isRtl) -delta else delta

                        val newX = if (isRtl) {
                            (startPx + adjustedDelta).coerceIn(
                                endPx + thumbRadiusPx,
                                widthPx - thumbRadiusPx
                            )
                        } else {
                            (startPx + adjustedDelta).coerceIn(
                                thumbRadiusPx,
                                endPx - thumbRadiusPx
                            )
                        }

                        startPx = newX
                        onRangeChange(pxToAge(newX), endValue)
                    }
                )

                // ✅ End Thumb (HIGH AGE)
                RangeThumb(
                    xPx = endPx,
                    thumbRadiusPx = thumbRadiusPx,
                    onDrag = { delta ->

                        // 🔥 FIX: reverse delta only
                        val adjustedDelta = if (isRtl) -delta else delta

                        val newX = if (isRtl) {
                            (endPx + adjustedDelta).coerceIn(
                                thumbRadiusPx,
                                startPx - thumbRadiusPx
                            )
                        } else {
                            (endPx + adjustedDelta).coerceIn(
                                startPx + thumbRadiusPx,
                                widthPx - thumbRadiusPx
                            )
                        }

                        endPx = newX
                        onRangeChange(startValue, pxToAge(newX))
                    }
                )
            }
        }
    }
}
@Composable
fun BoxScope.RangeThumb(
    xPx: Float,
    thumbRadiusPx: Float,
    onDrag: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .align(Alignment.CenterStart) // ✅ now allowed
            .offset {
                IntOffset(
                    (xPx - thumbRadiusPx).toInt(),
                    0
                )
            }
            .size(26.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF6A6D6),
                        Color(0xFF8B5DF6)
                    )
                )
            )
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    onDrag(delta)
                }
            )
    )
}


@Composable
fun AboutArrowItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground,

            )

        Image(
            painter = painterResource(R.drawable.side_arrow),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color(0xFF590988)),
            modifier = Modifier.size(18.dp)
        )
    }
}


@Composable
fun InterestRow(
    interests: List<String>,
    onRemove: (String) -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
    ) {
        Row( modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }){

            Text(
                text = stringResource(R.string.interests),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.side_arrow),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color(0xFF590988)),
                modifier = Modifier.size(18.dp)
            )


        }



        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(interests) { interest ->
                AssistChip(
                    onClick = {},
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(interest)
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        onRemove(interest)
                                    }
                            )
                        }
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = Color(0xFF8B5DF6),
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, Color(0xFF8B5DF6))
                )
            }
        }
    }
}

@Composable
fun AboutEditBottomSheet(
    viewModel: M4ViewModel,
    authViewModel: AuthViewModel,
    context: Context,
    type: FilterEditType,
    onClose: () -> Unit
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = when (type) {
                    FilterEditType.COUNTRY -> stringResource(R.string.country)
                    FilterEditType.USER_TYPE -> stringResource(R.string.user_type)
                    FilterEditType.CHILDREN -> stringResource(R.string.children)
                    FilterEditType.INTERESTS -> stringResource(R.string.interests)
                    FilterEditType.SECT -> stringResource(R.string.religion)
                    FilterEditType.PROFESSION -> stringResource(R.string.profession)
                    FilterEditType.LANGUAGE -> stringResource(R.string.language_spoken)
                    FilterEditType.DRINKING -> stringResource(R.string.drinking)
                    FilterEditType.SMOKING -> stringResource(R.string.smoking)
                    FilterEditType.MARITAL_STATUS -> stringResource(R.string.marital_status)
                    else -> stringResource(R.string.edit_bio)
                },
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
                        interactionSource = remember { MutableInteractionSource() })
                    {
                        if (type == FilterEditType.INTERESTS) {
                            viewModel.restoreInterestState()
                        }
                        onClose()
                    })


        }

        when (type) {

            FilterEditType.COUNTRY -> {
                val languageManager = LocalLanguageManager.current

                val countryNamesEn = remember { CountryListHelper.getEnglishCountryNames(context) }
                val countryNamesAr = remember { CountryListHelper.getArabicCountryNames(context) }
                val countryListIso = remember(countryNamesEn, countryNamesAr) {
                    CommonResource().countryIsoCodes.mapIndexed { index, code ->
                        Country(
                            isoCode = code,
                            nameEn = countryNamesEn.getOrNull(index) ?: "",
                            nameAr = countryNamesAr.getOrNull(index) ?: ""
                        )
                    }
                }

                CountryMultiSelectBottomSheet(
                    items = countryListIso,
                    selectedIndexes = viewModel.tempCountryIndexes,
                    onItemSelected = { viewModel.onCountryIndexToggle(it) },
                    allowUnselect = true,
                    onDone = {
                        onClose()
                    }
                )
            }
            /*     FilterEditType.INTERESTED_IN -> {
                     InterestedInBottomSheet(
                         items = CommonResource().interestedList,
                         selectedIndex = viewModel.tempInterestedInIndex.value,
                         onItemSelected = {

                             viewModel.onInterestedInChange(it)
                         },
                         allowUnselect = true,
                         onDone = {
                             viewModel.commitInterestedIn()
                             onClose()
                         }
                     )
                 }
     */
            FilterEditType.USER_TYPE -> {
                val userTypeList = listOf<UserType>(
                    UserType(context.getString(R.string.platinum), R.drawable.silver_coin_ic),
                    UserType(context.getString(R.string.gold), R.drawable.gold_coin_ic)
                )
                UserTypeBottomSheet(
                    items = userTypeList,
                    selectedIndex = viewModel.tempPlanTypeIndex.value,
                    onItemSelected = {viewModel.onPlanTypeChange(it)
                    },
                    allowUnselect = true,
                    onDone = {
                        //viewModel.commitPlanType()
                        onClose()

                    }
                )
            }

            FilterEditType.MARITAL_STATUS -> {
                MaritalStatusBottomSheet(
                    item = stringArrayResource(R.array.marital_status_list).toList(),
                    selectedIndex = viewModel.tempMartialStatusIndex.value,
                    onItemSelected = {
                        viewModel.onMartialStatusChange(it)
                    },
                    allowUnselect = true,
                    onDone = {
                        // viewModel.commitMaritalStatus()
                        onClose()
                    }

                )
            }

            FilterEditType.DRINKING -> {
                DrinkBottomSheet(
                    items = stringArrayResource(R.array.drinking_frequency_list).toList(),
                    selectedIndex = viewModel.tempDrinkStatusIndex.value,
                    onItemSelected = {
                        viewModel.onDrinkStatusChange(it)
                    },
                    allowUnselect = true,
                    onDone = {
                        //  viewModel.commitDrinkStatus()
                        onClose()
                    }

                )
            }

            FilterEditType.SMOKING -> {
                DrinkSmokeSheet (
                    items = stringArrayResource(R.array.smoking_frequency_list).toList(),
                    selectedIndex = viewModel.tempSmokeStatusIndex.value,
                    onItemSelected = {

                        viewModel.onSmokeStatusChange(it)
                    },
                    allowUnselect = true,
                    onDone = {
                        // viewModel.commitSmokeStatus()
                        onClose()
                    }

                )
            }

            FilterEditType.LANGUAGE -> {
                LanguageBottomSheetFilter(
                    items = stringArrayResource(R.array.spoken_languages).toList(),
                    selectedIndexes = viewModel.tempLanguageIndexes,
                    onItemSelected = {
                        viewModel.onLanguageIndexToggle(it)
                    },
                    allowUnselect = true,
                    onDone = {
                        // viewModel.commitLanguageSelection()
                        onClose()
                    }
                )
            }

            FilterEditType.PROFESSION -> {
                ProfessionBottomSheet(
                    item = stringArrayResource(R.array.profession_list).toList(),
                    selectedIndex = viewModel.tempProfessionIndex.value,
                    onItemSelected = {
                        viewModel.onProfessionChange(it)
                    },
                    allowUnselect = true,
                    onDone = {
                        // viewModel.commitProfession()
                        onClose()
                    }

                )
            }

            FilterEditType.CHILDREN -> {
                val childrenList = listOf(
                    context.getString(R.string.yes),
                    context.getString(R.string.no),
                    //"Prefer not to say"
                )

                ProfessionBottomSheet(
                    item = childrenList,
                    selectedIndex = viewModel.tempChildrenIndex.value,
                    onItemSelected = {
                        viewModel.onChildrenChange(it)
                    },
                    allowUnselect = true,
                    onDone = {
                        //viewModel.commitChildren()
                        onClose()
                    }
                )
            }

            FilterEditType.SECT -> {
                var customText by rememberSaveable() { mutableStateOf("") }
                SectBottomSheet(
                    items = stringArrayResource(R.array.belong_list).toList(),
                    selectedIndex = viewModel.tempSectIndex.value,
                    onItemSelected = {
                        viewModel.onSectChange(it)
                    }, allowUnselect = true,
                    onDone = {
                        // viewModel.commitSect()
                        onClose()
                    }
                    ,
                    customSectText = customText,
                    onCustomSectChange = {
                        customText= it
                    }
                )
            }

            FilterEditType.INTERESTS -> {
                InterestBottomSheet(
                    viewModel=authViewModel,
                    viewModelM4 = viewModel,
                    selectedInterests = viewModel.tempInterestIds.value,
                    onSelectionChanged = {
                        viewModel.onInterestSelectionChanged(it)
                    }
                    ,
                    onDone = {
                        // viewModel.submitInterestUpdate(context)
                        onClose()
                    }

                    /* onDone = {
                         viewModel.commitInterestedIn()
                         viewModel.submitInterestUpdate(context)
                         onClose()
                     }*/

                )
            }

            else -> {
                null
            }


        }

    }
}


@Composable
fun LanguageBottomSheetFilter(
    items: List<String>,
    selectedIndexes: List<Int>, // 🔥 multi select
    allowUnselect: Boolean = true, // 🔑 KEY
    onItemSelected: (Int) -> Unit, // 🔥 toggle index
    onDone: () -> Unit
) {
    val context = LocalContext.current
    var search by rememberSaveable { mutableStateOf("") }

    val filteredItems = remember(search, items) {
        val indexedItems = items.withIndex().toList()

        if (search.isBlank()) {
            indexedItems
        } else {
            prioritizeSearch(
                indexedItems,
                search
            ) { it.value }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            SearchBar(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.background(MaterialTheme.colorScheme.background) .padding(top = 12.dp, start = 12.dp, end = 12.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)
                    .verticalScroll(rememberScrollState())
                   .background(MaterialTheme.colorScheme.background)
            ) {

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
                            color = Color(0xFF6D6D6D)
                        )
                    }
                } else {
                    filteredItems.forEach { indexedValue ->
                        val index = indexedValue.index
                        val item = indexedValue.value
                        val isSelected = selectedIndexes.contains(index)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected && !allowUnselect) return@clickable
                                    onItemSelected(index)
                                }
                                .padding(horizontal = 15.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = item,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = if (isSelected) Color(0xFF8B5DF6) else MaterialTheme.colorScheme.onBackground,
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

        verticalSpace(20)

        AppButton(
            text = stringResource(R.string.done),
            onClick = onDone
        )
    }
}


@Composable
fun UserTypeBottomSheet(
    items: List<UserType>,
    selectedIndex: Int, // 🔥 THIS IS API VALUE (3 / 2)
    allowUnselect: Boolean = false,
    onItemSelected: (Int) -> Unit,
    onDone: () -> Unit
) {

    // 🔥 UI index → API value mapping
    val userTypeApiMap = mapOf(
        0 to 3, // First item → API = 3
        1 to 2  // Second item → API = 2
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 10.dp)
                    .verticalScroll(rememberScrollState())

            ) {

                items.forEachIndexed { index, item ->

                    // 🔥 API VALUE for this row
                    val apiValue = userTypeApiMap[index] ?: -1

                    // 🔥 Selection check (CORRECT)
                    val isSelected = apiValue == selectedIndex

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {

                                if (isSelected) {
                                    if (allowUnselect) {
                                        onItemSelected(-1)
                                    }
                                } else {
                                    onItemSelected(apiValue)
                                }
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )

                        horizontalSpace(10)

                        Text(
                            text = item.title,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (isSelected)
                                Color(0xFF8B5DF6)
                             else MaterialTheme.colorScheme.onBackground,
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

                verticalSpace(20)
            }
        }

        verticalSpace(20)

        AppButton(
            text = stringResource(R.string.done),
            onClick = onDone
        )
    }
}

@Composable
fun ProfessionBottomSheet(
    item: List<String>,
    selectedIndex: Int,
    allowUnselect: Boolean = false, // 🔑 KEY
    onItemSelected: (Int) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                item.forEachIndexed { index, text ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {

                                // 🔥 SAME CONTROLLED LOGIC
                                if (index == selectedIndex) {
                                    if (allowUnselect) {
                                        onItemSelected(-1)
                                    }
                                } else {
                                    onItemSelected(index)
                                }
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = text,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (index == selectedIndex)
                                Color(0xFF8B5DF6)
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

        verticalSpace(20)

        AppButton(
            text = stringResource(R.string.done),
            onClick = onDone
        )
    }
}

@Composable
fun DrinkBottomSheet(
    items: List<String>,
    selectedIndex: Int,
    allowUnselect: Boolean = false, // 🔑 KEY
    onItemSelected: (Int) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                items.forEachIndexed { index, item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {

                                // 🔥 SAME CUSTOM LOGIC
                                if (index == selectedIndex) {
                                    if (allowUnselect) {
                                        onItemSelected(-1)
                                    }
                                } else {
                                    onItemSelected(index)
                                }
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = item,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (index == selectedIndex)
                                Color(0xFF8B5DF6)
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

                verticalSpace(20)
            }
        }

        verticalSpace(20)

        AppButton(
            text = stringResource(R.string.done),
            onClick = onDone
        )
    }
}


@Composable
fun DrinkSmokeSheet(
    items: List<String>,
    selectedIndex: Int,
    allowUnselect: Boolean = false, // 🔑 KEY
    onItemSelected: (Int) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .background(MaterialTheme.colorScheme.background)

                    .padding(top = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                items.forEachIndexed { index, item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {

                                // 🔥 CUSTOM UNSELECT LOGIC
                                if (index == selectedIndex) {
                                    if (allowUnselect) {
                                        onItemSelected(-1)
                                    }
                                } else {
                                    onItemSelected(index)
                                }
                            }
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = item,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            color = if (index == selectedIndex)
                                Color(0xFF8B5DF6)
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

                verticalSpace(20)
            }
        }

        verticalSpace(20)

        AppButton(
            text = stringResource(R.string.done),
            onClick = onDone
        )
    }
}

@Composable
fun CountryMultiSelectBottomSheet(
    items: List<Country>,
    selectedIndexes: List<Int>,
    allowUnselect: Boolean = true, // 🔑 KEY
    onItemSelected: (Int) -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    var search by rememberSaveable { mutableStateOf("") }

    // 🔥 Filter list
    val languageManager = LocalLanguageManager.current

    val filteredItems = remember(search, items, languageManager.currentLanguage) {
        val indexedItems = items.withIndex().toList()
        val query = search.trim()

        when {
            query.isBlank() -> {
                indexedItems
            }

            query.length == 1 -> {
                indexedItems.filter {
                    val displayName = if (languageManager.currentLanguage == "ar")
                        it.value.nameAr
                    else
                        it.value.nameEn
                    displayName.startsWith(query, ignoreCase = true)
                }
            }

            else -> {
                prioritizeSearch(
                    indexedItems,
                    query
                ) {
                    if (languageManager.currentLanguage == "ar")
                        it.value.nameAr
                    else
                        it.value.nameEn
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            SearchBar(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)
                   .background(MaterialTheme.colorScheme.background)
            ) {

                if (filteredItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.country_not_found),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = Color(0xFF6D6D6D)
                            )
                        }
                    }
                } else {
                    items(
                        items = filteredItems,
                        key = { it.index }
                    ) { indexedItem ->

                        val item = indexedItem.value
                        val originalIndex = indexedItem.index
                        val isSelected = selectedIndexes.contains(originalIndex)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected && !allowUnselect) return@clickable
                                    onItemSelected(originalIndex)
                                }
                                .padding(horizontal = 15.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = countryCodeToFlagEmojiFilter(item.isoCode),
                                fontSize = 15.sp
                            )

                            horizontalSpace(15)
                            val displayName = if (languageManager.currentLanguage == "ar")
                                item.nameAr
                            else
                                item.nameEn
                            Text(
                                text = displayName,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                color = if (isSelected) Color(0xFF8B5DF6) else MaterialTheme.colorScheme.onBackground,
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

        verticalSpace(20)

        AppButton(
            text = stringResource(R.string.done),
            onClick = onDone
        )
    }
}


fun countryCodeToFlagEmojiFilter(countryCode: String): String {
    return countryCode
        .uppercase()
        .map { char ->
            Character.toChars(char.code + 0x1F1E6 - 'A'.code)
        }
        .joinToString("") { String(it) }
}


