package com.example.calendar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.ZonedDateTime

@Dao
interface EventDAO {
    @Query("SELECT * FROM event WHERE datetime = :targetDate")
    fun getByDateTime(targetDate: ZonedDateTime): List<Event>
    @Query("SELECT * FROM event")
    fun getAll() : List<Event>
    @Insert
    fun insert(event: Event)
    @Delete
    fun delete(event: Event)
}