package com.aritra.notify.components.actions
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aritra.notify.R

@Composable
fun LayoutToggleButton(
    isGridView: Boolean,
    onToggleClick: () -> Unit,
) {
    val customGridViewImage = painterResource(R.drawable.grid_icon)
    val customAgendaViewImage = painterResource(R.drawable.list_icon)

    val imageToShow = if (isGridView) customGridViewImage else customAgendaViewImage

    val tint = LocalContentColor.current

    IconButton(
        onClick = onToggleClick,
        modifier = Modifier.padding(4.dp)
    ) {
        Image(imageToShow, contentDescription = "Toggle Button", colorFilter = ColorFilter.tint(tint))
    }
}
