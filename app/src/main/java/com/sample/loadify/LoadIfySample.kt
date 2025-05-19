package com.sample.loadify

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quad.loadify.compose.Loadify

@Composable
fun LoadIfySample(
    data: Any,
    modifier: Modifier = Modifier,
    circleCrop: Boolean = false,
    blurRadius: Int? = 10
) {
    Loadify(
        data = data,
        modifier = modifier
            .padding(16.dp)
            .size(80.dp),
        contentDescription = "Loaded content",
        circleCrop = circleCrop,
        blurRadius = blurRadius,
        placeholder = { Text("Loading...") },
        onLoadSuccess = {
            Log.d("Loadify", "Load Success")
        },
        onLoadError = {
            Log.d("Loadify", "Error: $it")
        },
        onLoading = {
            Log.d("Loadify", "Loading...")
        }
    )
}
