package com.pairlix.dating.view.profileDetails

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import com.pairlix.dating.ThemeManager.isAppInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.FormProgressBar
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.view.home.HomePageObserver

import com.pairlix.dating.view.newAccountRegistrationScreen.Items
import com.pairlix.dating.view.newAccountRegistrationScreen.TextHeading
import com.pairlix.dating.viewModel.AuthViewModel
import ir.kaaveh.sdpcompose.sdp


import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CommonSelection
import com.pairlix.dating.ReusedComponents.GradientExpandableCardIndex
import com.pairlix.dating.ReusedComponents.GradientExpandableCardWithMultipleSelectApi
import com.pairlix.dating.ReusedComponents.SearchBar
import com.pairlix.dating.ReusedComponents.UnitChip
import com.pairlix.dating.view.newAccountRegistrationScreen.FaithItem
import com.pairlix.dating.view.newAccountRegistrationScreen.UploadGridItem
import com.pairlix.dating.view.newAccountRegistrationScreen.UploadGridUI
import kotlinx.coroutines.launch
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.pairlix.dating.LanguageManager.LanguageManager
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.MainActivity
import com.pairlix.dating.ReusedComponents.Chips
import com.pairlix.dating.ReusedComponents.CustomInputField
import com.pairlix.dating.ReusedComponents.ProfileProgressBar
import com.pairlix.dating.ReusedComponents.SingleImagePicker
import com.pairlix.dating.ReusedComponents.countryCodeToFlagEmoji
import com.pairlix.dating.ReusedComponents.countryNameToIsoCode
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.CountryListHelper
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.prioritizeSearch
import com.pairlix.dating.helper.startCrop
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.requests.UpdateProfileRequest
import com.pairlix.dating.response.GetAllCategoriesResponse
import com.pairlix.dating.response.PreviewProfileResponse
import com.pairlix.dating.response.RecentSearchResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.newAccountRegistrationScreen.ChipItem
import com.pairlix.dating.view.newAccountRegistrationScreen.SelectedInterest
import com.pairlix.dating.view.newAccountRegistrationScreen.createAccountStep6Observer
import com.pairlix.dating.view.newAccountRegistrationScreen.createAccountStep7Observer
import com.pairlix.dating.view.newAccountRegistrationScreen.onlyAlphabetsNoInitial
import com.pairlix.dating.viewModel.AuthViewModel.UploadSource
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import ir.kaaveh.sdpcompose.ssp

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun getSectDisplayText(
    sectValue: String?,
    customSect: Any?,
    belongList: List<String>
): String {

    val index = sectValue?.toIntOrNull()
    val OTHER_INDEX = belongList.lastIndex

    return when {
        // 🔥 Other selected → show custom text
        index == OTHER_INDEX && customSect is String && customSect.isNotBlank() ->
            customSect

        // Normal index case (0,1,2)
        index != null && index in belongList.indices ->
            belongList[index]

        // API sent text directly in sect (legacy case)
        index == null && !sectValue.isNullOrBlank() ->
            sectValue

        else -> "-"
    }
}

fun List<String?>?.formatLanguages(limit: Int = 3): String {
    return this?.filterNotNull()?.take(limit)?.joinToString(", ") { it.formatTitle() } ?: "-"
}


fun PreviewProfileObserver(
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    context: MainActivity,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    onSuccess: (PreviewProfileResponse.Data?) -> Unit
) {
    viewModel.getPreviewProfile.observe(lifecycleOwner) { state ->
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
                    state.let {
                        onSuccess(state.value.data)
                        val imageUrl = state.value.data?.profileImages?.firstOrNull()

                        SharedPreference.get(context).profileImage = imageUrl ?: ""
                    }
                    //viewModel.getPreviewProfileData.value= state.value.data
                    state.value.success = false
                }
            }

            else -> {
                // no-op
            }
        }
    }
    viewModel.updateProfile.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                CustomLoader.showLoader(context as Activity?)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (state.value.success == true) {

                    context.showToast(state.value.message?:"")
                    viewModel.hitPreviewProfile(
                        access_token = SharedPreference.get(context).accessToken
                    )
                    val filterRequest by viewModelM4.currentFilterRequest

                    viewModel.hitGetMatch(
                        accessToken = SharedPreference.get(context).accessToken,
                        filter = filterRequest
                    )

                    state.value.success = false
                }
            }

            else -> {
                // no-op
            }
        }
    }


}

enum class ProfileTab { EDIT, PREVIEW }
enum class AboutEditType {
    HEIGHT, INTERESTED_IN, LANGUAGE, SECT, MARITAL_STATUS, RELIGION_PRACTICE, CHILDREN_STATUS, RELOCATION, PERSONALITY, FAITH, HABITS, INTEREST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(
    navController: NavHostController,
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    m5ViewModel: M5ViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var previousPersonalityBio by remember { mutableStateOf("") }

    val checkAbusive by m5ViewModel.checkAbusiveWord.collectAsState()


    LaunchedEffect(checkAbusive) {

        checkAbusive.let{
            when (it){

                is EmpResource.Loading->{
                    //CustomLoader.showLoader(context as MainActivity)

                }

                is EmpResource.Success->{
                    //context.showToast(it.value.message?:"")

                    viewModel.hitUpdateProfile(
                        access_token = SharedPreference.get(context).accessToken,
                        request = UpdateProfileRequest(
                            personalDetails = UpdateProfileRequest.PersonalDetails(
                                description = viewModel.personalityBio.trim()
                            )
                        )
                    )
                    m5ViewModel.resetCheckAbusiveWord()
                }

                is EmpResource.Failure->{

                    CustomLoader.hideLoader()
                    it.throwable?.let {
                        ErrorUtil.handlerGeneralError(context, it)
                    }

                    viewModel.forceSetPersonalityBio(viewModel.previousPersonalityBio)
                    m5ViewModel.resetCheckAbusiveWord()

                }

                else->{ }
            }

        }


    }

    LaunchedEffect(key1 = Unit) {
        viewModel.hitPreviewProfile(
            access_token = SharedPreference.get(context).accessToken
        )
    }
    PreviewProfileObserver(
        viewModel = viewModel,
        viewModelM4=viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            viewModel.getPreviewProfileData.value = it
        })


    val categoryList = viewModel.categoryBackup

    var text by rememberSaveable() { mutableStateOf("") }


    val originalList = viewModel.categoryBackup

    var filteredCategoryList by remember {
        mutableStateOf(listOf<GetAllCategoriesResponse.Data?>())
    }
    val languageManager = LocalLanguageManager.current
    val chips = stringArrayResource(R.array.profile_chips).toList()
    val height = stringResource(R.string.height)
    val interestedIn = stringResource(R.string.interested_in)
    val language = stringResource(R.string.language)
    val sect = stringResource(R.string.sect)
    val maritalStatus = stringResource(R.string.marital_status)
    val religionPractice = stringResource(R.string.religion_practice)
    val childrenStatus = stringResource(R.string.children_status)
    val relocation = stringResource(R.string.relocation_after_marriage)

    LaunchedEffect(Unit) {
        if (categoryList.isEmpty()) {
            viewModel.hitGetAllCategoriesStep6(
                SharedPreference.get(context).accessToken
            )
        }
    }

    createAccountStep6Observer(
        context = context as MainActivity,
        viewModel = viewModel,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController,
        onSuccess = { it ->

            val list = it ?: emptyList()

            // Backup in ViewModel (persistent across navigation)
            viewModel.categoryBackup.clear()
            viewModel.categoryBackup.addAll(list)

            // Show full list initially
            filteredCategoryList = list

            viewModel.getCategoryList.clear()
            viewModel.getCategoryList.addAll(list)
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


    val homeData = viewModel.getPreviewProfileData.value
    var selectedTab by remember { mutableStateOf(ProfileTab.EDIT) }
    val interactionSource = remember { MutableInteractionSource() }
    val interactionSource2 = remember { MutableInteractionSource() }
    val mainListState = rememberLazyListState()
    val profileImages = listOf(
        R.drawable.profile_pic_2, R.drawable.profile_pic, R.drawable.profile_pic
    )

    val images: List<String> = homeData?.personalDetails?.images?.filterNotNull() ?: emptyList()

    val pagerState = rememberPagerState {
        images.size.coerceAtLeast(1)
    }
    val coroutineScope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedAboutEditType by remember {
        mutableStateOf<AboutEditType?>(null)
    }
    var aboutY by remember { mutableStateOf(0f) }
    var interestY by remember { mutableStateOf(0f) }
    var faithY by remember { mutableStateOf(0f) }
    var personalityY by remember { mutableStateOf(0f) }
    var educationY by remember { mutableStateOf(0f) }
    var lifestyleY by remember { mutableStateOf(0f) }
    var stickyHeaderHeight by remember { mutableStateOf(0f) }

 /*   val chips = remember {
        listOf(
            "About",
            "Education",
            "Interest",
            "Faith Identity",
            "Personality (Bio)",
            // "Life Style"
        )
    }
*/
    LaunchedEffect(Unit) {
        viewModel.hitGetAllFaithsStep7(
            access_token = SharedPreference.get(context).accessToken
        )
    }

    createAccountStep7Observer(
        context = context as MainActivity,
        viewModel = viewModel,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController,
        onSuccess = { it ->
            viewModel.getFaithList.clear()
            it?.let {
                viewModel.getFaithList.addAll(it)
            }
        })

    LaunchedEffect(
        mainListState.firstVisibleItemScrollOffset,
        aboutY,
        interestY,
        faithY,
        personalityY,
        educationY,
        lifestyleY
    ) {
        val headerBottom = stickyHeaderHeight + 1f

        val sections = listOf(
            aboutY to 0,
            interestY to 1,
            faithY to 2,
            personalityY to 3,
            educationY to 4,
            lifestyleY to 5
        )

        val current = sections.filter { it.first >= headerBottom }.minByOrNull { it.first }


    }

    val openAboutEditSheet: (AboutEditType) -> Unit = { type ->
        selectedAboutEditType = type
        showBottomSheet = true
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }


    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()

    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
                .statusBarsPadding()

        ) {
            LazyColumn(
                state = mainListState, modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    TopBackBtnHeading(
                        navController,
                        stringResource(R.string.complete_profile),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )

                }
                item {

                    ProfileProgressBar(
                        percentage = (homeData?.personalDetails?.profileCompletionPercentage
                            ?: "").toString(), modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    verticalSpace(20)
                }
                item {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .border(2.dp, Color.White, shape = CircleShape)
                                .size(60.dp)
                                .clip(shape = CircleShape), contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(homeData?.profileImages?.firstOrNull()?:"" )
                                    .crossfade(true).build(),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds ,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                            )
                        }


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = buildString {
                                        val fullName =
                                            "${homeData?.firstName.orEmpty()} ${homeData?.lastName.orEmpty()}".trim()
                                        append(fullName.take(20))
                                        if (fullName.length > 20) append("...")
                                    },
                                    color = Color(0xFF590988),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                                    maxLines = 1,
                                )

                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = ",${homeData?.age ?: 0}",
                                    color = Color(0xFF590988),
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    fontFamily = FontFamily(Font(R.font.axiforma_bold)))

                                Spacer(modifier = Modifier.width(4.dp))

                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (homeData?.personalDetails?.isFaceVerified == true) {
                                        Image(
                                            modifier = Modifier.size(20.sdp),
                                            painter = painterResource(R.drawable.blue1),
                                            contentDescription = ""
                                        )

                                    }

                                    Spacer(modifier = Modifier.width(2.dp))

                                    if (homeData?.personalDetails?.isDocumentVerified == true) {
                                        Image(
                                            modifier = Modifier.size(20.sdp),
                                            painter = painterResource(R.drawable.blue2),
                                            contentDescription = ""
                                        )

                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Image(
                                        painterResource(R.drawable.pencil_icon),
                                        contentDescription = "edit",
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable {

                                                SingletonObject.isFromEditProfile = true
                                                navController.navigate(Screen.CreateAccountScreen.route) {
                                                    popUpTo(Screen.ViewProfileScreen.route) {
                                                        inclusive = false
                                                    }
                                                }
                                            },
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.sdp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!homeData?.countryName.isNullOrEmpty()) {
                                    val context = LocalContext.current
                                    val countryNamesEn =
                                        remember { CountryListHelper.getEnglishCountryNames(context) }
                                    val isoCodes = CommonResource().countryIsoCodes

                                    // Find index using English name (since API returns English)
                                    val index = countryNamesEn.indexOfFirst {
                                        it.equals(homeData?.countryName ?: "", ignoreCase = true)
                                    }

                                    if (index >= 0 && index < isoCodes.size) {
                                        Text(
                                            text = countryCodeToFlagEmoji(isoCodes[index]),
                                            fontSize = 15.sp
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.width(5.sdp))

                                Text(
                                    "${ if(languageManager.currentLanguage=="en") homeData?.city ?: "" else homeData?.cityAr?:homeData?.city} ,${if(languageManager.currentLanguage=="en")homeData?.countryName ?: "" else homeData?.countryNameAr?:homeData?.countryName}",
                                    color = Color(0xFF530386),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                )
                            }
                        }
                    }
                    verticalSpace(20)

                }
                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(12.dp)
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .padding(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(R.string.name),
                                fontSize = 14.sp,
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )
                            Text(modifier = Modifier.padding(start = 5.dp),
                                text = homeData?.firstName ?: "",
                                fontSize = 14.sp,
                                maxLines = 1, overflow = TextOverflow.Ellipsis,
                                color = Color(0xFF6D6D6D),
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                            )


                        }

                        verticalSpace(20)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(R.string.email_id),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground ,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )

                            Text(modifier = Modifier.padding(start = 5.dp),
                                text = homeData?.email ?: "",
                                fontSize = 12.ssp,
                                color = Color(0xFF6D6D6D),
                                maxLines = 1, overflow = TextOverflow.Ellipsis,
                                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                            )


                        }
                    }


                    verticalSpace(20)

                }
                item {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(12.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    selectedTab = ProfileTab.EDIT
                                }) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.edit_profile),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = if (selectedTab == ProfileTab.EDIT) MaterialTheme.colorScheme.onBackground else Color(
                                        0xFF6D6D6D
                                    ),
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                )
                                verticalSpace(5)

                                if (selectedTab == ProfileTab.EDIT) {
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp,
                                        color =   MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = interactionSource2, indication = null
                                ) {
                                    selectedTab = ProfileTab.PREVIEW

                                }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Text(
                                    text = stringResource(R.string.preview_profile),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = if (selectedTab == ProfileTab.PREVIEW) MaterialTheme.colorScheme.onBackground  else Color(
                                        0xFF6D6D6D
                                    ),
                                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                )
                                verticalSpace(5)

                                if (selectedTab == ProfileTab.PREVIEW) {
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp,
                                        color =   MaterialTheme.colorScheme.outlineVariant

                                    )
                                }
                            }
                        }


                    }
                }

                when (selectedTab) {

                    ProfileTab.EDIT -> {
                        item {
                            EditProfileData(
                                navController, viewModel, onAboutEditClick = openAboutEditSheet
                            )
                        }
                    }


                    ProfileTab.PREVIEW -> {


                        item {

                            PreviewHeader(
                                viewModel,
                                pagerState = pagerState,
                                profileImages = images,
                                parentListState = mainListState
                            )
                        }

                        stickyHeader {
                            val ABOUT_INDEX = 6
                            val INTEREST_INDEX = 7
                            val FAITH_INDEX = 10
                            val PERSONALITY_INDEX = 11
                            val EDUCATION_INDEX = 12
                            val LIFESTYLE_INDEX = 13

                            Box(
                                modifier = Modifier.onGloballyPositioned {
                                    stickyHeaderHeight = it.size.height.toFloat()
                                }) {
                                PreviewStickyChips(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(
                                            top = 30.dp, start = 16.dp, end = 16.dp, bottom = 10.dp
                                        ),
                                    list = chips,
                                    selectedIndex = viewModel.selectedPreviewChip.value,
                                    onChipClick = { index ->
                                        viewModel.selectedPreviewChip.value = index
                                        coroutineScope.launch {
                                            val offset = -stickyHeaderHeight.toInt()
                                            when (index) {
                                                0 -> mainListState.animateScrollToItem(
                                                    ABOUT_INDEX, offset
                                                )

                                                1 -> mainListState.animateScrollToItem(
                                                    INTEREST_INDEX, offset
                                                )

                                                2 -> mainListState.animateScrollToItem(
                                                    FAITH_INDEX, offset
                                                )

                                                3 -> mainListState.animateScrollToItem(
                                                    PERSONALITY_INDEX, offset
                                                )

                                                4 -> mainListState.animateScrollToItem(
                                                    EDUCATION_INDEX, offset
                                                )
                                                // 5 -> mainListState.animateScrollToItem(LIFESTYLE_INDEX, offset)
                                            }
                                        }

                                    }


                                )
                            }
                        }


                        item {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceTint)// ✅ ONE PLACE ONLY
                            )
                            {
                                val languageManager = LocalLanguageManager.current // ✅ Add this
                                val spokenLanguages = stringArrayResource(R.array.spoken_languages) // ✅ Get array from resources


                                Box(Modifier.onGloballyPositioned {
                                    aboutY = it.positionInWindow().y
                                }
                                    // .bringIntoViewRequester(aboutRequester)
                                ) {
                                    val about =
                                        viewModel.getPreviewProfileData?.value?.personalDetails

                                    val aboutList = listOf(
                                        Items(
                                            stringResource(R.string.interested_in),
                                            if (about?.interestedIn == "0") stringResource(R.string.male) else if (about?.interestedIn == "1") stringResource(R.string.female) else if (about?.interestedIn == "2") stringResource(R.string.everyone) else "-"
                                        ),


                                    Items(
                                        item = stringResource(R.string.language_spoken),
                                        value = about?.spokenLanguages?.mapNotNull { lang ->
                                            val index = lang?.toIntOrNull()
                                            if (index != null && index >= 0 && index < spokenLanguages.size) {
                                                spokenLanguages[index] // ✅ Get language name from string array
                                            } else {
                                                null
                                            }
                                        }?.joinToString(", ") ?: "-"
                                    ),





                                        Items(
                                            stringResource(R.string.sect),
                                            getSectDisplayText(
                                                sectValue = about?.sect,
                                                customSect = about?.customSect,
                                                belongList = stringArrayResource(R.array.belong_list).toList()
                                            )
                                        ),


                                        Items(
                                            stringResource(R.string.marital_status),
                                            if (about?.maritalStatus == "0") stringResource(R.string.never_married) else if (about?.maritalStatus == "1") stringResource(R.string.divorced) else if (about?.maritalStatus == "2") stringResource(R.string.widowed) else if (about?.maritalStatus == "3") stringResource(R.string.separated) else "-"
                                        ),
                                        Items(
                                            stringResource(R.string.religion_practice),
                                            if (about?.religionPractice == "0") stringResource(R.string.very_practicing) else if (about?.religionPractice == "1") stringResource(R.string.practicing) else if (about?.religionPractice == "2") stringResource(R.string.moderately_practicing) else if (about?.religionPractice == "3") stringResource(R.string.cultural_muslim) else "-"
                                        ),
                                        Items(
                                            stringResource(R.string.children_status),
                                            if (about?.haveChildren == "0") stringResource(R.string.yes) else if (about?.haveChildren == "1") stringResource(R.string.no) else if (about?.haveChildren == "2") stringResource(R.string.prefer_not_to_say) else "-"
                                        ),
                                        Items(
                                            stringResource(R.string.relocation_after_marriage),
                                            if (about?.aboardAfterMarriage == "0") stringResource(R.string.yes) else if (about?.aboardAfterMarriage == "1") stringResource(R.string.no) else if (about?.aboardAfterMarriage == "2") stringResource(R.string.depends_on_spouse) else if (about?.aboardAfterMarriage == "3") stringResource(R.string.already_living_abroad) else "-"
                                        ),
                                    )
                                    AboutSection(
                                        list = aboutList
                                    )
                                }


                                Column(
                                    modifier = Modifier.fillMaxWidth()

                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        shape = RoundedCornerShape(20.dp),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 2.dp
                                        ),
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    start = 12.dp,
                                                    end = 12.dp,
                                                    top = 15.dp,
                                                    bottom = 12.dp
                                                )
                                        ) {

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {

                                                TextHeading(text = stringResource(R.string.habits))

                                            }
                                            verticalSpace(10)

                                            Row(modifier = Modifier.fillMaxWidth()) {


                                                Row(modifier = Modifier.weight(1f)) {

                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                brush = Brush.linearGradient(
                                                                    colors = listOf(
                                                                        Color(0xFFD9C8FF),  // lavender
                                                                        Color(0xFFEFD8FF),  // soft pink-purple
                                                                        Color(0xFFFFEFF8)   // light peach
                                                                    )
                                                                ), shape = RoundedCornerShape(
                                                                    12.dp
                                                                )
                                                            )
                                                            .padding(
                                                                horizontal = 12.dp, vertical = 8.dp
                                                            )
                                                    ) {
                                                        Image(
                                                            painter = painterResource(R.drawable.smoking_im),
                                                            contentDescription = null,
                                                            modifier = Modifier.size(24.dp),
                                                        )

                                                        Spacer(Modifier.height(8.dp))

                                                        Text(
                                                            text = stringResource(R.string.smoking),
                                                            color = Color(0xff6D6D6D),
                                                            fontSize = 12.sp,
                                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                                        )

                                                        Spacer(Modifier.height(8.dp))

                                                        Text(
                                                            text = if (homeData?.personalDetails?.howOftenSmoke == "0") stringResource(R.string.casual) else if (homeData?.personalDetails?.howOftenSmoke == "1") stringResource(R.string.smoker) else if (homeData?.personalDetails?.howOftenSmoke == "2") stringResource(R.string.trying_to_quit) else if (homeData?.personalDetails?.howOftenSmoke == "3") stringResource(R.string.smoking_when_drinking) else if (homeData?.personalDetails?.howOftenSmoke == "4") stringResource(R.string.never) else "-",
                                                            color = Color(0xff590988),
                                                            fontSize = 16.sp,
                                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                                        )
                                                    }

                                                }
                                                horizontalSpace(15)
                                                Row(modifier = Modifier.weight(1f)) {

                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                brush = Brush.linearGradient(
                                                                    colors = listOf(
                                                                        Color(0xFFD9C8FF),  // lavender
                                                                        Color(0xFFEFD8FF),  // soft pink-purple
                                                                        Color(0xFFFFEFF8)   // light peach
                                                                    )
                                                                ), shape = RoundedCornerShape(
                                                                    12.dp
                                                                )
                                                            )
                                                            .padding(
                                                                horizontal = 12.dp, vertical = 8.dp
                                                            )
                                                    ) {
                                                        Image(
                                                            painter = painterResource(R.drawable.glass_ic),
                                                            contentDescription = null,
                                                            modifier = Modifier.size(24.dp),
                                                        )

                                                        Spacer(Modifier.height(8.dp))

                                                        Text(
                                                            text = stringResource(R.string.drinking),
                                                            color = Color(0xff6D6D6D),
                                                            fontSize = 12.sp,
                                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                                        )

                                                        Spacer(Modifier.height(8.dp))

                                                        Text(
                                                            text = if (homeData?.personalDetails?.howOftenDrink == "0") stringResource(R.string.never) else if (homeData?.personalDetails?.howOftenDrink == "1") stringResource(R.string.occasional) else if (homeData?.personalDetails?.howOftenDrink == "2") stringResource(R.string.regular) else "-",
                                                            color = Color(0xff590988),
                                                            fontSize = 16.sp,
                                                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                                        )
                                                    }

                                                }
                                            }

                                            verticalSpace(20)
                                            Row() {

                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth(0.5f)
                                                        .background(
                                                            brush = Brush.linearGradient(
                                                                colors = listOf(
                                                                    Color(0xFFD9C8FF),  // lavender
                                                                    Color(0xFFEFD8FF),  // soft pink-purple
                                                                    Color(0xFFFFEFF8)   // light peach
                                                                )
                                                            ), shape = RoundedCornerShape(12.dp)
                                                        )
                                                        .padding(
                                                            horizontal = 12.dp, vertical = 8.dp
                                                        )
                                                ) {
                                                    Image(
                                                        painter = painterResource(R.drawable.dumble_ic),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.dp),
                                                    )

                                                    Spacer(Modifier.height(8.dp))

                                                    Text(
                                                        text = stringResource(R.string.workout),
                                                        color = Color(0xff6D6D6D),
                                                        fontSize = 12.sp,
                                                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                                    )

                                                    Spacer(Modifier.height(8.dp))

                                                    Text(
                                                        text = if (homeData?.personalDetails?.workOut == "0") stringResource(R.string.everyday) else if (homeData?.personalDetails?.workOut == "1") stringResource(R.string.often) else if (homeData?.personalDetails?.workOut == "2") stringResource(R.string.sometimes) else if (homeData?.personalDetails?.workOut == "3") stringResource(R.string.never) else "-",
                                                        color = Color(0xff590988),
                                                        fontSize = 16.sp,
                                                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                                    )
                                                }
                                                verticalSpace(20)
                                            }
                                        }
                                    }
                                }


                                Box(Modifier.onGloballyPositioned {
                                    interestY = it.positionInWindow().y
                                }

                                    // .bringIntoViewRequester(interestRequester)
                                ) {
                                    val languageManager = LocalLanguageManager.current

                                    val interestChips: List<ChipItem> =
                                        homeData?.personalDetails?.interests?.filterNotNull()
                                            ?.flatMap { interest ->
                                                interest.tags?.filterNotNull()?.map { tag ->
                                                    val displayName =
                                                        if (languageManager.currentLanguage == "ar") {
                                                            tag.tagNameAr ?: tag.tagNameEn.orEmpty()
                                                        } else {
                                                            tag.tagNameEn.orEmpty()
                                                        }

                                                    ChipItem(
                                                        tagId = tag.id.orEmpty(),
                                                        tagName = displayName,
                                                        iconUrl = tag.iconImage
                                                    )
                                                } ?: emptyList()

                                            } ?: emptyList()


                                    CategoryEditCard(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        heading = stringResource(R.string.interests),
                                        chips = interestChips,
                                        isEditable = false,
                                        onClick = {})

                                }

                                verticalSpace(20)
                                Box(Modifier.onGloballyPositioned {
                                    faithY = it.positionInWindow().y
                                }
                                    // .bringIntoViewRequester(faithRequester)
                                ) {
                                    val languageManager = LocalLanguageManager.current
                                    CategoryEditCard(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        heading = stringResource(R.string.faith_identity),
                                        chips = homeData?.personalDetails?.faith?.filterNotNull()
                                            ?.map {
                                                val displayName = if (languageManager.currentLanguage == "ar") {
                                                    it.faithNameAr ?: it.faithNameEn.orEmpty()
                                                } else {
                                                    it.faithNameEn.orEmpty()
                                                }
                                                ChipItem(
                                                    tagId = it.id.orEmpty(),
                                                    tagName = displayName,
                                                    iconUrl = ""
                                                )
                                            } ?: emptyList(),

                                        isEditable = false,
                                        onClick = {})
                                }

                                verticalSpace(20)

                                Box(Modifier.onGloballyPositioned {
                                    personalityY = it.positionInWindow().y
                                }
                                    //.bringIntoViewRequester(personalityRequester)
                                ) {
                                    PersonalitySection(
                                        text = homeData?.personalDetails?.description ?: ""
                                    )
                                }
                                val educationLevels = stringArrayResource(R.array.education_levels)

                                Box(
                                    Modifier.onGloballyPositioned {
                                        educationY = it.positionInWindow().y
                                    }
                                ) {

                                    val educationText = homeData?.personalDetails?.educationLevel
                                        ?.toIntOrNull()
                                        ?.let { index ->
                                            if (index in educationLevels.indices) {
                                                educationLevels[index]
                                            } else {
                                                "-"
                                            }
                                        } ?: "-"

                                    val educationList = listOf(
                                        Items(
                                            stringResource(R.string.education_level),
                                            educationText
                                        )
                                    )

                                    EducationSection(list = educationList)
                                }

                                Box(Modifier.onGloballyPositioned {
                                    lifestyleY = it.positionInWindow().y
                                }
                                    //.bringIntoViewRequester(lifestyleRequester)
                                ) {
                                    val lifeStyleListPreview = listOf<Items>(


                                        Items(
                                            stringResource(R.string.drinking),
                                            if (homeData?.personalDetails?.howOftenDrink == "0") stringResource(R.string.never) else if (homeData?.personalDetails?.howOftenDrink == "1") stringResource(R.string.occasional) else if (homeData?.personalDetails?.howOftenDrink == "2") stringResource(R.string.regular) else "-"
                                        ),
                                        Items(
                                            stringResource(R.string.smoking),
                                            if (homeData?.personalDetails?.howOftenSmoke == "0") stringResource(R.string.casual) else if (homeData?.personalDetails?.howOftenSmoke == "1") stringResource(R.string.smoker) else if (homeData?.personalDetails?.howOftenSmoke == "2") stringResource(R.string.trying_to_quit) else if (homeData?.personalDetails?.howOftenSmoke == "3") stringResource(R.string.smoking_when_drinking) else if (homeData?.personalDetails?.howOftenSmoke == "4") stringResource(R.string.never) else "-"
                                        ),
                                        Items(
                                            stringResource(R.string.workout),
                                            if (homeData?.personalDetails?.workOut == "0") stringResource(R.string.everyday) else if (homeData?.personalDetails?.workOut == "1") stringResource(R.string.often) else if (homeData?.personalDetails?.workOut == "2") stringResource(R.string.sometimes) else if (homeData?.personalDetails?.workOut == "3") stringResource(R.string.never) else "-"
                                        ),
                                    )
                                    //  LifestyleSection(lifeStyleListPreview)
                                }


                                Spacer(modifier = Modifier.height(20.dp))
                            }

                        }

                    }
                }
            }
        }
    }

    if (showBottomSheet && selectedAboutEditType != null) {
        ModalBottomSheet(sheetState = sheetState, dragHandle = { null }, onDismissRequest = {
            showBottomSheet = false
            selectedAboutEditType = null
        }) {
            AboutEditBottomSheet(
                viewModel,
                viewModelM4 = viewModelM4,
                m5ViewModel,
                context,
                type = selectedAboutEditType!!,
                onClose = {
                    showBottomSheet = false
                    selectedAboutEditType = null
                }

            )
        }
    }
}


@Composable
fun EditProfileData(
    navController: NavController,
    viewModel: AuthViewModel,
    onAboutEditClick: (AboutEditType) -> Unit = {}
) {
    val context = LocalContext.current
    //val moderateContent by viewModel.moderateContent.collectAsState()

    val imagePicker = SingleImagePicker(
        context = context,
        viewModel = viewModel,
        navHostController = navController as NavHostController,
        onClick = { isPdf ->
        })

    val upload by viewModel.uploadImageFile.observeAsState()
    var selectedImgPosition by remember { mutableStateOf(0) }
    val interactionSource = remember { MutableInteractionSource() }
    val editableProfileChips = listOf(stringResource(R.string.profile), stringResource(R.string.about), stringResource(R.string.education))
    val data = viewModel.getPreviewProfileData.value?.personalDetails
    var imageList = remember { mutableStateListOf<String>() }

    LaunchedEffect(data?.images) {
        imageList.clear()
        imageList.addAll(data?.images ?: arrayListOf())

    }

    LaunchedEffect(upload) {
        upload?.let {
            if (it is EmpResource.Loading) {
                CustomLoader.showLoader(context as MainActivity)
            }
            if (it is EmpResource.Success) {
                CustomLoader.hideLoader()
                val imageUrl = it.value.data?.firstOrNull()?.documentImageUrl ?: ""

                if (imageUrl.isNotEmpty()) {
                    if (selectedImgPosition < imageList.size) {
                        imageList[selectedImgPosition] = imageUrl
                    } else {
                        imageList.add(imageUrl)
                    }

                    viewModel.hitUpdateProfile(
                        access_token = SharedPreference.get(context).accessToken,
                        request = UpdateProfileRequest(
                            personalDetails = UpdateProfileRequest.PersonalDetails(
                                images = imageList
                            )
                        )
                    )
                }
            } else if (it is EmpResource.Failure) {
                CustomLoader.hideLoader()
                it.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }
        }
    }

/*
    LaunchedEffect(moderateContent) {
        moderateContent.let {

            if (it is EmpResource.Success && viewModel.uploadSource == UploadSource.GRID_IMAGE) {
                CustomLoader.hideLoader()
                context.showToast(it.value.message?:"")

                val imageUrl =
                    it.value.data?.moderationLabels?.firstOrNull()?.documentImageUrl ?: ""

                if (selectedImgPosition < imageList.size) {
                    imageList[selectedImgPosition] = imageUrl
                } else {
                    imageList.add(imageUrl)
                }

                viewModel.hitUpdateProfile(
                    access_token = SharedPreference.get(context).accessToken,
                    request = UpdateProfileRequest(
                        personalDetails = UpdateProfileRequest.PersonalDetails(
                            images = imageList
                        )
                    )
                )

            } else if (it is EmpResource.Failure) {
                CustomLoader.hideLoader()
                it.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            } else if (it is EmpResource.Loading) {
                CustomLoader.showLoader(context as MainActivity)
            }
        }
    }
*/


    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearUpload()
        }
    }
    val educationLevels = stringArrayResource(R.array.education_levels)

    val educationText = data?.educationLevel
        ?.toIntOrNull()
        ?.let { index ->
            if (index in educationLevels.indices) {
                educationLevels[index]
            } else {
                "-"
            }
        } ?: "-"

    val educationList = listOf(
        Items(
            stringResource(R.string.education_level),
            educationText
        )
    )


    val lifeStyleList = listOf<Items>(
        Items(
            stringResource(R.string.drinking),
            if (data?.howOftenDrink == "0") stringResource(R.string.never) else if (data?.howOftenDrink == "1") stringResource(R.string.occasional) else if (data?.howOftenDrink == "2") stringResource(R.string.regular) else "-"
        ),
        Items(
            stringResource(R.string.smoking),
            if (data?.howOftenSmoke == "0") stringResource(R.string.casual) else if (data?.howOftenSmoke == "1") stringResource(R.string.smoker) else if (data?.howOftenSmoke == "2") stringResource(R.string.trying_to_quit) else if (data?.howOftenSmoke == "3") stringResource(R.string.smoking_when_drinking) else if (data?.howOftenSmoke == "4") stringResource(R.string.never) else "-"
        ),
        Items(
            stringResource(R.string.workout),
            if (data?.workOut == "0") stringResource(R.string.everyday) else if (data?.workOut == "1") stringResource(R.string.often) else if (data?.workOut == "2") stringResource(R.string.sometimes) else if (data?.workOut == "3") stringResource(R.string.never) else "-"
        ),
    )
    val spokenLanguages = stringArrayResource(R.array.spoken_languages)

    val personalDetailsList = listOf<Items>(
    Items(
        stringResource(R.string.height),
        "${data?.height}  ${if (data?.heightType == "0") stringResource(R.string.cm) else stringResource(R.string.ft)}"
    ),
    Items(
        stringResource(R.string.interested_in),
        if (data?.interestedIn == "0") stringResource(R.string.male) else if (data?.interestedIn == "1") stringResource(R.string.female) else if (data?.interestedIn == "2") stringResource(R.string.everyone) else "-"
    ),
        Items(
            item = stringResource(R.string.language_spoken),
            value = data?.spokenLanguages?.mapNotNull { lang ->
                lang?.toIntOrNull()?.let { index ->
                    spokenLanguages.getOrNull(index)
                }
            }?.joinToString(", ") ?: "-"
        ),

        Items(
            stringResource(R.string.sect), getSectDisplayText(
                sectValue = data?.sect,
                customSect = data?.customSect,
                belongList = stringArrayResource(R.array.belong_list).toList())),
        Items(
            stringResource(R.string.marital_status),
            if (data?.maritalStatus == "0") stringResource(R.string.never_married) else if (data?.maritalStatus == "1") stringResource(R.string.divorced) else if (data?.maritalStatus == "2") stringResource(R.string.widowed) else if (data?.maritalStatus == "3") stringResource(R.string.separated) else "-"
        ),
        Items(
            stringResource(R.string.religion_practice),
            if (data?.religionPractice == "0") stringResource(R.string.very_practicing) else if (data?.religionPractice == "1") stringResource(R.string.practicing) else if (data?.religionPractice == "2") stringResource(R.string.moderately_practicing) else if (data?.religionPractice == "3") stringResource(R.string.cultural_muslim) else "-"
        ),
        Items(
            stringResource(R.string.children_status),
            if (data?.haveChildren == "0") stringResource(R.string.yes) else if (data?.haveChildren == "1") stringResource(R.string.no) else if (data?.haveChildren == "2") stringResource(R.string.prefer_not_to_say) else "-"
        ),
        Items(
            stringResource(R.string.relocation_after_marriage),
            if (data?.aboardAfterMarriage == "0") stringResource(R.string.yes) else if (data?.aboardAfterMarriage == "1") stringResource(R.string.no) else if (data?.aboardAfterMarriage == "2") stringResource(R.string.depends_on_spouse) else if (data?.aboardAfterMarriage == "3") stringResource(R.string.already_living_abroad) else "-"
        ))
    verticalSpace(20)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .background(
                    Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(vertical = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(20.sdp),
                    painter = painterResource(R.drawable.blue1),
                    contentDescription = "img"
                )
                Spacer(modifier = Modifier.height(5.sdp))

                Text(
                    text = stringResource(R.string.face_verified),
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )

            }


        }

        Spacer(modifier = Modifier.width(20.sdp))



        Row(
            modifier = Modifier
                .weight(1f)
                .background(
                    Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    SingletonObject.isFromOwnProfile=true
                    if (data?.isDocumentVerified == false) navController.navigate(Screen.UploadIdScreen.route) {
                        popUpTo(Screen.ViewProfileScreen.route) { inclusive = false }
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(vertical = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(20.sdp),
                    painter = painterResource(if (data?.isDocumentVerified == true) R.drawable.blue2 else R.drawable.upload_image_ic),
                    contentDescription = "img"
                )
                Spacer(modifier = Modifier.height(5.sdp))

                Text(
                    text = if (data?.isDocumentVerified == true) stringResource(R.string.age_verified) else stringResource(R.string.tab_to_age_verify),
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
            }


        }

    }

    Spacer(modifier = Modifier.height(20.sdp))
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(editableProfileChips) { index, data ->
            Text(
                text = data,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                color = if (index == viewModel.editProfileChipIndex.value) Color.White else Color(
                    0xFF590988
                ),
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource, indication = null
                    ) {
                        viewModel.editProfileChipIndex.value = index
                    }
                    .background(
                        brush = if (index == viewModel.editProfileChipIndex.value) Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF8B5DF6),
                                Color(0xFF8B5DF6).copy(alpha = 0.8f),
                                Color(0xFFF6A6D6).copy(alpha = 0.95f),
                            )

                        )
                        else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.White, Color.White
                                ),
                            )
                        }, shape = RoundedCornerShape(42.dp)
                    )
                    .padding(
                        horizontal = if (index == viewModel.editProfileChipIndex.value) (42.dp) else 10.dp,
                        vertical = (12.dp)
                    ))
        }
    }


    if (viewModel.editProfileChipIndex.value == 0) {
        var images by rememberSaveable(
            stateSaver = listSaver(
                save = { it.map { uri -> uri?.toString() } },
                restore = { it.map { s -> s?.toUri() } })
        ) { mutableStateOf(listOf<Uri?>(null)) }
        val pendingIndices = remember { mutableStateListOf<Int>() }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.upload_upto_9_photo_three_mandatory),
                color = MaterialTheme.colorScheme.onBackground ,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
            )
            verticalSpace(10)

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 2000.dp),
                contentPadding = PaddingValues(18.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(9) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(12.dp)
                            )
                    ) {

                        AsyncImage(
                            model = if (index < imageList.size) imageList[index]
                            else R.drawable.upload_image_ic,
                            "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    if (index < imageList.size == false) {
                                        selectedImgPosition = index
                                        viewModel.uploadSource = UploadSource.GRID_IMAGE
                                        imagePicker { uri ->
                                        }
                                    }

                                },

                            contentScale = if (index < imageList.size)ContentScale.Crop else ContentScale.None,
                            colorFilter = if (index >= imageList.size)
                                ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            else null
                        )

                        if (index < imageList.size) {
                            Image(
                                painter = painterResource(R.drawable.cross_red),
                                contentDescription = "Delete image",
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 2.dp, top = 2.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        if (index < imageList.size && imageList.size > 3) {
                                            imageList.removeAt(index)
                                            viewModel.hitUpdateProfile(
                                                access_token = SharedPreference.get(context).accessToken,
                                                request = UpdateProfileRequest(
                                                    personalDetails = UpdateProfileRequest.PersonalDetails(
                                                        images = imageList
                                                    )
                                                )
                                            )
                                        }

                                    },
                                contentScale = ContentScale.Fit
                            )
                        }



                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(Color(0XFFFFFFFF).copy(0.3f))
                                .padding(vertical = 5.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    if (index < imageList.size) {
                                        selectedImgPosition = index
                                        viewModel.uploadSource = UploadSource.GRID_IMAGE
                                        imagePicker { uri ->
                                        }
                                    }
                                }, contentAlignment = Alignment.Center
                        ) {

                            if (index < imageList.size) {
                                Image(
                                    painter = painterResource(R.drawable.edit_ic),
                                    "",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            selectedImgPosition = index
                                            viewModel.uploadSource = UploadSource.GRID_IMAGE
                                            imagePicker { uri ->
                                            }
                                        })
                            }

                        }

                    }
                }

            }
        }


    }
    else if (viewModel.editProfileChipIndex.value == 1) {
        val height = stringResource(R.string.height)
        val interestedIn = stringResource(R.string.interested_in)
        val language = stringResource(R.string.language_spoken)
        val sect = stringResource(R.string.sect)
        val maritalStatus = stringResource(R.string.marital_status)
        val religionPractice = stringResource(R.string.religion_practice)
        val childrenStatus = stringResource(R.string.children_status)
        val relocation = stringResource(R.string.relocation_after_marriage)

        verticalSpace(20)

        AboutEditRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            list = personalDetailsList,
            onClick = { item ->
                when (item.item) {

                   height -> onAboutEditClick(AboutEditType.HEIGHT)
                    interestedIn -> onAboutEditClick(AboutEditType.INTERESTED_IN)
                   language -> onAboutEditClick(AboutEditType.LANGUAGE)
                   sect -> onAboutEditClick(AboutEditType.SECT)
                   maritalStatus -> onAboutEditClick(AboutEditType.MARITAL_STATUS)
                    religionPractice -> onAboutEditClick(AboutEditType.RELIGION_PRACTICE)
                   childrenStatus -> onAboutEditClick(AboutEditType.CHILDREN_STATUS)
                   relocation-> onAboutEditClick(AboutEditType.RELOCATION)
                }
            }

        )



        Column(
            modifier = Modifier.fillMaxWidth()

        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp, end = 12.dp, top = 15.dp, bottom = 12.dp
                        )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        TextHeading(text = stringResource(R.string.habits))

                        Image(
                            painter = painterResource(R.drawable.edit_ic),
                            contentDescription = "edit",
                            modifier = Modifier
                                .size(15.sdp)
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    onAboutEditClick(AboutEditType.HABITS)


                                }

                        )

                    }
                    verticalSpace(10)

                    Row(modifier = Modifier.fillMaxWidth()) {


                        Row(modifier = Modifier.weight(1f)) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFD9C8FF),  // lavender
                                                Color(0xFFEFD8FF),  // soft pink-purple
                                                Color(0xFFFFEFF8)   // light peach
                                            )
                                        ), shape = RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .padding(
                                        horizontal = 12.dp, vertical = 8.dp
                                    )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.smoking_im),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = stringResource( R.string.smoking),
                                    color = Color(0xff6D6D6D),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = if (data?.howOftenSmoke == "0") stringResource(R.string.casual) else if (data?.howOftenSmoke == "1") stringResource(R.string.smoker) else if (data?.howOftenSmoke == "2") stringResource(R.string.trying_to_quit) else if (data?.howOftenSmoke == "3")stringResource(R.string.smoking_when_drinking) else if (data?.howOftenSmoke == "4") stringResource(R.string.never) else "-",
                                    color = Color(0xff590988),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                )
                            }

                        }
                        horizontalSpace(15)
                        Row(modifier = Modifier.weight(1f)) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFD9C8FF),  // lavender
                                                Color(0xFFEFD8FF),  // soft pink-purple
                                                Color(0xFFFFEFF8)   // light peach
                                            )
                                        ), shape = RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .padding(
                                        horizontal = 12.dp, vertical = 8.dp
                                    )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.glass_ic),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = stringResource( R.string.drinking),
                                    color = Color(0xff6D6D6D),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = if (data?.howOftenDrink == "0") stringResource(R.string.never) else if (data?.howOftenDrink == "1") stringResource(R.string.occasional) else if (data?.howOftenDrink == "2") stringResource(R.string.regular) else "-",
                                    color = Color(0xff590988),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                )
                            }

                        }
                    }

                    verticalSpace(20)
                    Row() {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFD9C8FF),  // lavender
                                            Color(0xFFEFD8FF),  // soft pink-purple
                                            Color(0xFFFFEFF8)   // light peach
                                        )
                                    ), shape = RoundedCornerShape(12.dp)
                                )
                                .padding(
                                    horizontal = 12.dp, vertical = 8.dp
                                )
                        ) {
                            Image(
                                painter = painterResource(R.drawable.dumble_ic),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = stringResource( R.string.workout),
                                color = Color(0xff6D6D6D),
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = if (data?.workOut == "0") stringResource(R.string.everyday) else if (data?.workOut == "1") stringResource(R.string.often) else if (data?.workOut == "2") stringResource(R.string.sometimes)else if (data?.workOut == "3") stringResource(R.string.never) else "-",
                                color = Color(0xff590988),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )
                        }
                    }
                }
            }
        }

        verticalSpace(20)

        val interestChips = data?.interests?.filterNotNull()?.flatMap { interest ->

            interest.tags?.filterNotNull()?.map { tag ->
                val languageManager = LocalLanguageManager.current

                val displayName =
                    if (languageManager.currentLanguage == "ar") {
                        tag.tagNameAr ?: tag.tagNameEn.orEmpty()
                    } else {
                        tag.tagNameEn.orEmpty()
                    }

                ChipItem(
                    tagId = tag.id.orEmpty(),
                    tagName = displayName,
                    iconUrl = tag.iconImage
                )
            } ?: emptyList()

        } ?: emptyList()

        CategoryEditCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            heading = stringResource( R.string.interests),
            chips = interestChips,
            isEditable = true,
            onClick = {
                onAboutEditClick(AboutEditType.INTEREST)
            })

        verticalSpace(15)
        val languageManager = LocalLanguageManager.current
        CategoryEditCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            heading = stringResource( R.string.faith_identity),
            chips = data?.faith?.filterNotNull()?.map {
                val displayName = if (languageManager.currentLanguage == "ar") {
                    it.faithNameAr ?: it.faithNameEn.orEmpty()
                } else {
                    it.faithNameEn.orEmpty()
                }
                ChipItem(
                    tagId = it.id.orEmpty(), tagName = displayName,
                    ""
                )
            } ?: emptyList(),   // ✅ IMPORTANT

            isEditable = true,
            onClick = {
                onAboutEditClick(AboutEditType.FAITH)
            })


        verticalSpace(15)


        categoryTextFieldCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            heading = stringResource(R.string.personality_bio),
            isEditable = true,
            text = data?.description ?: "n/a",
            onClick = {
                onAboutEditClick(AboutEditType.PERSONALITY)
            })


        verticalSpace(20)


    }
    else if (viewModel.editProfileChipIndex.value == 2) {

        verticalSpace(20)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp, end = 12.dp, top = 15.dp,
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextHeading(text = stringResource( R.string.education))
                        Image(
                            painter = painterResource(R.drawable.edit_ic),
                            contentDescription = "edit",
                            modifier = Modifier
                                .size(15.sdp)
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    SingletonObject.isFromEditProfile = true
                                    navController.navigate(Screen.CompleteProfile3.route) {
                                        popUpTo(Screen.ViewProfileScreen.route) {
                                            inclusive = false
                                        }
                                    }
                                }

                        )

                    }
                    Spacer(Modifier.height(10.dp))

                    educationList.forEach { it ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(it.item, color = Color(0xff6D6D6D), fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.axiforma_regular)))

                            Text(
                                it.value,
                                modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onBackground ,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.axiforma_regular)
                                ),
                                textAlign = TextAlign.End
                            )
                        }
                        verticalSpace(15)

                    }

                }


            }
            verticalSpace(20)

        }


    }

}


@Composable
fun PreviewHeader(
    viewModel: AuthViewModel,

    pagerState: PagerState, profileImages: List<String>, parentListState: LazyListState
) {
    val data = viewModel.getPreviewProfileData.value
   val  languageManager= LocalLanguageManager.current
    Box(modifier = Modifier.fillMaxWidth()) {

        HorizontalPager(
            state = pagerState
        ) { page ->

            if (profileImages.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(profileImages[page])
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.no_dp_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }


    }

    Column(
        modifier = Modifier
            .offset(y = -10.sdp)
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    topStart = 24.dp, topEnd = 24.dp
                )
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 16.dp, end = 16.dp, top = 20.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${data?.firstName.orEmpty()} ${data?.lastName.orEmpty()}",
                color = Color(0xFF590988),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                maxLines = 1,
            )


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ",${data?.age ?: 0}",
                    color = Color(0xFF590988),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                )

                Spacer(modifier = Modifier.width(4.dp))

                if (data?.personalDetails?.isFaceVerified == true) {

                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.blue1),
                        contentDescription = ""
                    )

                }

                Spacer(modifier = Modifier.width(4.dp))

                if (data?.personalDetails?.isDocumentVerified == true) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.blue2),
                        contentDescription = ""
                    )

                }

            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .background(
                        color = Color(0xFFCCE1EFE0), shape = RoundedCornerShape(48.dp)
                    )
                    .padding(
                        horizontal = 8.sdp, vertical = 7.sdp
                    ),
                text = stringResource( R.string.active),
                textAlign = TextAlign.Center,
                color = Color(0xFF128807),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
            )
        }


        Spacer(modifier = Modifier.height(10.sdp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!data?.countryName.isNullOrEmpty()) {
                val context = LocalContext.current
                val countryNamesEn =
                    remember { CountryListHelper.getEnglishCountryNames(context) }
                val isoCodes = CommonResource().countryIsoCodes

                // Find index using English name (since API returns English)
                val index = countryNamesEn.indexOfFirst {
                    it.equals(data?.countryName ?: "", ignoreCase = true)
                }

                if (index >= 0 && index < isoCodes.size) {
                    Text(
                        text = countryCodeToFlagEmoji(isoCodes[index]),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }

            Spacer(modifier = Modifier.width(5.sdp))

            Text(

                "${ if(languageManager.currentLanguage=="en") data ?.city ?: "" else data?.cityAr?:data?.city} ,${if(languageManager.currentLanguage=="en")data?.countryName ?: "" else data?.countryNameAr?:data?.countryName}",
                color = Color(0xFF590988),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium)),)

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.sdp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)   // 🔥 equal gap between items
        ) {


            if (data?.personalDetails?.isDocumentVerified == true) {

                Row(
                    modifier = Modifier
                        .background(
                            Color(0xFF1F128807), RoundedCornerShape(8.dp)
                        )
                        .padding(
                            vertical = 8.sdp, horizontal = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.tick_verified_black),
                        contentDescription = "",
                        modifier = Modifier.size(12.sdp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground,)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource( R.string.age_verified),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )
                }

            }

            if (data?.personalDetails?.isDocumentVerified == true) {


                Row(
                    modifier = Modifier

                        .background(
                            Color(0xFF1F128807), RoundedCornerShape(8.dp)
                        )
                        .padding(
                            vertical = 8.sdp, horizontal = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.smile_face_ic),
                        contentDescription = "",
                        modifier = Modifier.size(12.sdp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground,)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource( R.string.id_verified),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )
                }


            }

            if (data?.personalDetails?.isFaceVerified == true) {

                Row(
                    modifier = Modifier

                        .background(
                            Color(0xFF1F128807), RoundedCornerShape(8.dp)
                        )
                        .padding(
                            vertical = 8.sdp, horizontal = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.smile_face_ic),
                        contentDescription = "",
                        modifier = Modifier.size(12.sdp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground,)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text =  stringResource(R.string.face_verified),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )
                }
            }

        }


        Spacer(modifier = Modifier.height(10.sdp))


        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color =  MaterialTheme.colorScheme.outlineVariant

        )

        Spacer(modifier = Modifier.height(10.sdp))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(50.dp)
        ) {

            Row(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(R.drawable.gender_ic),
                    contentDescription = "",
                    modifier = Modifier.size(20.sdp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground,)
                )
                Spacer(modifier = Modifier.width(8.dp))

                val genderText = when (data?.gender?.lowercase()) {
                    "male" -> stringResource(R.string.male)
                    "female" -> stringResource(R.string.female)
                    else -> ""
                }
                Text(
                    text =  genderText,
                    color= MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(R.drawable.height_ic),
                    contentDescription = "",
                    modifier = Modifier.size(20.sdp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground,)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${data?.personalDetails?.height} ${if (data?.personalDetails?.heightType == "0") stringResource( R.string.cm) else stringResource( R.string.ft)}",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
            }

        }

        Spacer(modifier = Modifier.height(10.sdp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant

        )

    }
}


@Composable
fun CategoryEditCard(
    modifier: Modifier = Modifier,
    heading: String = "",
    chips: List<ChipItem> = emptyList(),
    isEditable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 15.dp, bottom = 12.dp)
        ) {

            // ===== HEADER =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextHeading(text = heading)

                if (isEditable) {
                    Image(
                        painter = painterResource(R.drawable.edit_ic),
                        contentDescription = "edit",
                        modifier = Modifier
                            .size(15.sdp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onClick() })
                }
            }

            verticalSpace(10)

            // ===== CHIPS =====
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chips.forEach { chip ->
                    SelectedChip(
                        text = chip.tagName, iconURl = chip.iconUrl.toString()
                    )
                }
            }
        }
    }
}


@Composable
fun categoryTextFieldCard(
    modifier: Modifier = Modifier,
    heading: String,
    text: String,
    isEditable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface

        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp, end = 12.dp, top = 15.dp, bottom = 12.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextHeading(text = heading)
                if (isEditable) {
                    Image(
                        painter = painterResource(R.drawable.edit_ic),
                        contentDescription = "edit",
                        modifier = Modifier
                            .size(15.sdp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onClick() }

                    )
                }
            }
            verticalSpace(10)

            Text(
                text = text,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color =  Color(0xFF6D6D6D),
                lineHeight = 18.sp
            )

        }
    }

}


@Composable
fun AboutEditRow(modifier: Modifier = Modifier, list: List<Items>, onClick: (Items) -> Unit = {}) {
    list.forEach {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 14.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = it.item,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                color = Color(0xFF6D6D6D),
            )
            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = it.value.formatTitle(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                    color = MaterialTheme.colorScheme.onBackground,
                )
                horizontalSpace(8)

                Image(
                    painter = painterResource(R.drawable.edit_ic),
                    contentDescription = "edit",
                    modifier = Modifier
                        .size(15.sdp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            onClick(
                                it
                            )
                        }

                )


            }


        }
        verticalSpace(15)
    }
}


@Composable
fun EducationSection(list: List<Items>) {


    Column(
        modifier = Modifier.fillMaxWidth()

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface

            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 15.dp,
                    )
            ) {
                TextHeading(text = stringResource( R.string.education))
                Spacer(Modifier.height(10.dp))

                list.forEach { it ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Text(
                            it.item,
                            color = Color(0xff6D6D6D),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(
                                Font(R.font.axiforma_regular)
                            )
                        )
                        Text(
                            it.value.formatTitle(),
                            modifier = Modifier.weight(1f),
                            MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(
                                Font(R.font.axiforma_regular)
                            ),
                            textAlign = TextAlign.End
                        )
                    }
                    verticalSpace(15)

                }
                //  verticalSpace(10)

            }


        }

        verticalSpace(20)
    }
}

@Composable
fun LifestyleSection(list: List<Items>) {


    Column(
        modifier = Modifier.fillMaxWidth()

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface

            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 15.dp,
                    )
            ) {
                TextHeading(text = stringResource(R.string.life_style))
                Spacer(Modifier.height(10.dp))

                list.forEach { it ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Text(
                            it.item,
                            color = Color(0xff6D6D6D),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(
                                Font(R.font.axiforma_regular)
                            )
                        )
                        Text(
                            it.value.formatTitle(),
                            modifier = Modifier.weight(1f),
                            color = Color(0xff000000),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(
                                Font(R.font.axiforma_regular)
                            ),
                            textAlign = TextAlign.End
                        )
                    }
                    verticalSpace(15)

                }
                //  verticalSpace(10)

            }


        }

        verticalSpace(15)
    }
}


@Composable
fun PersonalitySection(text: String) {

    Column(
        modifier = Modifier.fillMaxWidth()

    ) {
        categoryTextFieldCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            heading = stringResource(R.string.personality_bio),
            text = text

        )

        verticalSpace(20)
    }
}


@Composable
fun AboutSection(list: List<Items>) {


    Column(
        modifier = Modifier.fillMaxWidth()

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface

            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 15.dp,
                    )
            ) {
                TextHeading(text = stringResource( R.string.about))
                Spacer(Modifier.height(10.dp))

                list.forEach { it ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Text(
                            it.item,
                            color = Color(0xff6D6D6D),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(
                                Font(R.font.axiforma_regular)
                            )
                        )
                        Text(
                            it.value,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(
                                Font(R.font.axiforma_regular)
                            ),
                            lineHeight = 18.sp,
                            textAlign = TextAlign.End
                        )
                    }

                    verticalSpace(15)

                }
                //  verticalSpace(10)

            }


        }


    }
}

@Composable
fun PreviewStickyChips(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onChipClick: (Int) -> Unit,
    list: List<String>
) {

    val interactionSource = remember { MutableInteractionSource() }
    val axiformaFont = remember {
        FontFamily(Font(R.font.axiforma_medium))
    }
    val isDark = isAppInDarkTheme()

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        itemsIndexed(
            items = list, key = { index, _ -> index }) { index, text ->

            val isSelected = index == selectedIndex
            val chipTextColor = if (isSelected) Color.White else (if (isDark) Color.White else Color(0xFF590988))
            val chipBrush = if (isSelected) {
                Brush.linearGradient(listOf(Color(0xFF8B5DF6), Color(0xFFF6A6D6)))
            } else if (isDark) {
                Brush.linearGradient(listOf(Color(0xFF242424), Color(0xFF242424)))
            } else {
                Brush.linearGradient(listOf(Color.White, Color.White))
            }

            Text(
                text = text,
                fontFamily = axiformaFont,
                color = chipTextColor,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable(
                        indication = null, interactionSource = interactionSource
                    ) {
                        if (selectedIndex != index) {
                            onChipClick(index)
                        }
                    }
                    .background(
                        brush = chipBrush,
                        shape = RoundedCornerShape(42.dp)
                    )
                    .then(
                        if (!isSelected && isDark) Modifier.border(1.dp, Color(0xFF333333), RoundedCornerShape(42.dp))
                        else Modifier
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp))
        }
    }
}


@Composable
fun AboutEditBottomSheet(
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    viewModel5: M5ViewModel,
    context: Context,
    type: AboutEditType,
    onClose: () -> Unit
) {
    val data = viewModel.getPreviewProfileData.value
    val existing = viewModel.getPreviewProfileData.value?.personalDetails

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
                    AboutEditType.HEIGHT -> stringResource(R.string.edit_height)
                    AboutEditType.INTERESTED_IN -> stringResource(R.string.edit_interested_in)
                    AboutEditType.LANGUAGE -> stringResource(R.string.edit_language)
                    AboutEditType.SECT -> stringResource(R.string.edit_sect)
                    AboutEditType.MARITAL_STATUS -> stringResource(R.string.edit_marital_status)
                    AboutEditType.RELIGION_PRACTICE -> stringResource(R.string.edit_religion_practice)
                    AboutEditType.CHILDREN_STATUS -> stringResource(R.string.edit_children_status)
                    AboutEditType.RELOCATION -> stringResource(R.string.edit_relocation)
                    AboutEditType.PERSONALITY -> stringResource(R.string.edit_bio)
                    AboutEditType.FAITH -> stringResource(R.string.edit_faith_identity)
                    AboutEditType.HABITS -> stringResource(R.string.edit_habits)
                    AboutEditType.INTEREST -> stringResource(R.string.edit_interest)
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
                        interactionSource = remember { MutableInteractionSource() }) { onClose() })
        }

        when (type) {
            AboutEditType.HEIGHT -> {
                fun unitToApiCode(unit: String): String {
                    return if (unit == "CM") "0" else "1"
                }

                LaunchedEffect(
                    data?.personalDetails?.height, data?.personalDetails?.heightType
                ) {
                    viewModel.setHeightFromApi(
                        value = data?.personalDetails?.height,
                        unit = data?.personalDetails?.heightType
                    )

                }

                HeightBottomSheet(
                    heightValue = viewModel.tempHeightValue.value,
                    unitValue = viewModel.tempHeightUnit.value,
                    onHeightChange = { viewModel.onHeightChange(it) },
                    onUnitChange = { viewModel.onHeightUnitChange(it) },
                    onDone = {
                        viewModel.submitHeight(context) {
                            viewModel.hitUpdateProfile(
                                access_token = SharedPreference.get(context).accessToken,
                                request = UpdateProfileRequest(
                                    personalDetails = UpdateProfileRequest.PersonalDetails(
                                        height = viewModel.heightValue.value,
                                        heightType = unitToApiCode(viewModel.heightUnit.value)
                                    )
                                )
                            )

                            onClose()
                        }
                    })
            }

            AboutEditType.INTERESTED_IN -> {
                val apiIndex = viewModel.getPreviewProfileData.value?.personalDetails?.interestedIn

                LaunchedEffect(apiIndex) {
                    viewModel.setInterestedInFromApi(apiIndex?.toIntOrNull())
                }

                InterestedInBottomSheet(
                    items =   stringArrayResource(R.array.interested_list).toList(),
                    selectedIndex = viewModel.tempInterestedInIndex.value,
                    onItemSelected = { viewModel.onInterestedInIndexSelect(it) },
                    onDone = {
                        viewModel.submitInterestedIn(context) {
                            onClose()
                        }
                    })
            }

            AboutEditType.LANGUAGE -> {

                val apiLanguages =
                    viewModel.getPreviewProfileData.value?.personalDetails?.spokenLanguages

                LaunchedEffect(apiLanguages) {
                    viewModel.setLanguagesFromApi(apiLanguages)
                }

                LanguageBottomSheet(

                    items = stringArrayResource(R.array.spoken_languages).toList(),
                    selectedIndexes = viewModel.tempLanguageIndexes,
                    onItemSelected = { index ->
                        viewModel.onLanguageIndexToggle(index)
                    },
                    onDone = {
                        viewModel.submitLanguage(context) {
                            onClose()
                        }
                    }

                )
            }

            AboutEditType.SECT -> {

                val personalDetails =
                    viewModel.getPreviewProfileData.value?.personalDetails

                LaunchedEffect(personalDetails) {

                    val rawSect = personalDetails?.sect

                    val indexFromApi = rawSect?.toIntOrNull()

                    val customTextFromApi =
                        if (indexFromApi == null && !rawSect.isNullOrBlank()) {
                            rawSect          // 👈 "hello"
                        } else {
                            personalDetails?.customSect
                        }

                    viewModel.setSectFromApi(
                        indexFromApi = indexFromApi ?: viewModel.belongList.lastIndex,
                        customTextFromApi = customTextFromApi
                    )
                }

                SectBottomSheet(
                    items = stringArrayResource(R.array.belong_list).toList(),
                    selectedIndex = viewModel.tempSectIndex.value,
                    customSectText = viewModel.customSectText,
                    onCustomSectChange = { viewModel.onCustomSectChange(it) },
                    onItemSelected = { viewModel.onSectIndexSelect(it) },
                    onDone = {
                        viewModel.submitSect(context) {
                            onClose()
                        }
                    }
                )
            }


            AboutEditType.MARITAL_STATUS -> {

                val apiIndex =
                    viewModel.getPreviewProfileData.value?.personalDetails?.maritalStatus   // 👈 API index

                LaunchedEffect(apiIndex) {
                    viewModel.setMaritalStatusFromApi(apiIndex?.toIntOrNull())
                }

                MaritalStatusBottomSheet(
                    item = stringArrayResource(R.array.marital_status_list).toList(),
                    selectedIndex = viewModel.tempMaritalStatusIndex.value,
                    onItemSelected = { viewModel.onMaritalStatusIndexSelect(it) },
                    onDone = {
                        viewModel.submitMaritalStatus(context) {
                            onClose()
                        }
                    })
            }

            AboutEditType.RELIGION_PRACTICE -> {
                val apiIndex =
                    viewModel.getPreviewProfileData.value?.personalDetails?.religionPractice   // 👈 API index

                LaunchedEffect(apiIndex) {
                    viewModel.setReligionPracticeFromApi(apiIndex?.toIntOrNull())
                }

                ReligionPracticeBottomSheet(
                    item = stringArrayResource(R.array.religious_practice_list).toList(),
                    selectedIndex = viewModel.tempReligionPracticeIndex.value,
                    onItemSelected = { viewModel.onReligionPracticeIndexSelect(it) },
                    onDone = {
                        viewModel.submitReligionPractice(context) {
                            onClose()
                        }
                    })
            }


            AboutEditType.CHILDREN_STATUS -> {

                val apiIndex =
                    viewModel.getPreviewProfileData.value?.personalDetails?.haveChildren   // 👈 API index

                LaunchedEffect(apiIndex) {
                    viewModel.setHalalFoodFromApi(apiIndex?.toIntOrNull())
                }

                HalalFoodBottomSheet(
                    viewModel = viewModel,
                    selectedIndex = viewModel.tempHalalFoodIndex.value,
                    onItemSelected = { viewModel.onHalalFoodIndexSelect(it) },
                    onDone = {
                        viewModel.submitHalalFood(context) {
                            onClose()
                        }
                    })
            }

            AboutEditType.RELOCATION -> {

                val apiIndex =
                    viewModel.getPreviewProfileData.value?.personalDetails?.aboardAfterMarriage   // 👈 API index

                LaunchedEffect(apiIndex) {
                    viewModel.setRelocationFromApi(apiIndex?.toIntOrNull())
                }

                RelocationBottomSheet(
                    viewModel = viewModel,
                    selectedIndex = viewModel.tempRelocationIndex.value,
                    onItemSelected = { viewModel.onRelocationIndexSelect(it) },
                    onDone = {
                        viewModel.submitRelocation(context) {
                            onClose()
                        }
                    })
            }


            AboutEditType.PERSONALITY -> {

                LaunchedEffect(Unit) {
                    viewModel.resetBioInitFlag()  // ✅ Allow re-init every time sheet opens
                }
                LaunchedEffect(data?.personalDetails?.description) {
                    viewModel.setPersonalityBioFromApi(
                        data?.personalDetails?.description
                    )
                }

                PersonalityBioBottomSheet(
                    value = viewModel.personalityBio,
                    onValueChange = { viewModel.onPersonalityBioChange(it) },
                    onDone = {

                        viewModel.previousPersonalityBio =
                            viewModel.getPreviewProfileData.value?.personalDetails?.description ?: ""

                        viewModel5.hitCheckAbusiveWord(SharedPreference.get(context).accessToken,viewModel.personalityBio.trim())

                     /*   viewModel.hitUpdateProfile(
                            access_token = SharedPreference.get(context).accessToken,
                            request = UpdateProfileRequest(
                                personalDetails = UpdateProfileRequest.PersonalDetails(
                                    description = viewModel.personalityBio.trim()
                                )
                            )
                        )*/

                        onClose()
                    }

                )


            }


            AboutEditType.FAITH -> {

                LaunchedEffect(data?.personalDetails?.faith) {
                    viewModel.setFaithFromApi(data?.personalDetails?.faith)
                }

                FaithBottomSheet(
                    faithItems = viewModel.getFaithList.map {
                        FaithItem(
                            id = it?.id.orEmpty(),
                            title = it?.faithNameEn.orEmpty(),
                            titleAr = it?.faithNameAr // ✅ Add Arabic name
                        )
                    },
                    selectedIds = viewModel.tempFaithIds,
                    onSelectionChanged = {
                        viewModel.onFaithSelectionChange(it)
                    },
                    onDone = {
                        viewModel.submitFaith(context) {
                            onClose()
                        }
                    }
                )
            }


            AboutEditType.HABITS -> {

                LaunchedEffect(
                    data?.personalDetails?.howOftenDrink,
                    data?.personalDetails?.howOftenSmoke,
                    data?.personalDetails?.workOut
                ) {
                    viewModel.setHabitsFromApi(
                        drink = data?.personalDetails?.howOftenDrink,
                        smoke = data?.personalDetails?.howOftenSmoke,
                        workout = data?.personalDetails?.workOut
                    )
                }
                HabitsBottomSheet(
                    selectedDrink = viewModel.tempDrinkIndex.value,
                    selectedSmoke = viewModel.tempSmokeIndex.value,
                    selectedWorkOut = viewModel.tempWorkoutIndex.value,

                    onDrinkChange = { viewModel.onDrinkIndexChange(it) },
                    onSmokeChange = { viewModel.onSmokeIndexChange(it) },
                    onWorkoutChange = { viewModel.onWorkoutIndexChange(it) },

                    onDone = {
                        viewModel.submitHabits(context) {
                            onClose()
                        }
                    })
            }

            AboutEditType.INTEREST -> {

                LaunchedEffect(data?.personalDetails?.interests) {
                    viewModel.setInterestsFromApi(
                        data?.personalDetails?.interests
                    )
                }

                InterestBottomSheet(
                    viewModel = viewModel,
                    viewModelM4 = viewModelM4,
                    selectedInterests = viewModel.tempInterestIds.value,
                    onSelectionChanged = {
                        viewModel.tempInterestIds.value = it
                    },
                    onDone = {
                        viewModel.submitInterestUpdate(context)
                        onClose()
                    })
            }


        }
    }
}

@Composable
fun HeightBottomSheet(
    heightValue: String,
    unitValue: String,
    onHeightChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        verticalSpace(10)
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            verticalSpace(10)

            OutlinedTextField(
                value = heightValue,
                onValueChange = { onHeightChange(it) },
                placeholder = { Text(stringResource(R.string.enter_height)) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE0E0E0), unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            UnitChip(
                text = stringResource(R.string.cm),
                selected = unitValue == "CM",
                onClick = { onUnitChange("CM") }       // 👈 send to parent
            )

            UnitChip(
                text =  stringResource(R.string.ft),
                selected = unitValue == "FT",
                onClick = { onUnitChange("FT") }       // 👈 send to parent
            )
        }

        verticalSpace(20)
        AppButton(
            modifier = Modifier.padding(horizontal = 16.dp), text = stringResource(R.string.done), onClick = onDone
        )

    }
}

@Composable
fun SectBottomSheet(
    items: List<String>,
    selectedIndex: Int,
    customSectText: String,
    allowUnselect: Boolean = false, // 🔑 KEY
    onCustomSectChange: (String) -> Unit,
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

                items.forEachIndexed { index, item ->

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

                // 👇 Custom input ONLY when index 3 is selected
                if (selectedIndex == 3) {
                    CustomInputField(
                        modifier = Modifier.padding(
                            start = 15.dp,
                            end = 15.dp,
                            top = 10.dp,
                            bottom = 15.dp
                        ),
                        heading = stringResource( R.string.what_sect_do_you_belong_to),
                        placeholder = stringResource( R.string.type_your_sect),
                        value = customSectText,
                        onValueChange = {
                            onCustomSectChange(
                                onlyAlphabetsNoInitial(it)
                            )
                        }
                    )
                }
            }
        }

        verticalSpace(20)

        AppButton(
            text = "${stringResource( R.string.done)}",
            onClick = onDone
        )
    }
}


@Composable
fun RelocationBottomSheet(
    viewModel: AuthViewModel, selectedIndex: Int, onItemSelected: (Int) -> Unit, onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .padding(vertical = (10.dp))
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                stringArrayResource(R.array.relocation_preference_list).toList(). forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null, interactionSource = MutableInteractionSource()
                            ) {
                                onItemSelected(index)   // ✅ send index to parent
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
        verticalSpace(20)
        AppButton(
            modifier = Modifier, text = stringResource(R.string.done), onClick = onDone
        )
    }
}

@Composable
fun HalalFoodBottomSheet(
    viewModel: AuthViewModel, selectedIndex: Int, onItemSelected: (Int) -> Unit, onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .padding(vertical = (10.dp))
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                stringArrayResource(R.array.children_list).toList(). forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null, interactionSource = MutableInteractionSource()
                            ) {
                                onItemSelected(index)   // ✅ send index to parent
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
        verticalSpace(20)
        AppButton(
            modifier = Modifier, text = stringResource(R.string.done), onClick = onDone
        )

    }
}

@Composable
fun ReligionPracticeBottomSheet(
    item: List<String>, selectedIndex: Int, onItemSelected: (Int) -> Unit, onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .padding(vertical = (10.dp))
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                item.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null, interactionSource = MutableInteractionSource()
                            ) {
                                onItemSelected(index)   // ✅ send index to parent
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
        verticalSpace(20)
        AppButton(
            modifier = Modifier, text = stringResource(R.string.done), onClick = onDone
        )
    }

}

@Composable
fun MaritalStatusBottomSheet(
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .padding(vertical = 10.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                item.forEachIndexed { index, text ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {

                                // 🔥 TOGGLE LOGIC
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
fun LanguageBottomSheet(
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
            colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            SearchBar(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
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
                            text = stringResource( R.string.language_not_found),
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

                                    if (!isSelected && selectedIndexes.size >= 10) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.you_can_select_maximum_10_languages),
                                            Toast.LENGTH_SHORT
                                        ).show()
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
            text = "${stringResource(R.string.done)}(${selectedIndexes.size}/10)",
            onClick = onDone
        )
    }
}

@Composable
fun InterestedInBottomSheet(
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
                    .padding(top = 10.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                items.forEachIndexed { index, item ->

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
            text = stringResource( R.string.done),
            onClick = onDone
        )
    }
}


@Composable
fun PersonalityBioBottomSheet(
    value: String, onValueChange: (String) -> Unit, onDone: () -> Unit
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
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .padding(vertical = 10.dp, horizontal = 12.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.write_here), color = Color(0x33000000)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFDDDDDD),
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        cursorColor = MaterialTheme.colorScheme.onBackground
                    ),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onDone() })
                )

            }

        }
        verticalSpace(20)
        AppButton(
            modifier = Modifier, text = stringResource(R.string.done), onClick = onDone
        )

    }
}

@Composable
fun FaithBottomSheet(
    faithItems: List<FaithItem>,
    selectedIds: List<String>,
    onSelectionChanged: (List<String>) -> Unit,
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
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .padding(top = 10.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                FaithMultiSelectTickContent(
                    faithItems = faithItems,
                    selectedIds = selectedIds,
                    onSelectionChanged = onSelectionChanged
                )

                verticalSpace(20)
            }
        }

        verticalSpace(20)

        AppButton(
            text = stringResource(
             R.string.done),
            onClick = onDone
        )
    }
}

/*
@Composable
fun FaithMultiSelectTickContent(
    faithItems: List<FaithItem>,
    selectedIds: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    Column {

        faithItems.forEach { item ->

            val isSelected = selectedIds.firstOrNull() == item.id

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        val updated =
                            if (isSelected) {
                                emptyList()              // ❌ deselect
                            } else {
                                listOf(item.id)          // ✅ single select
                            }

                        onSelectionChanged(updated)
                    }
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

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
    }
}
*/


@Composable
fun FaithMultiSelectTickContent(
    faithItems: List<FaithItem>,
    selectedIds: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    val languageManager = LocalLanguageManager.current // ✅ Add language manager

    Column {

        faithItems.forEach { item ->

            val isSelected = selectedIds.firstOrNull() == item.id

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        val updated =
                            if (isSelected) {
                                emptyList()              // ❌ deselect
                            } else {
                                listOf(item.id)          // ✅ single select
                            }

                        onSelectionChanged(updated)
                    }
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ✅ Show title based on current language
                val displayTitle = if (languageManager.currentLanguage == "ar") {
                    item.titleAr ?: item.title // Fallback to English if Arabic is null
                } else {
                    item.title
                }

                Text(
                    text = displayTitle, // ✅ Use displayTitle instead of item.title
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
    }
}

@Composable
fun HabitsBottomSheet(
    selectedDrink: Int,
    selectedSmoke: Int,
    selectedWorkOut: Int,
    onDrinkChange: (Int) -> Unit,
    onSmokeChange: (Int) -> Unit,
    onWorkoutChange: (Int) -> Unit,
    onDone: () -> Unit
) {

    val drinkingFrequencyList =  stringArrayResource(R.array.drinking_frequency_list).toList()
    val smokingFrequencyList = stringArrayResource(R.array.smoking_frequency_list).toList()
    val workoutFrequencyList = stringArrayResource(R.array.workout_frequency_list).toList()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 420.dp)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                verticalSpace(20)

                GradientExpandableCardIndex(
                    title = stringResource( R.string.how_often_do_you_drink),
                    items = drinkingFrequencyList,
                    selectedIndex = selectedDrink,
                    onItemSelected = { onDrinkChange(it) })

                verticalSpace(20)

                GradientExpandableCardIndex(
                    title = stringResource( R.string.how_often_do_you_smoke),
                    items = smokingFrequencyList,
                    selectedIndex = selectedSmoke,
                    onItemSelected = { onSmokeChange(it) })

                verticalSpace(20)

                GradientExpandableCardIndex(
                    title = stringResource( R.string.how_often_do_you_workout),
                    items = workoutFrequencyList,
                    selectedIndex = selectedWorkOut,
                    onItemSelected = { onWorkoutChange(it) })

                verticalSpace(20)
            }
        }

        verticalSpace(20)

        AppButton(
            text = stringResource( R.string.done), onClick = onDone
        )
    }
}

fun String.formatTitle(): String {
    return this.replace("_", " ")          // remove underscore
        .lowercase().split(" ").filter { it.isNotBlank() }.joinToString(" ") { word ->
            word.replaceFirstChar { it.titlecase() }
        }
}

fun PreviewProfileResponse.Data.PersonalDetails.Faith.toFaithItem(): FaithItem {
    return FaithItem(
        id = this.id.orEmpty(),
        title = this.faithNameEn.orEmpty(),
        titleAr = this.faithNameAr.orEmpty()// ya Ar if needed
    )
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestBottomSheet(
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    selectedInterests: List<SelectedInterest>,
    onSelectionChanged: (List<SelectedInterest>) -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lifecycleOwner = LocalLifecycleOwner.current

    var searchText by rememberSaveable { mutableStateOf("") }

    val originalList = viewModel.categoryBackup

    var filteredCategoryList by remember {
        mutableStateOf<List<GetAllCategoriesResponse.Data?>>(originalList)
    }
    val categoryList = viewModel.categoryBackup

    LaunchedEffect(Unit) {
        if (categoryList.isEmpty()) {
            viewModel.hitGetAllCategoriesStep6(
                SharedPreference.get(context).accessToken
            )
        }
    }
    LaunchedEffect(originalList) {
        filteredCategoryList = originalList.toList()
    }


    var selectedChips by remember { mutableStateOf<List<ChipItem>>(emptyList()) }
    LaunchedEffect(Unit) {
        viewModel.prepareInterestedInEdit()
    }

    // ============================
    // PREPARE SELECTED CHIPS
    // ============================
    LaunchedEffect(selectedInterests, originalList) {
        selectedChips = selectedInterests.flatMap { selected ->
            val category = originalList.find { it?.id == selected.categoryId }
            selected.tagIds.mapNotNull { tagId ->
                val tag = category?.tags?.find { it?.id == tagId }
                tag?.let {
                    ChipItem(
                        tagId = tagId, tagName = it.tagName?.en.orEmpty(), iconUrl = it.iconImage
                    )
                }
            }
        }
    }

    // ============================
    // RECENT SEARCH OBSERVER
    // ============================
    LaunchedEffect(Unit) {
        viewModel.getRecentSearchTag.observe(lifecycleOwner) { state ->
            if (state is EmpResource.Success) {

                val searched = state.value.data?.filterNotNull().orEmpty()

                filteredCategoryList = originalList.mapNotNull { category ->
                    if (category == null) return@mapNotNull null

                    val matchedTags = category.tags?.filter { tag ->
                        searched.any { it.tagNameEn == tag?.tagName?.en }
                    }

                    if (!matchedTags.isNullOrEmpty()) {
                        category.copy(tags = matchedTags)
                    } else null
                }
            }
        }
    }


    BoxWithConstraints {

        val max = this.maxHeight

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                stringResource( R.string.select_up_to_10),
                fontSize = 12.sp,
                color = Color(0xff7C7C7C),
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )

            Spacer(Modifier.height(8.dp))

            SearchBar(
                value = searchText,
                onValueChange = {
                    searchText = it

                    if (it.length >= 2) {
                        viewModel.hitGetRecentSearchTag(
                            SharedPreference.get(context).accessToken, it
                        )
                    } else {
                        filteredCategoryList = originalList.toList()
                    }
                })

            Spacer(Modifier.height(20.dp))

            if (selectedChips.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    selectedChips.forEach { chip ->
                        Chips(
                            text = chip.tagName, iconUrl = chip.iconUrl ?: "", onClose = {
                                val updated = selectedInterests.mapNotNull { selected ->
                                    if (!selected.tagIds.contains(chip.tagId)) selected
                                    else {
                                        val newTags = selected.tagIds.filterNot { it == chip.tagId }
                                        if (newTags.isEmpty()) null
                                        else selected.copy(tagIds = newTags)
                                    }
                                }
                                onSelectionChanged(updated)
                            })
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            filteredCategoryList.forEach { category ->
                if (category == null) return@forEach

                val categoryId = category.id ?: return@forEach
                val tags = category.tags?.filterNotNull().orEmpty()
                if (tags.isEmpty()) return@forEach

                Text(
                    text = category.categoryName?.en.orEmpty(),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_bold))
                )

                Spacer(Modifier.height(14.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp)) {

                    FlowRow(
                        maxItemsInEachRow = 3,
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {

                        tags.forEach { tag ->
                            val tagId = tag.id ?: return@forEach
                            val isSelected = selectedInterests.any {
                                it.categoryId == categoryId && it.tagIds.contains(tagId)
                            }

                            CommonSelection(
                                text = tag.tagName?.en.orEmpty(),
                                iconUrl = tag.iconImage.orEmpty(),
                                selected = isSelected,
                                onToggle = {

                                    val totalSelected = selectedInterests.sumOf { it.tagIds.size }
                                    if (!isSelected && totalSelected >= 10) {
                                        context.showToast(context.getString(R.string.you_can_select_maximum_10_tags))
                                        return@CommonSelection
                                    }

                                    val updated = selectedInterests.toMutableList()
                                    val existing = updated.find { it.categoryId == categoryId }

                                    if (existing != null) {
                                        val newTags = existing.tagIds.toMutableList()
                                        if (newTags.contains(tagId)) newTags.remove(tagId)
                                        else newTags.add(tagId)

                                        updated.remove(existing)
                                        if (newTags.isNotEmpty()) {
                                            updated.add(existing.copy(tagIds = newTags))
                                        }
                                    } else {
                                        updated.add(
                                            SelectedInterest(
                                                categoryId = categoryId, tagIds = listOf(tagId)
                                            )
                                        )
                                    }

                                    onSelectionChanged(updated)
                                }

                            )
                        }
                    }


                }


                Spacer(Modifier.height(30.dp))

            }

            Spacer(Modifier.height(100.dp))

        }

        AppButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 10.dp, start = 16.dp, end = 16.dp, top = 10.dp),
            text = stringResource(R.string.done),
            onClick = {

                searchText = ""
                filteredCategoryList = originalList.toList()

                onDone()
            }
        )
    }

}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestBottomSheet(
    viewModel: AuthViewModel,
    viewModelM4: M4ViewModel,
    selectedInterests: List<SelectedInterest>,
    onSelectionChanged: (List<SelectedInterest>) -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val languageManager = LocalLanguageManager.current

    var searchText by rememberSaveable { mutableStateOf("") }

    val originalList = viewModel.categoryBackup

    var filteredCategoryList by remember {
        mutableStateOf<List<GetAllCategoriesResponse.Data?>>(emptyList())
    }

    // ✅ Load data on open
    LaunchedEffect(Unit) {
        if (originalList.isEmpty()) {
            viewModel.hitGetAllCategoriesStep6(
                SharedPreference.get(context).accessToken
            )
        } else {
            filteredCategoryList = originalList.toList()
        }
    }

    // ✅ Update filtered list when original list is loaded for the first time
    LaunchedEffect(originalList.size) {
        if (originalList.isNotEmpty() && searchText.isEmpty()) {
            filteredCategoryList = originalList.toList()
        }
    }

    var selectedChips by remember { mutableStateOf<List<ChipItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel.prepareInterestedInEdit()
    }

    // ============================
    // PREPARE SELECTED CHIPS
    // ============================
    LaunchedEffect(selectedInterests, originalList, languageManager.currentLanguage) {
        selectedChips = selectedInterests.flatMap { selected ->
            val category = originalList.find { it?.id == selected.categoryId }
            selected.tagIds.mapNotNull { tagId ->
                val tag = category?.tags?.find { it?.id == tagId }
                tag?.let {
                    val displayName = if (languageManager.currentLanguage == "ar") {
                        it.tagName?.ar ?: it.tagName?.en.orEmpty()
                    } else {
                        it.tagName?.en.orEmpty()
                    }
                    ChipItem(
                        tagId = tagId,
                        tagName = displayName,
                        iconUrl = it.iconImage
                    )
                }
            }
        }
    }

    // ============================
    // CATEGORIES OBSERVER (same pattern as CompleteProfileScreen6)
    // ============================
    viewModel.getAllCategoriesStep6.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> CustomLoader.showLoader(context as MainActivity)
            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { ErrorUtil.handlerGeneralError(context as MainActivity, it) }
            }
            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (state.value.success == true) {
                    val list = state.value.data ?: emptyList()
                    viewModel.categoryBackup.clear()
                    viewModel.categoryBackup.addAll(list)
                    filteredCategoryList = list
                    viewModel.getCategoryList.clear()
                    viewModel.getCategoryList.addAll(list)
                    state.value.success = false
                }
            }
            EmpResource.Idle -> {}
        }
    }

    // ============================
    // RECENT SEARCH OBSERVER (reactive, same pattern as CompleteProfileScreen6)
    // ============================
    viewModel.getRecentSearchTag.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> { /* optional: show subtle loading */ }
            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                // On failure, restore full list
                filteredCategoryList = originalList.toList()
                state.throwable?.let { ErrorUtil.handlerGeneralError(context as MainActivity, it) }
            }
            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (searchText.isEmpty()) {
                    // If user cleared the bar before response returned, show full list
                    filteredCategoryList = originalList.toList()
                } else {
                    val searched = state.value.data?.filterNotNull() ?: emptyList()

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

                state.value.success = false
            }
            EmpResource.Idle -> {}
        }
    }



    BoxWithConstraints {

        val max= this.maxWidth
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.select_up_to_10),
                fontSize = 12.sp,
                color = Color(0xff7C7C7C),
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )

            Spacer(Modifier.height(8.dp))

            SearchBar(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText

                    when {
                        newText.length >= 2 -> {
                            // ✅ Trigger API search (same as working screen)
                            viewModel.hitGetRecentSearchTag(
                                SharedPreference.get(context).accessToken, newText
                            )
                        }
                        newText.isEmpty() -> {
                            // ✅ Restore full list when search bar is cleared
                            filteredCategoryList = originalList.toList()
                        }
                        else -> {
                            // Length is 1 — restore full list (no partial search)
                            filteredCategoryList = originalList.toList()
                        }
                    }
                })

            Spacer(Modifier.height(20.dp))

            if (selectedChips.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    selectedChips.forEach { chip ->
                        Chips(
                            text = chip.tagName,
                            iconUrl = chip.iconUrl ?: "",
                            onClose = {
                                val updated = selectedInterests.mapNotNull { selected ->
                                    if (!selected.tagIds.contains(chip.tagId)) selected
                                    else {
                                        val newTags = selected.tagIds.filterNot { it == chip.tagId }
                                        if (newTags.isEmpty()) null
                                        else selected.copy(tagIds = newTags)
                                    }
                                }
                                onSelectionChanged(updated)
                            })
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            // ✅ Show data, loading, or no-results message
            when {
                originalList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.loading),
                            fontSize = 14.sp,
                            color = Color(0xff7C7C7C),
                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                        )
                    }
                }

                // ✅ Show "No results found" only when actively searching and nothing matched
                searchText.length >= 2 && filteredCategoryList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_results_found),
                            fontSize = 14.sp,
                            color = Color(0xff7C7C7C),
                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                        )
                    }
                }

                else -> {
                    filteredCategoryList.forEach { category ->
                        if (category == null) return@forEach

                        val categoryId = category.id ?: return@forEach
                        val tags = category.tags?.filterNotNull().orEmpty()
                        if (tags.isEmpty()) return@forEach
                        val categoryDisplayName = if (languageManager.currentLanguage == "ar") {
                            category.categoryName?.ar ?: category.categoryName?.en.orEmpty()
                        } else {
                            category.categoryName?.en.orEmpty()
                        }

                        Text(
                            text = categoryDisplayName,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_bold)),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .padding(20.dp)
                        ) {
                            FlowRow(
                                maxItemsInEachRow = 3,
                                horizontalArrangement = Arrangement.spacedBy(15.dp),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                tags.forEach { tag ->
                                    val tagId = tag.id ?: return@forEach
                                    val isSelected = selectedInterests.any {
                                        it.categoryId == categoryId && it.tagIds.contains(tagId)
                                    }

                                    val tagDisplayName = if (languageManager.currentLanguage == "ar") {
                                        tag.tagName?.ar ?: tag.tagName?.en.orEmpty()
                                    } else {
                                        tag.tagName?.en.orEmpty()
                                    }

                                    CommonSelection(
                                        text = tagDisplayName,
                                        iconUrl = tag.iconImage.orEmpty(),
                                        selected = isSelected,
                                        onToggle = {
                                            val totalSelected = selectedInterests.sumOf { it.tagIds.size }
                                            if (!isSelected && totalSelected >= 10) {
                                                context.showToast(context.getString(R.string.you_can_select_maximum_10_tags))
                                                return@CommonSelection
                                            }

                                            val updated = selectedInterests.toMutableList()
                                            val existing = updated.find { it.categoryId == categoryId }

                                            if (existing != null) {
                                                val newTags = existing.tagIds.toMutableList()
                                                if (newTags.contains(tagId)) newTags.remove(tagId)
                                                else newTags.add(tagId)

                                                updated.remove(existing)
                                                if (newTags.isNotEmpty()) {
                                                    updated.add(existing.copy(tagIds = newTags))
                                                }
                                            } else {
                                                updated.add(
                                                    SelectedInterest(
                                                        categoryId = categoryId,
                                                        tagIds = listOf(tagId)
                                                    )
                                                )
                                            }

                                            onSelectionChanged(updated)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(30.dp))
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }

        AppButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 10.dp, start = 16.dp, end = 16.dp, top = 10.dp),
            text = stringResource(R.string.done),
            onClick = {
                searchText = ""
                filteredCategoryList = originalList.toList()
                onDone()
            }
        )
    }
}

@Composable
fun SelectedChip(text: String, iconURl: String = "") {
    Row(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD9C8FF), Color(0xFFEFD8FF), Color(0xFFFFEFF8)
                    )
                ), shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (iconURl.isNotEmpty()) {
            AsyncImage(
                model = iconURl, modifier = Modifier.size(16.sdp), contentDescription = "img"
            )
        }

        Spacer(modifier = Modifier.width(5.sdp))

        Text(
            text = text,
            color = Color(0xFF530386),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
        )
    }
}
