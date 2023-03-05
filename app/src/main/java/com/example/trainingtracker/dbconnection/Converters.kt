package com.example.trainingtracker.dbconnection

import androidx.room.TypeConverter
import com.example.trainingtracker.dbconnection.items.Equipment
import com.example.trainingtracker.dbconnection.items.Muscle
import com.example.trainingtracker.dbconnection.items.SerieItem
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalDateTime

// todo - fix date saving, check time saving
object Converters {
    @TypeConverter @JvmStatic fun stringToLocalDateTime(dateString: String): LocalDateTime = LocalDateTime.parse(dateString)
    @TypeConverter @JvmStatic fun localDateTimeToString(date: LocalDateTime): String = date.toString()

    @TypeConverter @JvmStatic fun listOfFloatToJson(value: List<Float>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfFloat(value: String) = Gson().fromJson(value, Array<Float>::class.java).toList()

    @TypeConverter @JvmStatic fun listOfDateToJson(value: List<LocalDate>): String = value.joinToString("|") { it.toString() }
    @TypeConverter @JvmStatic fun jsonToListOfDate(value: String): List<LocalDate> = value.split("|").filter { it.isNotEmpty() }.map { LocalDate.parse(it) }

    @TypeConverter @JvmStatic fun listOfSerieItemToJson(value: List<SerieItem>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfSerieItem(value: String) = Gson().fromJson(value, Array<SerieItem>::class.java).toList()

    @TypeConverter @JvmStatic fun listOfEquipmentToJson(value: List<Equipment>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfEquipment(value: String) = Gson().fromJson(value, Array<Equipment>::class.java).toList()

    @TypeConverter @JvmStatic fun listOfMuscleToJson(value: List<Muscle>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfMuscle(value: String) = Gson().fromJson(value, Array<Muscle>::class.java).toList()

}