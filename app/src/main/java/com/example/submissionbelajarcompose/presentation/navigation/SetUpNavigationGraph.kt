package com.example.submissionbelajarcompose.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.submissionbelajarcompose.presentation.components.TabBarItem
import com.example.submissionbelajarcompose.presentation.components.TabView
import com.example.submissionbelajarcompose.presentation.screen.createRecipe.CreateRecipe
import com.example.submissionbelajarcompose.presentation.screen.detailRecipe.DetailRecipeScreen
import com.example.submissionbelajarcompose.presentation.screen.editRecipe.EditRecipeScreen
import com.example.submissionbelajarcompose.presentation.screen.favorite.FavoriteScreen
import com.example.submissionbelajarcompose.presentation.screen.home.HomeScreen

@Composable
fun SetUpNavigationGraph(
    navHostController: NavHostController = rememberNavController(),

    ) {
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    Scaffold(

        bottomBar = {
            if (
                currentRoute == TabNavigationGraph.HomeScreen.route ||
                currentRoute == TabNavigationGraph.CreateScreen.route ||
                currentRoute == TabNavigationGraph.FavoriteScreen.route
            ) {
                TabView(
                    tabBarItems = listOf(
                        TabBarItem(
                            route = TabNavigationGraph.HomeScreen.route,
                            title = "Home",
                            selectedIcon = Icons.Default.Home,
                            unselectedIcon = Icons.Outlined.Home
                        ),
                        TabBarItem(
                            route = TabNavigationGraph.CreateScreen.route,
                            title = "Create",
                            selectedIcon = Icons.Default.Add,
                            unselectedIcon = Icons.Outlined.Add
                        ),
                        TabBarItem(
                            route = TabNavigationGraph.FavoriteScreen.route,
                            title = "Favorite",
                            selectedIcon = Icons.Default.Favorite,
                            unselectedIcon = Icons.Default.FavoriteBorder
                        ),
                    ),
                    navController = navHostController
                )
            }
        }
    ) {
        @Suppress("UNUSED_EXPRESSION")
        it
        NavHost(
            navController = navHostController,
            startDestination = TabNavigationGraph.HomeScreen.route
        ) {
            composable(TabNavigationGraph.HomeScreen.route) {
                HomeScreen(navHostController)
            }

            composable(TabNavigationGraph.CreateScreen.route) {
                CreateRecipe(
                    navHostController = navHostController
                )
            }

            composable(NavigationGraph.DetailScreen("{id}").route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                DetailRecipeScreen(navHostController = navHostController, id = id)
            }

            composable(NavigationGraph.EditScreen("{id}").route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                EditRecipeScreen(navHostController = navHostController, id = id)
            }

            composable(
                TabNavigationGraph.FavoriteScreen.route
            ) {
                FavoriteScreen(navHostController)
            }
        }
    }

}
