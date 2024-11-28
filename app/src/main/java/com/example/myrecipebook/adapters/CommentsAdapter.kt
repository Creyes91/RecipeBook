package com.example.myrecipebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipebook.data.CommentTask
import com.example.myrecipebook.databinding.CommentsBinding
import com.example.myrecipebook.databinding.ItemCommentsBinding


class CommentsAdapter(private val comments: List<CommentTask>):  RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding= ItemCommentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val comment= comments[position]
        holder.render(comment)

    }

    override fun getItemCount(): Int {
        return comments.size
    }

    class ViewHolder(val binding: ItemCommentsBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun render(comment:CommentTask)
        {
            binding.CommentTXT.setText(comment.comment)

        }

    }


}