package com.example.myrecipebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipebook.data.CommentTask
import com.example.myrecipebook.data.Recipe
import com.example.myrecipebook.databinding.CommentsBinding
import com.example.myrecipebook.databinding.ItemCommentsBinding


class CommentsAdapter(private var comments: List<CommentTask>,
                      val onItemDelete:(Int)-> Unit):  RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding= ItemCommentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val comment= comments[position]
        holder.render(comment)
        holder.binding.deleteBTN.setOnClickListener{
            onItemDelete(position)

        }

    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun updatesItems(comment: List<CommentTask>) {
        comments=comment
        notifyDataSetChanged()


    }

    class ViewHolder(val binding: ItemCommentsBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun render(comment:CommentTask)
        {
            binding.CommentTXT.setText(comment.comment)

        }

    }


}