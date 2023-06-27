package com.aritra.notify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aritra.notify.screens.addNoteScreen.AddNotesScreen
import com.aritra.notify.screens.homeScreen.HomeScreen
import com.aritra.notify.screens.settingsScreen.SettingsScreen

@Composable
fun NotifyApp(navController: NavHostController = rememberNavController(),
){
    NavHost(
        navController = navController,
        startDestination = NotifyScreens.Home.name
    ) {
        composable(route = NotifyScreens.Home.name) {
            HomeScreen(

            )
        }
        composable(NotifyScreens.AddNotes.name) {
            AddNotesScreen()
        }
        composable(NotifyScreens.Settings.name) {
            SettingsScreen()
        }
    }
}