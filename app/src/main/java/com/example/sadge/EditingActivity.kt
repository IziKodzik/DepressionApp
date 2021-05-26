package com.example.sadge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class EditingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editing)
        Log.i("s",intent.extras?.get("bitmapka_essa").toString())
    }
}