package com.example.submissionbelajarcompose.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionbelajarcompose.model.Recipe
import com.example.submissionbelajarcompose.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val supabaseClient: SupabaseClient

) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _isRefreshing = MutableStateFlow(false)

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    val query = mutableStateOf("")

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

    fun deleteRecipe(id: String, urlImage: String) {
        viewModelScope.launch {
            try {
                val bucket = supabaseClient.storage.from("recipe")
                bucket.delete(listOf(urlImage.substringAfter("recipe/")))


                recipeRepository.deleteRecipe(id)

                getRecipes()
            } catch (e: Exception) {
                Log.e(TAG, "deleteRecipe: ", e)
            }
        }
    }

    fun searchRecipe(text: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val recipesResult = recipeRepository.searchRecipe(text)
                _recipes.value = recipesResult
            } catch (e: Exception) {
                Log.e(TAG, "searchRecipe: ", e)
            } finally {
                _loading.value = false
            }
        }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}