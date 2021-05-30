package com.example.sadge.activity

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sadge.R
import com.example.sadge.databinding.ActivityDisplayBinding
import com.example.sadge.model.PicDto

class DisplayActivity : AppCompatActivity() {

    val binding by lazy{ActivityDisplayBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        (intent.extras?.get("note") as? String)?.let{
            binding.textView2.text = it
        }
        (intent.extras?.get("id") as? String)?.let{
            val b = PicDto.getBitmap(it)
            if(b != null)
                binding.imageView2.setImageBitmap(b)
            else
                binding.imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_no_photography_24))
        }

    }
}