package com.example.myrecipebook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipebook.R
import com.example.myrecipebook.adapters.CommentsAdapter
import com.example.myrecipebook.data.CommentTask
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.databinding.ActivityRecipeBinding
import com.example.myrecipebook.services.RecipeService
import com.example.myrecipebook.utils.CommentsDAO
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
    lateinit var commentsAdapter: CommentsAdapter
    lateinit var coment: CommentTask


    var commentTask: MutableList<CommentTask> = mutableListOf()
    lateinit var commentsDAO: CommentsDAO

    companion object{

        const val EXTRA_RECIPE_ID="recipeID"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRecipeBinding.inflate(layoutInflater)
        selectionMenu= binding.autoCompleteSelection
        setContentView(binding.root)
        coment= CommentTask(-1,-1,"")

        val id= intent.getStringExtra(EXTRA_RECIPE_ID).toString()

        getRecipe(id)





        val selection= resources.getStringArray(R.array.Selection)
        val arrayAdapter = ArrayAdapter(this,R.layout.selection_list_menu,selection)
        binding.autoCompleteSelection.setAdapter(arrayAdapter)

        selectionMenu.setOnItemClickListener { parent, view, position, id ->
            val selected= parent.getItemAtPosition(position).toString()

            when(selected){
                "Instruction"->{binding.instruccionTextview.visibility= View.VISIBLE
                    binding.ingredienteTextview.visibility= View.GONE
                    binding.commentsReciclerView.visibility=View.GONE}

                "Ingredients"->{binding.instruccionTextview.visibility= View.GONE
                    binding.ingredienteTextview.visibility= View.VISIBLE
                    binding.commentsReciclerView.visibility=View.GONE}

                "Comments"->{binding.instruccionTextview.visibility= View.GONE
                    binding.ingredienteTextview.visibility= View.GONE
                    binding.commentsReciclerView.visibility=View.VISIBLE
                }


            }



        }



        binding.commentsContent.CommentBtn.setOnClickListener{

            println("boton")

            var comment = binding.commentsContent.CommentTXT.editText?.text.toString()
            if(comment.isEmpty()){
                binding.commentsContent.CommentTXT.error= "Escribe algo"
                return@setOnClickListener}
            if(comment.length> 50){
                binding.commentsContent.CommentTXT.error="Texto demasiado largo"
                return@setOnClickListener}

            coment.comment=comment
            coment.recipeid=recipe.id.toLong()

            commentsDAO.insert(coment)
            commentTask= commentsDAO.findByID(recipe.id.toInt()).toMutableList()
            commentsAdapter.updatesItems(commentTask)



            /*  task.name=name
              if (task.id==-1L)
                  taskDAO.insert(task)
              else taskDAO.update(task)*/



        }





    }

    override fun onResume() {
        super.onResume()
        binding.commentsContent.CommentTXT.editText?.setText("")


    }

    private fun loadData(recipe: Recipe){
        //binding.tittleRecipe.text=recipe.name.toString()
        supportActionBar?.title= recipe.name.toString()
        binding.ingredienteTextview.text=recipe.ingredients.joinToString("\n \n-","-",)
        binding.instruccionTextview.text=recipe.instructions.joinToString("\n \n-","-")
        Picasso.get().load(recipe.image).into(binding.imageRecipeView)



        commentsDAO= CommentsDAO(this)
        commentTask= commentsDAO.findByID(recipe.id.toInt()).toMutableList()
        commentsAdapter= CommentsAdapter(commentTask) {
            val c = commentTask[it]
            deleteComment(c)
        }
        binding.commentsReciclerView.adapter=commentsAdapter
        binding.commentsReciclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
      //  commentTask=commentsDAO.findByID(recipe.id.toInt()).toMutableList()




    }

    private fun deleteComment(comment: CommentTask) {

        AlertDialog.Builder(this)
            .setTitle("Borrar?")
            .setMessage("Estas seguro que quieres borrar este commentario?")
            .setPositiveButton(android.R.string.ok) { dialog, wich->
                commentsDAO.delete(comment)
                commentTask.remove(comment)
                commentsAdapter.updatesItems(commentTask)
            }
            .setNegativeButton(android.R.string.cancel,null)
            .setIcon(R.drawable.ic_delete)
            .show()
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