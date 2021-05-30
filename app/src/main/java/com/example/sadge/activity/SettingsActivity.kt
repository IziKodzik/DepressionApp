package com.example.sadge.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.sadge.Shared
import com.example.sadge.databinding.ActivitySettingsBinding
import java.lang.Exception
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

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveSet(view: View) {

        try {
            var r = binding.RedVal.text.toString().toInt()
            if (r > 255) r = 255
            var g = binding.GreenVal.text.toString().toInt()
            if (g > 255) g = 255
            var b = binding.BlueVal.text.toString().toInt()
            if (b > 255) b = 255
            var a = binding.Alpha.text.toString().toInt()
            if (a > 255) a = 255


            val madgeSettings = com.example.sadge.model.MadgeSettings(
                0, r, g,
                b, a, binding.editTextSize.text.toString().toFloat(),
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