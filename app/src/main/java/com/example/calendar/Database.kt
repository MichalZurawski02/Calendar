package com.example.calendar

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class], version = 1)
@TypeConverters(ZonedDateTimeConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun eventDao(): EventDAO
}