package com.pairlix.dating.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.luck.picture.lib.engine.ImageEngine

class GlideEngine : ImageEngine {

    override fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
        if (context == null || imageView == null) return

        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    override fun loadAlbumCover(context: Context?, url: String?, imageView: ImageView?) {
        if (context == null || imageView == null) return

        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(imageView)
    }

    override fun loadGridImage(context: Context?, url: String?, imageView: ImageView?) {
        if (context == null || imageView == null) return

        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(imageView)
    }

    // ⭐ THIS ONE WAS MISSING — NOW FIXED
    override fun loadImage(
        context: Context?,
        imageView: ImageView?,
        url: String?,
        maxWidth: Int,
        maxHeight: Int
    ) {
        if (context == null || imageView == null) return

        Glide.with(context)
            .load(url)
            .override(maxWidth, maxHeight)
            .centerCrop()
            .into(imageView)
    }

    override fun pauseRequests(context: Context?) {
        if (context != null) Glide.with(context).pauseRequests()
    }

    override fun resumeRequests(context: Context?) {
        if (context != null) Glide.with(context).resumeRequests()
    }
}
