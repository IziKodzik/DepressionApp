package com.example.sadge.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sadge.R
import com.example.sadge.Shared
import com.example.sadge.databinding.ListItemBinding
import com.example.sadge.model.Pic
import kotlin.concurrent.thread

class PicAdapter(val context: Context) : RecyclerView.Adapter<ItemHolder>() {

    private var pics: List<Pic> = arrayListOf()
    private val handeler = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(pics[position])
    }

    fun refresh(context: Context) = thread {
       Shared.db?.let{it.pics.selectAll() }?.map{it.toPic()}?.also{
           pics = it
           handeler.post{  notifyDataSetChanged()}

       }


    }

    override fun getItemCount() = pics.size


}