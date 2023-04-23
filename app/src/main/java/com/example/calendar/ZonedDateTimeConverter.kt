package com.example.calendar

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.ZonedDateTime

@ProvidedTypeConverter
class ZonedDateTimeConverter {
    @TypeConverter
    fun fromString(value: String?): ZonedDateTime? {
        return ZonedDateTime.parse(value)
    }

    @TypeConverter
    fun dateToString(date: ZonedDateTime?): String {
        return date.toString()
    }
}