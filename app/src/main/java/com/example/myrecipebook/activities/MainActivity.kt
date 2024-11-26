package com.example.myrecipebook.activities

import android.content.Intent
import android.icu.lang.UCharacter.VerticalOrientation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipebook.R
import com.example.myrecipebook.adapters.FavAdapter
import com.example.myrecipebook.adapters.ListAdapter
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.databinding.ActivityMainBinding
import com.example.myrecipebook.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var searchAdapter: ListAdapter
    lateinit var favAdapter: FavAdapter
    var recipesList: List<Recipe> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchAdapter= ListAdapter(recipesList) {
            val recipe= recipesList[it]
            navigateTo(recipe)
        }

        favAdapter=FavAdapter(recipesList){

            val recipe=recipesList[it]
            navigateTo(recipe)

        }



        binding.searchReciclerView.adapter= searchAdapter
        binding.searchReciclerView.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)









    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val btnSearch = menu?.findItem(R.id.itemSearch)!!

        val searchView = btnSearch.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchRecipes(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }


        })

        return true
    }

    private fun searchRecipes(query: String?) {
        val service= RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            val result = service.findRecipeByName(query!!)

           // val result= service.findRecipeById("1")

            //println(service.findRecipeById("1"))
            println(result)

            CoroutineScope(Dispatchers.Main).launch {
                if (result.total!= "0")
                { recipesList= result.recipes
                    searchAdapter.updatesItems(result.recipes)}



            }

        }
    }


    private fun navigateTo(recipe: Recipe)
    {
        intent= Intent(this,RecipeActivity::class.java)

        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipe.id)
        startActivity(intent)



    }
}