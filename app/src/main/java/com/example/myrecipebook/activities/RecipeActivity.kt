package com.example.myrecipebook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.myrecipebook.R
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.databinding.ActivityRecipeBinding
import com.example.myrecipebook.services.RecipeService
import com.example.myrecipebook.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class RecipeActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecipeBinding
    lateinit var recipe: Recipe
    lateinit var selectionMenu: AutoCompleteTextView

    companion object{

        const val EXTRA_RECIPE_ID="recipeID"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRecipeBinding.inflate(layoutInflater)
        selectionMenu= binding.autoCompleteSelection
        setContentView(binding.root)

        val id= intent.getStringExtra(EXTRA_RECIPE_ID).toString()

        getRecipe(id)

        val selection= resources.getStringArray(R.array.Selection)
        val arrayAdapter = ArrayAdapter(this,R.layout.selection_list_menu,selection)
        binding.autoCompleteSelection.setAdapter(arrayAdapter)

        selectionMenu.setOnItemClickListener { parent, view, position, id ->
            val selected= parent.getItemAtPosition(position).toString()

            if (selected== "Instruction")
            {
                binding.instruccionTextview.visibility= View.VISIBLE
                binding.ingredientsLayout.visibility= View.GONE
            }else {
                if (selected== "Ingredients")
                {
                    binding.instruccionTextview.visibility= View.GONE
                    binding.ingredientsLayout.visibility= View.VISIBLE

                }
            }


        }




    }

    override fun onResume() {
        super.onResume()



    }

    private fun loadData(recipe: Recipe){
        //binding.tittleRecipe.text=recipe.name.toString()
        supportActionBar?.title= recipe.name.toString()
        binding.ingredienteTextview.text=recipe.ingredients.joinToString("\n \n-","-",)
        binding.instruccionTextview.text=recipe.instructions.joinToString("\n \n-","-")
        Picasso.get().load(recipe.image).into(binding.imageRecipeView)


    }

    private fun getRecipe(id: String){

        val service= RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                recipe= service.findRecipeById(id)
                CoroutineScope(Dispatchers.Main).launch {
                loadData(recipe)}
            }catch (e:Exception)
            {
                Log.e("API", e.stackTraceToString())

            }



        }
    }
}