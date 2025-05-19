package com.quad.loadify.fetcher

import android.content.Context
import com.quad.loadify.model.ImageRequest
import java.io.InputStream

object ResourceFetcher {
    fun fetch(context: Context, request: ImageRequest): InputStream {
        val resId = request.data as Int
        return context.resources.openRawResource(resId)
    }
}