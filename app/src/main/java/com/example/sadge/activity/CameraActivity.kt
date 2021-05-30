package com.example.sadge.activity

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sadge.DepressionUtil
import com.example.sadge.databinding.ActivityCameraBinding


class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater)}
    private lateinit var cam : DepressionUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        cam = DepressionUtil(getSystemService(CAMERA_SERVICE) as CameraManager, this)
        cam.openCamera()
        binding.surfaceView.holder.addCallback(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

        cam.acquire()

    }

}