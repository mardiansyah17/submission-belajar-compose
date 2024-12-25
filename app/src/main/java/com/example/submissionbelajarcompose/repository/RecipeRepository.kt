package com.example.submissionbelajarcompose.repository

import android.util.Log
import com.example.submissionbelajarcompose.model.Recipe
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class RecipeRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createRecipe(recipe: Recipe) {
        try {
            Log.i("", "Creating $recipe")
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
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val mapData = querySnapshot.documents.map { document ->

                Recipe(
                    id = document.id,
                    createdAt = document.getTimestamp("createdAt")!!,
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

    suspend fun getFavoriteRecipes(): List<Recipe> {
        return try {
            val querySnapshot = firestore.collection("recipes")
                .whereNotEqualTo("favorite", null)
                .orderBy("favorite", Query.Direction.DESCENDING)
                .get()
                .await()
            val mapData = querySnapshot.documents.map { document ->

                Recipe(
                    id = document.id,
                    createdAt = document.getTimestamp("createdAt")!!,
                    title = document.getString("title")!!,
                    description = document.getString("description")!!,
                    imageUrl = document.getString("imageUrl")!!,
                    ingredients = document.get("ingredients") as List<String>,

                    )

            }
            mapData
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error getting recipes", e)
            throw e
        }
    }


    suspend fun getRecipe(id: String): Recipe {
        return try {
            val documentSnapshot = firestore.collection("recipes")
                .document(id)
                .get()
                .await()
            Recipe(
                id = documentSnapshot.id,
                createdAt = documentSnapshot.getTimestamp("createdAt")!!,
                title = documentSnapshot.getString("title")!!,
                description = documentSnapshot.getString("description")!!,
                imageUrl = documentSnapshot.getString("imageUrl")!!,
                ingredients = documentSnapshot.get("ingredients") as List<String>,
                favorite = documentSnapshot.getTimestamp("favorite")
            )
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error getting recipe", e)
            throw e
        }
    }

    suspend fun deleteRecipe(id: String) {
        try {
            firestore.collection("recipes")
                .document(id)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error deleting recipe", e)
            throw e
        }
    }

    suspend fun updateRecipe(recipe: Recipe) {
        try {
            firestore.collection("recipes")
                .document(recipe.id)
                .set(recipe)
                .await()
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error updating recipe", e)
            throw e
        }
    }

    suspend fun searchRecipe(query: String): List<Recipe> {
        return try {
            val querySnapshot = firestore.collection("recipes")
                .orderBy("titleLower")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()

            val mapData = querySnapshot.documents.map { document ->

                Recipe(
                    id = document.id,
                    createdAt = document.getTimestamp("createdAt")!!,
                    title = document.getString("title")!!,
                    description = document.getString("description")!!,
                    imageUrl = document.getString("imageUrl")!!,
                    ingredients = document.get("ingredients") as List<String>
                )

            }
            mapData
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error searching recipes", e)
            throw e
        }
    }

    suspend fun updateFavorite(id: String, favorite: Timestamp?) {
        try {
            firestore.collection("recipes")
                .document(id)
                .update("favorite", favorite)
                .await()
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error updating favorite", e)
            throw e
        }
    }
}