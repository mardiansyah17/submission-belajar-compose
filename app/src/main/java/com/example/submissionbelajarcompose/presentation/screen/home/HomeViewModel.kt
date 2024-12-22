package com.example.submissionbelajarcompose.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
class HomeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getRecipes()
    }

    fun getRecipes() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val recipesResult = recipeRepository.getRecipes()
                _recipes.value = recipesResult
            } catch (e: Exception) {
                Log.e(TAG, "getRecipes: ", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteRecipe(id: String) {
        viewModelScope.launch {
            try {
                recipeRepository.deleteRecipe(id)
                getRecipes()
            } catch (e: Exception) {
                Log.e(TAG, "deleteRecipe: ", e)
            }
        }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}