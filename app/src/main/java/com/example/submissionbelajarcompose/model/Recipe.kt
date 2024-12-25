package com.example.submissionbelajarcompose.model

import com.google.firebase.Timestamp

data class Recipe(
    val id: String,
    val createdAt: Timestamp,
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    var titleLower: String? = "",
    val favorite: Timestamp? = null,
)
