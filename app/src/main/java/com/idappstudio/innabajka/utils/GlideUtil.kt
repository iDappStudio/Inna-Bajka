package com.idappstudio.innabajka.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.idappstudio.innabajka.R

object GlideUtil {

    val options = RequestOptions()
        .error(R.mipmap.background)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transforms(CenterCrop(), RoundedCorners(10))
        .priority(Priority.HIGH)

    fun setImage(target: ImageView, ctx: Context, url: String) {

        GlideApp.with(ctx).asBitmap().load(url)
            .apply(options)
            .into(target)

    }

}