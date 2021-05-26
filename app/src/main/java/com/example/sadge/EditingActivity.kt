package com.example.sadge

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sadge.databinding.ActivityEditingBinding

class EditingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var bitmap = intent.extras?.get("bitmap") as Bitmap
        Log.i("aa", bitmap.width.toString())
        Log.i("ss", bitmap.height.toString())
        binding.paint.mBitmap = bitmap
    }

    fun savePhoto(view: View) {

    }

}