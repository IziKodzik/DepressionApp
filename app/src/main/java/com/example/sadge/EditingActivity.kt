package com.example.sadge

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.*
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sadge.databinding.ActivityEditingBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.util.*


class EditingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditingBinding.inflate(layoutInflater) }
    private lateinit var bitmap: Bitmap

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bitmap = intent.extras?.get("bitmap") as Bitmap
        this.bitmap = bitmap
        binding.paint.mBitmap = bitmap
        Shared.location?.let {
            Thread {
                binding.paint.text = locToCity(it) + "\n" + LocalDate.now()
                binding.paint.invalidate()
            }.start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun addGeofence(loci: Location) {
        val pi = PendingIntent.getBroadcast(
            applicationContext,
            1,
            Intent(this, BroReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        LocationServices.getGeofencingClient(this)
            .addGeofences(
                generateRequest(loci), pi
            ).addOnSuccessListener {
                Log.i("Geof", "Geofence added")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateRequest(loci: Location): GeofencingRequest {
        val mordo = Geofence.Builder().setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setRequestId(LocalDate.now().toString())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
            .setCircularRegion(loci.latitude, loci.longitude, 500f).build()
        return GeofencingRequest.Builder()
            .addGeofence(mordo)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }


    private fun locToCity(loc: Location): String? {
        if (Geocoder.isPresent()) {
            return Geocoder(this)
                .getFromLocation(loc.latitude, loc.longitude, 1)[0].locality
        }
        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun savePhoto(view: View) {

        val images = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "obrazek.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        Shared.location?.let {
            addGeofence(it)
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