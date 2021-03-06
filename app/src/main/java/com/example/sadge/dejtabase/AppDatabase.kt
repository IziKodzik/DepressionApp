package com.example.sadge.dejtabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sadge.model.MadgeSettings
import com.example.sadge.model.PicDto

@Database(
    entities = [PicDto::class,MadgeSettings::class],
    version = 2

)
abstract class AppDatabase: RoomDatabase() {
    abstract val pics: PicDao

    companion object{
        fun open(context: Context) = Room.databaseBuilder(
            context, AppDatabase::class.java, "dishdb"
        ).build()
    }

}