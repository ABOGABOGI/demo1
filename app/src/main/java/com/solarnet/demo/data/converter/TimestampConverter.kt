package com.solarnet.demo.data.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

class TimestampConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        if (value == null) return null
        return Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

}