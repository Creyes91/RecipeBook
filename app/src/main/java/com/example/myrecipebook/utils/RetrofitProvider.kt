package com.example.myrecipebook.utils

import com.example.myrecipebook.services.RecipeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {

    companion object
    {
        fun GetRetrofit() : RecipeService
        {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dummyjson.com/recipes")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RecipeService::class.java)
        }



    }
}
