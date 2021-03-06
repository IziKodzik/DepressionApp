package com.example.sadge.dejtabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sadge.model.MadgeSettings
import com.example.sadge.model.PicDto

@Dao
interface PicDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(picDto: PicDto)

    @Query("SELECT * FROM pic;" )
    fun selectAll(): List<PicDto>

    @Query("SELECT * FROM pic WHERE id = :id;" )
    fun selectById(id: Int): PicDto

    @Query("SELECT * FROM pic WHERE date= :date;")
    fun selectByDate(date: String): PicDto

    @Query("SELECT * FROM settings WHERE id = 0;")
    fun selectDefaultSettings(): MadgeSettings

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settings: MadgeSettings)

}