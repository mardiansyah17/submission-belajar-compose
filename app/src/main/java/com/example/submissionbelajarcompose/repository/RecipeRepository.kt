package com.example.submissionbelajarcompose.repository

import android.util.Log
import com.example.submissionbelajarcompose.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getRecipes(): List<Recipe> {
        val recipes = firestore.collection("recipes").get().await()
        return recipes.documents.map {
            Recipe(
                id = it.id,
                name = it.getString("name") ?: ""
            )
        }
    }
}