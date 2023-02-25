package com.example.trainingtracker.dbconnection

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Time

// todo - fix date saving, check time saving
object Converters {
    /** String -> Date */
    @TypeConverter
    @JvmStatic
    fun toDate(dateString: String?): Date? {
        if (dateString == null) return null
        return Date.valueOf(dateString)
    }

    /** Date -> String */
    @TypeConverter
    @JvmStatic
    fun toDateString(date: Date?): String? {
        return date?.toString()
    }

    /** String -> Time */
    @TypeConverter
    @JvmStatic
    fun toTime(timeString: String?): Time? {
        if (timeString == null) return null
        return Time.valueOf(timeString)
    }

    /** Time -> String */
    @TypeConverter
    @JvmStatic
    fun toTimeString(time: Time?): String? {
        return time?.toString()
    }
}