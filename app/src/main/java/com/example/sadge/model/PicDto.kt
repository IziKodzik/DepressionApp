package com.example.sadge.model

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
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
}


