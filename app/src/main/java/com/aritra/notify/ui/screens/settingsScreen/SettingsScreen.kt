package com.aritra.notify.ui.screens.settingsScreen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aritra.notify.BuildConfig
import com.aritra.notify.R
import com.aritra.notify.components.actions.SettingsComponent
import com.aritra.notify.components.actions.SettingsSwitchCard
import com.aritra.notify.navigation.NotifyScreens
import com.aritra.notify.ui.screens.MainActivity
import com.aritra.notify.ui.theme.PrimaryContainerLight
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

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.settings),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily(Font(R.font.poppins_light))
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            SettingsItemGroup {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = stringResource(R.string.general),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
                SettingsSwitchCard(
                    text = stringResource(id = R.string.dark_mode),
                    icon = R.drawable.moon_icon,
                    isChecked = themeState.isDarkMode,
                    onCheckedChange = {
                        themeViewModel.toggleTheme()
                    }
                )
                SettingsComponent(
                    onClick = {
                        controller.navigate(NotifyScreens.TrashNoteScreen.name)
                    },
                    itemName = stringResource(id = R.string.trash),
                    iconId = R.drawable.ic_delete
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsItemGroup {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = stringResource(R.string.security),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
                SettingsSwitchCard(
                    text = stringResource(id = R.string.block_ss),
                    icon = R.drawable.block_ss,
                    isChecked = themeState.isSecureEnv,
                    onCheckedChange = {
                        themeViewModel.toggleSecureEnv()
                    }
                )

                SettingsSwitchCard(
                    text = stringResource(id = R.string.biometric),
                    icon = R.drawable.ic_fingerprint,
                    isChecked = biometricAuthState,
                    onCheckedChange = {
                        settingsViewModel.showBiometricPrompt(context as MainActivity)
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsItemGroup {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = stringResource(R.string.import_export),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                )
                SettingsComponent(
                    onClick = {
                        exportLauncher.launch(Const.DATABASE_FILE_NAME)
                    },
                    itemName = stringResource(R.string.backup_data),
                    iconId = R.drawable.backup_icon
                )

                SettingsComponent(
                    onClick = {
                        importLauncher.launch(arrayOf("*/*"))
                    },
                    itemName = stringResource(R.string.import_data),
                    iconId = R.drawable.import_icon
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsItemGroup {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = stringResource(R.string.others),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                )

                SettingsComponent(
                    itemName = stringResource(R.string.rate_us_on_google_play),
                    iconId = R.drawable.star_icon
                ) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Const.PLAY_STORE)
                    )
                    context.startActivity(intent)
                }

                SettingsComponent(
                    itemName = stringResource(R.string.share_notify),
                    iconId = R.drawable.share_icon
                ) {
                    shareApp(context)
                }

                SettingsComponent(
                    itemName = stringResource(R.string.request_feature),
                    iconId = R.drawable.code
                ) {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(context.resources.getString(R.string.mailTo))
                    context.startActivity(openURL)
                }

                SettingsComponent(
                    itemName = stringResource(R.string.privacy_policy),
                    iconId = R.drawable.policy
                ) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Const.PRIVACY_POLICY)
                    )
                    context.startActivity(intent)
                }
            }
        }

        Text(
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
            text = "Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            style = TextStyle(fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.poppins_light))),
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.by_aritra_das),
            style = TextStyle(fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.poppins_light))),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsItemGroup(columnScope: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(PrimaryContainerLight)
            .padding(top = 13.dp, bottom = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        columnScope()
    }
}
