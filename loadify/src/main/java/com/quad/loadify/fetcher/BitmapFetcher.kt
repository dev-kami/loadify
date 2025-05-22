package com.quad.loadify.fetcher

import android.graphics.Bitmap
import com.quad.loadify.model.ImageRequest

internal object BitmapFetcher {
    fun fetch(request: ImageRequest): Bitmap {
        return request.data as Bitmap
    }
}