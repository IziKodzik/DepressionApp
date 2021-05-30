package com.example.sadge

import android.graphics.Bitmap
import android.location.Location
import com.example.sadge.dejtabase.AppDatabase
import com.example.sadge.model.MadgeSettings

object Shared {

    var db: AppDatabase? = null
    var location: Location? = null
    var settings: MadgeSettings? = null
    var lastBitmap: Bitmap? = null

}