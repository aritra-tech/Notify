package com.aritra.notify.screens.settingsScreen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.customSwitch.CustomSwitch
import com.aritra.notify.components.actions.SettingsComponent
import com.aritra.notify.components.topbar.SettingsTopAppBar
import com.aritra.notify.utils.Const

@Composable
fun SettingsScreen() {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val context = LocalContext.current
    var isDialogShowingState by rememberSaveable { mutableStateOf(false) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*"),
        onResult = { uri ->
            if (uri != null) settingsViewModel.onExport(uri)
        }
    )
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) settingsViewModel.onImport(uri)
        }
    )

    Scaffold(
        topBar = { SettingsTopAppBar() }
    ) {
        Surface(
            modifier = Modifier.padding(it)
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
                        .background(MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Dark Mode",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        )
                        CustomSwitch()
                    }
                }
                SettingsComponent(
                    settingHeaderText = "Export/Import",
                    settingText = "Export or Import your notes to a file.",
                    painterResourceID = R.drawable.history
                ) {
                    // Dialog will open up
                    isDialogShowingState = true
                }
                SettingsComponent(
                    settingHeaderText = "Visit Github",
                    settingText = "Notify is completely open source. \n Have a feedback visit Github!",
                    painterResourceID = R.drawable.code
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/aritra-tech/Notify"))
                    context.startActivity(intent)
                }
            }
        }
    }
    if(isDialogShowingState){
        AlertDialog(
            onDismissRequest = {
                isDialogShowingState = false
            },
            title = {
                Text("Export & Import")
            },
            text = {
                Text("Export or Import of your notes internally on your phone.")
            },
            confirmButton = {
                OutlinedButton(onClick = {
                    exportLauncher.launch(Const.DATABASE_FILE_NAME)
                    isDialogShowingState = false
                }) {
                    Text(text = "Export")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    importLauncher.launch(arrayOf("*/*"))
                    isDialogShowingState = false
                }) {
                    Text(text = "Import")
                }
            }
        )
    }
}