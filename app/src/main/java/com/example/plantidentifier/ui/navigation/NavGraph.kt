package com.example.plantidentifier.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantidentifier.ui.screens.HomeScreen
import com.example.plantidentifier.ui.screens.ResultScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("result/{plantName}") { backStackEntry ->
            val plantName = backStackEntry.arguments?.getString("plantName")
            ResultScreen(plantName = plantName ?: "", navController = navController)
        }
    }
}