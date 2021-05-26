package com.example.sadge;

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.IllegalStateException
import java.util.*

@Suppress("DEPRECATION")
class DepressionUtil(
    private val cameraManager: CameraManager,
    private val activity: Activity
) : CameraDevice.StateCallback() {

    private val thread by lazy { HandlerThread("CameraInit").apply { start() } }
    val handler by lazy { Handler(thread.looper) }

    private var cameraDevice: CameraDevice? = null
    private var characters: CameraCharacteristics? = null
    private var surface: Surface? = null

    val captureCallback = object : CameraCaptureSession.CaptureCallback() {}
    private val stateCallbackForPreview = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {}
        override fun onConfigured(session: CameraCaptureSession) {
            surface?.let {
                val builder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                builder?.apply {
                    addTarget(it)
                    set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO)
                    set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)
                }?.build()?.also {
                    session.setRepeatingRequest(it, captureCallback, handler)
                }
            }
        }
    }

    inner class StateCallbackForAcquire(private val imageReader: ImageReader, imageView: ImageView) :
        CameraCaptureSession.StateCallback() {
        init {
            imageReader.setOnImageAvailableListener({
                val img = it.acquireLatestImage()
                val buffer = img.planes[0].buffer
                val bytes = ByteArray(buffer.capacity())
                buffer.get(bytes)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView.post {
                    imageView.setImageBitmap(bitmap)
                }
                it.close()
                surface?.let { setupPreviewSession(it) }
            }, handler)

        }

        override fun onConfigureFailed(session: CameraCaptureSession) {}
        override fun onConfigured(session: CameraCaptureSession) {
            val builder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            builder?.apply {
                addTarget(imageReader.surface)
                set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO)
                set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)
            }?.build()?.also { session.capture(it, captureCallback, handler) }
        }
    }

    fun openCamera() {
        if (ActivityCompat.
            checkSelfPermission(activity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        )
            requestPermission()

        cameraManager.openCamera(setCameraId(), this, handler)

    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.CAMERA),
            200
        )
    }

    fun closeCamera() {
        cameraDevice?.close()
    }

    private fun setCameraId(): String {
        for (cameraId in cameraManager.cameraIdList) {
            characters = cameraManager.getCameraCharacteristics(cameraId)
            if (characters?.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK)
                return cameraId
        }
        throw IllegalStateException("Cdnt set camera id")

    }

    fun setupPreviewSession(surface: Surface) {
        this.surface = surface
        cameraDevice?.createCaptureSession(
            listOf(surface),
            stateCallbackForPreview,
            handler
        )
    }

    override fun onOpened(camera: CameraDevice) {
        cameraDevice = camera
    }

    fun acquire(iv: ImageView) {
        val size = characters?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            ?.getOutputSizes(ImageFormat.JPEG)?.get(2)?.let { Size(it.width, it.height) }
            ?: Size(640, 840)
        val imageReader = ImageReader.newInstance(size.width, size.height, ImageFormat.JPEG, 1)
        cameraDevice?.createCaptureSession(
            listOf(imageReader.surface),
            StateCallbackForAcquire(imageReader, iv),
            handler
        )
        iv.rotation = 90f
    }

    override fun onDisconnected(camera: CameraDevice) {
        cameraDevice = null
    }

    override fun onError(camera: CameraDevice, error: Int) {}
}



