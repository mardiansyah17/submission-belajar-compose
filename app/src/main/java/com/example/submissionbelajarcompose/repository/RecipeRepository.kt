package com.example.submissionbelajarcompose.repository

import android.util.Log
import com.example.submissionbelajarcompose.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class RecipeRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createRecipe(recipe: Recipe) {
        try {
            firestore.collection("recipes")
                .add(recipe)
                .await()

        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error creating recipe", e)
            throw e

        }
    }

    suspend fun getRecipes(): List<Recipe> {
        return try {
            val querySnapshot = firestore.collection("recipes")
                .get()
                .await()
            val mapData = querySnapshot.documents.map { document ->

                Recipe(
                    id = document.id,
                    title = document.getString("title")!!,
                    description = document.getString("description")!!,
                    imageUrl = document.getString("imageUrl")!!,
                    ingredients = document.get("ingredients") as List<String>
                )

            }
            mapData
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error getting recipes", e)
            throw e
        }
    }
}