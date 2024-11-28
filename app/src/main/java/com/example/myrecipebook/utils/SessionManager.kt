package com.example.myrecipebook.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager (private val context: Context)  {

    private val prefs= context.getSharedPreferences("Recipes:session", Context.MODE_PRIVATE )

    fun setFavorite(recipeID: String)
    {
        val editor= prefs.edit()
        editor.putString("favoriteRecipeID",recipeID)
        editor.apply()

    }

    fun getFavorite(): String
    {
        return prefs.getString("favoriteRecipeID","")!!

    }

    fun isFav(recipeID: String):Boolean
    {
        return recipeID==getFavorite()
    }
}