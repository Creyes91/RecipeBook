package com.example.myrecipebook.services

import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.data.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {


    @GET("recipes/search") suspend fun findRecipeByName(@Query("q") query: String): RecipeResponse

    @GET("recipes/{id}") suspend fun findRecipeById (@Path("id") query: String): Recipe

    @GET("recipes/") suspend fun findAllRecipes (): RecipeResponse

    @GET("recipes/meal-type/{meal-type}") suspend fun findRecipesByMeal (@Path("meal-type")query: String): RecipeResponse






}