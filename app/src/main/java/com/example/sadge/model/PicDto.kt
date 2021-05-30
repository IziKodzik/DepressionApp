package com.example.sadge.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sadge.R
import java.io.IOException
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime


@Entity(tableName = "pic")
data class PicDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var lon: Double,
    var lat: Double,
    var note: String,
    var date: String


    ){
    @RequiresApi(Build.VERSION_CODES.O)
    constructor():this(0,0.0,0.0,"", LocalDateTime.now().toString().replace('.','_').replace(":",""))

    fun getBitmap():Bitmap? {
        return getBitmap(date)
    }


    companion object {
        fun getBitmap(path: String): Bitmap? {
            return try {
                BitmapFactory.decodeFile("/storage/emulated/0/Pictures/${path}.jpg")
            } catch (ex: Exception) {
               null

            }
        }
    }




}


