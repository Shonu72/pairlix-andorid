package com.pairlix.dating.helper

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.pairlix.dating.R
import com.yalantis.ucrop.UCrop
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Environment
import android.telephony.TelephonyManager
import android.util.Log
import android.util.Patterns
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.AppSpecificStorageConfiguration
import com.abedelazizshe.lightcompressorlibrary.config.Configuration

import com.luck.picture.lib.language.LanguageConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



object CountryListHelper {

    fun getEnglishCountryNames(context: Context): Array<String> {
        val config = android.content.res.Configuration(context.resources.configuration)
        config.setLocale(Locale("en"))
        val resources = context.createConfigurationContext(config).resources
        return resources.getStringArray(R.array.country_list)
    }

    fun getArabicCountryNames(context: Context): Array<String> {
        val config = android.content.res.Configuration(context.resources.configuration)
        config.setLocale(Locale("ar"))
        val resources = context.createConfigurationContext(config).resources
        return resources.getStringArray(R.array.country_list)
    }
}
fun parseApiError(errorBody: ResponseBody?): String? {
    return errorBody?.let {
        try {
            val errorBodyString = it.string() // Read it once
            val jsonObject = JSONObject(errorBodyString)
            val message = jsonObject.optString("message")
            if (message.isNullOrBlank()) null else message
        } catch (e: Exception) {
            null // Return null on exception
        }
    }
}

fun convertDateForApi(date: String?): String {

    if (date.isNullOrBlank()) return ""

    return try {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val parsedDate = inputFormat.parse(date)

        if (parsedDate != null) {
            outputFormat.format(parsedDate)
        } else {
            ""
        }

    } catch (e: Exception) {
        ""
    }
}

fun formatDate(apiDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        val date = inputFormat.parse(apiDate)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        ""
    }
}

fun getRemainingSecondsFromUtc(utcTime: String?): Long {

    if (utcTime.isNullOrEmpty()) return 0L

    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val endDate = sdf.parse(utcTime) ?: return 0L
        val nowMillis = System.currentTimeMillis()

        val diff = endDate.time - nowMillis

        TimeUnit.MILLISECONDS.toSeconds(diff).coerceAtLeast(0)

    } catch (e: Exception) {
        0L
    }
}

fun getCountryNameFromCode(code: String): Single<String> {
    return Single.fromCallable {
        if (code.isBlank()) return@fromCallable "Invalid code"

        val locale = Locale("", code.uppercase())

        val countryName = locale.displayCountry

        if (countryName.isNullOrEmpty()) {
            "Unknown Country"
        } else {
            countryName
        }
    }
}

fun Uri.toMultipartDirect(context: Context, key: String): MultipartBody.Part? {
    val resolver = context.contentResolver
    val inputStream = resolver.openInputStream(this) ?: return null

    val tempFile = File(context.cacheDir, "file_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(tempFile)

    inputStream.copyTo(outputStream)
    inputStream.close()
    outputStream.close()

    val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(key, tempFile.name, requestBody)
}

fun Bitmap.toMultipartDirect(context: Context, key: String): MultipartBody.Part? {
    val tempFile = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")

    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bos)   // 100 = no compression quality
    val data = bos.toByteArray()

    val fos = FileOutputStream(tempFile)
    fos.write(data)
    fos.flush()
    fos.close()
    val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(key, tempFile.name, requestBody)
}
fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun isValidPhone(phone: String): Boolean {
    return phone.length in 7..15 && phone.all { it.isDigit() }
}

fun extractUniversalDate(text: String): String? {

    // 1️⃣ ALL COMMON GLOBAL DATE PATTERNS
    val patterns = listOf(
        // DD-MM-YYYY / DD/MM/YYYY / DD.MM.YYYY
        Regex("""\b(\d{2})[-/.](\d{2})[-/.](\d{4})\b"""),

        // YYYY-MM-DD / YYYY/MM/DD / YYYY.MM.DD
        Regex("""\b(\d{4})[-/.](\d{2})[-/.](\d{2})\b"""),

        // D-M-YYYY (single digit)
        Regex("""\b(\d{1})[-/.](\d{1})[-/.](\d{4})\b"""),

        // Month-name formats → 20 June 1986, June 20, 1986
        Regex(
            """\b(\d{1,2})\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)[a-z]*[ ,.-]+(\d{4})\b""",
            RegexOption.IGNORE_CASE
        ), Regex(
            """\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)[a-z]*\s+(\d{1,2}),?\s+(\d{4})\b""",
            RegexOption.IGNORE_CASE
        )
    )

    val lower = text.lowercase()

    // 2️⃣ TEST ALL PATTERNS
    for (pattern in patterns) {
        val match = pattern.find(lower)
        if (match != null) {

            return when (pattern) {

                // Format: DD-MM-YYYY
                patterns[0] -> {
                    val (d, m, y) = match.destructured
                    "%02d/%02d/%04d".format(d.toInt(), m.toInt(), y.toInt())
                }

                // Format: YYYY-MM-DD
                patterns[1] -> {
                    val (y, m, d) = match.destructured
                    "%02d/%02d/%04d".format(d.toInt(), m.toInt(), y.toInt())
                }

                // Format: D-M-YYYY
                patterns[2] -> {
                    val (d, m, y) = match.destructured
                    "%02d/%02d/%04d".format(d.toInt(), m.toInt(), y.toInt())
                }

                // Format: 20 June 1986
                patterns[3] -> {
                    val (d, monthName, y) = match.destructured
                    val m = convertMonth(monthName)
                    "%02d/%02d/%04d".format(d.toInt(), m, y.toInt())
                }

                // Format: June 20 1986
                patterns[4] -> {
                    val (monthName, d, y) = match.destructured
                    val m = convertMonth(monthName)
                    "%02d/%02d/%04d".format(d.toInt(), m, y.toInt())
                }

                else -> null
            }
        }
    }

    return null
}

fun convertMonth(name: String): Int {
    return when (name.take(3).lowercase()) {
        "jan" -> 1
        "feb" -> 2
        "mar" -> 3
        "apr" -> 4
        "may" -> 5
        "jun" -> 6
        "jul" -> 7
        "aug" -> 8
        "sep" -> 9
        "oct" -> 10
        "nov" -> 11
        "dec" -> 12
        else -> 0
    }
}

fun getCountryFromLatLng(
    context: Context,
    latitude: Double,
    longitude: Double,
    onResult: (String) -> Unit
) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            val countryCode = addresses[0].countryCode?.lowercase() ?: ""
            onResult(countryCode) // "in", "pk"
        } else {
            onResult("")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onResult("")
    }
}


fun getCountryIso(context: Context): String {

    val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE)
                as TelephonyManager

    return when {

        telephonyManager.simCountryIso.isNotEmpty() ->
            telephonyManager.simCountryIso.uppercase()

        telephonyManager.networkCountryIso.isNotEmpty() ->
            telephonyManager.networkCountryIso.uppercase()

        else ->
            Locale.getDefault().country.uppercase()
    }
}
fun Context.forceLocale(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    @Suppress("DEPRECATION")
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun getLanguageConfig(languageCode: String): Int {
    return when (languageCode.uppercase()) {
        "AR"    -> LanguageConfig.AR
        "EN"    -> LanguageConfig.ENGLISH

        "ZH_TW" -> LanguageConfig.TRADITIONAL_CHINESE
        "DE"    -> LanguageConfig.GERMANY
        "FR"    -> LanguageConfig.FRANCE
        "JA"    -> LanguageConfig.JAPAN
        "KO"    -> LanguageConfig.KOREA
        "PT"    -> LanguageConfig.PORTUGAL
        "RU"    -> LanguageConfig.RU
        else    -> LanguageConfig.ENGLISH // fallback
    }
}

fun isImage(context: Context, uri: Uri): Boolean =

    context.contentResolver.getType(uri)?.startsWith("image") == true
fun isVideo(context: Context, uri: Uri): Boolean =

    context.contentResolver.getType(uri)?.startsWith("video") == true
fun copyUriToFile(context: Context, uri: Uri): File? {
    return try {
        val outputDir = File(context.cacheDir, "originals").also { it.mkdirs() }

        // ✅ detect real extension from mime type
        val mimeType = context.contentResolver.getType(uri)
        Log.e("memem", "${mimeType}: ", )
        val extension = when (mimeType) {
            "video/mp4" -> ".mp4"
            "video/quicktime" -> ".mov"
            "video/x-matroska" -> ".mkv"
            "video/3gpp" -> ".3gp"
            "video/webm" -> ".webm"
            else -> ".mp4"
        }

        val outputFile = File(outputDir, "original_${System.currentTimeMillis()}$extension")

        context.contentResolver.openInputStream(uri)?.use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // ✅ validate copy was successful
        if (!outputFile.exists() || outputFile.length() == 0L) {
            Log.e("VIDEO", "❌ copyUriToFile failed — empty file")
            return null
        }

        Log.e("VIDEO", "✅ copied: ${outputFile.length() / 1024}KB, mime=$mimeType")
        outputFile

    } catch (e: Exception) {
        Log.e("VIDEO", "❌ copyUriToFile crash: ${e.message}")
        null
    }
}
data class PickedMedia(

    val file: File,

    val uri: Uri,

    val isVideo: Boolean

)
fun showCameraOptions(
    context: Context,
    onImage: () -> Unit,
    onVideo: () -> Unit
) {
    val options = arrayOf(context.getString(R.string.photo),context.getString(R.string.video) )

    AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.choose_camera_option))
        .setItems(options) { dialog, which ->
            when (which) {
                0 -> onImage()
                1 -> onVideo()
            }
            dialog.dismiss()
        }
        .setCancelable(false)
        .show()
}
fun startCrop(
    context: Context,
    sourceUri: Uri,
    launcher: ActivityResultLauncher<Intent>
) {
    val destinationUri = Uri.fromFile(
        File(context.cacheDir, "crop_${System.currentTimeMillis()}.jpg")
    )

    val intent = UCrop.of(sourceUri, destinationUri)
        .withAspectRatio(1f, 1f)
        .getIntent(context)

    launcher.launch(intent)
}

@Composable
fun ImagePicker(
    dismiss:Boolean,
    onMediaPicked: (PickedMedia) -> Unit,
    onDismiss: () -> Unit
) {
    var dialogDismiss by remember { mutableStateOf(true) }
    LaunchedEffect(dismiss) {
        dialogDismiss=dismiss
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /* ---------------- FILES ---------------- */
    val imageFile = remember {
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "camera_image_${System.currentTimeMillis()}.jpg"
        )
    }

    val videoFile = remember {
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
            "camera_video_${System.currentTimeMillis()}.mp4"
        )
    }

    val imageUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    val videoUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", videoFile)
    }

    /* ---------------- CROP ---------------- */
    val cropLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            UCrop.getOutput(result.data!!)?.let { uri ->
                onMediaPicked(PickedMedia(file = File(uri.path!!), uri = uri, isVideo = false))
            }
        }
        onDismiss()
    }

    /* ---------------- SHARED VIDEO HANDLER ---------------- */
    // ✅ single function handles both camera and gallery video
    fun handleVideo(file: File) {
        if (!file.exists() || file.length() == 0L) {
            Log.e("VIDEO", "❌ invalid file: ${file.path}")
            onDismiss()
            return
        }
        scope.launch {
            Log.e("VIDEO", "compressing: ${file.length() / 1024}KB")
            val compressed = withContext(Dispatchers.IO) {
                compressVideo(context, file)
            }
            val finalFile = compressed ?: file
            Log.e("VIDEO", "✅ final file: ${finalFile.length() / 1024}KB")
            onMediaPicked(
                PickedMedia(
                    file = finalFile,
                    uri = Uri.fromFile(finalFile),
                    isVideo = true
                )
            )
            onDismiss()
        }
    }

    /* ---------------- CAMERA ---------------- */
    val cameraImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) startCrop(context, imageUri, cropLauncher)
        else {
            dialogDismiss=true
            onDismiss()}
    }

    val cameraVideoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) handleVideo(videoFile) // ✅ now compresses camera video too
        else{
            dialogDismiss=true
            onDismiss()}
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) showCameraOptions(
            onImage = {
                cameraImageLauncher.launch(imageUri)

            },
            onVideo = {
                cameraVideoLauncher.launch(videoUri)

            },
            context = context

        )
    }

    /* ---------------- GALLERY ---------------- */
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri==null){
            dialogDismiss=true
        }
        uri ?: return@rememberLauncherForActivityResult
        when {

            isImage(context, uri) -> startCrop(context, uri, cropLauncher)
            isVideo(context, uri) -> {
                val copied = copyUriToFile(context, uri)
                if (copied == null || copied.length() == 0L) {

                    return@rememberLauncherForActivityResult
                }
                handleVideo(copied) // ✅ same handler as camera
            }

            else -> onDismiss()
        }
    }


    if (dialogDismiss) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.choose_option), fontSize = 16.sp)
                    Image(painter = painterResource(R.drawable.cross_red_ic),"", modifier = Modifier.clickable {

                        onDismiss()
                    })
                }
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {

                                val granted = ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                                if (granted) showCameraOptions(
                                    onImage = {
                                        cameraImageLauncher.launch(imageUri)

                                    },
                                    onVideo = {
                                        cameraVideoLauncher.launch(videoUri)

                                    },
                                    context = context
                                ) else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                dialogDismiss=false

                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.camera_ic),
                            null,
                            Modifier.size(48.dp)
                        )
                        Text(stringResource(R.string.camera))
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                galleryLauncher.launch("*/*")
                                dialogDismiss=false
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.upload_image_ic),
                            null,
                            Modifier.size(48.dp)
                        )
                        Text(stringResource(R.string.gallery))
                    }
                }
            }
        }
    }
}

suspend fun compressVideo(context: Context, inputFile: File): File? {
    if (!inputFile.exists() || inputFile.length() == 0L) {
        Log.e("VIDEO", "❌ input invalid")
        return null
    }

    if (inputFile.length() < 5 * 1024 * 1024) {
        Log.e("VIDEO", "✅ under 5MB, skipping")
        return inputFile
    }

    val outputFileName = "compressed_${System.currentTimeMillis()}.mp4"
    Log.e("VIDEO", "input: ${inputFile.length() / 1024}KB → $outputFileName")

    return suspendCancellableCoroutine { continuation ->
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                VideoCompressor.start(
                    context = context,
                    uris = listOf(Uri.fromFile(inputFile)),
                    isStreamable = false,
                    sharedStorageConfiguration = null,
                    appSpecificStorageConfiguration = AppSpecificStorageConfiguration(
                        subFolderName = "compressed"  // ✅ no StorageType in 1.3.2
                    ),
                    configureWith = Configuration(
                        quality = VideoQuality.MEDIUM,
                        isMinBitrateCheckEnabled = false,
                        videoNames = listOf(outputFileName)
                    ),
                    listener = object : CompressionListener {
                        override fun onStart(index: Int) {
                            Log.e("VIDEO", "started")
                        }
                        override fun onProgress(index: Int, percent: Float) {}
                        override fun onSuccess(index: Int, size: Long, path: String?) {
                            Log.e("VIDEO", "✅ path=$path size=${size / 1024}KB")

                            if (path.isNullOrEmpty()) {
                                Log.e("VIDEO", "❌ null path")
                                if (continuation.isActive) continuation.resume(null)
                                return
                            }

                            val result = File(path)
                            if (!result.exists() || result.length() == 0L) {
                                Log.e("VIDEO", "❌ output file invalid")
                                if (continuation.isActive) continuation.resume(null)
                                return
                            }

                            // ✅ copy to safe location for upload
                            try {
                                val safeDir = File(
                                    context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                                    "compressed"
                                ).also { it.mkdirs() }

                                val safeFile = File(safeDir, outputFileName)
                                result.copyTo(safeFile, overwrite = true)
                                Log.e("VIDEO", "✅ safe: ${safeFile.path}")
                                if (continuation.isActive) continuation.resume(safeFile)
                            } catch (e: Exception) {
                                // fallback to original compressed path
                                Log.e("VIDEO", "copy failed, using original: $path")
                                if (continuation.isActive) continuation.resume(result)
                            }
                        }
                        override fun onFailure(index: Int, failureMessage: String) {
                            Log.e("VIDEO", "❌ $failureMessage")
                            if (continuation.isActive) continuation.resume(null)
                        }
                        override fun onCancelled(index: Int) {
                            if (continuation.isActive) continuation.resume(null)
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("VIDEO", "❌ crash: ${e.message}")
                if (continuation.isActive) continuation.resume(null)
            }
        }
        continuation.invokeOnCancellation {
            VideoCompressor.cancel()
            job.cancel()
        }
    }
}
fun isVideoUrl(url: String): Boolean {
    return url.lowercase().endsWith(".mp4")
            || url.lowercase().endsWith(".mov")
            || url.lowercase().endsWith(".webm")
}
fun getMimeType(fileName: String): String {
    return when {
        fileName.lowercase().endsWith(".mp4") -> "video/mp4"
        fileName.lowercase().endsWith(".avi") -> "video/x-msvideo"
        fileName.lowercase().endsWith(".mkv") -> "video/x-matroska"
        fileName.lowercase().endsWith(".mov") -> "video/quicktime"
        fileName.lowercase().endsWith(".wmv") -> "video/x-ms-wmv"
        fileName.lowercase().endsWith(".flv") -> "video/x-flv"
        fileName.lowercase().endsWith(".webm") -> "video/webm"
        fileName.lowercase().endsWith(".m4v") -> "video/x-m4v"
        fileName.lowercase().endsWith(".3gp") -> "video/3gpp"
        fileName.lowercase().endsWith(".jpg") || fileName.lowercase().endsWith(".jpeg") -> "image/jpeg"
        fileName.lowercase().endsWith(".png") -> "image/png"
        fileName.lowercase().endsWith(".gif") -> "image/gif"
        fileName.lowercase().endsWith(".webp") -> "image/webp"
        else -> "image/jpeg" // default
    }
}
fun parseMediaUrls(message: String?): List<String> {
    if (message.isNullOrEmpty()) return emptyList()

    return try {
        Json.decodeFromString<List<String>>(message)
    } catch (e: Exception) {
        listOf(message) // fallback for old single image
    }
}
fun isVideoUrlSafe(url: String): Boolean {
    val cleanUrl = url.substringBefore("?").lowercase()

    return cleanUrl.endsWith(".mp4") ||
            cleanUrl.endsWith(".mkv") ||
            cleanUrl.endsWith(".mov") ||
            cleanUrl.endsWith(".avi") ||
            cleanUrl.endsWith(".webm")
}
fun formatMillis(millis: Long): String {
    if (millis <= 0) return "00:00"

    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d", minutes, seconds)
}
fun getFileType(url: String): String {
    val lower = url.lowercase()

    return when {
        // 🔊 Audio
        lower.endsWith(".m4a") ||
                lower.endsWith(".mp3") ||
                lower.endsWith(".aac") ||
                lower.endsWith(".wav") -> "audio"

        // 🎥 Video
        lower.endsWith(".mp4") ||
                lower.endsWith(".avi") ||
                lower.endsWith(".mkv") ||
                lower.endsWith(".mov") ||
                lower.endsWith(".wmv") ||
                lower.endsWith(".flv") ||
                lower.endsWith(".webm") ||
                lower.endsWith(".m4v") ||
                lower.endsWith(".3gp") -> "video"

        // 🖼️ Image
        lower.endsWith(".jpg") ||
                lower.endsWith(".jpeg") ||
                lower.endsWith(".png") ||
                lower.endsWith(".webp") ||
                lower.endsWith(".gif") ||
                lower.endsWith(".bmp") -> "image"

        // 📝 Default
        else -> "text"
    }
}
fun formatChatTime(isoTime: String,context: Context): String {
    return try {
        // Parse ISO time
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(isoTime) ?: return ""

        val now = Calendar.getInstance()
        val msgCal = Calendar.getInstance().apply { time = date }

        // Difference in days
        val diffMillis = now.timeInMillis - msgCal.timeInMillis
        val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis)

        // Time format → 12-hour am/pm
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Date format → Jan 30
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        when {
            diffDays == 0L &&
                    now.get(Calendar.DAY_OF_YEAR) == msgCal.get(Calendar.DAY_OF_YEAR) -> {
                context.getString(R.string.today, timeFormat.format(date).lowercase())
            }

            diffDays == 1L ||
                    (now.get(Calendar.DAY_OF_YEAR) - msgCal.get(Calendar.DAY_OF_YEAR) == 1) -> {
                context.getString(R.string.yesterday, timeFormat.format(date).lowercase())
            }

            else -> {
                "${dateFormat.format(date)}, ${timeFormat.format(date).lowercase()}"
            }
        }
    } catch (e: Exception) {
        ""
    }
}
fun formatMatchDate(timestamp: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outputFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    val date = inputFormat.parse(timestamp) ?: return ""

    return addDaySuffix(outputFormat.format(date))
}
fun addDaySuffix(dateStr: String): String {
    val parts = dateStr.split(" ")
    val day = parts[0].toInt()

    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    return "$day$suffix ${parts[1]} ${parts[2]}"
}

fun <T> prioritizeSearch(
    list: List<T>,
    search: String,
    label: (T) -> String
): List<T> {
    if (search.isBlank()) return list

    return list
        .filter { label(it).contains(search, ignoreCase = true) }
        .sortedWith(
            compareBy<T> {
                !label(it).startsWith(search, ignoreCase = true)
            }.thenBy {
                label(it).lowercase()
            }
        )
}


fun MutableList<String?>.safeSet(index: Int, value: String?, max: Int = 9) {
    if (index < 0 || index >= max) return

    while (this.size <= index) {
        this.add(null)
    }
    this[index] = value
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}


