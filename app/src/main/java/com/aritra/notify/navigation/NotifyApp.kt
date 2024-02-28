package com.aritra.notify.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aritra.notify.R
import com.aritra.notify.ui.screens.notes.addEditScreen.route.AddEditRoute
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreen
import com.aritra.notify.ui.screens.notes.trash.TrashNoteEffect
import com.aritra.notify.ui.screens.notes.trash.TrashNoteScreen
import com.aritra.notify.ui.screens.notes.trash.TrashNoteViewModel
import com.aritra.notify.ui.screens.settingsScreen.SettingsScreen
import com.aritra.notify.ui.theme.FadeIn
import com.aritra.notify.ui.theme.FadeOut

@Composable
fun NotifyApp(navController: NavHostController = rememberNavController()) {
    val screensWithHiddenNavBar = listOf(
        "${NotifyScreens.AddEditNotes.name}/{noteId}",
        NotifyScreens.TrashNoteScreen.name
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val trashViewModel = hiltViewModel<TrashNoteViewModel>()
    val state by trashViewModel.state.collectAsState()
    val effect by trashViewModel.effect.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                TrashNoteEffect.Close -> {
                    navController.popBackStack()
                    trashViewModel.resetEffect()
                }

                is TrashNoteEffect.Message -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    trashViewModel.closePage()
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                backStackEntry,
                screensWithHiddenNavBar,
                navController
            )
        }
    ) { scaffoldPadding ->
        NavHost(
            navController = navController,
            startDestination = NotifyScreens.Notes.name,
            modifier = Modifier.padding(scaffoldPadding),
            enterTransition = { FadeIn },
            exitTransition = { FadeOut },
            popEnterTransition = { FadeIn },
            popExitTransition = { FadeOut }
        ) {
            composable(
                route = NotifyScreens.Notes.name
            ) {
                NoteScreen(
                    onFabClicked = { navController.navigate(NotifyScreens.AddEditNotes.name + "/-1") },
                    navigateToUpdateNoteScreen = { noteId ->
                        navController.navigate("${NotifyScreens.AddEditNotes.name}/$noteId")
                    }
                )
            }

            composable(
                route = "${NotifyScreens.AddEditNotes.name}/{noteId}",
                arguments = listOf(navArgument("noteId") { type = IntType })
            ) { backStack ->
                AddEditRoute(
                    navController = navController,
                    backStack = backStack
                )
            }

            composable(
                route = NotifyScreens.Settings.name
            ) {
                SettingsScreen(controller = navController)
            }

            composable(
                route = NotifyScreens.TrashNoteScreen.name
            ) {
                TrashNoteScreen(trashNoteState = state, onEvent = trashViewModel::onEvent)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    backStackEntry: State<NavBackStackEntry?>,
    screensWithHiddenNavBar: List<String>,
    navController: NavHostController,
) {
    if (backStackEntry.value?.destination?.route !in screensWithHiddenNavBar) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.height(75.dp)
        ) {
            val bottomNavItem = listOf(
                BottomNavItem(
                    name = "Notes",
                    route = NotifyScreens.Notes.name,
                    icon = ImageVector.vectorResource(R.drawable.note_outline)
                ),
                BottomNavItem(
                    name = "Settings",
                    route = NotifyScreens.Settings.name,
                    icon = ImageVector.vectorResource(R.drawable.settings_outline)
                )
            )

            bottomNavItem.forEach { item ->
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = if (backStackEntry.value?.destination?.route == item.route) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.secondary
                            }
                        )
                    },
                    label = {
                        Text(
                            text = item.name,
                            color = if (backStackEntry.value?.destination?.route == item.route) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.secondary
                            },
                            fontWeight = if (backStackEntry.value?.destination?.route == item.route) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    selected = backStackEntry.value?.destination?.route == item.route,
                    onClick = {
                        val currentDestination = navController.currentBackStackEntry?.destination?.route
                        if (item.route != currentDestination) {
                            navController.navigate(item.route) {
                                navController.graph.findStartDestination().let { route ->
                                    popUpTo(route.id) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}
