package com.quad.loadify.fetcher

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.quad.loadify.model.ImageRequest

object FetcherRegistry {

    @Throws(Exception::class)
    fun fetch(context: Context, request: ImageRequest): Any {
        return when (val data = request.data) {
            is String -> {
                if (data.startsWith("http")) {
                    NetworkFetcher.fetch(request)
                } else {
                    FileFetcher.fetch(request)
                }
            }
            is Uri -> UriFetcher.fetch(context, request)
           /* is Int -> context.resources.openRawResource(data)*/
            is Int -> ResourceFetcher.fetch(context,request)
            is Bitmap -> BitmapFetcher.fetch(request)
            else -> throw IllegalArgumentException("Unsupported data type: ${data::class}")
        }
    }
}