package com.example.submissionbelajarcompose.presentation.screen.detailRecipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.submissionbelajarcompose.R
import com.example.submissionbelajarcompose.presentation.components.NotFoundLayout
import com.example.submissionbelajarcompose.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRecipeScreen(
    navHostController: NavHostController,
    viewModel: DetailRecipeViewModel = hiltViewModel(),
    id: String
) {

    val recipe = viewModel.recipe.collectAsState().value

    LaunchedEffect(id) {
        viewModel.getRecipe(id)

    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text("Detail Recipe")
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier.padding(padding)
        ) {

            if (viewModel.loading.collectAsState().value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@Box
            }

            if (recipe == null) {
                NotFoundLayout()
                return@Box
            }

            LazyColumn {

                item {
                    Image(
                        painter = rememberAsyncImagePainter(recipe.imageUrl),
                        contentDescription = "Recipe Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                item {


                    Column(
                        Modifier.padding(10.dp)
                    ) {
                        Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            Modifier.defaultMinSize(minHeight = 100.dp)
                        ) {
                            Text(
                                recipe.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Bahan:", style = MaterialTheme.typography.titleMedium)
                    }
                }

                items(recipe.ingredients.size) {
                    val number = it + 1
                    Text(
                        "• ${recipe.ingredients[it]}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }


            }
        }
    }
}

