package com.example.submissionbelajarcompose.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.submissionbelajarcompose.presentation.components.CardRecipe
import com.example.submissionbelajarcompose.presentation.navigation.NavigationGraph

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val listRecipe by homeViewModel.recipes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navHostController.navigate(NavigationGraph.CreateScreen.route) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },

        ) { pading ->

        Box(
            modifier = Modifier.padding(pading)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                items(listRecipe.size) {
                    val recipe = listRecipe[it]
                    CardRecipe(
                        title = recipe.title,
                        description = recipe.description,
                        imageUrl = recipe.imageUrl,
                        onClick = {
                            navHostController.navigate("detail_screen/${recipe.id}")
                        },
                        onEdit = {},
                        onDelete = {
                            homeViewModel.deleteRecipe(recipe.id)
                        }
                    )
                }
            }
        }
    }
}
