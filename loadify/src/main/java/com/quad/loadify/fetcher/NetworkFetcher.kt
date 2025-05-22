package com.quad.loadify.fetcher

import com.quad.loadify.model.ImageRequest
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

internal object NetworkFetcher {

    private val client = OkHttpClient()

    @Throws(Exception::class)
    fun fetch(request: ImageRequest): InputStream {
        val httpRequest = Request.Builder()
            .url(request.data.toString())
            .apply {
                request.headers.forEach { (key, value) ->
                    addHeader(key, value)
                }
            }
            .build()

        val response = client.newCall(httpRequest).execute()

        if (!response.isSuccessful) {
            throw Exception("HTTP error ${response.code}")
        }

        return response.body?.byteStream() ?: throw Exception("No response body")
    }
}