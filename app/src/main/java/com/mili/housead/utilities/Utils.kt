package com.mili.housead.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mili.housead.R
import java.text.DecimalFormat


class Utils {

    companion object {
        @JvmStatic
        fun persianNumber(text: String): String {
            val persianNumbers = listOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
            if (text.isEmpty())
                return ""

            var out = ""
            val length = text.length

            for (i in 0 until length) {
                when (val c: Char = text[i]) {
                    in '0'..'9' -> {
                        val number: Int = c.toString().toInt()
                        out += persianNumbers[number]
                    }
                    ',' -> {
                        out += '،'
                    }
                    else -> {
                        out += c
                    }
                }
            }
            return out
        }

        @JvmStatic
        fun convertToNumberFormat(number: Long): String {
            val df = DecimalFormat("#,###,###,###,###")
            return df.format(number)
        }

        fun calculateSpanCount(
            context: Context,
            recyclerPaddingId: Int,
            columnWidthId: Int,
            columnPaddingId: Int
        ): Int {

            val displayMetrics = context.resources.displayMetrics
            val recyclerPadding =
                context.resources.getDimension(recyclerPaddingId) / displayMetrics.density
            val columnPadding =
                context.resources.getDimension(columnPaddingId) / displayMetrics.density
            val columnWidth = context.resources.getDimension(columnWidthId) / displayMetrics.density
            val recyclerWidthDp =
                (displayMetrics.widthPixels / displayMetrics.density) - (recyclerPadding * 2)
            var spanCount = (recyclerWidthDp / columnWidth).toInt()
            var originalWidth = columnWidth
            if (spanCount > 1) {
                originalWidth += (spanCount - 1) * columnPadding
                spanCount = (recyclerWidthDp / originalWidth).toInt()
            }
            return spanCount
        }

        fun loadImage(
            imgUrl: String,
            imgView: ImageView,
            placeholder: Int?,
            transition: Boolean,
            centerCrop: Boolean
        ) {
            if (transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgView.transitionName = imgUrl
                }
            }
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            var glide = Glide.with(imgView.context)
                .load(imgUri)
            if (centerCrop) {
                glide = glide.centerCrop()
            }
            if (placeholder != null) {
                val avd = AnimatedVectorDrawableCompat.create(imgView.context, placeholder)
                avd?.registerAnimationCallback(
                    object : Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            imgView.post { avd.start() }
                        }
                    })
                avd!!.start()
                glide = glide.apply(
                    RequestOptions()
                        .placeholder(avd)
                        .error(R.drawable.ic_broken_image_black_24dp)
                )
            } else {
                glide = glide.apply(
                    RequestOptions()
                        .error(R.drawable.ic_broken_image_black_24dp)
                )
            }
            glide.into(imgView)
        }
    }
}

