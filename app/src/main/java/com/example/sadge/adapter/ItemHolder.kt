package com.example.sadge.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.sadge.databinding.ListItemBinding
import com.example.sadge.model.Pic

class ItemHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pic: Pic){
        with(binding){
            imageView.setImageBitmap(pic.bitmap)
        }
    }


}