package com.aritra.notify.components.drawing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Picture
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.aritra.notify.ui.theme.NotifyTheme
import java.io.File

@Composable
fun DrawingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSave: (Uri) -> Unit,
) {
    var drawableFactory: DrawableFactory by remember { mutableStateOf({ Pencil(it) }) }
    var colorFactory: ColorFactory by remember { mutableStateOf({ Colors.last() }) }
    val picture = remember { Picture() }
    val context = LocalContext.current

    BackHandler(onBack = onBack)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                content = {
                    IconButton(
                        onClick = onBack,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    )
                    Toolbar(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        color = { color ->
                            colorFactory = { color }
                        },
                        updateDrawableFactory = { factory ->
                            drawableFactory = factory ?: drawableFactory
                        }
                    )
                    IconButton(
                        onClick = {
                            onSave(createBitmapFromPicture(picture).saveToDisk(context))
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save"
                            )
                        }
                    )
                }
            )
        },
        content = { scaffoldPadding ->
            DrawingCanvas(
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            val pictureCanvas = Canvas(
                                picture.beginRecording(
                                    size.width.toInt(),
                                    size.height.toInt()
                                )
                            )
                            draw(this, layoutDirection, pictureCanvas, size) {
                                this@onDrawWithContent.drawContent()
                            }
                            picture.endRecording()

                            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                        }
                    },
                colorFactory = colorFactory,
                drawableFactory = drawableFactory
            )
        }
    )
}

private fun createBitmapFromPicture(picture: Picture): Bitmap {
    val bitmap = Bitmap.createBitmap(
        picture.width,
        picture.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.drawPicture(picture)
    return bitmap
}

/**
 * Saves the bitmap to the disk
 */
private fun Bitmap.saveToDisk(context: Context): Uri {
    val file = File(
        File(context.externalCacheDir, "drawing").also {
            if (!it.exists()) {
                it.mkdir()
            }
        },
        "${System.currentTimeMillis()}.png"
    )

    file.outputStream().use { out ->
        compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() = NotifyTheme {
    DrawingScreen(
        onBack = {},
        onSave = { }
    )
}
