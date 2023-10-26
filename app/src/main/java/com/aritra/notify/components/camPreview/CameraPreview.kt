package com.aritra.notify.components.camPreview

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.Locale

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        }
    )
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    close: () -> Unit,
    onImageCaptured: (Uri) -> Unit,
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    val iconModifier = Modifier.background(
        color = MaterialTheme.colorScheme.surface,
        shape = CircleShape
    )

    Box(
        modifier = modifier.fillMaxSize(),
        content = {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                controller = controller
            )

            IconButton(
                modifier = Modifier
                    .offset(16.dp, 16.dp)
                    .then(iconModifier),
                onClick = close,
                content = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                content = {
                    IconButton(
                        modifier = iconModifier,
                        onClick = {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Cameraswitch,
                                contentDescription = "Switch Camera"
                            )
                        }
                    )

                    IconButton(
                        modifier = iconModifier,
                        onClick = {
                            takePhoto(
                                controller,
                                context,
                                onPhotoCaptured = { uri ->
                                    if (uri == null) return@takePhoto
                                    onImageCaptured(uri)
                                }
                            )
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.PhotoCamera,
                                contentDescription = "Click To Capture"
                            )
                        }
                    )
                }
            )
        }
    )
}

private fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    onPhotoCaptured: (Uri?) -> Unit,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                    if (controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                        postScale(-1f, 1f)
                    }
                }
                val bitmap = Bitmap.createBitmap(image.toBitmap(), 0, 0, image.width, image.height, matrix, true)
                onPhotoCaptured(bitmap.toUri(context = context))
                Toast.makeText(context, "Photo Attached Successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Toast.makeText(context, "Something Went Wrong ! Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

private fun Bitmap.toUri(
    context: Context,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
): Uri? {
    val uri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/${format.name.lowercase(Locale.ROOT)}")
        }
    ) ?: return null

    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
        if (compress(format, 100, outputStream)) {
            return uri
        }
    }

    return null
}
