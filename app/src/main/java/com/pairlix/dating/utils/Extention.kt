package com.pairlix.dating.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun uriToFile(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver ?: return null
    var time = System.currentTimeMillis()
    val tempFile = File(context.cacheDir, "img$time")
    contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(tempFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return tempFile
}

fun containsContactInfo(text: String): Boolean {
    // Email
    val emailRegex = Regex("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}")

    // URLs: http, https, www, or domain-like endings (.com .net .org etc)
    val urlRegex = Regex(
        "(https?://|www\\.)[\\S]+|[\\w\\-]+\\.(com|net|org|io|co|app|info|biz|me)([\\/\\?#][\\S]*)?",
        RegexOption.IGNORE_CASE
    )

    // Phone: 7+ digits with optional separators like spaces, dashes, dots, +, brackets
    val phoneRegex = Regex("[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{3,4}[-\\s.]?[0-9]{2,4}")

    return emailRegex.containsMatchIn(text) ||
            urlRegex.containsMatchIn(text) ||
            phoneRegex.containsMatchIn(text)
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val imagesDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyImages")
    if (!imagesDir.exists()) imagesDir.mkdirs()

    val file = File(imagesDir, "profile_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

}

fun String.formatText(): String {
    return this
        .replace("_", " ")
        .lowercase()
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }
}


