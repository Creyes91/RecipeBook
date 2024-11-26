package com.example.myrecipebook.services

import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.data.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {


    @GET("search") suspend fun findRecipeByName(@Query("q") query: String): RecipeResponse

    @GET("{id}") suspend fun findRecipeById (@Path("id") query: String): Recipe




}