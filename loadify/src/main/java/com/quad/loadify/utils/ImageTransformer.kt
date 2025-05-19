package com.quad.loadify.utils

import android.graphics.*
import kotlin.math.min
import androidx.core.graphics.createBitmap

object ImageTransformer {

    fun circleCrop(source: Bitmap): Bitmap {
        val size = min(source.width, source.height)
        val output = createBitmap(size, size)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())

        canvas.drawOval(rect, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        val left = (source.width - size) / 2
        val top = (source.height - size) / 2

        canvas.drawBitmap(source, -left.toFloat(), -top.toFloat(), paint)
        return output
    }

    fun blur(source: Bitmap, radius: Int = 10): Bitmap {
        val bitmap = source.copy(Bitmap.Config.ARGB_8888, true)
        val paint = Paint().apply {
            isAntiAlias = true
            alpha = 0x40
        }
        val canvas = Canvas(bitmap)
        repeat(radius) {
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }
        return bitmap
    }
}