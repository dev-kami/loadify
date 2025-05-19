package com.quad.loadify.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimatedImageDrawable
import android.util.Log
import com.quad.loadify.cache.DiskCache
import com.quad.loadify.cache.MemoryCache
import com.quad.loadify.decoder.DecoderRegistry
import com.quad.loadify.fetcher.FetcherRegistry
import com.quad.loadify.model.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.InputStream

/*object ImageRequestManager {

    suspend fun execute(context: Context, request: ImageRequest): Result<Any> {
        return withContext(Dispatchers.IO) {
            try {
                val key = request.key ?: request.data.toString()

                // Check Memory Cache
                MemoryCache.get(key)?.let {
                    return@withContext Result.success(it)
                }

                // Check Disk Cache
                val diskData = DiskCache.read(context, key)
                val inputStream: InputStream = if (diskData != null) {
                    ByteArrayInputStream(diskData)
                } else {
                    val fetched = FetcherRegistry.fetch(context, request)
                    if (fetched is InputStream) {
                        val bytes = fetched.readBytes()
                        DiskCache.save(context, key, bytes)
                        ByteArrayInputStream(bytes)
                    } else if (fetched is Bitmap) {
                        MemoryCache.put(key, fetched)
                        return@withContext Result.success(fetched)
                    } else {
                        throw Exception("Unsupported fetch result")
                    }
                }

                val extension = request.data.toString().substringAfterLast('.', "")

                val decoded = when (request.data) {
                    is Int -> DecoderRegistry.decodeResource(context, request.data)
                    else -> DecoderRegistry.decode(context, inputStream, extension)
                }
                // Apply transform only to Bitmap
                val finalResult = when (decoded) {
                    is Bitmap -> {
                        val transformed = request.transform?.invoke(decoded) ?: decoded
                        MemoryCache.put(key, transformed)
                        transformed
                    }
                    is AnimatedImageDrawable -> decoded
                    else -> throw Exception("Unsupported decoded type: ${decoded::class}")
                }

                Result.success(finalResult)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}*/


object ImageRequestManager {

    suspend fun execute(context: Context, request: ImageRequest): Result<Any> {
        return withContext(Dispatchers.IO) {
            try {
                val key = request.key ?: request.data.toString()

                // Check memory cache
                MemoryCache.get(key)?.let {
                    return@withContext Result.success(it)
                }

                val decoded: Any = when (val data = request.data) {
                    is Int -> {
                        // Resource ID (image or vector drawable or gif)
                        DecoderRegistry.decodeResource(context, data)
                    }
                    is Bitmap -> data
                    else -> {
                        // Check Disk Cache
                        val diskData = DiskCache.read(context, key)
                        val inputStream: InputStream = if (diskData != null) {
                            ByteArrayInputStream(diskData)
                        } else {
                            val fetched = FetcherRegistry.fetch(context, request)
                            if (fetched is InputStream) {
                                val bytes = fetched.readBytes()
                                DiskCache.save(context, key, bytes)
                                ByteArrayInputStream(bytes)
                            } else if (fetched is Bitmap) {
                                MemoryCache.put(key, fetched)
                                return@withContext Result.success(fetched)
                            } else {
                                throw Exception("Unsupported fetch result")
                            }
                        }
                        val extension = data.toString()
                            .substringBefore('?')      // Remove token/query
                            .substringBefore('#')      // Remove hash if any
                            .substringAfterLast('.', "")

                        Log.d("substringAfterLast","${extension}")
                        DecoderRegistry.decode(context, inputStream, extension)
                    }
                }

                // Apply transform if bitmap
                val finalResult = when (decoded) {
                    is Bitmap -> {
                        val transformed = request.transform?.invoke(decoded) ?: decoded
                        MemoryCache.put(key, transformed)
                        transformed
                    }
                    is AnimatedImageDrawable -> {
                        decoded
                    }
                    else -> throw Exception("Unsupported decoded type: ${decoded::class}")
                }

                Result.success(finalResult)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
