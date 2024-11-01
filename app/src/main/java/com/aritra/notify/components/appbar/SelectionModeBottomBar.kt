package com.aritra.notify.components.appbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritra.notify.R

@Composable
fun SelectionModeBottomBar(
    modifier: Modifier = Modifier,
    shouldShowPinIcon: Boolean = true,
    onPinClick: () -> Unit,
    onUnpinClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val items = listOf(
        if (shouldShowPinIcon) {
            SelectionModeBottomBarItem(
                name = stringResource(id = R.string.pin_note),
                icon = Icons.Outlined.PushPin,
                onClick = onPinClick
            )
        } else {
            SelectionModeBottomBarItem(
                name = stringResource(id = R.string.unpin_note),
                icon = ImageVector.vectorResource(R.drawable.unpin_icon),
                onClick = onUnpinClick
            )
        },
        SelectionModeBottomBarItem(
            name = stringResource(id = R.string.trash),
            icon = Icons.Outlined.DeleteOutline,
            onClick = onDeleteClick
        )
    )

    BottomAppBar(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        containerColor = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { it.onClick() }) {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.name
                        )
                    }
                    Text(
                        text = it.name,
                        modifier = Modifier.offset(y = -(10.dp)),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                }
            }
        }
    }
}
