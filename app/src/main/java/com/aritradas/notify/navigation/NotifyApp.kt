package com.aritradas.notify.navigation

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aritradas.notify.ui.screens.notes.addEditScreen.route.AddEditRoute
import com.aritradas.notify.ui.screens.notes.homeScreen.NoteScreen
import com.aritradas.notify.ui.screens.notes.trash.TrashNoteEffect
import com.aritradas.notify.ui.screens.notes.trash.TrashNoteScreen
import com.aritradas.notify.ui.screens.notes.trash.TrashNoteViewModel
import com.aritradas.notify.ui.screens.settingsScreen.SettingsScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NotifyApp(
    navController: NavHostController = rememberNavController(),
) {
    val trashViewModel: TrashNoteViewModel = hiltViewModel()
    val state by trashViewModel.state.collectAsState()
    val effect by trashViewModel.effect.collectAsState()
    val context = LocalContext.current

    val bottomNavItems = remember {
        listOf(
            BottomNavItem(
                name = "Notes",
                route = NotifyScreens.Notes.name,
                selectedIcon = Icons.Filled.Lightbulb,
                unselectedIcon = Icons.Outlined.Lightbulb
            ),
            BottomNavItem(
                name = "Settings",
                route = NotifyScreens.Settings.name,
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings
            )
        )
    }

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val routesWithoutBottomBar = remember {
        setOf("${NotifyScreens.AddEditNotes.name}/{noteId}", NotifyScreens.TrashNoteScreen.name)
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    var shouldShowNavBar by rememberSaveable { mutableStateOf(true) }
    val hideNavBar = { shouldShowNavBar = false }
    val showNavBar = { shouldShowNavBar = true }

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            TrashNoteEffect.Close -> {
                navController.popBackStack()
                trashViewModel.resetEffect()
            }
            is TrashNoteEffect.Message -> {
                Toast.makeText(context, currentEffect.message, Toast.LENGTH_SHORT).show()
                trashViewModel.closePage()
            }
            null -> {}
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute !in routesWithoutBottomBar && shouldShowNavBar) {
                NavigationBar {
                    bottomNavItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            label = { Text(text = item.name) },
                            icon = {
                                Icon(
                                    imageVector =
                                    if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.name
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { scaffoldPadding ->
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = NotifyScreens.Notes.name,
                modifier = Modifier.padding(scaffoldPadding)
            ) {
                composable(NotifyScreens.Notes.name) {
                    NoteScreen(
                        onFabClicked = { navController.navigate("${NotifyScreens.AddEditNotes.name}/-1/false") },
                        navigateToUpdateNoteScreen = { noteId, pinned ->
                            navController.navigate("${NotifyScreens.AddEditNotes.name}/$noteId/$pinned")
                        },
                        animatedVisibilityScope = this,
                        hideNavBar = hideNavBar,
                        showNavBar = showNavBar
                    )
                }

                composable(
                    route = "${NotifyScreens.AddEditNotes.name}/{noteId}/{isPinned}",
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.IntType },
                        navArgument("isPinned") { type = NavType.BoolType }
                    ),
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = NavDeepLinks.addNotesUriPattern
                            action = Intent.ACTION_VIEW
                        }
                    )
                ) { backStack ->
                    AddEditRoute(
                        navController = navController,
                        backStack = backStack,
                        animatedVisibilityScope = this
                    )
                }

                composable(NotifyScreens.Settings.name) {
                    SettingsScreen(controller = navController)
                }

                composable(NotifyScreens.TrashNoteScreen.name) {
                    TrashNoteScreen(
                        trashNoteState = state,
                        onEvent = trashViewModel::onEvent,
                        animatedVisibilityScope = this
                    )
                }
            }
        }
    }
}
