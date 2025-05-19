package com.quad.loadify.compose


import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.AnimatedImageDrawable
import android.view.View
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.AsyncUpdates
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.*
import com.quad.loadify.core.ImageLoader
import com.quad.loadify.model.ImageRequest
import com.quad.loadify.utils.ImageTransformer

@Composable
fun Loadify(
    data: Any,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    filterQuality: FilterQuality = DefaultFilterQuality,
    headers: Map<String, String> = emptyMap(),
    placeholder: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null,
    circleCrop: Boolean = false,
    blurRadius: Int? = null,
    lottieOptions: LottieOptions = LottieOptions(),
    onLoadSuccess: (() -> Unit)? = null,
    onLoadError: ((Throwable?) -> Unit)? = null,
    onLoading: (() -> Unit)? = null) {
    val context = LocalContext.current
    var result by remember { mutableStateOf<Any?>(null) }
    var loading by remember { mutableStateOf(true) }
    var failure by remember { mutableStateOf<Throwable?>(null) }

    LaunchedEffect(data) {
        loading = true
        result = null
        failure = null
        onLoading?.invoke()

        val transform: ((Bitmap) -> Bitmap)? = when {
            circleCrop -> ImageTransformer::circleCrop
            blurRadius != null -> { bmp -> ImageTransformer.blur(bmp, blurRadius) }
            else -> null
        }

        val request = ImageRequest(data = data, headers = headers, transform = transform)
        val response = ImageLoader.load(context, request)

        response.onSuccess {
            result = it
            loading = false
            onLoadSuccess?.invoke()
        }.onFailure {
            failure = it
            loading = false
            onLoadError?.invoke(it)
        }
    }

    when {
        loading && placeholder != null -> placeholder()
        failure != null && error != null -> error()
        result is Bitmap -> Image(
            bitmap = (result as Bitmap).asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            colorFilter = colorFilter,
            alignment = alignment,
            alpha = alpha,
            filterQuality = filterQuality,
            modifier = modifier
        )

        result is AnimatedImageDrawable -> {
            val gif = result as AnimatedImageDrawable
            AndroidView(
                factory = { ctx ->
                    ImageView(ctx).apply {
                        setImageDrawable(gif)
                        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                            override fun onViewAttachedToWindow(v: View) {
                                gif.start()
                                removeOnAttachStateChangeListener(this)
                            }

                            override fun onViewDetachedFromWindow(v: View) {}
                        })
                    }
                },
                modifier = modifier
            )
        }

        result == null && data is Int && context.resources.getResourceTypeName(data) == "raw" -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(data))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = lottieOptions.iterations,
                isPlaying = lottieOptions.isPlaying
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = modifier,

            )
        }

        result == null && data is String && data.substringBefore('?').endsWith(".json", ignoreCase = true) -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.Url(data))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = lottieOptions.iterations,
                isPlaying = lottieOptions.isPlaying
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = modifier,
                alignment=alignment,
                contentScale=contentScale,
                outlineMasksAndMattes = lottieOptions.outlineMasksAndMattes,
                applyOpacityToLayers = lottieOptions.applyOpacityToLayers,
                enableMergePaths = lottieOptions.enableMergePaths,
                renderMode = lottieOptions.renderMode,
                maintainOriginalImageBounds = lottieOptions.maintainOriginalImageBounds,
                dynamicProperties = lottieOptions.dynamicProperties,
                clipToCompositionBounds = lottieOptions.clipToCompositionBounds,
                fontMap = lottieOptions.fontMap,
                asyncUpdates = lottieOptions.asyncUpdates,
                safeMode = lottieOptions.safeMode,
                clipTextToBoundingBox = lottieOptions.clipTextToBoundingBox,

            )
        }

        else -> Unit
    }
}

data class LottieOptions(
    val outlineMasksAndMattes: Boolean = false,
    val applyOpacityToLayers: Boolean = false,
    val enableMergePaths: Boolean = false,
    val renderMode: RenderMode = RenderMode.AUTOMATIC,
    val maintainOriginalImageBounds: Boolean = false,
    val dynamicProperties: LottieDynamicProperties? = null,
    val clipToCompositionBounds: Boolean = true,
    val clipTextToBoundingBox: Boolean = false,
    val fontMap: Map<String, Typeface>? = null,
    val asyncUpdates: AsyncUpdates = AsyncUpdates.AUTOMATIC,
    val safeMode: Boolean = false,
    val isPlaying: Boolean = true,
    val iterations: Int = LottieConstants.IterateForever
)



