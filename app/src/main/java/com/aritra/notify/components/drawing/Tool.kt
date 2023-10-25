package com.aritra.notify.components.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val Toolbar = Color(0xFFF4F4F4)
val Tool = Color(0xFF747474)
val SelectedTool = Color(0xFFe4e4e4)

private val Tools = listOf(
    "Pencil" to Icons.Outlined.Edit,
    "Arrow" to Icons.Outlined.ArrowOutward,
    "Rectangle" to Icons.Outlined.Square,
    "Oval" to Icons.Outlined.Circle,
    "Color Palette" to Icons.Outlined.ColorLens
)

internal val Colors = listOf(
    Color(251, 0, 8),
    Color(0, 127, 8),
    Color(0, 120, 122),
    Color(0, 0, 0)
)

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    color: (Color) -> Unit,
    updateDrawableFactory: (DrawableFactory?) -> Unit,
) {
    // holds the currently selected tool index
    var selected by remember { mutableIntStateOf(0) }
    var showColorPalette by remember { mutableStateOf(false) }
    var lastSelected by remember { mutableIntStateOf(0) }

    val bar = Modifier
        .clip(MaterialTheme.shapes.large)
        .background(Toolbar)
        .padding(5.dp)

    val updateColor = remember {
        { color: Color ->
            // revert the selected item back to the tool that was selected before
            selected = lastSelected
            // hide the color palette once a color is clicked
            showColorPalette = false
            // update the color used for drawing
            color(color)
        }
    }

    val updateTool: (Int) -> Unit = remember {
        { index ->
            selected = index
            showColorPalette = (index == Tools.lastIndex)
            if (!showColorPalette) {
                lastSelected = index

                if (index in 0..Tools.lastIndex) {
                    updateDrawableFactory {
                        when (index) {
                            0 -> Pencil(it)
                            1 -> Arrow(it)
                            2 -> Rectangle(it)
                            else -> Oval(it)
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top,
        content = {
            Row(
                modifier = bar.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                content = {
                    Tools.mapIndexed { index, tool ->
                        Tool(
                            name = tool.first,
                            image = tool.second,
                            selected = selected == index,
                            onClick = { updateTool(index) }
                        )
                    }
                }
            )
            if (showColorPalette) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = bar,
                    content = {
                        Colors.forEach { color ->
                            Color(
                                color = color,
                                onClick = { updateColor(color) }
                            )
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun Tool(
    name: String,
    image: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(if (selected) SelectedTool else Color.Transparent)
            .clickable(onClick = onClick),
        content = {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                imageVector = image,
                contentDescription = name,
                tint = if (selected) Color.Black else Tool
            )
        }
    )
}

@Composable
fun Color(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color)
            .size(32.dp)
            .clickable(onClick = onClick)
    )
}
