package com.aritra.notify.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aritra.notify.R
import com.aritra.notify.components.drawing.DrawingScreen
import com.aritra.notify.ui.screens.notes.addEditScreen.AddEditRoute
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreen
import com.aritra.notify.ui.screens.notes.trash.trashNoteDest
import com.aritra.notify.ui.screens.settingsScreen.SettingsScreen

@Composable
fun NotifyApp(navController: NavHostController = rememberNavController()) {
    val bottomNavItem = getBottomNavItems()
    val screensWithHiddenNavBar = listOf(
        "${NotifyScreens.AddEditNotes.name}/{noteId}",
        NotifyScreens.TrashNoteScreen.name,
        NotifyScreens.Drawing.name
    )
    val backStackEntry = navController.currentBackStackEntryAsState()

    var shouldHideBottomBar: Boolean by remember {
        mutableStateOf(true)
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = shouldHideBottomBar,
                enter = fadeIn(animationSpec = tween(delayMillis = 500, easing = LinearOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween(delayMillis = 500, easing = LinearOutSlowInEasing))
            ) {
                BottomNavigationBar(
                    backStackEntry,
                    bottomNavItem,
                    screensWithHiddenNavBar,
                    navController
                )
            }
        }
    ) { scaffoldPadding ->
        NavHost(
            navController = navController,
            startDestination = NotifyScreens.Notes.name,
            modifier = Modifier.padding(scaffoldPadding),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(220, delayMillis = 90)
                ) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(220, delayMillis = 90)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(90))
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(220, delayMillis = 90)
                ) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(220, delayMillis = 90)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(90))
            }
        ) {
            composable(
                route = NotifyScreens.Notes.name
            ) {
                NoteScreen(
                    onFabClicked = { navController.navigate(NotifyScreens.AddEditNotes.name + "/-1") },
                    navigateToUpdateNoteScreen = { noteId ->
                        navController.navigate("${NotifyScreens.AddEditNotes.name}/$noteId")
                    }
                ) { shouldHide ->
                    shouldHideBottomBar = shouldHide
                }
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
            trashNoteDest(navController)

            composable(
                route = NotifyScreens.Drawing.name
            ) {
                DrawingScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = { drawing ->
                        navController.popBackStack()
                        navController.currentBackStackEntry?.savedStateHandle?.set("drawing", drawing)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    backStackEntry: State<NavBackStackEntry?>,
    bottomNavItem: List<BottomNavItem>,
    screensWithHiddenNavBar: List<String>,
    navController: NavHostController,
) {
    if (backStackEntry.value?.destination?.route !in screensWithHiddenNavBar) {
        NavigationBar(containerColor = Color.Transparent, modifier = Modifier.height(75.dp)) {
            bottomNavItem.forEach { item ->
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
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
                                FontWeight.Bold
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
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}

fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            name = "Notes",
            route = NotifyScreens.Notes.name,
            icon = R.drawable.note_outline
        ),
        BottomNavItem(
            name = "Settings",
            route = NotifyScreens.Settings.name,
            icon = R.drawable.settings_outline
        )
    )
}
