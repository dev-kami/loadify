package com.quad.loadify.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.ByteBuffer

internal object BitmapDecoder {

    @Throws(Exception::class)
    fun decode(inputStream: InputStream): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val bytes = inputStream.readBytes()
            val buffer = ByteBuffer.wrap(bytes)
            val source = ImageDecoder.createSource(buffer)
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
            bitmap
        } else {
            BitmapFactory.decodeStream(ByteArrayInputStream(inputStream.readBytes()))
                ?: throw Exception("Failed to decode bitmap")
        }
    }


}