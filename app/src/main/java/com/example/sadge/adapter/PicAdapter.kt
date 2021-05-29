package com.example.sadge.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sadge.R
import com.example.sadge.Shared
import com.example.sadge.databinding.ListItemBinding
import com.example.sadge.model.Pic
import kotlin.concurrent.thread

class PicAdapter(val context: Context) : RecyclerView.Adapter<ItemHolder>() {

    var pics = arrayListOf<Pic>()

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

    fun refresh() {
        thread {
            val data = Shared.db?.pics?.selectAll()
            data?.forEach {
                Log.i("japierdole","/storage/emulated/0/Pictures/${it.date}.jpg")
                this.pics.add(
                    Pic(
                        it.note, it.lon, it.lat,
                        BitmapFactory.decodeFile(  "/storage/emulated/0/Pictures/${it.date}.jpg")
                    )
                )
            }
        }

        notifyDataSetChanged()

    }

    override fun getItemCount() = pics.size


}