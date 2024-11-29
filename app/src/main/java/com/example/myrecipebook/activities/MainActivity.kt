package com.example.myrecipebook.activities

import android.content.Intent
import android.icu.lang.UCharacter.VerticalOrientation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipebook.R
import com.example.myrecipebook.adapters.FavAdapter
import com.example.myrecipebook.adapters.ListAdapter
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.data.RecipeResponse
import com.example.myrecipebook.databinding.ActivityMainBinding
import com.example.myrecipebook.services.RecipeService
import com.example.myrecipebook.utils.RetrofitProvider
import com.example.myrecipebook.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var searchAdapter: ListAdapter
    lateinit var favAdapter: FavAdapter
    private var query= "all"

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

     binding.mainReciclerView.adapter= favAdapter
        binding.mainReciclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)



        binding.navigationBar.selectedItemId=R.id.item_All
        searchRecipesByType(query)
        binding.navigationBar.setOnItemSelectedListener {

            when (it.itemId){
                R.id.item_All->{
                       query="all"
                }
                R.id.item_Snack->{
                    query="Snack"

                }
                R.id.item_Lunch->{
                    query="Lunch"
            }
                R.id.item_Dinner->{
                    query="Dinner"
                }


            }
            searchRecipesByType(query)
            return@setOnItemSelectedListener true


        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val btnSearch = menu?.findItem(R.id.itemSearch)!!

        val searchView = btnSearch.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.mainReciclerView.adapter=searchAdapter
                binding.mainReciclerView.layoutManager=LinearLayoutManager(parent,LinearLayoutManager.VERTICAL,false)

                searchRecipesbyName(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                println(newText)
                return false
            }


        })

        btnSearch.setOnActionExpandListener(object : OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                binding.mainReciclerView.adapter= favAdapter
                binding.mainReciclerView.layoutManager=LinearLayoutManager(parent,LinearLayoutManager.HORIZONTAL,false)
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                println("Expand")
                return true
            }
        })

        return true
    }

    private fun searchRecipesByType(mealType: String) {

        val service= RetrofitProvider.getRetrofit()
        var result= RecipeResponse(emptyList(),"0")
        CoroutineScope(Dispatchers.IO).launch {
            if(mealType== "all") {
                 result = service.findAllRecipes()
            }else{
                 result = service.findRecipesByMeal(mealType)
            }


            CoroutineScope(Dispatchers.Main).launch {
                if (result.total!= "0")
                { recipesList= result.recipes
                    favAdapter.updatesItems(result.recipes)

                }



            }

        }


    }

    private fun searchRecipesbyName(query: String?) {
        val service= RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            val result = service.findRecipeByName(query!!)

           // val result= service.findRecipeById("1")

            //println(service.findRecipeById("1"))
            println(result)

            CoroutineScope(Dispatchers.Main).launch {
                if (result.total!= "0")
                { recipesList= result.recipes

                    searchAdapter.updatesItems(result.recipes)

                }



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