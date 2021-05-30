package com.example.sadge

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.location.*
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sadge.databinding.ActivityEditingBinding
import com.example.sadge.model.PicDto
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.concurrent.thread


class EditingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditingBinding.inflate(layoutInflater) }
    private lateinit var bitmap: Bitmap
    private var savedLoc: Location? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        savedLoc = Shared.location
        val bitmap = intent.extras?.get("bitmap") as Bitmap
        this.bitmap = bitmap
        binding.paint.mBitmap = bitmap

        savedLoc?.let {
            thread {
                binding.paint.text = locToFancy(it)
                binding.paint.invalidate()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun addGeofence(loci: Location, date:String) {
        val pi = PendingIntent.getBroadcast(
            applicationContext,
            1,
            Intent(this, BroReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        LocationServices.getGeofencingClient(this)
            .addGeofences(
                generateRequest(loci,date), pi
            ).addOnSuccessListener {
                Log.i("Geof", "Geofence added")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDataPic(): PicDto? {
        var str = LocalDateTime.now().toString().replace('.','_')
        str = str.replace(":","")
        savedLoc?.let {
            return PicDto(
                lon = it.longitude, lat = it.latitude, note = binding.noteText.text.toString()
                ,date = str
            )
        }
        return null

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateRequest(loci: Location,date: String): GeofencingRequest {
        val mordo = Geofence.Builder().setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setRequestId(LocalDate.now().toString())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setCircularRegion(loci.latitude, loci.longitude, 500f).setRequestId(date).build()
        return GeofencingRequest.Builder()
            .addGeofence(mordo)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }


    private fun locToFancy(loc: Location): String? {
        try {
            if (Geocoder.isPresent()) {
                val geos = Geocoder(this)
                    .getFromLocation(loc.latitude, loc.longitude, 1)[0]
                return geos.locality + ", " + geos.countryName
            }
        } catch (ex: IOException) {
            Log.e("errpr", ex.message.toString())
            Log.i("v","No internet connection!")
        }
        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun savePhoto(view: View) {

        val dataPic = createDataPic()
        dataPic?.let {
            thread {
                Shared.db?.let {
                    it.pics.insert(dataPic)
                }
                val images = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                else
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "${dataPic.date}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }

                savedLoc?.let {
                    addGeofence(it,dataPic.date)
                    contentResolver.run {
                        val imgUri = insert(images, contentValues)
                        imgUri?.let {
                            openOutputStream(imgUri)?.use {
                                binding.paint.loadBitmapFromView()
                                    .compress(Bitmap.CompressFormat.JPEG, 100, it)
                            }
                        }
                        finish()
                    }
                }
            }
        }

    }

}