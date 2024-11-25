package com.example.myrecipebook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myrecipebook.R
import com.example.myrecipebook.adapters.ListAdapter
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.databinding.ActivityMainBinding
import com.example.myrecipebook.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: ListAdapter
    var recipesList: List<Recipe> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter= ListAdapter(recipesList) {

            val recipe= recipesList[it]


        }

        binding.favReciclerView.adapter= adapter
        binding.favReciclerView.layoutManager= GridLayoutManager(this,2)









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
            println(result)

            CoroutineScope(Dispatchers.Main).launch {
                if (result.total!= "0")
                { recipesList= result.recipes
                    adapter.updatesItems(result.recipes)}



            }

        }
    }
}