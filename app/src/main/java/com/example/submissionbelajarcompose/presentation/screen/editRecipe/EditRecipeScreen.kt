package com.example.submissionbelajarcompose.presentation.screen.editRecipe

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.submissionbelajarcompose.presentation.components.AppButton
import com.example.submissionbelajarcompose.presentation.components.DialogMessage
import com.example.submissionbelajarcompose.presentation.components.InputTextField
import com.example.submissionbelajarcompose.presentation.navigation.NavigationGraph

@Composable
fun EditRecipeScreen(
    navHostController: NavHostController,
    viewModel: EditRecipeViewModel = hiltViewModel(),
    id: String,
) {
    val uiInfo = viewModel.editRecipeUiInfo.collectAsStateWithLifecycle().value
    val context = LocalContext.current


    LaunchedEffect(id) {
        viewModel.getRecipe(id)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val uri = data?.data
            viewModel.onEvent(EditRecipeEvent.OnValueChange(uri.toString(), "imageUrl"))
        }
    }
    if (viewModel.successMsg.value.isNotEmpty()) {
        DialogMessage(

            message = viewModel.successMsg.value,
            confirmButton = {
                TextButton(onClick = {
                    viewModel.successMsg.value = ""
                    navHostController.navigate(NavigationGraph.HomeScreen.route) {
                        popUpTo(0)

                    }

                }) {
                    Text("Oke")
                }
            },
        )
    }

    if (viewModel.loading.value) {
        return Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

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
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        if (uiInfo.imageUrl.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(uiInfo.imageUrl),
                                contentDescription = "Image",
                                Modifier
                                    .fillMaxSize()
                                    .height(200.dp)
                            )
                        } else {
                            Box(
                                Modifier
                                    .fillMaxSize()
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
                                    .clickable {
                                        val galleryIntent =
                                            Intent(Intent.ACTION_GET_CONTENT).apply {
                                                type = "image/*"
                                            }

                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                                        val chooser = Intent(Intent.ACTION_CHOOSER).apply {
                                            putExtra(Intent.EXTRA_INTENT, galleryIntent)
                                            putExtra(Intent.EXTRA_TITLE, "Select from:")
                                            putExtra(
                                                Intent.EXTRA_INITIAL_INTENTS,
                                                arrayOf(cameraIntent)
                                            )
                                        }

                                        launcher.launch(chooser)

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
                                    Text(
                                        text = "Add Image",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }

                    InputTextField(
                        text = uiInfo.title,
                        onValueChange = {
                            viewModel.onEvent(EditRecipeEvent.OnValueChange(it, "title"))
                        },
                        label = "Nama Resep",
                        modifier = Modifier.padding(10.dp)
                    )


                    InputTextField(
                        text = uiInfo.description,
                        onValueChange = {
                            viewModel.onEvent(EditRecipeEvent.OnValueChange(it, "description"))
                        },
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
            items(uiInfo.ingredients.size) {
                val number = it + 1
                InputTextField(
                    text = uiInfo.ingredients.getOrNull(it) ?: "",
                    onValueChange = { value ->
                        viewModel.onEvent(EditRecipeEvent.OnIngredientChange(value, it))
                    },
                    label = "Bahan $number",
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
        ) {

            viewModel.onEvent(EditRecipeEvent.UpdateRecipe(id))

        }
    }
}

