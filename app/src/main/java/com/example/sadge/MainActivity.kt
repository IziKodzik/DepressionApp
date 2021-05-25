package com.example.sadge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sadge.databinding.ActivityMainBinding
import com.example.sadge.fragments.CameraFragment
import com.example.sadge.fragments.GalleryFragment

class MainActivity : AppCompatActivity() {

    val cameraFragment = CameraFragment()
    val settingsFragment = SettingsFragment()
    val galleryFragment = GalleryFragment()
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        makeCurrentFragment(cameraFragment)



    }
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.holder, fragment)
            commit()
        }

    fun showCamera(view: View) {
        makeCurrentFragment(cameraFragment)
    }
    fun showGallery(view: View) {
        makeCurrentFragment(galleryFragment)

    }
    fun showSettings(view: View) {
        makeCurrentFragment(settingsFragment)

    }
}