package com.quad.loadify.decoder

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import com.caverock.androidsvg.SVG
import java.io.InputStream
import androidx.core.graphics.createBitmap

internal object SvgDecoder {

    @Throws(Exception::class)
    fun decode(inputStream: InputStream, width: Int = 512, height: Int = 512): Bitmap {
        val svg = SVG.getFromInputStream(inputStream)
        svg.setDocumentWidth("100%")
        svg.setDocumentHeight("100%")
        val picture: Picture = svg.renderToPicture()

        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        canvas.drawPicture(picture)

        return bitmap
    }
}