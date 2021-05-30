package com.example.sadge.adapter

import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.sadge.databinding.ListItemBinding
import com.example.sadge.model.PicDto

class ItemHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pic: PicDto){
        with(binding){
            imageView.setImageBitmap(BitmapFactory.decodeFile(  "/storage/emulated/0/Pictures/${pic.date}.jpg"))
        }
    }


}