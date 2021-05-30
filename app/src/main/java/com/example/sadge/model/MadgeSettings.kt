package com.example.sadge.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "settings")
data class MadgeSettings(
    @PrimaryKey
    var id: Long = 0,
    var r: Int = 0,
    var g: Int = 0,
    var b: Int = 0,
    var a: Int = 0,
    var textSize: Float = 60f,
    var radius: Float = 1000f


){
    @RequiresApi(Build.VERSION_CODES.O)
    constructor():this(0,0,0,0,0,0f,0f)
}
