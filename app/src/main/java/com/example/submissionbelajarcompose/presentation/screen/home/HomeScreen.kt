package com.example.submissionbelajarcompose.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.submissionbelajarcompose.presentation.components.CardRecipe
import com.example.submissionbelajarcompose.presentation.components.EmptyLayout
import com.example.submissionbelajarcompose.presentation.components.PullToRefreshBox
import com.example.submissionbelajarcompose.presentation.navigation.NavigationGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val listRecipe by homeViewModel.recipes.collectAsState()
    val state = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate(NavigationGraph.CreateScreen.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },

        ) { pading ->

        PullToRefreshBox(
            modifier = Modifier.padding(pading),
            isRefreshing = isRefreshing.value,
            onRefresh = {
                homeViewModel.getRecipes()
            }
        ) {

            if (homeViewModel.loading.collectAsState().value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@PullToRefreshBox
            }

            if (listRecipe.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyLayout()
                }
                return@PullToRefreshBox
            }

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
                            navHostController.navigate(NavigationGraph.DetailScreen(recipe.id).route)
                        },
                        onEdit = {},
                        onDelete = {
                            homeViewModel.deleteRecipe(recipe.id, recipe.imageUrl)
                        }
                    )
                }
            }
        }
    }
}
