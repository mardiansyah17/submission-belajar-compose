package com.example.submissionbelajarcompose.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.submissionbelajarcompose.R
import com.example.submissionbelajarcompose.ui.theme.AppTheme


@Composable
fun CardRecipe() {
    Box(
        Modifier
            .height(100.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = MaterialTheme.shapes.medium
            )
            .background(Color.White),
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.nasi_goreng),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .width(125.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Text(
                "Nasi Goreng",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun CardRecipePreview() {
    AppTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            items(4) {
                CardRecipe()
            }
        }
    }
}