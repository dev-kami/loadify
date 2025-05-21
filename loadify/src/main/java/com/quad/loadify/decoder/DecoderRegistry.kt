package com.quad.loadify.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import java.io.InputStream
import androidx.core.graphics.createBitmap
import com.quad.loadify.utils.BitmapUtils
object DecoderRegistry {

    @Throws(Exception::class)
    fun decode(inputStream: InputStream, extension: String): Any {
        return when {
            extension.endsWith("svg", ignoreCase = true) -> SvgDecoder.decode(inputStream)
          /*  extension.endsWith("gif", ignoreCase = true) -> GifDecoder.decode(inputStream)
                ?: throw Exception("Failed to decode GIF")*/
            extension.endsWith("webp", ignoreCase = true) -> BitmapDecoder.decode(inputStream)
            else -> BitmapDecoder.decode(inputStream)
        }
    }

    @Throws(Exception::class)
    fun decodeResource(context: Context, resId: Int): Any {
        return when (val resName = context.resources.getResourceTypeName(resId)) {
            "drawable", "mipmap" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        val source = ImageDecoder.createSource(context.resources, resId)
                        val drawable = ImageDecoder.decodeDrawable(source)
                        if (drawable is AnimatedImageDrawable) {
                            drawable.start()
                            drawable
                        } else {
                            BitmapUtils.drawableToBitmap(drawable)
                        }
                    } catch (e: Exception) {
                        // Fallback for API 28+ error
                        val fallbackDrawable = AppCompatResources.getDrawable(context, resId)
                            ?: throw Exception("Drawable not found")
                        BitmapUtils.drawableToBitmap(fallbackDrawable)
                    }
                } else {
                    // Fallback for API < 28
                    val fallbackDrawable = AppCompatResources.getDrawable(context, resId)
                        ?: throw Exception("Drawable not found")
                    BitmapUtils.drawableToBitmap(fallbackDrawable)
                }
            }

            else -> throw IllegalArgumentException("Unsupported resource type: $resName")
        }
    }

}


