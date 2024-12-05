package com.example.myrecipebook.activities

import android.content.Intent
import android.graphics.Rect
import android.icu.lang.UCharacter.VerticalOrientation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
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
    lateinit var Session: SessionManager
    lateinit var layoutManager: LinearLayoutManager
    var recipesList: List<Recipe> = emptyList()
    var faId=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Session= SessionManager(this)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.mainReciclerView)


        searchAdapter= ListAdapter(recipesList) {
            val recipe= recipesList[it]
            navigateTo(recipe)
        }

        favAdapter=FavAdapter(recipesList,{

            val recipe=recipesList[it]
            navigateTo(recipe)

        },{
            var recipe= recipesList[it]
            if(recipe.id==Session.getFavorite())
            Session.setFavorite("-1")
            else{
                if(Session.getFavorite()=="-1")
                Session.setFavorite(recipe.id)}
        })

        binding.mainReciclerView.adapter= favAdapter
        layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.mainReciclerView.layoutManager=layoutManager


        binding.mainReciclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)



                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                for (position in firstVisiblePosition..lastVisiblePosition) {
                    var view = layoutManager.findViewByPosition(position)

                    val center = recyclerView.width / 2
                    val viewCenter = (view?.left ?: 0) + view?.width!! /2
                    val distanceFromCenter = Math.abs(center - viewCenter)


                    // Calculamos el tamaño de la imagen basado en su distancia al centro
                    val scale = 1 - (distanceFromCenter.toFloat() / recyclerView.width)
                    view?.scaleX = scale
                    view?.scaleY = scale

                    try {
                        if (firstVisiblePosition != RecyclerView.NO_POSITION) {
                            // Mostrar información de la imagen central
                            showImageInfo(lastVisiblePosition)}

                }catch (e : Exception){
                    Log.e("error", e.stackTraceToString())
                }




                }

            }
            })






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
                R.id.item_Favorite->{
                    query="Favorite"
                }


            }
            searchRecipesByType(query)
            return@setOnItemSelectedListener true


        }





    }

    private fun showImageInfo(position: Int)
    {
        binding.cookTimeTextView.text= recipesList[position].cookTimeMinutes + "'"
        binding.ratingBar.rating= recipesList[position].rating.toFloat()
        binding.tittleRecipeTextView.text=recipesList[position].name


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
            }else

            {
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