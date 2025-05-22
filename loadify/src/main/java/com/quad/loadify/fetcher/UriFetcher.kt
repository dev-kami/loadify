package com.quad.loadify.fetcher

import android.content.Context
import android.net.Uri
import com.quad.loadify.model.ImageRequest
import java.io.InputStream

internal object UriFetcher {

    @Throws(Exception::class)
    fun fetch(context: Context, request: ImageRequest): InputStream {
        val uri = request.data as? Uri
            ?: throw IllegalArgumentException("Invalid URI data: ${request.data}")
        return context.contentResolver.openInputStream(uri)
            ?: throw Exception("Unable to open URI: $uri")
    }
}