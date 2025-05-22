package com.quad.loadify.core

import android.content.Context
import android.graphics.Bitmap
import com.quad.loadify.manager.ImageRequestManager
import com.quad.loadify.model.ImageRequest

internal object ImageLoader {
    suspend fun load(context: Context, request: ImageRequest): Result<Any> {
        return ImageRequestManager.execute(context, request)
    }
}