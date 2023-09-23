package com.aritra.notify.navigation

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aritra.notify.R
import com.aritra.notify.components.dialog.PermissionDialog
import com.aritra.notify.components.dialog.RationaleDialog
import com.aritra.notify.ui.screens.notes.addNoteScreen.AddNotesScreen
import com.aritra.notify.ui.screens.notes.editNoteScreen.EditNotesScreen
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreen
import com.aritra.notify.ui.screens.settingsScreen.SettingsScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun NotifyApp(navController: NavHostController = rememberNavController()) {

    val bottomNavItem = listOf(
        BottomNavItem(
            name = "Notes",
            route = NotifyScreens.Notes.name,
            icon = R.drawable.note_outline
        ),
        BottomNavItem(
            name = "Settings",
            route = NotifyScreens.Settings.name,
            icon = R.drawable.settings_outline
        ),
    )
    val screensWithHiddenNavBar = listOf(
        NotifyScreens.AddNotes.name,
        "${NotifyScreens.UpdateNotes.name}/{noteId}"
    )
    val backStackEntry = navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (backStackEntry.value?.destination?.route !in screensWithHiddenNavBar) {
                NavigationBar(modifier = Modifier.height(75.dp)) {
                    bottomNavItem.forEach { item ->
                        NavigationBarItem(
                            alwaysShowLabel = true,
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.name,
                                    tint = if (backStackEntry.value?.destination?.route == item.route)
                                        MaterialTheme.colorScheme.onSurface
                                    else
                                        MaterialTheme.colorScheme.secondary
                                )
                            },
                            label = {
                                Text(
                                    text = item.name,
                                    color = if (backStackEntry.value?.destination?.route == item.route)
                                        MaterialTheme.colorScheme.onSurface
                                    else
                                        MaterialTheme.colorScheme.secondary,
                                    fontWeight = if (backStackEntry.value?.destination?.route == item.route)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal,
                                )
                            },
                            selected = backStackEntry.value?.destination?.route == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NotifyScreens.Notes.name,
            modifier = Modifier
                .padding(it)
        ) {
            composable(route = NotifyScreens.Notes.name) {
                NoteScreen(
                    onFabClicked = { navController.navigate(NotifyScreens.AddNotes.name) },
                    navigateToUpdateNoteScreen = { noteId ->
                        navController.navigate("${NotifyScreens.UpdateNotes.name}/$noteId")
                    }
                )
            }
            composable(
                route = "${NotifyScreens.UpdateNotes.name}/{noteId}",
                arguments = listOf(navArgument("noteId") { type = IntType })
            ) { backStack ->
                val noteId = backStack.arguments?.getInt("noteId") ?: 0
                EditNotesScreen(
                    noteId = noteId,
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable(NotifyScreens.AddNotes.name) {
                AddNotesScreen(navigateBack = { navController.popBackStack() })
            }
            composable(NotifyScreens.Settings.name) {
                SettingsScreen()
            }
        }
    }
}
