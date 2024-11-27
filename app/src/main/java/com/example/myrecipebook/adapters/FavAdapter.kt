package com.example.myrecipebook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.databinding.ItemRecipeBinding

import com.squareup.picasso.Picasso

class FavAdapter (var items: List<Recipe>, val onItemClick: (Int)-> Unit): RecyclerView.Adapter<FavAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ViewHolder(val binding: ItemRecipeBinding): RecyclerView.ViewHolder(binding.root){

        fun render(recipe: Recipe){
            binding.root.visibility= View.VISIBLE
            Picasso.get().load(recipe.image).into(binding.recipeImageView)
            binding.cookTimeTextView.text= recipe.cookTimeMinutes + "'"
            binding.ratingBar.rating= recipe.rating.toFloat()
            binding.tittleRecipeTextView.text=recipe.name

        }

    }


}