package com.example.sadge

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PaintView(context: Context,
                attributeSet: AttributeSet
) : View(context, attributeSet) {

    lateinit var mBitmap: Bitmap



    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        mBitmap.let {
            val paint = Paint(Color.GRAY)
            canvas.drawBitmap(Bitmap.createScaledBitmap(it,width,height,true)
                , 0f, 0f, paint)

            paint.strokeWidth = 10f
            paint.textSize = 50f
            canvas.drawText("Some Text", width/3f, height - height/4f, paint)

        }


    }
}