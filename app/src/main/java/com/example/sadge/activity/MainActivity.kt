package com.example.sadge.activity

import android.Manifest
import android.Manifest.permission.*
import android.app.NotificationChannel
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sadge.Shared
import com.example.sadge.recycler.PicAdapter
import com.example.sadge.databinding.ActivityMainBinding
import com.example.sadge.dejtabase.AppDatabase
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var channel: NotificationChannel

    //    private val geofencingClient by lazy { LocationServices.getGeofencingClient(this) }
    private val locClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val picAdapter by lazy { PicAdapter(this) }
    private fun requestLoc() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val request = LocationRequest.create().apply {
            fastestInterval = 500L
            interval = 1000L
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locClient.requestLocationUpdates(request, object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                Shared.location = p0.lastLocation
            }
        }, Looper.getMainLooper())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Shared.db = AppDatabase.open(applicationContext)
        setupRecycler()
        if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            binding.buttonCamera.isClickable = false

            requestPermissions(
                arrayOf(
                    CAMERA, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                ), 1
            )
        }else{
            requestLoc()
            picAdapter.refresh(this)
        }
        thread {
            Shared.settings = Shared.db?.pics?.selectDefaultSettings()
        }
    }


    fun showCamera(view: View) {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecycler() {
        binding.recyclerView.apply {
            adapter = picAdapter
            layoutManager = GridLayoutManager(context, 4)
        }
    }

    override fun onResume() {
        super.onResume()
        picAdapter.refresh(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(PackageManager.PERMISSION_DENIED !in grantResults) {
            binding.buttonCamera.isClickable = true
            requestLoc()
            requestPermissions(arrayOf(ACCESS_BACKGROUND_LOCATION),2)
        }
    }

    fun showSettings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
    }


}