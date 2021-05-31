package com.example.sadge.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.sadge.Shared
import com.example.sadge.databinding.ActivitySettingsBinding
import kotlin.concurrent.thread

class SettingsActivity : AppCompatActivity() {

    val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Shared.settings?.let {
            binding.RedVal.setText(it.r.toString())
            binding.GreenVal.setText(it.g.toString())
            binding.BlueVal.setText(it.b.toString())
            binding.Alpha.setText(it.a.toString())
            binding.editTextSize.setText(it.textSize.toString())
            binding.editTextRadi.setText(it.radius.toString())
            binding.example.setTextColor(readColor())
            binding.example.setBackgroundColor(getOposColor(readColor()))
            readTextSize()
        }
        binding.editTextSize.doOnTextChanged { text, start, before, count ->
            readTextSize()
        }
        binding.GreenVal.doOnTextChanged { text, start, before, count ->
            binding.example.setTextColor(readColor())
            binding.example.setBackgroundColor(getOposColor(readColor()))

        }
        binding.RedVal.doOnTextChanged { text, start, before, count ->
            binding.example.setTextColor(readColor())
            binding.example.setBackgroundColor(getOposColor(readColor()))
        }
        binding.BlueVal.doOnTextChanged { text, start, before, count ->
            binding.example.setTextColor(readColor())
            binding.example.setBackgroundColor(getOposColor(readColor()))
        }
        binding.Alpha.doOnTextChanged { text, start, before, count ->
            binding.example.setTextColor(readColor())
            binding.example.setBackgroundColor(getOposColor(readColor()))
        }

    }
    private fun getOposColor(color: Int): Int
        =  Color.argb(255,
     255 - (color shr 16) and 0b11111111,
            255 - (color shr 16) and 0b11111111,
            255 -(color shr 8) and 0b11111111
        )

    private fun readTextSize() {
        var size: Float?
        try {
            size = binding.editTextSize.text.toString().toFloat()
        } catch (ex: Exception) {
            size = null
        }
        binding.example.textSize = size ?: 60f
    }

    fun readColor(): Int {
        return try {
            var r = binding.RedVal.text.toString().toInt()
            if (r > 255) r = 255
            var g = binding.GreenVal.text.toString().toInt()
            if (g > 255) g = 255
            var b = binding.BlueVal.text.toString().toInt()
            if (b > 255) b = 255
            var a = binding.Alpha.text.toString().toInt()
            if (a > 255) a = 255
            Color.argb(a,r,g,b)
        }catch (e: Exception){
            Color.BLACK
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveSet(view: View) {

        try {

            val color = readColor()

            val madgeSettings = com.example.sadge.model.MadgeSettings(
                0, (color shr 16 ) and 0b11111111, (color shr 8) and 0b11111111,
                (color) and 0b11111111, (color shr 24) and 0b11111111, binding.editTextSize.text.toString().toFloat(),
                binding.editTextRadi.text.toString().toFloat()
            )
            thread {
                Shared.db?.pics?.insert(madgeSettings)
                Shared.settings = madgeSettings
                finish()
            }
        } catch (ex: Exception) {
            Log.i("wutf", ex.message.toString())
        }
    }
}