package com.aritra.notify.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.aritra.notify.ui.screens.notes.addEditScreen.AddEditScreen
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreen
import com.aritra.notify.ui.screens.settingsScreen.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun NotifyApp(navController: NavHostController = rememberNavController()) {

    val bottomNavItem = getBottomNavItems()
    val screensWithHiddenNavBar = listOf(
        "${NotifyScreens.AddEditNotes.name}/{noteId}"
    )
    val backStackEntry = navController.currentBackStackEntryAsState()

    val listState: LazyListState = rememberLazyListState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                backStackEntry,
                bottomNavItem,
                screensWithHiddenNavBar,
                navController,
                listState
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NotifyScreens.Notes.name,
            modifier = Modifier.padding(it),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            composable(
                route = NotifyScreens.Notes.name,
            ) {
                NoteScreen(
                    onFabClicked = { navController.navigate(NotifyScreens.AddEditNotes.name+"/0") },
                    navigateToUpdateNoteScreen = { noteId ->
                        navController.navigate("${NotifyScreens.AddEditNotes.name}/$noteId")
                    }, listState
                )
            }

            composable(
                route = "${NotifyScreens.AddEditNotes.name}/{noteId}",
                arguments = listOf(navArgument("noteId") { type = IntType }),
            ) { backStack ->
                val noteId = backStack.arguments?.getInt("noteId") ?: 0
                AddEditScreen(
                    noteId = noteId,
                    navigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = NotifyScreens.Settings.name,
            ) {
                SettingsScreen()
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
    lazyListState: LazyListState
) {
    val scope = rememberCoroutineScope()


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
                        if (item.name == getBottomNavItems().first().name) {
                            scope.launch {
                                lazyListState.scrollToItem(0)
                            }
                        }
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
        ),
    )
}