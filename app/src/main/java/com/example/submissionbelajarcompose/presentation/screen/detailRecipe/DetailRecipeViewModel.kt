package com.example.submissionbelajarcompose.presentation.screen.detailRecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionbelajarcompose.model.Recipe
import com.example.submissionbelajarcompose.repository.RecipeRepository
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


    fun getRecipe(id: String) {
        viewModelScope.launch {
            val recipeResult = recipeRepository.getRecipe(id)
            _recipe.value = recipeResult
        }
    }

}