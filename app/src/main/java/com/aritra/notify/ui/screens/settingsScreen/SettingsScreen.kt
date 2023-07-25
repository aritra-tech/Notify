package com.aritra.notify.ui.screens.settingsScreen

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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.actions.SettingsComponent
import com.aritra.notify.components.topbar.SettingsTopAppBar
import com.aritra.notify.viewmodel.ThemeViewModel
import com.aritra.notify.utils.Const

@Composable
fun SettingsScreen() {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val context = LocalContext.current
    var isDialogShowingState by rememberSaveable { mutableStateOf(false) }
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val themeState by themeViewModel.themeState.collectAsState()

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*"),
        onResult = { uri ->
            uri?.let { settingsViewModel.onExport(uri) }
        }
    )
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { settingsViewModel.onImport(uri) }
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
                            text = stringResource(R.string.dark_mode),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        )
                        Switch(
                            modifier = Modifier.semantics { contentDescription = "Theme Switch" },
                            checked = themeState.isDarkMode,
                            onCheckedChange = { themeViewModel.toggleTheme() }
                        )
                    }
                }
                SettingsComponent(
                    settingHeaderText = stringResource(R.string.export_import),
                    settingText = stringResource(R.string.export_or_import_your_notes_to_a_file),
                    painterResourceID = R.drawable.history
                ) {
                    // Dialog will open up
                    isDialogShowingState = true
                }
                SettingsComponent(
                    settingHeaderText = stringResource(R.string.visit_github),
                    settingText = stringResource(R.string.notify_is_completely_open_source_have_a_feedback_visit_github),
                    painterResourceID = R.drawable.code
                ) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/aritra-tech/Notify")
                    )
                    context.startActivity(intent)
                }
            }
        }
    }
    if (isDialogShowingState) {
        AlertDialog(
            onDismissRequest = {
                isDialogShowingState = false
            },
            title = {
                Text(stringResource(R.string.export_and_import))
            },
            text = {
                Text(stringResource(R.string.export_or_import_of_your_notes_internally_on_your_phone))
            },
            confirmButton = {
                OutlinedButton(onClick = {
                    exportLauncher.launch(Const.DATABASE_FILE_NAME)
                    isDialogShowingState = false
                }) {
                    Text(text = stringResource(R.string.export))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    importLauncher.launch(arrayOf("*/*"))
                    isDialogShowingState = false
                }) {
                    Text(text = stringResource(R.string.Import))
                }
            }
        )
    }
}