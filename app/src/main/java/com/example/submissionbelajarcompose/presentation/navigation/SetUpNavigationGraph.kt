package com.example.submissionbelajarcompose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.submissionbelajarcompose.presentation.screen.createRecipe.CreateRecipe
import com.example.submissionbelajarcompose.presentation.screen.home.HomeScreen

@Composable
fun SetUpNavigationGraph(
    navHostController: NavHostController = rememberNavController(),

    ) {

    NavHost(
        navController = navHostController,
        startDestination = NavigationGraph.HomeScreen.route
    ) {
        composable(NavigationGraph.HomeScreen.route) {
            HomeScreen(navHostController)
        }

        composable(NavigationGraph.CreateScreen.route) {
            CreateRecipe(
                navHostController = navHostController
            )
        }

    }

}