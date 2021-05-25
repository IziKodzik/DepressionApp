package com.example.sadge

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sadge.databinding.ActivityCameraBinding
import com.example.sadge.databinding.ActivityMainBinding


class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater)}
    private val cam by lazy {
        DepressionUtil(getSystemService(Context.CAMERA_SERVICE) as CameraManager, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.surfaceView.holder.addCallback(this)
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

    fun click(view: View) {
        cam.acquire(binding.bruh)
    }
}