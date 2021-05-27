package com.example.sadge

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PaintView(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {

    lateinit var mBitmap: Bitmap
    var text: String? = null

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        mBitmap.let {
            mBitmap = Bitmap.createScaledBitmap(it, width, height, true)
            val paint = Paint(Color.GRAY)
            canvas.drawBitmap(mBitmap, 0f, 0f, paint)
            val buff = Canvas(mBitmap)
            paint.strokeWidth = 10f
            paint.textSize = 100f
            buff.drawText(text ?: "Unknown", width / 3f, height - height / 4f, paint)
            canvas.drawText(text ?: "Unknown", width / 3f, height - height / 4f, paint)
        }


    }

    fun loadBitmapFromView(): Bitmap {
        return mBitmap
    }

}