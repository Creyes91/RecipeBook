package com.example.myrecipebook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.databinding.ItemRecipeBinding
import com.example.myrecipebook.databinding.SearchRecipeBinding

import com.squareup.picasso.Picasso

class ListAdapter (var items: List<Recipe>, val onItemClick: (Int)-> Unit): RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding= SearchRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= items[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    fun updatesItems(recipes: List<Recipe>) {
        this.items=recipes
        notifyDataSetChanged()

    }

    class ViewHolder(val binding: SearchRecipeBinding): RecyclerView.ViewHolder(binding.root){

        fun render(recipe: Recipe){
        //  binding.root.visibility= View.VISIBLE
          binding.idtextView.setText(recipe.id.toString())
          binding.cuisineTextView.text= recipe.cuisine.toString()
            binding.recipeTextView.text=recipe.name.toString()
            Picasso.get().load(recipe.image).into(binding.ImageView)


        }

    }


}

