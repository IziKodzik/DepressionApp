package com.example.sadge

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.SurfaceHolder
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sadge.databinding.ActivityCameraBinding


class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater)}
    private lateinit var cam : DepressionUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.surfaceView.holder.addCallback(this)
        cam = DepressionUtil(getSystemService(Context.CAMERA_SERVICE) as CameraManager, this)

    }




    override fun onResume() {
        super.onResume()
        cam.openCamera()
    }

    override fun onPause() {
        cam.closeCamera()
        super.onPause()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        holder.surface?.let { cam.setupPreviewSession(it) }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    fun capture(view: View) {
        val images = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "obrazek.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
       applicationContext.contentResolver.run{
           val imgUri = insert(images, contentValues)
           imgUri ?: return
           openOutputStream(imgUri)?.use{
           }
       }

    }

}