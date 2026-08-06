package com.pairlix.dating.view.splash

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

import com.pairlix.dating.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    val context = LocalContext.current

    // GIF support ke liye image loader
    val imageLoader = ImageLoader.Builder(context).components {
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    Box(
        modifier = Modifier.fillMaxSize()
            .background(

                Brush.linearGradient(
                    listOf(
                        Color(0xFF8B5DF6),
                        Color(0xFFF6A6D6),
                    )
                )),
                contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.raw.splashm2)
                .memoryCacheKey("splash_gif")
                .build(),
            imageLoader = imageLoader,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


    }


}


