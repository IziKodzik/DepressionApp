package com.example.sadge

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.*
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sadge.databinding.ActivityEditingBinding
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*


class EditingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditingBinding.inflate(layoutInflater) }
    private val locMan by lazy { getSystemService(LocationManager::class.java) }
    private val locClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val geofencingClient by lazy { LocationServices.getGeofencingClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        oldGetLoc()

        val bitmap = intent.extras?.get("bitmap") as Bitmap
        val loc = oldGetLoc()
        Thread {
            binding.paint.mBitmap = bitmap
            binding.paint.text = loc?.let {
                Log.i("locacaca",it.latitude.toString())
                Log.i("locacaca",it.longitude.toString())
                locToCity(it)
            }
            binding.paint.invalidate()
        }.start()



    }


    fun newGetLoc() {
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
        locClient.lastLocation
            .addOnSuccessListener { loc: Location? ->
                loc?.let {
                }
            }
    }
    fun locToCity(loc: Location): String? {
        val gcd = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = gcd.getFromLocation(loc.latitude, loc.longitude, 1)
            return if (addresses.isNotEmpty()) {
                addresses[0].locality
            }else {
                null
            }


    }

    fun oldGetLoc(): Location? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        } else {
            val c = Criteria().apply {
                accuracy = Criteria.ACCURACY_FINE
            }
            val best = locMan.getBestProvider(c, true) ?: ""
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0f, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                    }

                    override fun onStatusChanged(
                        provider: String?,
                        status: Int,
                        extras: Bundle?
                    ) {
                    }

                    override fun onProviderEnabled(provider: String) {
                    }

                    override fun onProviderDisabled(provider: String) {
                    }

                }
            )
            return locMan.getLastKnownLocation(best)

        }
    }

    fun savePhoto(view: View) {
        val images = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "obrazek.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        contentResolver.run {
            val imgUri = insert(images, contentValues)
            imgUri ?: return
            openOutputStream(imgUri)?.use {
                binding.paint.loadBitmapFromView()
                    .compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            finish()
        }

    }

}