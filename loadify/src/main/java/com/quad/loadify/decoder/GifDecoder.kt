package com.quad.loadify.decoder

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.ByteBuffer

internal object GifDecoder {

    @RequiresApi(Build.VERSION_CODES.P)
    @Throws(Exception::class)
    fun decode(inputStream: InputStream): AnimatedImageDrawable? {
        val bytes = inputStream.readBytes()
        val buffer = ByteBuffer.wrap(bytes)
        val source = ImageDecoder.createSource(buffer) // ✅ Safe for API 28+
        val drawable = ImageDecoder.decodeDrawable(source)
        if (drawable is AnimatedImageDrawable) {
            drawable.start()
           return drawable
        } else {
            throw Exception("GIF decoding failed: Not animated")
        }
    }
}
