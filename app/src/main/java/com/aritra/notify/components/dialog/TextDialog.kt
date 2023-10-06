package com.aritra.notify.components.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.utils.Const

@Composable
fun TextDialog(
    modifier: Modifier = Modifier,
    isOpened: Boolean,
    onDismissCallback: () -> Unit,
    onConfirmCallback: () -> Unit,
    title: String = stringResource(R.string.warning),
    description: String = stringResource(R.string.are_you_sure_want_to_delete_these_items_it_cannot_be_recovered),
) {
    if (isOpened) {
        AlertDialog(
            onDismissRequest = { onDismissCallback() },
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmCallback() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.semantics { testTag = Const.CONFIRM_BUTTON }
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissCallback() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.semantics { testTag = Const.DISMISS_BUTTON }
                ) {
                    Text(
                        text = stringResource(R.string.dismiss),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
        )
    }
}
