package com.sample.loadify

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap

@Composable
fun LoadIfyDemoScreen() {
    val scrollState = rememberScrollState()
    var uri by remember { mutableStateOf<Uri?>(null) }
    val testBitmap = createBitmap(100, 100)
    val canvas = Canvas(testBitmap)
    val paint = Paint().apply {
        color = Color.RED
    }
    canvas.drawRect(0f, 0f, 100f, 100f, paint)
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
        ) {
            Row{
                SampleSection(title = "Vector Drawable") {
                    LoadIfySample(data = R.drawable.ic_benfit)
                }

                SampleSection(title = "Image from URL") {
                    LoadIfySample(
                        data = "https://cdn.shopclues.com/images/thumbnails/79835/320/320/104787525124666394ID1006929615021796911502242942.jpg"
                    )
                }
            }
            Divider()

            Row{
                SampleSection(title = "GIF from Drawable") {
                    LoadIfySample(data = R.drawable.earth)
                }

                SampleSection(title = "Lottie from Raw") {
                    LoadIfySample(data = R.raw.earth)
                }

            }
            Divider()

            Row{
                SampleSection(title = "Lottie from URL") {
                    LoadIfySample(
                        data = "https://d3cb3akjtc97pv.cloudfront.net/lottie/premium/original/11125668.json?token=eyJhbGciOiJoczI1NiIsImtpZCI6ImRlZmF1bHQifQ__.eyJpc3MiOiJkM2NiM2FranRjOTdwdi5jbG91ZGZyb250Lm5ldCIsImV4cCI6MTc0Nzg5MjE5NiwicSI6bnVsbCwiaWF0IjoxNzQ3NjMyOTk2fQ__.fdda23dad67ead4a4caebac214db9960351d9790bc9d990bba165cbb6b83ba66"
                    )
                }

                SampleSection(title = "Image from URI") {
                    if (uri == null) {
                        UriImagePicker { pickedUri ->
                            uri = pickedUri
                        }
                    } else {
                        uri?.let {
                            LoadIfySample(data = it)
                        }
                    }
                }

            }
            Divider()

            Row{
                SampleSection(title = "Bitmap") {
                    LoadIfySample(data = testBitmap, circleCrop = true)
                }

                SampleSection(title = "WebP Image") {
                    LoadIfySample(data = R.drawable.cherry)
                }


            }
            Divider()
            Row{
            SampleSection(title = "PNG Image") {
                LoadIfySample(data = R.drawable.butterfly)
            }
                SampleSection(title = "SVG Image") {
                LoadIfySample(data = "https://www.svgrepo.com/show/19461/url-link.svg")
            }
            }
        }
    }
}

@Composable
fun RowScope.SampleSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.weight(1f)) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        style = MaterialTheme.typography.titleMedium
    )
    content()
    }
}



@Composable
fun UriImagePicker(onImagePicked: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }

    Button(onClick = { launcher.launch("image/*") }) {
        Text("Pick Image")
    }
}