package com.quad.loadify.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import androidx.appcompat.content.res.AppCompatResources
import java.io.InputStream
import androidx.core.graphics.createBitmap
import com.quad.loadify.utils.BitmapUtils

/*
object DecoderRegistry {

    @Throws(Exception::class)
    fun decode(context:Context,inputStream: InputStream, extension: String): Any {
        return when {
            extension.endsWith(".svg", ignoreCase = true) -> SvgDecoder.decode(inputStream)
            extension.endsWith(".gif", ignoreCase = true) -> GifDecoder.decode(inputStream)
                ?: throw Exception("Failed to decode GIF")
            else -> BitmapDecoder.decode(inputStream)
        }
    }

    @Throws(Exception::class)
    fun decodeResource(context: Context, resId: Int): Any {
        val source = ImageDecoder.createSource(context.resources, resId)
        val drawable = ImageDecoder.decodeDrawable(source)

        return if (drawable is AnimatedImageDrawable) {
            drawable.start()
            drawable
        } else {
            // For static drawables, render to bitmap
            val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

}
*/


object DecoderRegistry {

    @Throws(Exception::class)
    fun decode(context: Context, inputStream: InputStream, extension: String): Any {
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
        val resName = context.resources.getResourceTypeName(resId)

        return when (resName) {
            "drawable", "mipmap" -> {
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
                    val drawable = AppCompatResources.getDrawable(context, resId)
                        ?: throw Exception("Drawable not found")
                    BitmapUtils.drawableToBitmap(drawable)
                }
            }
            else -> throw IllegalArgumentException("Unsupported resource type: $resName")
        }
    }
}


