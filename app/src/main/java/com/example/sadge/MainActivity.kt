package com.example.sadge

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sadge.databinding.ActivityMainBinding
import com.example.sadge.fragments.GalleryFragment

class MainActivity : AppCompatActivity() {

    private val settingsFragment = SettingsFragment()
    private val galleryFragment = GalleryFragment()
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        makeCurrentFragment(galleryFragment)



    }
    private fun requestPermission() {


    }
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.holder, fragment)
            commit()
        }

    fun showCamera(view: View) {
        val intent = Intent(this, CameraActivity::class.java)

        startActivity(intent)
    }
    fun showGallery(view: View) {
        makeCurrentFragment(galleryFragment)

    }
    fun showSettings(view: View) {
        makeCurrentFragment(settingsFragment)

    }


}