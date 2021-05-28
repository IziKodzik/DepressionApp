package com.example.sadge

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sadge.databinding.ActivityMainBinding
import com.example.sadge.fragments.GalleryFragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private val settingsFragment = SettingsFragment()
    private val galleryFragment = GalleryFragment()
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val locClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val geofencingClient by lazy { LocationServices.getGeofencingClient(this) }


    fun requestLoc(){
        val request = LocationRequest.create().apply {
            fastestInterval = 500L
            interval = 1000L
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locClient.requestLocationUpdates(request,object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                Shared.location = p0.lastLocation
            } }, Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        makeCurrentFragment(galleryFragment)
        if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(CAMERA, ACCESS_FINE_LOCATION),1)
        else
            requestLoc()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLoc()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
    fun showSettings(view: View) {
        makeCurrentFragment(settingsFragment)

    }


}