package com.quad.loadify.fetcher

import com.quad.loadify.model.ImageRequest
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

internal object FileFetcher {

    @Throws(Exception::class)
    fun fetch(request: ImageRequest): InputStream {
        val filePath = request.data.toString()
        val file = File(filePath)

        if (!file.exists()) {
            throw Exception("File not found: $filePath")
        }

        return FileInputStream(file)
    }
}