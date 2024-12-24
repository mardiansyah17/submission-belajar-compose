package com.example.submissionbelajarcompose.presentation.screen.editRecipe

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionbelajarcompose.model.Recipe
import com.example.submissionbelajarcompose.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val supabaseClient: SupabaseClient
) : ViewModel() {


    val editRecipeUiInfo by lazy {
        MutableStateFlow(
            EditRecipeUiInfo(
                title = "",
                description = "",
                imageUrl = "",
                ingredients = listOf("", "")
            )
        )
    }

    val successMsg = mutableStateOf("")
    val loading = mutableStateOf(false)
    val prevImage = mutableStateOf("")

    fun getRecipe(id: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val recipeResult = recipeRepository.getRecipe(id)
                editRecipeUiInfo.value = EditRecipeUiInfo(
                    title = recipeResult.title,
                    description = recipeResult.description,
                    imageUrl = recipeResult.imageUrl,
                    ingredients = recipeResult.ingredients
                )
            } catch (e: Exception) {
                Log.e(TAG, "getRecipe: ", e)
            } finally {
                loading.value = false

            }
        }
    }


    @OptIn(ExperimentalUuidApi::class)
    fun onEvent(event: EditRecipeEvent) {
        when (event) {
            is EditRecipeEvent.OnValueChange -> {
                onValueChange(event.value, event.field)
            }

            is EditRecipeEvent.OnIngredientChange -> {
                onIngredientChange(event.value, event.index)
            }

            is EditRecipeEvent.UpdateRecipe -> {
                val recipe = editRecipeUiInfo.value


                viewModelScope.launch {
                    try {
                        var imageUrl: String? = null
                        if (prevImage.value.isNotEmpty()) {
                            val bucket = supabaseClient.storage.from("recipe")
                            val byteArray = withContext(Dispatchers.IO) {
                                val inputStream: InputStream =
                                    event.context.contentResolver.openInputStream(recipe.imageUrl.toUri())!!
                                inputStream.readBytes()
                            }

                            val response = bucket.upload(
                                "${Uuid.random()}.jpg",
                                byteArray
                            )

                            imageUrl = bucket.publicUrl(response.path)
                            bucket.delete(listOf(prevImage.value.substringAfter("recipe/")))

                        }

                        val updateImage = if (prevImage.value.isNotEmpty()) {
                            imageUrl!!
                        } else {
                            recipe.imageUrl
                        }

                        recipeRepository.updateRecipe(
                            Recipe(
                                id = event.id,
                                title = recipe.title,
                                description = recipe.description,
                                imageUrl = updateImage,
                                ingredients = recipe.ingredients.filter { it.isNotEmpty() }

                            )
                        )

                        successMsg.value = "Berhasil menambahkan resep"

                    } catch (e: Exception) {
                        Log.e(TAG, "createRecipe: ", e)
                    }
                }
            }
        }
    }


    private fun onIngredientChange(value: String, index: Int) {
        val ingredients = editRecipeUiInfo.value.ingredients.toMutableList()
        ingredients[index] = value
        editRecipeUiInfo.value = editRecipeUiInfo.value.copy(ingredients = ingredients)

        if (index == ingredients.size - 1 && value.isNotEmpty()) {
            ingredients.add("")
            editRecipeUiInfo.value = editRecipeUiInfo.value.copy(ingredients = ingredients)
        } else {
            if (value.isEmpty() && ingredients.size > 1) {
                ingredients.removeAt(index)
                editRecipeUiInfo.value = editRecipeUiInfo.value.copy(ingredients = ingredients)
            }
        }

    }

    private fun onValueChange(value: String, field: String) {
        when (field) {
            "title" -> editRecipeUiInfo.value = editRecipeUiInfo.value.copy(title = value)
            "description" -> editRecipeUiInfo.value =
                editRecipeUiInfo.value.copy(description = value)

            "imageUrl" -> {
                prevImage.value = editRecipeUiInfo.value.imageUrl
                editRecipeUiInfo.value = editRecipeUiInfo.value.copy(imageUrl = value)

            }

        }
    }

    companion object {
        private const val TAG = "EditRecipeViewModel"
    }
}

data class EditRecipeUiInfo(
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
)

sealed interface EditRecipeEvent {
    data class OnValueChange(val value: String, val field: String) : EditRecipeEvent
    data class OnIngredientChange(val value: String, val index: Int) : EditRecipeEvent
    data class UpdateRecipe(val id: String, val context: Context) : EditRecipeEvent
}