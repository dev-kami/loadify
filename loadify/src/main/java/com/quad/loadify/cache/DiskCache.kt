package com.quad.loadify.cache

import android.content.Context
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.security.MessageDigest

object DiskCache {

    private fun cacheDir(context: Context): File {
        val dir = File(context.cacheDir, "loadify_images")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun keyHash(key: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(key.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun save(context: Context, key: String, data: ByteArray) {
        val file = File(cacheDir(context), keyHash(key))
        file.writeBytes(data)
    }

    fun read(context: Context, key: String): ByteArray? {
        val file = File(cacheDir(context), keyHash(key))
        return if (file.exists()) file.readBytes() else null
    }

    fun clear(context: Context) {
        cacheDir(context).deleteRecursively()
    }
}