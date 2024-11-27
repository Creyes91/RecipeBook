package com.example.myrecipebook.data

import com.google.gson.annotations.SerializedName

class RecipeResponse (

    @SerializedName("recipes") val recipes: List<Recipe>,
    @SerializedName("total") val total: String
)

{
}

data class Recipe( //busqueda por id
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("cuisine") val cuisine: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("instructions") val instructions: List<String>,
    @SerializedName("cookTimeMinutes") val cookTimeMinutes: String,
    @SerializedName("rating") val rating: String
)
{}

