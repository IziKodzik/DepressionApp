package com.example.sadge

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sadge.databinding.ActivityDisplayBinding

class DisplayActivity : AppCompatActivity() {

    val binding by lazy{ActivityDisplayBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        (intent.extras?.get("note") as? String)?.let{
            binding.textView2.text = it
        }
        (intent.extras?.get("id") as? String)?.let{
            binding.imageView2.setImageBitmap(BitmapFactory.decodeFile(  "/storage/emulated/0/Pictures/${it}.jpg"))
        }

    }
}