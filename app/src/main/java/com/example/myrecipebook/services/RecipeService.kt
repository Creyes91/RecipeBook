package com.example.myrecipebook.services

import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.data.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {


    @GET("search?q={name}") suspend fun findRecipeByName(@Path("name") query: String): RecipeResponse

    @GET("{id}") suspend fun findRecipeById (@Path("id") query: String): Recipe




}