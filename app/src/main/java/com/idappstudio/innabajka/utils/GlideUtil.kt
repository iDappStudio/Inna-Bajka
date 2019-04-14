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

    private val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transforms(CenterCrop(), RoundedCorners(10))
        .priority(Priority.HIGH)

    fun setImage(target: ImageView, ctx: Context, url: String) {

        if(url == ""){

            GlideApp.with(ctx).load("https://scontent.fktw2-1.fna.fbcdn.net/v/t1.0-9/34885587_2191544121132342_7909114513884971008_n.jpg?_nc_cat=102&_nc_ht=scontent.fktw2-1.fna&oh=5618e9b37b4d68c0cab13aaf03a2c780&oe=5D4A8E92")
                .apply(options)
                .into(target)

        } else {

            GlideApp.with(ctx).load(url)
                .apply(options)
                .into(target)

        }

    }

}