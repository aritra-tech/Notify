package com.aritradas.notify.components.appbar

import androidx.compose.ui.graphics.vector.ImageVector

data class SelectionModeBottomBarItem(
    val name: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)
