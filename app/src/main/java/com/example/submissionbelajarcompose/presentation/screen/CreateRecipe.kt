package com.example.submissionbelajarcompose.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.submissionbelajarcompose.presentation.components.AppButton
import com.example.submissionbelajarcompose.presentation.components.InputTextField
import com.example.submissionbelajarcompose.ui.theme.AppTheme

@Composable
fun CreateRecipe() {
    ConstraintLayout(
        Modifier.fillMaxSize()
    ) {
        val (content, button) = createRefs()

        LazyColumn(
            Modifier
                .padding(10.dp)
                .fillMaxSize()

                .constrainAs(content) {
                    top.linkTo(parent.top, margin = 100.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(button.top)
                }
        ) {
            item {
                Column {
                    Box(
                        Modifier
                            .height(160.dp)
                            .fillParentMaxWidth()
                            .drawBehind {
                                drawRoundRect(
                                    Color.Gray,
                                    style = Stroke(
                                        width = 4f,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(10f, 5f),
                                            0f
                                        )
                                    )
                                )
                            }
                    ) {
                        Column(
                            Modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = "Image",
                                Modifier.size(50.dp)
                            )
                            Text(text = "Add Image", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    InputTextField(
                        text = "",
                        onValueChange = {},
                        label = "Nama Resep",
                        modifier = Modifier.padding(10.dp)
                    )


                    InputTextField(
                        text = "",
                        onValueChange = {},
                        label = "Deskripsi",
                        maxLine = 2,

                        modifier = Modifier
                            .padding(10.dp)
                            .height(100.dp),
                    )

                    Text(
                        text = "Bahan - Bahan",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(10.dp)
                    )


                }
            }
            items(2) {
                val index = it + 1
                InputTextField(
                    text = "",
                    onValueChange = {},
                    label = "Bahan $index",
                    modifier = Modifier.padding(10.dp)
                )
            }


        }
        AppButton("Simpan",
            modifier = Modifier
                .padding(10.dp)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) { }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateRecipePreview() {
    AppTheme {
        CreateRecipe()

    }
}