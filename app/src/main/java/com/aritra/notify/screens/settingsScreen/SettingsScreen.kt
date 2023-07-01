package com.aritra.notify.screens.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.components.BaseViewModel
import com.aritra.notify.components.SettingsComponent
import com.aritra.notify.components.SettingsTopAppBar

@Composable
fun SettingsScreen(

) {
    val baseViewModel = BaseViewModel()
    val context = LocalContext.current

    Scaffold(
        topBar = { SettingsTopAppBar() }
    ) {
        Surface(
            modifier = Modifier.padding(it),
            color = MaterialTheme.colorScheme.onSecondary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondary),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Dark Mode",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        )

                    }
                }
                SettingsComponent(
                    settingHeaderText = "Backup/Restore",
                    settingText = "Backup or Restore your tasks to a local zip file.",
                    painterResourceID = R.drawable.history
                ) {
                    // Dialog will open up
                }
                SettingsComponent(
                    settingHeaderText = "Visit Github",
                    settingText = "Notify is completely open source. \n Have a feedback visit Github!",
                    painterResourceID = R.drawable.code
                ) {
                    baseViewModel.openNotify(context)
                }
            }

        }

    }
}