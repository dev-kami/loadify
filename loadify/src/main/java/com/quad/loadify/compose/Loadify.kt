package com.quad.loadify.compose


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Movie
import android.graphics.Typeface
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperties
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.quad.loadify.core.ImageLoader
import com.quad.loadify.model.ImageRequest
import com.quad.loadify.utils.BitmapUtils.safeDownsample
import com.quad.loadify.utils.ImageTransformer

@Composable
fun Loadify(
    data: Any,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
    enableCache:Boolean = true,
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

        val request = ImageRequest(data = data, headers = headers, transform = transform, isCacheEnabled = enableCache)
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

        result is Bitmap -> {
            val safeBitmap = remember(result) { (result as Bitmap).safeDownsample(2048) }
            Image(
                bitmap = safeBitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                colorFilter = colorFilter,
                alignment = alignment,
                alpha = alpha,
                filterQuality = filterQuality,
                modifier = modifier
            )
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && result is AnimatedImageDrawable   -> {
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

        Build.VERSION.SDK_INT > Build.VERSION_CODES.P && result == null && data is Int && data.toString().contains(".gif",ignoreCase = true) &&  context.resources.getResourceTypeName(data) == "drawable" -> {
            LegacyGifImage(gifResId = data, modifier = modifier)
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
@Composable
internal fun LegacyGifImage(
    @RawRes gifResId: Int,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            object : View(ctx) {
                private val movie: Movie = ctx.resources.openRawResource(gifResId).use {
                    Movie.decodeStream(it)
                }
                private val handler = Handler(Looper.getMainLooper())
                private var startTime: Long = 0

                private val drawRunnable = object : Runnable {
                    override fun run() {
                        invalidate()
                        handler.postDelayed(this, 16L)
                    }
                }

                init {
                    setLayerType(LAYER_TYPE_SOFTWARE, null)
                    startTime = SystemClock.uptimeMillis()
                    handler.post(drawRunnable)
                }

                override fun onDraw(canvas: Canvas) {
                    val now = SystemClock.uptimeMillis()
                    val relTime = (now - startTime) % movie.duration()
                    movie.setTime(relTime.toInt())
                    movie.draw(canvas, 0f, 0f)
                }
            }
        },
        modifier = modifier
    )
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



