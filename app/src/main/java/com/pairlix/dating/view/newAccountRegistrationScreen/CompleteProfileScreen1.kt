package com.pairlix.dating.view.newAccountRegistrationScreen
import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.delay
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.FormProgressBar
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.toMultipartDirect
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CompleteProfileRequest1
import com.pairlix.dating.utils.uriToFile
import com.pairlix.dating.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.pairlix.dating.LanguageManager.AppLanguageManager
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.helper.forceLocale
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.response.ModerateContentResponse
import com.pairlix.dating.utils.GlideEngine
import com.pairlix.dating.utils.isInternetAvailable
import com.pairlix.dating.viewModel.AuthViewModel.PendingImage
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import java.util.Locale


//for moderate


/*fun uploadImageObserverStep1(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    onUploadSuccess: () -> Unit,
    onUploadStart: () -> Unit,
    onAllUploadsDone: () -> Unit,
    pendingIndices: MutableList<Int>
) {
    var activeUploads = 0
    var isLoaderShown = false

    viewModel.uploadImageFile.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
                activeUploads++
                onUploadStart()
                if (!isLoaderShown) {
                  //  CustomLoader.showLoader(context)
                    isLoaderShown = true
                }
            }

            is EmpResource.Failure -> {
                activeUploads--
                if (activeUploads <= 0) {
                    onAllUploadsDone()
                    CustomLoader.hideLoader()
                    isLoaderShown = false
                }
                state.throwable.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                    Toast.makeText(
                        context,
                        context.getString(R.string.upload_failed, err.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is EmpResource.Success -> {
                val indices = pendingIndices.toList()
                pendingIndices.clear()

                state.value.data?.forEachIndexed { i, item ->
                    val idx = if (i < indices.size) indices[i] else -1
                    val url = item?.documentImageUrl ?: ""
                    if (idx in 0 until 9 && url.isNotEmpty()) {
                        viewModel.uploadedImageUrls[idx] = url
                        onUploadSuccess()
                    }
                }

                activeUploads--
                if (activeUploads <= 0) {
                    onAllUploadsDone()
                    CustomLoader.hideLoader()
                    isLoaderShown = false
                }
            }

            //moderate
*//*
            is EmpResource.Success -> {
                // Copy indices before clearing
                val indices = pendingIndices.toList()
                pendingIndices.clear()
                // Add to pending with correct indices
                state.value.data?.forEachIndexed { i, item ->
                    val idx = if (i < indices.size) indices[i] else -1
                    viewModel.pendingModerationImage.add(
                        PendingImage(
                            key = item?.documentImageKey ?: "",
                            url = item?.documentImageUrl ?: "",
                            index = idx
                        )
                    )
                }

                // Call moderation
                viewModel.hitModerateContent(
                    access_token = SharedPreference.get(context).accessToken,
                    request = ModerateContentRequest(imageKey = state.value.data?.map { it?.documentImageKey })
                )

                activeUploads--
                if (activeUploads <= 0) {
                    onAllUploadsDone()
                    CustomLoader.hideLoader()
                    isLoaderShown = false
                }
            }
*//*

            EmpResource.Idle -> {}
        }
    }
}*/


fun uploadImageObserverStep1(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    onUploadSuccess: () -> Unit,
    onUploadStart: () -> Unit,
    onAllUploadsDone: () -> Unit,
    pendingIndices: MutableList<Int>,
    onUploadFailed: (List<Int>) -> Unit,
    onRejectedIndices: (List<Int>) -> Unit  // ✅ NEW
) {
    var activeUploads = 0
    var isLoaderShown = false

    viewModel.uploadImageFile.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
                activeUploads++
                onUploadStart()
                if (!isLoaderShown) isLoaderShown = true
            }

            is EmpResource.Failure -> {
                activeUploads--
                val failedIndices = pendingIndices.toList()
                pendingIndices.clear()
                failedIndices.forEach { index ->
                    if (index in 0 until 9) viewModel.uploadedImageUrls[index] = ""
                }
                onUploadFailed(failedIndices)
                if (activeUploads <= 0) {
                    onAllUploadsDone()
                    CustomLoader.hideLoader()
                    isLoaderShown = false
                }
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                    Toast.makeText(context, context.getString(R.string.upload_failed, err.message ?: ""), Toast.LENGTH_SHORT).show()
                }
            }

            is EmpResource.Success -> {
                val indices = pendingIndices.toList()
                pendingIndices.clear()


                val returnedItems = state.value.data?.filterNotNull() ?: emptyList()

                returnedItems.forEachIndexed { i, item ->

                    val idx = indices.getOrNull(i) ?: return@forEachIndexed
                    val url = item.documentImageUrl ?: ""
                    if (idx in 0 until 9 && url.isNotEmpty()) {
                        viewModel.uploadedImageUrls[idx] = url
                    }
                }

                // ✅ Find rejected indices and notify immediately
                val returnedCount = returnedItems.size
                val rejectedIndices = mutableListOf<Int>()
                if (indices.size > returnedCount) {
                    val rejected = indices.drop(returnedCount)
                    rejected.forEach { idx ->
                        if (idx in 0 until 9) {
                            viewModel.uploadedImageUrls[idx] = ""
                            rejectedIndices.add(idx)
                        }
                    }
                }

                // ✅ Also rejected if returned but url empty
                returnedItems.forEachIndexed { i, item ->
                    val idx = indices.getOrNull(i) ?: return@forEachIndexed
                    if ((item.documentImageUrl ?: "").isEmpty() && idx in 0 until 9) {
                        rejectedIndices.add(idx)
                    }
                }

                if (rejectedIndices.isNotEmpty()) {
                    onRejectedIndices(rejectedIndices)  // ✅ clear UI + paths immediately
                }

                activeUploads--
                if (activeUploads <= 0) {
                    onAllUploadsDone()
                    CustomLoader.hideLoader()
                    isLoaderShown = false
                }
            }

            EmpResource.Idle -> {}
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteProfileScreen1(navController: NavController, viewModel: AuthViewModel) {
    var images by rememberSaveable(
        stateSaver = listSaver(
            save = { it.map { uri -> uri?.toString() } },
            restore = { it.map { s -> s?.toUri() } })
    ) {
        mutableStateOf(listOf<Uri?>(null))
    }
    val uploadedCount by remember {
        derivedStateOf { viewModel.uploadedImageUrls.count { it.isNotEmpty() } }
    }

    //var uploadedCount by remember { mutableIntStateOf(viewModel.uploadedImageUrls.count { it.isNotEmpty() }) }
    var isUploading by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
// Replace your current isButtonEnabled:
    val isButtonEnabled by remember {
        derivedStateOf {
            val uploaded = viewModel.uploadedImageUrls.count { it.isNotEmpty() }
            uploaded >= 3 && !isUploading
        }
    }   // val isButtonEnabled = (images.size - 1) >= 3
    val pendingIndices = remember { mutableStateListOf<Int>() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
   // val moderateContent by viewModel.moderateContent.collectAsState()


    var imagePaths by rememberSaveable { mutableStateOf(listOf<String?>(null)) }

    val blockedItemList = listOf<BlockedItem>(
        BlockedItem(R.drawable.multiple_face_ic, stringResource(R.string.multiple_face)),
        BlockedItem(R.drawable.ai_generated_ic, stringResource(R.string.ai_generated)),
        BlockedItem(R.drawable.weapon_ic, stringResource(R.string.weapons)),
        BlockedItem(R.drawable.face_ic_red, stringResource(R.string.face_covered)),
        BlockedItem(R.drawable.hidden_face_ic, stringResource(R.string.hidden)),
        BlockedItem(R.drawable.drug_ic, stringResource(R.string.drugs)),)
    val passItemList = listOf<BlockedItem>(
        BlockedItem(R.drawable.original_face_ic, stringResource(R.string.clear_face)),
        BlockedItem(R.drawable.original_face_ic, stringResource(R.string.original_image)),
        BlockedItem(R.drawable.authentic_ic, stringResource(R.string.authentic_expression)),
    )


// Full updated LaunchedEffect block:
/*
    LaunchedEffect(moderateContent) {
        when (moderateContent) {
            is EmpResource.Loading -> {
//                CustomLoader.showLoader(context as MainActivity)
                isUploading = true
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                isUploading = false

                val failure = moderateContent as EmpResource.Failure
                failure.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }

                // Treat as rejection: Clear everything and reset state
                images = emptyList()
                imagePaths = listOf(null)  // Add this to clear persisted paths
                viewModel.pendingModerationImage.clear()
                viewModel.uploadedImageUrls = MutableList(9) { "" }
                pendingIndices.clear()
                uploadedCount = 0  // Reset count
                viewModel.resetModerationState()
                context.showToast(context.getString(R.string.moderation_failed_images_rejected))
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                isUploading = false

                val response =
                    (moderateContent as EmpResource.Success<ModerateContentResponse>).value
                if (response.data?.nudityDetected == true) {
                    // Rejection: Clear images and state (effectively "unselects" from UI/picker)
                    images = emptyList()
                    imagePaths = listOf(null)  // Add this to clear persisted paths
                    viewModel.pendingModerationImage.clear()
                    viewModel.uploadedImageUrls = MutableList(9) { "" }
                    pendingIndices.clear()
                    uploadedCount = 0  // Reset count
                    viewModel.resetModerationState()
                    context.showToast(context.getString(R.string.images_rejected_due_to_content_policy))
                } else {
                    // Approval: Set URLs and update count
                    viewModel.pendingModerationImage.forEach { pending ->
                        if (pending.index in 0 until 9) {
                            viewModel.uploadedImageUrls[pending.index] = pending.url
                        }
                    }
                    uploadedCount =
                        viewModel.uploadedImageUrls.count { it.isNotEmpty() }  // Update count
                    viewModel.pendingModerationImage.clear()
                    viewModel.resetModerationState()
                    context.showToast(context.getString(R.string.images_approved))
                }
            }

            EmpResource.Idle -> Unit
        }
    }
*/

    LaunchedEffect(Unit) {


/*uploadImageObserverStep1(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            onUploadSuccess = { uploadedCount++ },
            onUploadStart = { isUploading = true },
            onAllUploadsDone = { isUploading = false },
            pendingIndices = pendingIndices
        )*/


        createAccountStep1Observer(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            navController = navController as NavHostController
        )


    }



   /* DisposableEffect(Unit) {
        uploadImageObserverStep1(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            onUploadSuccess = { uploadedCount++ },  // ✅ was no-op, now increments
            onUploadStart = { isUploading = true },
            onAllUploadsDone = { isUploading = false },
            pendingIndices = pendingIndices
        )
        onDispose { }
    }*/

   /* DisposableEffect(Unit) {
        uploadImageObserverStep1(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            onUploadSuccess = { uploadedCount++ },
            onUploadStart = { isUploading = true },
            onAllUploadsDone = { isUploading = false },
            pendingIndices = pendingIndices,

            // ✅🔥 NEW BLOCK (VERY IMPORTANT)
            onUploadFailed = { failedIndices ->
                val updated = images.toMutableList()
                failedIndices.forEach { index ->
                    if (index < updated.size) {
                        updated[index] = null
                    }
                }
                images = updated
            }
        )
        onDispose { }
    }*/


    DisposableEffect(Unit) {
        uploadImageObserverStep1(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            onUploadSuccess = { /* NO-OP */ },
            onUploadStart = { isUploading = true },
            onAllUploadsDone = { isUploading = false },
            pendingIndices = pendingIndices,
            onUploadFailed = { failedIndices ->
                val updatedImages = images.toMutableList()
                val updatedPaths = imagePaths.toMutableList()

                failedIndices.forEach { index ->
                    if (index < updatedImages.size) updatedImages[index] = null
                    if (index < updatedPaths.size) updatedPaths[index] = null
                }

                // ✅ Compact
                val compactedImages = MutableList<Uri?>(9) { null }
                val compactedPaths = MutableList<String?>(9) { null }
                val compactedUrls = MutableList<String>(9) { "" }

                var writeIdx = 0
                for (readIdx in 0 until 9) {
                    if (updatedImages.getOrNull(readIdx) != null) {
                        compactedImages[writeIdx] = updatedImages[readIdx]
                        compactedPaths[writeIdx] = updatedPaths.getOrNull(readIdx)
                        compactedUrls[writeIdx] = viewModel.uploadedImageUrls.getOrNull(readIdx) ?: ""
                        writeIdx++
                    }
                }

                for (i in 0 until 9) {
                    viewModel.uploadedImageUrls[i] = compactedUrls[i]
                }

                images = compactedImages
                imagePaths = compactedPaths
            },
            onRejectedIndices = { rejectedIndices ->
                val updatedImages = images.toMutableList()
                val updatedPaths = imagePaths.toMutableList()

                // Clear rejected slots
                rejectedIndices.forEach { index ->
                    if (index < updatedImages.size) updatedImages[index] = null
                    if (index < updatedPaths.size) updatedPaths[index] = null
                    if (index in 0 until 9) viewModel.uploadedImageUrls[index] = ""
                }

                // ✅ Compact everything — same as onDelete
                val compactedImages = MutableList<Uri?>(9) { null }
                val compactedPaths = MutableList<String?>(9) { null }
                val compactedUrls = MutableList<String>(9) { "" }

                var writeIdx = 0
                for (readIdx in 0 until 9) {
                    if (updatedImages.getOrNull(readIdx) != null) {
                        compactedImages[writeIdx] = updatedImages[readIdx]
                        compactedPaths[writeIdx] = updatedPaths.getOrNull(readIdx)
                        compactedUrls[writeIdx] = viewModel.uploadedImageUrls.getOrNull(readIdx) ?: ""
                        writeIdx++
                    }
                }

                // ✅ Apply compacted URLs back to viewModel
                for (i in 0 until 9) {
                    viewModel.uploadedImageUrls[i] = compactedUrls[i]
                }

                images = compactedImages
                imagePaths = compactedPaths
            }
        )
        onDispose { }
    }


    //moderate
  /*  val uploadSuccess by viewModel.uploadSuccessEvent.collectAsState()

    LaunchedEffect(uploadSuccess) {
        if (uploadSuccess) {
            uploadedCount++
            viewModel.resetUploadSuccessEvent() // Reset the flag
        }
    }*/


    /*LaunchedEffect(viewModel.uploadedImageUrls) {
            val existing = viewModel.uploadedImageUrls.filter { it.isNotEmpty() }
            uploadedCount = existing.size
            Log.d("RESTORE", "Restored count = $uploadedCount from URLs = $existing")
        }
    */

    /*snapshotFlow { viewModel.uploadedImageUrls.toList() }.collect { list ->
    val count = list.count { it.isNotEmpty() }
    uploadedCount = count
    Log.d("RESTORE", "Count = $uploadedCount , URLs = $list")
}*/

    /*  LaunchedEffect(Unit) {
    if (viewModel.uploadedImageUrls.all { it.isEmpty() }) {
        viewModel.uploadedImageUrls = MutableList(9) { "" }
    }
    pendingIndices.clear()
}*/




    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .navigationBarsPadding()
    ) {
        val maxHeight = this.maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.complete_profile),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                verticalSpace(20)


                /*FormProgressBar(
                                    currentPage = 0,
                                    percentage = if (SharedPreference.get(context).profileCompletionPercentage.isEmpty()) "0"
                                    else SharedPreference.get(context).profileCompletionPercentage
                                )*/
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(R.string.photos_upload_guideline),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )
                    horizontalSpace(5)
                    Image(
                        painter = painterResource(R.drawable.info_icon),
                        contentDescription = "null",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { showBottomSheet = true }
                    )
                }
                FormProgressBar(
                    currentPage = 0.0, percentage = "0"
                )
                verticalSpace(20)

                Text(
                    text = stringResource(R.string.upload_upto_9_photo_three_mandatory)+ " "+ stringResource(R.string.required_symbol),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                )

                verticalSpace(10)

                UploadGridUI(
                    images = images,
                    onImagesChanged = { images = it },
                    viewModel = viewModel,
                    pendingIndices = pendingIndices,
                    imagePaths = imagePaths,
                    onImagePathsChanged = { imagePaths = it })


                verticalSpace(10)


                /* Text(
                                    text = "Debug: Current Uploaded Count: ${images.size - 1}",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                )*/



                verticalSpace(10)


            }
        }

        AppButton(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
                .align(Alignment.BottomCenter)
                .alpha(if (isButtonEnabled) 1f else 0.5f),
            text = stringResource(R.string.next),
            onClick = {
                // ✅ Block if disabled — covers ALL cases
                if (!isButtonEnabled) {
                    val message = if (isUploading) {
                        context.getString(R.string.please_wait_for_uploads_to_complete)
                    } else {
                        context.getString(R.string.please_wait_for_at_least_3_images_to_upload_successfully)
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    return@AppButton
                }

                val finalUrls = viewModel.uploadedImageUrls.filter { it.isNotEmpty() }
                Log.d(
                    "VALIDATION",
                    "Uploaded Count = $uploadedCount, Final URLs Count = ${finalUrls.size}, URLs = $finalUrls"
                )
                viewModel.hitCompleteProfile1(
                    access_token = SharedPreference.get(context).accessToken,
                    request = CompleteProfileRequest1(
                        data = CompleteProfileRequest1.Data(images = finalUrls), step = 1
                    )
                )
            }
        )

        UploadLoadingDialog(show = isUploading)
    }


    if (showBottomSheet) {

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            sheetGesturesEnabled = false,
            dragHandle = null,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF14590988))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = stringResource(R.string.photo_guidline),
                        fontSize = 18.sp,
                        color = Color.Black,
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
                                interactionSource = remember { MutableInteractionSource() }) {
                                showBottomSheet = false
                            })
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row() {
                        Image(
                            painter = painterResource(R.drawable.green_tick_verificatin_ic),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                        horizontalSpace(10)
                        Text(
                            text = stringResource(R.string.pass),
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    verticalSpace(10)


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(passItemList.size) { index ->
                            val item = passItemList[index]
                            ImageBottomText(
                                img = item.img,
                                text = item.text,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    verticalSpace(10)

                    Row() {
                        Image(
                            painter = painterResource(R.drawable.block_ic_red),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                        horizontalSpace(10)
                        Text(
                            text = stringResource(R.string.blocked),
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    verticalSpace(10)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(blockedItemList.size) { index ->
                            val item = blockedItemList[index]

                            ImageTextRedBorder(
                                img = item.img,
                                text = item.text,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageBottomText(img: Int, text: String, modifier: Modifier) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = img),
            contentDescription = "",
            modifier = modifier
                .height(100.dp)

        )
        verticalSpace(5)

        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            fontSize = 10.ssp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}


@Composable
fun ImageTextRedBorder(img: Int, text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(1.dp, Color.Red, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Image(
            painter = painterResource(id = img),
            contentDescription = null,
            modifier = Modifier.size(38.dp)
        )


        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
            fontSize = 10.ssp,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
fun UploadGridUI(
    images: List<Uri?>,
    onImagesChanged: (List<Uri?>) -> Unit,
    viewModel: AuthViewModel,
    pendingIndices: MutableList<Int>,
    imagePaths: List<String?>,
    onImagePathsChanged: (List<String?>) -> Unit
) {
    val context = LocalContext.current
    var showPickerDialog by rememberSaveable { mutableStateOf(false) }
    var selectedIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    val maxImages = 9

    fun ensureNineSlots(list: List<Uri?>): List<Uri?> {
        val result = MutableList<Uri?>(9) { null }
        list.forEachIndexed { i, uri -> if (i < 9) result[i] = uri }
        return result
    }

    fun ensureNinePathSlots(list: List<String?>): List<String?> {
        val result = MutableList<String?>(9) { null }
        list.forEachIndexed { i, path -> if (i < 9) result[i] = path }
        return result
    }

    val safe = remember(images) { ensureNineSlots(images) }
    val safePaths = remember(imagePaths) { ensureNinePathSlots(imagePaths) }
    val latestSafe = remember { mutableStateOf(safe) }
    val latestPaths = remember { mutableStateOf(safePaths) }
    LaunchedEffect(images) { latestSafe.value = ensureNineSlots(images) }
    LaunchedEffect(imagePaths) { latestPaths.value = ensureNinePathSlots(imagePaths) }

    val addButtonIndex = safe.indexOfFirst { it == null }.takeIf { it >= 0 }

    // ✅ Single image picker — for replacing
    val singleImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val idx = selectedIndex ?: return@rememberLauncherForActivityResult
        if (uri == null) { selectedIndex = null; return@rememberLauncherForActivityResult }

        if (!isInternetAvailable(context)) {
            Toast.makeText(context, "Internet not available", Toast.LENGTH_SHORT).show()
            selectedIndex = null
            return@rememberLauncherForActivityResult
        }

        val snapSafe = latestSafe.value
        val snapPaths = latestPaths.value

        val updated = ensureNineSlots(snapSafe).toMutableList()
        updated[idx] = uri
        onImagesChanged(updated)

        val updatedPaths = ensureNinePathSlots(snapPaths).toMutableList()
        updatedPaths[idx] = uri.toString()
        onImagePathsChanged(updatedPaths)

        viewModel.uploadedImageUrls[idx] = ""
        while (pendingIndices.contains(idx)) pendingIndices.remove(idx)

        val file = uriToFile(context, uri) ?: run { selectedIndex = null; return@rememberLauncherForActivityResult }
        val fileName = if (file.name.endsWith(".jpg", true) || file.name.endsWith(".png", true) ||
            file.name.endsWith(".webp", true)) file.name else "${System.currentTimeMillis()}.webp"
        val part = MultipartBody.Part.createFormData(
            "upload_file", fileName,
            file.asRequestBody("image/webp".toMediaTypeOrNull())
        )
        pendingIndices.add(idx)
        viewModel.uploadImageFile(SharedPreference.get(context).accessToken, listOf(part))
        selectedIndex = null
    }

    // ✅ Multi image picker — for adding new
    val multiImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 9)
    ) { uris ->
        if (uris.isEmpty()) { selectedIndex = null; return@rememberLauncherForActivityResult }

        if (!isInternetAvailable(context)) {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            selectedIndex = null
            return@rememberLauncherForActivityResult
        }

        val snapSafe = latestSafe.value
        val snapPaths = latestPaths.value

        val updated = ensureNineSlots(snapSafe).toMutableList()
        val updatedPaths = ensureNinePathSlots(snapPaths).toMutableList()
        val parts = mutableListOf<MultipartBody.Part>()
        val trackedIndices = mutableListOf<Int>()

        uris.forEach { uri ->
            val emptySlot = updated.indexOfFirst { it == null }
            if (emptySlot < 0 || emptySlot >= 9) return@forEach

            updated[emptySlot] = uri
            updatedPaths[emptySlot] = uri.toString()

            val file = uriToFile(context, uri) ?: return@forEach
            val fileName = if (file.name.endsWith(".jpg", true) || file.name.endsWith(".png", true) ||
                file.name.endsWith(".webp", true)) file.name else "${System.currentTimeMillis()}.webp"

            parts.add(MultipartBody.Part.createFormData(
                "upload_file", fileName,
                file.asRequestBody("image/webp".toMediaTypeOrNull())
            ))
            trackedIndices.add(emptySlot)
        }

        onImagesChanged(updated)
        onImagePathsChanged(updatedPaths)

        if (parts.isNotEmpty()) {
            pendingIndices.addAll(trackedIndices)
            viewModel.uploadImageFile(SharedPreference.get(context).accessToken, parts)
        }
        selectedIndex = null
    }

    // ✅ Camera launcher — unchanged
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null && selectedIndex != null) {
            val file = File(context.cacheDir, "cam_${System.currentTimeMillis()}.webp")
            FileOutputStream(file).use {
                @Suppress("DEPRECATION")
                bitmap.compress(Bitmap.CompressFormat.WEBP, 80, it)
            }
            val uri = file.toUri()
            val idx = selectedIndex!!
            val updated = ensureNineSlots(latestSafe.value).toMutableList()
            updated[idx] = uri
            if (isInternetAvailable(context)) onImagesChanged(updated)
            else Toast.makeText(context, "Internet not available", Toast.LENGTH_SHORT).show()
            val updatedPaths = ensureNinePathSlots(latestPaths.value).toMutableList()
            updatedPaths[idx] = file.absolutePath
            if (isInternetAvailable(context)) onImagePathsChanged(updatedPaths)
            val part = MultipartBody.Part.createFormData(
                "upload_file", file.name,
                file.asRequestBody("image/webp".toMediaTypeOrNull())
            )
            pendingIndices.add(idx)
            viewModel.uploadImageFile(SharedPreference.get(context).accessToken, listOf(part))
        }
        selectedIndex = null
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) cameraLauncher.launch(null)
        else context.showToast(context.getString(R.string.camera_permission_is_required))
    }

    // ✅ Grid UI — unchanged
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().heightIn(max = 2000.dp)
        ) {
            itemsIndexed(safe) { index, uri ->
                if (uri != null) {
                    UploadGridItem(
                        imageUri = uri,
                        serverUrl = viewModel.uploadedImageUrls.getOrNull(index)?.takeIf { it.isNotEmpty() },
                        onClick = { selectedIndex = index; showPickerDialog = true },
                        onDelete = {
                            val currentImages = ensureNineSlots(latestSafe.value).toMutableList()
                            val currentPaths = ensureNinePathSlots(latestPaths.value).toMutableList()
                            if (index in 0 until 9) viewModel.uploadedImageUrls[index] = ""
                            while (pendingIndices.contains(index)) pendingIndices.remove(index)
                            currentImages[index] = null
                            currentPaths[index] = null
                            val compactedImages = MutableList<Uri?>(9) { null }
                            val compactedPaths = MutableList<String?>(9) { null }
                            val compactedUrls = MutableList<String>(9) { "" }
                            var writeIdx = 0
                            for (readIdx in 0 until 9) {
                                if (currentImages[readIdx] != null) {
                                    compactedImages[writeIdx] = currentImages[readIdx]
                                    compactedPaths[writeIdx] = currentPaths[readIdx]
                                    compactedUrls[writeIdx] = viewModel.uploadedImageUrls.getOrNull(readIdx) ?: ""
                                    writeIdx++
                                }
                            }
                            for (i in 0 until 9) viewModel.uploadedImageUrls[i] = compactedUrls[i]
                            latestSafe.value = compactedImages
                            latestPaths.value = compactedPaths
                            onImagesChanged(compactedImages)
                            onImagePathsChanged(compactedPaths)
                        },
                        modifier = Modifier.aspectRatio(1f)
                    )
                } else if (index == addButtonIndex) {
                    UploadGridItem(
                        imageUri = null,
                        onClick = { selectedIndex = index; showPickerDialog = true },
                        modifier = Modifier.aspectRatio(1f)
                    )
                }
            }
        }
    }

    if (showPickerDialog) {
        AlertDialog(
            onDismissRequest = { showPickerDialog = false; selectedIndex = null },
            title = { Text(stringResource(R.string.select_option)) },
            text = { Text(stringResource(R.string.choose_source)) },
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
                    val idx = selectedIndex ?: return@TextButton
                    val isReplacing = latestSafe.value.getOrNull(idx) != null

                    if (isReplacing) {
                        // ✅ Single pick for replacement
                        singleImagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        // ✅ Multi pick for new images
                        multiImagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }) { Text(stringResource(R.string.gallery)) }
            }
        )
    }
}

//without webp
/*
@Composable
fun UploadGridUI(
    images: List<Uri?>,
    onImagesChanged: (List<Uri?>) -> Unit,
    viewModel: AuthViewModel,
    pendingIndices: MutableList<Int>,
    onDeleteSuccess: () -> Unit,
    imagePaths: List<String?>,
    onImagePathsChanged: (List<String?>) -> Unit
) {
    val context = LocalContext.current
    var showPickerDialog by rememberSaveable { mutableStateOf(false) }
    var selectedIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()

    val maxImages = 9

    fun normalize(list: List<Uri?>): List<Uri?> {
        val result = list.filterNotNull().toMutableList<Uri?>()
        if (result.size < maxImages) result.add(null)
        return result
    }

    fun normalizePaths(list: List<String?>): List<String?> {
        val result = list.filterNotNull().toMutableList<String?>()
        if (result.size < maxImages) result.add(null)
        return result
    }

    val safe = remember(images) { normalize(images) }
    val safePaths = remember(imagePaths) { normalizePaths(imagePaths) }

    val camera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

        if (bitmap != null && selectedIndex != null) {

            // 1️⃣ Save bitmap to cache
            val filename = "img_${System.currentTimeMillis()}.png"
            val file = File(context.cacheDir, filename)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, it)            }

            val uri = file.toUri()
            val path = uri.toString()

            // 2️⃣ Update UI image list
            val updated = safe.toMutableList()
            updated[selectedIndex!!] = uri
            onImagesChanged(normalize(updated))

            // 3️⃣ Update path list
            val updatedPaths = MutableList<String?>(maxImages) {
                if (it < safePaths.size) safePaths[it] else null
            }
            updatedPaths[selectedIndex!!] = path
            onImagePathsChanged(normalizePaths(updatedPaths))

            // 4️⃣ Convert bitmap → multipart LIST
            val parts = mutableListOf<MultipartBody.Part>()

            val part = bitmap.toMultipartDirect(context, "upload_file")
            if (part != null) {
                parts.add(part)
                pendingIndices.add(selectedIndex!!)
            }

            // 5️⃣ Upload list
            if (parts.isNotEmpty()) {
                viewModel.uploadImageFile(
                    SharedPreference.get(context).accessToken, parts
                )
            }
        }

        showPickerDialog = false
        selectedIndex = null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 2000.dp)
        ) {
            itemsIndexed(safe) { index, uri ->
                UploadGridItem(
                    imageUri = uri, onClick = {
                        selectedIndex = index
                        showPickerDialog = true
                    }, onDelete = {
                        val updated = safe.toMutableList()
                        updated[index] = null
                        onImagesChanged(normalize(updated))
                        val updatedPaths = safePaths.toMutableList()
                        updatedPaths[index] = null
                        onImagePathsChanged(normalizePaths(updatedPaths))
                        if (viewModel.uploadedImageUrls.getOrNull(index)?.isNotEmpty() == true) {
                            viewModel.uploadedImageUrls[index] = ""
                            onDeleteSuccess()
                            Log.d(
                                "DELETE",
                                "Deleted uploaded image at index $index, decremented count"
                            )
                        } else {
                            if (pendingIndices.contains(index)) {
                                pendingIndices.remove(index)
                                Log.d("DELETE", "Removed pending upload at index $index")
                            }
                            onDeleteSuccess()
                            Log.d("DELETE", "Deleted non-uploaded image at index $index")
                        }
                    }, modifier = Modifier.aspectRatio(1f)
                )
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

        if (bitmap != null && selectedIndex != null) {

            // 1️⃣ Save bitmap to cache
            val filename = "cam_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, filename)
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
            }

            val uri = file.toUri()

            // 2️⃣ UI update
            val updated = safe.toMutableList()
            updated[selectedIndex!!] = uri
            onImagesChanged(normalize(updated))

            // 3️⃣ Path tracking
            val updatedPaths = MutableList<String?>(maxImages) { null }
            updated.forEachIndexed { i, u ->
                if (u != null) updatedPaths[i] = u.toString()
            }
            onImagePathsChanged(normalizePaths(updatedPaths))

            // 4️⃣ Prepare multipart
            val part = MultipartBody.Part.createFormData(
                "upload_file", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )

            // 5️⃣ Track index & hit API
            pendingIndices.add(selectedIndex!!)
            viewModel.uploadImageFile(
                SharedPreference.get(context).accessToken, listOf(part)
            )
        }

        selectedIndex = null
    }


    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            context.showToast(context.getString(R.string.camera_permission_is_required))
        }
    }

    if (showPickerDialog) {
        AlertDialog(
            onDismissRequest = {
                showPickerDialog = false
                selectedIndex = null
            },
            title = { Text(stringResource(R.string.select_option)) },
            text = { Text(stringResource(R.string.choose_source)) },
            confirmButton = {


                TextButton(onClick = {
                    showPickerDialog = false
                    if (hasCameraPermission(context)) {
                        // ✅ Permission already granted
                        cameraLauncher.launch(null)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) { Text(stringResource(R.string.camera)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPickerDialog = false
                    val remainingSlots = maxImages - safe.filterNotNull().size
                    val isReplacing = selectedIndex != null && safe[selectedIndex!!] != null
                    val selectionMode =
                        if (isReplacing) SelectModeConfig.MULTIPLE else if (remainingSlots > 0) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE
                    val maxSelect =
                        if (selectionMode == SelectModeConfig.SINGLE) 1 else if (isReplacing) 1 else maxImages  // For replacement, limit to 1 even in MULTIPLE mode
                    val preselected =
                        if (selectionMode == SelectModeConfig.MULTIPLE && !isReplacing) {
                            safePaths.filterNotNull().map { LocalMedia().apply { path = it } }
                                .toCollection(ArrayList())
                        } else if (isReplacing && selectedIndex != null && safePaths.getOrNull(
                                selectedIndex!!
                            ) != null
                        ) {
                            listOf(LocalMedia().apply {
                                path = safePaths[selectedIndex!!]
                            })  // Preselect only the one being replaced
                        } else {
                            ArrayList<LocalMedia>()
                        }

                    PictureSelector.create(context as Activity)
                        .openGallery(SelectMimeType.ofImage()).setImageEngine(GlideEngine())
                        .setSelectionMode(selectionMode).setMaxSelectNum(maxSelect)
                        .setSelectedData(preselected).isPreviewImage(true).isPreviewVideo(false)
                        .forResult(object : OnResultCallbackListener<LocalMedia> {
                            override fun onResult(result: ArrayList<LocalMedia>?) {
                                if (result != null) {
                                    val uniqueResult = result.distinctBy { it.path }
                                    val uris = uniqueResult.map { media ->
                                        Uri.parse(media.availablePath)
                                    }

                                    if (selectionMode == SelectModeConfig.MULTIPLE && !isReplacing) {
                                        // Multi-pick: Handle additions and removals (unchanged)
                                        val currentImages = safe.filterNotNull()
                                        val currentPaths = safePaths.filterNotNull()

                                        val unselectedPaths =
                                            currentPaths.filter { path -> uniqueResult.none { it.path == path } }
                                        val unselectedIndices = unselectedPaths.mapNotNull { path ->
                                            safePaths.indexOf(path).takeIf { it >= 0 }
                                        }

                                        val updated = safe.toMutableList()
                                        unselectedIndices.forEach { idx ->
                                            updated[idx] = null
                                            if (viewModel.uploadedImageUrls.getOrNull(idx)
                                                    ?.isNotEmpty() == true
                                            ) {
                                                viewModel.uploadedImageUrls[idx] = ""
                                                viewModel.uploadedImageUrls[idx] = ""
                                                onDeleteSuccess()
                                            }
                                        }

                                        val newUris = uris.filter { it !in currentImages }
                                            .take(remainingSlots)

                                        val finalUpdated =
                                            (updated.filterNotNull() + newUris).toMutableList<Uri?>()
                                        if (finalUpdated.size < maxImages) finalUpdated.add(null)
                                        onImagesChanged(finalUpdated)

                                        // Fix: Correctly update paths for all current images
                                        val updatedPaths = MutableList<String?>(maxImages) { null }
                                        finalUpdated.forEachIndexed { idx, uri ->
                                            if (uri != null) updatedPaths[idx] = uri.toString()
                                        }
                                        onImagePathsChanged(normalizePaths(updatedPaths))

                                        val indicesToAdd =
                                            ((finalUpdated.filterNotNull().size - newUris.size) until finalUpdated.filterNotNull().size).toList()
                                        pendingIndices.addAll(indicesToAdd)
                                        Log.d(
                                            "MULTI_PICK",
                                            "Added indices: $indicesToAdd, Pending: $pendingIndices"
                                        )

                                        // Changed: Collect all parts into a list and call uploadImageMultiple once, instead of individual uploadImageFile calls with delays
                                        val parts = mutableListOf<MultipartBody.Part>()
                                        newUris.forEachIndexed { index, uri ->
                                            val pos =
                                                finalUpdated.filterNotNull().size - newUris.size + index
                                            if (pos >= 9) return@forEachIndexed

                                            val file = uriToFile(context, uri)

                                            file?.let {
                                                val fileName = if (it.name.endsWith(
                                                        ".jpg", true
                                                    ) || it.name.endsWith(".png", true)
                                                ) {
                                                    it.name
                                                } else {
                                                    "${System.currentTimeMillis()}.jpg"
                                                }

                                                val part = MultipartBody.Part.createFormData(
                                                    "upload_file",
                                                    fileName,
                                                    it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                                )

                                                // ✅ ADD INDEX HERE (IMPORTANT)
                                                parts.add(part)
                                                pendingIndices.add(pos) // keep index mapping


                                            }

                                        }
                                        if (parts.isNotEmpty()) {
                                            viewModel.uploadImageFile(
                                                SharedPreference.get(context).accessToken, parts
                                            )
                                        }

                                    } else {
                                        // Single-pick or replacement: Replace the selected slot (or do nothing if unselected)
                                        if (uris.isNotEmpty() && selectedIndex != null) {
                                            // Clear old state for replacement
                                            if (viewModel.uploadedImageUrls.getOrNull(selectedIndex!!)
                                                    ?.isNotEmpty() == true
                                            ) {
                                                viewModel.uploadedImageUrls[selectedIndex!!] = ""
                                                onDeleteSuccess()
                                                Log.d(
                                                    "REPLACE",
                                                    "Cleared old uploaded URL at index $selectedIndex"
                                                )
                                            }
                                            if (pendingIndices.contains(selectedIndex!!)) {
                                                pendingIndices.remove(selectedIndex!!)
                                                Log.d(
                                                    "REPLACE",
                                                    "Removed old pending upload at index $selectedIndex"
                                                )
                                            }

                                            val updated = safe.toMutableList()
                                            updated[selectedIndex!!] = uris[0]
                                            onImagesChanged(normalize(updated))

                                            val updatedPaths =
                                                MutableList<String?>(maxImages) { if (it < safePaths.size) safePaths[it] else null }
                                            updatedPaths[selectedIndex!!] = uris[0].toString()
                                            onImagePathsChanged(normalizePaths(updatedPaths))

                                            val file = uriToFile(context, uris[0])
                                            file?.let {
                                                val part = MultipartBody.Part.createFormData(
                                                    "upload_file",
                                                    it.name,
                                                    it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                                )
                                                pendingIndices.add(selectedIndex!!)
                                                // Changed: Now calling uploadImageMultiple with a list of parts instead of uploadImageFile
                                                viewModel.uploadImageFile(
                                                    SharedPreference.get(
                                                        context
                                                    ).accessToken, listOf(part)
                                                )
                                            }
                                        }
                                        // If uris.isEmpty(), do nothing (user unselected, no replacement)
                                    }
                                }
                                showPickerDialog = false
                                selectedIndex = null
                            }

                            override fun onCancel() {
                                showPickerDialog = false
                                selectedIndex = null
                            }
                        })


                }) {

                    Text(stringResource(R.string.gallery))
                }
            })
    }
}
*/

@Composable
fun UploadGridItem(
    imageUri: Uri? = null,
    serverUrl: String? = null,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val imageModel: Any? = when {
        !serverUrl.isNullOrEmpty() -> serverUrl   // ← server image takes priority
        imageUri != null -> imageUri
        else -> null
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageModel  != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageModel),
                contentDescription = "Selected image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(R.drawable.edit_ic),
                contentDescription = "edit",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(
                        color = Color(0xff59FFFFFF),
                        RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp)
                    )
                    .padding(vertical = 2.dp)
                    .align(Alignment.BottomEnd),
                contentScale = ContentScale.Fit
            )


            Image(
                painter = painterResource(R.drawable.cross_ic),
                contentDescription = "Delete image",
                modifier = Modifier
                    .size(16.dp)
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) { onDelete() },
                contentScale = ContentScale.Fit
            )
        } else {
            Image(
                painter = painterResource(R.drawable.upload_image_ic),
                contentDescription = "Add image",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val filename = "img_${System.currentTimeMillis()}.png"
    val file = File(context.cacheDir, filename)

    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    return file.toUri()
}


fun createAccountStep1Observer(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController
) {
    viewModel.completeProfile.observe(lifecycleOwner) { state ->

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

                    context.showToast(state.value.message ?: "")

                    // clear uploaded urls for next step
                  //  viewModel.uploadedImageUrls = MutableList(9) { "" }

                    navController.navigate(Screen.CompleteProfile2.route)

                    state.value.success = false
                }
            }

            EmpResource.Idle -> {
                // do nothing
            }
        }
    }
}

fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}


@Composable
fun UploadLoadingDialog(show: Boolean) {
    if (!show) return
    AlertDialog(
        onDismissRequest = { }, confirmButton = {},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.please_wait_until_images_upload),
                    fontSize = 14.sp
                )
            }
        })
}
data class BlockedItem(val img: Int, val text: String)
