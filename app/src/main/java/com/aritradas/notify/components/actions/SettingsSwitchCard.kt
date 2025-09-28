package com.aritradas.notify.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritradas.notify.R
import com.aritradas.notify.ui.theme.OnPrimaryContainerLight

@Composable
fun SettingsSwitchCard(
    text: String,
    icon: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = icon),
            contentDescription = stringResource(R.string.icon),
            tint = Color.Unspecified
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = OnPrimaryContainerLight,
            fontFamily = FontFamily(Font(R.font.poppins_light))
        )

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            modifier = Modifier
                .semantics { contentDescription = "Theme Switch" },
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
