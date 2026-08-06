package com.pairlix.dating.view.newAccountRegistrationScreen

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.Chips
import com.pairlix.dating.ReusedComponents.CommonSelection
import com.pairlix.dating.ReusedComponents.FormProgressBar
import com.pairlix.dating.ReusedComponents.SearchBar
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest6
import com.pairlix.dating.response.GetAllCategoriesResponse
import com.pairlix.dating.response.RecentSearchResponse
import com.pairlix.dating.viewModel.AuthViewModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedInterest(
    val categoryId: String, val tagIds: List<String>
) : Parcelable

@Parcelize
data class ChipItem(
    val tagId: String, val tagName: String, val iconUrl: String?
) : Parcelable

@Composable
fun CompleteProfileScreen6(navController: NavController, viewModel: AuthViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var text by rememberSaveable() { mutableStateOf("") }
    val languageManager = LocalLanguageManager.current

    val selectedInterests = rememberSaveable {
        mutableStateOf(
            listOf<SelectedInterest>()
        )
    }

    var recentTagList by rememberSaveable {
        mutableStateOf(listOf<RecentSearchResponse.Data>())
    }
    val categoryData = viewModel.getCategoryList
    val originalList = viewModel.categoryBackup

    //val originalList = remember { mutableStateOf(listOf<GetAllCategoriesResponse.Data?>()) }

    // UI me show hone wali categories (after search filtering)
    var filteredCategoryList by remember {
        mutableStateOf(listOf<GetAllCategoriesResponse.Data?>())
    }

// Selected tag chips
    var selectedChips by rememberSaveable { mutableStateOf(listOf<ChipItem>()) }



    LaunchedEffect(true) {

        if (originalList.isNotEmpty()) {
            filteredCategoryList = originalList
        }
        if (originalList.isEmpty()) {
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


    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
           .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding( start = 16.dp, end = 16.dp)
    ) {

        TopBackBtnHeading(navController, text = stringResource(R.string.complete_profile))
        verticalSpace(20)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            FormProgressBar(
                currentPage = 5.0,
                percentage = "70"
                    //SharedPreference.get(context).profileCompletionPercentage
            )

            Spacer(Modifier.height(24.dp))
            Text(
                stringResource(R.string.select_up_to_10),
                color = Color(0xff7C7C7C),
                fontSize = 12.sp,
                fontFamily = FontFamily(
                    Font(R.font.axiforma_regular)
                )
            )
            Spacer(Modifier.height(8.dp))
            SearchBar(
                value = text, onValueChange = {
                    text = it
                    if (it.length >= 2) {
                        viewModel.hitGetRecentSearchTag(
                            SharedPreference.get(context).accessToken, it
                        )
                    } else {
                        // user cleared search
                        filteredCategoryList = originalList.toList()
                    }
                })

            verticalSpace(30)

            if (selectedChips.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    selectedChips.forEach { chip ->

                        Chips(
                            text = chip.tagName, iconUrl = chip.iconUrl ?: "",
                            onClose = {

                                // 1️⃣ Remove from chip list
                                selectedChips = selectedChips.filterNot { it.tagId == chip.tagId }

                                // 2️⃣ Update selectedInterests
                                val updatedList = selectedInterests.value.toMutableList()

                                val categoryFound = originalList.find { category ->
                                    category?.tags?.any { it?.id == chip.tagId } == true
                                }

                                val categoryId = categoryFound?.id
                                val tagId = chip.tagId

                                if (categoryId != null) {
                                    val existingCategory = updatedList.find { it.categoryId == categoryId }

                                    if (existingCategory != null) {

                                        // immutable → mutable convert
                                        val updatedTags = existingCategory.tagIds.toMutableList()

                                        // remove tag safely
                                        updatedTags.remove(tagId)

                                        // remove old entry
                                        updatedList.remove(existingCategory)

                                        // add new entry only if tags left
                                        if (updatedTags.isNotEmpty()) {
                                            updatedList.add(
                                                existingCategory.copy(tagIds = updatedTags.toList())
                                            )
                                        }
                                    }
                                }


                                selectedInterests.value = updatedList
                            })
                    }

                }

                Spacer(Modifier.height(20.dp))
            }

            LaunchedEffect(selectedInterests.value, originalList, languageManager.currentLanguage) {
                selectedChips = selectedInterests.value.flatMap { selected ->
                    val category = originalList.find { it?.id == selected.categoryId }

                    selected.tagIds.mapNotNull { tagId ->
                        val tagObj = category?.tags?.find { it?.id == tagId }

                        tagObj?.let {

                            val displayName =
                                if (languageManager.currentLanguage == "ar") {
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

            verticalSpace(8, true)

            filteredCategoryList.forEach { category ->

                if (category == null) return@forEach

                val validTags = category.tags?.filterNotNull().orEmpty()
                if (validTags.isEmpty()) return@forEach

                val categoryId = category.id ?: return@forEach
                val categoryName =
                    if (languageManager.currentLanguage == "ar") {
                        category.categoryName?.ar ?: category.categoryName?.en ?: stringResource(R.string.unknown_category)
                    } else {
                        category.categoryName?.en ?: stringResource(R.string.unknown_category)
                    }
                Text(
                    text = categoryName,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
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
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        validTags.forEach { tag ->

                            val tagId = tag.id ?: return@forEach
                            val tagName =
                                if (languageManager.currentLanguage == "ar") {
                                    tag.tagName?.ar ?: tag.tagName?.en ?: stringResource(R.string.unnamed)
                                } else {
                                    tag.tagName?.en ?: stringResource(R.string.unnamed)
                                }
                            val tagICon = tag.iconImage

                            CommonSelection(
                                text = tagName,
                                iconUrl = tag.iconImage.toString(),

                                selected = selectedInterests.value.any {
                                    it.categoryId == categoryId && it.tagIds.contains(
                                        tagId
                                    )
                                },
                                onToggle = {

                                    val totalSelected =
                                        selectedInterests.value.sumOf { it.tagIds.size }
                                    val isSelecting = !selectedInterests.value.any {
                                        it.categoryId == categoryId && it.tagIds.contains(tagId)
                                    }

                                    if (isSelecting && totalSelected >= 10) {
                                        context.showToast(context.getString(R.string.you_can_select_maximum_10_tags))
                                        return@CommonSelection
                                    }

                                    // 🔥 Update CHIP UI using tagId
                                    selectedChips = if (selectedChips.any { it.tagId == tagId }) {
                                        selectedChips.filterNot { it.tagId == tagId }
                                    } else {
                                        selectedChips + ChipItem(tagId, tagName, tagICon)
                                    }
                                    // 🔥 Update selectedInterests (ID based)
                                    val updatedList = selectedInterests.value.toMutableList()
                                    val existingCategory =
                                        updatedList.find { it.categoryId == categoryId }
                                    val updatedTags = existingCategory?.tagIds?.toMutableList()

                                    if (existingCategory != null) {
                                        if (existingCategory.tagIds.contains(tagId)) {
                                            val updatedTags =
                                                existingCategory.tagIds.toMutableList()

                                            updatedTags.remove(tagId)
                                            updatedList.remove(existingCategory)

                                            if (updatedTags.isNotEmpty()) {
                                                updatedList.add(
                                                    existingCategory.copy(tagIds = updatedTags.toList())
                                                )
                                            }

                                        } else {
                                            val updatedTags =
                                                existingCategory.tagIds.toMutableList()
                                            updatedTags.add(tagId)

                                            updatedList.remove(existingCategory)

                                            updatedList.add(
                                                existingCategory.copy(tagIds = updatedTags.toList())
                                            )
                                        }

                                    } else {
                                        updatedList.add(
                                            SelectedInterest(
                                                categoryId = categoryId,
                                                tagIds = listOf(tagId)
                                            )
                                        )
                                    }
                                    selectedInterests.value = updatedList
                                },
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            AppButton(
                modifier = Modifier.padding(bottom = 10.dp), text = stringResource(R.string.next), onClick = {

                    if (selectedInterests.value.isEmpty()) {
                        context.showToast(context.getString(R.string.please_select_at_least_one_tag))
                        return@AppButton
                    }

                    val interestListForApi = selectedInterests.value.map { selected ->

                        CompleteProfileRequest6.Data.Interest(
                            categoryId = selected.categoryId, tagIds = selected.tagIds
                        )
                    }

                    val finalRequest = CompleteProfileRequest6(
                        step = 6, data = CompleteProfileRequest6.Data(
                            interests = interestListForApi
                        )
                    )

                    viewModel.hitCompleteProfile6(
                        access_token = SharedPreference.get(context).accessToken,
                        request = finalRequest
                    )
                })

        }


    }
}


fun createAccountStep6Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    onSuccess: (List<GetAllCategoriesResponse.Data?>?) -> Unit = {},
    onSuccessRecentTags: (List<RecentSearchResponse.Data?>?) -> Unit = {},

    ) {
    viewModel.completeProfile6.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (state.value.success == true) {
                    SharedPreference.get(context).profileCompletionPercentage =
                        state.value.data?.profileCompletionPercentage.toString()
                    context.showToast(state.value.message?:"")
                    navController.navigate(Screen.CompleteProfile7.route)
                    state.value.success = false
                }
            }                EmpResource.Idle -> {}

        }
    }

    viewModel.getAllCategoriesStep6.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (state.value.success == true) {
                    onSuccess(state.value.data)
                    state.value.success = false
                }
            }                EmpResource.Idle -> {}

        }
    }


    viewModel.getRecentSearchTag.observe(lifecycleOwner) { state ->
        when (state) {

            is EmpResource.Loading -> {
                // CustomLoader.showLoader(context)
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                onSuccessRecentTags(state.value.data)

                state.value.success = false
            }

            EmpResource.Idle -> {}

        }
    }
}

