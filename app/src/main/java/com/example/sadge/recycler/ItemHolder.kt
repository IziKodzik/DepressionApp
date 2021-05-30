package com.example.sadge.recycler

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.sadge.R
import com.example.sadge.databinding.ListItemBinding
import com.example.sadge.model.PicDto

class ItemHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(pic: PicDto, context: Context){
        with(binding){

            val icon = pic.getBitmap()
            if(icon != null){
                imageView.setImageBitmap(icon)
            }else
                imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_baseline_no_photography_24))
        }
    }


}