package com.aritra.notify.ui.screens.notes.addEditScreen

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aritra.notify.R
import com.aritra.notify.ui.theme.NotifyTheme
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage

@Composable
fun NoteImages(
    images: List<Uri>,
    isNew: Boolean,
    modifier: Modifier = Modifier,
    onRemoveImage: (Int) -> Unit,
) {
    if (images.isNotEmpty()) {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            content = {
                items(
                    count = images.size,
                    itemContent = { index ->
                        Box(
                            Modifier
                                .size(180.dp)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            content = {
                                ZoomableAsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = images[index],
                                    contentDescription = stringResource(R.string.image),
                                    contentScale = ContentScale.Crop
                                )
                                if (isNew) {
                                    FilledTonalIconButton(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(25.dp),
                                        onClick = {
                                            onRemoveImage(index)
                                        },
                                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                                alpha = 0.6f
                                            )
                                        ),
                                        content = {
                                            Icon(
                                                imageVector = Icons.Outlined.Close,
                                                contentDescription = stringResource(R.string.clear_image)
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteImagesPreview() = NotifyTheme {
    NoteImages(
        images = listOf(),
        onRemoveImage = {},
        isNew = false
    )
}
