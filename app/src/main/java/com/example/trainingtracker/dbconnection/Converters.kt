package com.example.trainingtracker.dbconnection

import androidx.room.TypeConverter
import com.example.trainingtracker.dbconnection.items.SerieItem
import com.google.gson.Gson
import java.sql.Date
import java.time.LocalDateTime

// todo - fix date saving, check time saving
object Converters {
    @TypeConverter
    @JvmStatic
    fun stringToDate(dateString: String?): Date? {
        if (dateString == null) return null
        return Date.valueOf(dateString)
    }

    @TypeConverter
    @JvmStatic
    fun dateToString(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    @JvmStatic
    fun stringToLocalDateTime(dateString: String?): LocalDateTime? {
        if (dateString == null) return null
        return LocalDateTime.parse(dateString)
    }

    @TypeConverter
    @JvmStatic
    fun localDateTimeToString(date: Date?): String? {
        return date?.toString()
    }

    @TypeConverter
    @JvmStatic
    fun listOfFloatToJson(value: List<Float>?) = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun jsonToListOfFloat(value: String) = Gson().fromJson(value, Array<Float>::class.java).toList()

    @TypeConverter
    @JvmStatic
    fun listOfDateToJson(value: List<Date>?) = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun jsonToListOfDate(value: String) = Gson().fromJson(value, Array<Date>::class.java).toList()

    @TypeConverter
    @JvmStatic
    fun listOfSerieItemToJson(value: List<SerieItem>?) = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun jsonToListOfSerieItem(value: String) = Gson().fromJson(value, Array<SerieItem>::class.java).toList()

}