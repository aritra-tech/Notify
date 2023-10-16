package com.aritra.notify.ui.screens.settingsScreen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aritra.notify.R
import com.aritra.notify.components.actions.SettingsComponent
import com.aritra.notify.components.actions.SettingsSwitchCard
import com.aritra.notify.navigation.NotifyScreens
import com.aritra.notify.ui.screens.MainActivity
import com.aritra.notify.utils.Const
import com.aritra.notify.utils.shareApp
import com.aritra.notify.viewmodel.ThemeViewModel

@Composable
fun SettingsScreen(controller: NavController) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val context = LocalContext.current
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val themeState by themeViewModel.themeState.collectAsState()
    val biometricAuthState by settingsViewModel.biometricAuthState.collectAsState()

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

    Scaffold {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                /** App Settings. */

                item {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(R.string.app_settings),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SettingsSwitchCard(
                        text = stringResource(id = R.string.dark_mode),
                        icon = painterResource(id = R.drawable.moon_icon),
                        isChecked = themeState.isDarkMode,
                        onCheckedChange = {
                            themeViewModel.toggleTheme()
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SettingsComponent(settingHeaderText = stringResource(id = R.string.trash), painterResourceID =R.drawable.ic_delete) {
                        controller.navigate(NotifyScreens.TrashNoteScreen.name)
                    }
                }

                /** Security Settings. */

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(R.string.security),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SettingsSwitchCard(
                        text = stringResource(id = R.string.secure_screen),
                        icon = painterResource(id = R.drawable.phonelink_lock),
                        isChecked = themeState.isSecureEnv,
                        onCheckedChange = {
                            themeViewModel.toggleSecureEnv()
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SettingsSwitchCard(
                        text = stringResource(id = R.string.biometric),
                        icon = painterResource(id = R.drawable.ic_fingerprint),
                        isChecked = biometricAuthState,
                        onCheckedChange = {
                            settingsViewModel.showBiometricPrompt(context as MainActivity)
                        }
                    )
                }

                /** Import & Export. **/

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(R.string.import_export),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
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
                }

                /** Others. **/

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(R.string.others),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                    SettingsComponent(
                        settingHeaderText = stringResource(R.string.rate_us_on_google_play),
                        painterResourceID = R.drawable.star_icon
                    ) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Const.PLAY_STORE)
                        )
                        context.startActivity(intent)
                    }
                    SettingsComponent(
                        settingHeaderText = stringResource(R.string.share_notify),
                        painterResourceID = R.drawable.share_icon
                    ) {
                        shareApp(context)
                    }
                }

                /** Product. **/

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(R.string.product),
                        fontSize = 14.sp,
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
                        settingHeaderText = "Roadmap",
                        painterResourceID = R.drawable.roadmap_icon
                    ) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Const.ROADMAP)
                        )
                        context.startActivity(intent)
                    }
                    SettingsComponent(
                        settingHeaderText = stringResource(R.string.request_feature),
                        painterResourceID = R.drawable.code
                    ) {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(context.resources.getString(R.string.mailTo))
                        context.startActivity(openURL)
                    }
                    SettingsComponent(
                        settingHeaderText = stringResource(R.string.privacy_policy),
                        painterResourceID = R.drawable.policy
                    ) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Const.PRIVACY_POLICY)
                        )
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}
