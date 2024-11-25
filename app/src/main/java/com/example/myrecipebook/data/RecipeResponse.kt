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
    @SerializedName("image") val image: String
)
{}

