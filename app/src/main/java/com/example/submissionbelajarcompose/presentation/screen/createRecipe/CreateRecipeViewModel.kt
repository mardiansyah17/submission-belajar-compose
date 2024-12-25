package com.example.submissionbelajarcompose.presentation.screen.createRecipe

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionbelajarcompose.model.Recipe
import com.example.submissionbelajarcompose.repository.RecipeRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.Date
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val supabaseClient: SupabaseClient
) : ViewModel() {
    val createRecipeUiInfo by lazy {
        MutableStateFlow(
            CreateRecipeUiInfo(
                title = "",
                description = "",
                imageUrl = "",
                ingredients = listOf("", "")
            )
        )
    }

    val successMsg = mutableStateOf("")
    val errorMsg = mutableStateOf("")

    val loading = mutableStateOf(false)

    @OptIn(ExperimentalUuidApi::class)
    fun onEvent(event: CreateRecipeEvent) {
        when (event) {
            is CreateRecipeEvent.OnValueChange -> {
                onValueChange(event.value, event.field)
            }

            is CreateRecipeEvent.OnIngredientChange -> {
                onIngredientChange(event.value, event.index)
            }

            is CreateRecipeEvent.CreateRecipe -> {
                val recipe = createRecipeUiInfo.value

                if (recipe.imageUrl.isEmpty()) {
                    errorMsg.value = "Harap pilih gambar"
                    return
                }

                if (recipe.title.isEmpty()) {
                    errorMsg.value = "Judul resep tidak boleh kosong"
                    return
                }

                if (recipe.description.isEmpty()) {
                    errorMsg.value = "Deskripsi tidak boleh kosong"
                    return
                }

                if (recipe.ingredients.filter { it.isNotEmpty() }.size < 2) {
                    errorMsg.value = "Minimal 2 bahan"
                    return
                }


                val bucket = supabaseClient.storage.from("recipe")



                viewModelScope.launch {
                    try {
                        loading.value = true
                        val byteArray = withContext(Dispatchers.IO) {
                            val inputStream: InputStream? =
                                event.context.contentResolver.openInputStream(recipe.imageUrl.toUri())
                            inputStream?.readBytes()
                                ?: throw Exception("Failed to read input stream")
                        }

                        val response = bucket.upload(
                            "${Uuid.random()}.jpg",
                            byteArray
                        )

                        val imageUrl = bucket.publicUrl(response.path)

                        recipeRepository.createRecipe(
                            Recipe(
                                id = Uuid.random().toString(),
                                createdAt = Timestamp.now(),
                                title = recipe.title,
                                description = recipe.description,
                                imageUrl = imageUrl,
                                ingredients = recipe.ingredients.filter { it.isNotEmpty() },
                                titleLower = recipe.title.lowercase(),
                            )
                        )

                        successMsg.value = "Berhasil menambahkan resep"

                    } catch (e: Exception) {
                        Log.e(TAG, "createRecipe: ", e)
                    } finally {
                        loading.value = false
                    }
                }
            }
        }
    }


    private fun onIngredientChange(value: String, index: Int) {
        val ingredients = createRecipeUiInfo.value.ingredients.toMutableList()
        ingredients[index] = value
        createRecipeUiInfo.value = createRecipeUiInfo.value.copy(ingredients = ingredients)

        if (index == ingredients.size - 1 && value.isNotEmpty()) {
            ingredients.add("")
            createRecipeUiInfo.value = createRecipeUiInfo.value.copy(ingredients = ingredients)
        } else {
            if (value.isEmpty() && ingredients.size > 1) {
                ingredients.removeAt(index)
                createRecipeUiInfo.value = createRecipeUiInfo.value.copy(ingredients = ingredients)
            }
        }

    }

    private fun onValueChange(value: String, field: String) {
        when (field) {
            "title" -> createRecipeUiInfo.value = createRecipeUiInfo.value.copy(title = value)
            "description" -> createRecipeUiInfo.value =
                createRecipeUiInfo.value.copy(description = value)

            "imageUrl" -> createRecipeUiInfo.value = createRecipeUiInfo.value.copy(imageUrl = value)

        }
    }

    companion object {
        private const val TAG = ""
    }
}

data class CreateRecipeUiInfo(
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
)

sealed interface CreateRecipeEvent {
    data class OnValueChange(val value: String, val field: String) : CreateRecipeEvent
    data class OnIngredientChange(val value: String, val index: Int) : CreateRecipeEvent
    data class CreateRecipe(val context: Context) : CreateRecipeEvent
}