package com.example.submissionbelajarcompose.presentation.navigation

sealed class NavigationGraph(val route: String) {
    data object HomeScreen : NavigationGraph("home_screen")
    data class DetailScreen(val id: Int) : NavigationGraph("detail_screen/$id")
    data object CreateScreen : NavigationGraph("create_screen")
}