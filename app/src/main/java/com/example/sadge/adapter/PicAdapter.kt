package com.example.sadge.adapter

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sadge.DisplayActivity
import com.example.sadge.Shared
import com.example.sadge.databinding.ListItemBinding
import com.example.sadge.model.PicDto
import kotlin.concurrent.thread

class PicAdapter(val context: Context) : RecyclerView.Adapter<ItemHolder>() {

    private var pics: List<PicDto> = arrayListOf()
    private val handeler = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ItemHolder(binding).also { holder ->
            binding.root.setOnClickListener { onItemClick(parent, holder.layoutPosition) }
        }
    }

    private fun onItemClick(parent: ViewGroup, layoutPosition: Int) {
        val intent = (Intent(parent.context, DisplayActivity::class.java))
        intent.putExtra("id",pics[layoutPosition].date)
        intent.putExtra("note",pics[layoutPosition].note)
        parent.context.startActivity(Intent(intent))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(pics[position])
    }

    fun refresh(context: Context) = thread {
       Shared.db?.let{it.pics.selectAll() }?.let{
           pics = it
           handeler.post{  notifyDataSetChanged()}

       }


    }

    override fun getItemCount() = pics.size


}