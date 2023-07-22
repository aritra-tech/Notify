package com.aritra.notify.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aritra.notify.R
import com.aritra.notify.ui.screens.notes.addNoteScreen.AddNotesScreen
import com.aritra.notify.ui.screens.notes.editNoteScreen.EditNotesScreen
import com.aritra.notify.ui.screens.notes.homeScreen.HomeScreen
import com.aritra.notify.ui.screens.settingsScreen.SettingsScreen
import com.aritra.notify.ui.screens.todo.todoHomeScreen.TodoHomeScreen
import kotlinx.coroutines.delay

@Composable
fun NotifyApp(navController: NavHostController = rememberNavController(), onLoaded: () -> Unit) {

    LaunchedEffect(key1 = Unit) {
        delay(2000)
        onLoaded()
    }

    val bottomNavItem = listOf(
        BottomNavItem(
            route = NotifyScreens.Home.name,
            Icon = R.drawable.home_outline
        ),
        BottomNavItem(
            route = NotifyScreens.Settings.name,
            Icon = R.drawable.settings_outline
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
                NavigationBar(modifier = Modifier.height(60.dp)) {
                    bottomNavItem.forEach { item ->
                        NavigationBarItem(
                            alwaysShowLabel = true,
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.Icon),
                                    contentDescription = null,
                                    tint = if (backStackEntry.value?.destination?.route == item.route)
                                        MaterialTheme.colorScheme.onSurface
                                    else
                                        MaterialTheme.colorScheme.secondary
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
            startDestination = NotifyScreens.Home.name,
            modifier = Modifier
                .padding(it)
        ) {
            composable(route = NotifyScreens.Home.name) {
                HomeScreen(
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
            composable(NotifyScreens.TodoHome.name) {
                TodoHomeScreen()
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