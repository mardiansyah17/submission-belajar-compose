package com.example.submissionbelajarcompose.presentation.screen.detailRecipe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionbelajarcompose.model.Recipe
import com.example.submissionbelajarcompose.repository.RecipeRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun getRecipe(id: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val recipeResult = recipeRepository.getRecipe(id)

                _recipe.value = recipeResult
            } catch (e: Exception) {
                Log.e(TAG, "getRecipe: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateFavoriteRecipe(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                val favorit = if (isFavorite) {
                    Timestamp.now()
                } else {
                    null
                }
                recipeRepository.updateFavorite(id = id, favorite = favorit)
                _recipe.value = recipe.value?.copy(favorite = favorit)

            } catch (e: Exception) {
                Log.e(TAG, "updateFavoriteRecipe: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "DetailRecipeViewModel"
    }

}