package com.aritra.notify.ui.screens.settingsScreen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
                    .padding(14.dp)
            ) {
                /** App Settings. */

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "App Settings",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                )
                Spacer(modifier = Modifier.height(10.dp))
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
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.moon_icon),
                            contentDescription = stringResource(R.string.icon),
                            modifier = Modifier.size(30.dp),
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = stringResource(R.string.dark_mode),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        )
                        Switch(
                            modifier = Modifier
                                .semantics { contentDescription = "Theme Switch" }
                                .padding(start = 120.dp),
                            checked = themeState.isDarkMode,
                            onCheckedChange = { themeViewModel.toggleTheme() }
                        )
                    }
                }

                /** Import & Export. **/

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = stringResource(R.string.import_export),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                )
                Spacer(modifier = Modifier.height(5.dp))
                SettingsComponent(
                    settingHeaderText = stringResource(R.string.backup_data),
                    painterResourceID = R.drawable.backup_icon
                ) {
                    exportLauncher.launch(Const.DATABASE_FILE_NAME)
                }
                SettingsComponent(
                    settingHeaderText = stringResource(R.string.import_data),
                    painterResourceID = R.drawable.import_icon
                ) {
                    importLauncher.launch(arrayOf("*/*"))
                }

                /** Product. **/

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = stringResource(R.string.product),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                )
                SettingsComponent(
                    settingHeaderText = stringResource(R.string.visit_github),
                    painterResourceID = R.drawable.github_icon
                ) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Const.GITHUB_URL)
                    )
                    context.startActivity(intent)
                }
                SettingsComponent(
                    settingHeaderText = stringResource(R.string.request_feature),
                    painterResourceID = R.drawable.code
                ) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Const.GITHUB_ISSUE)
                    )
                    context.startActivity(intent)
                }
            }
        }
    }
}