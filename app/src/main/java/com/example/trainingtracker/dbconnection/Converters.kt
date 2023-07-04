package com.example.trainingtracker.dbconnection

import android.util.Log
import androidx.room.TypeConverter
import com.example.trainingtracker.dbconnection.items.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Collectors

object Converters {
    @TypeConverter @JvmStatic fun stringToMap(value: String): HashMap<LocalDate, Float> {
        return if (value.isEmpty()) hashMapOf() else HashMap(value.split("|").stream().collect(Collectors.toMap(
            { LocalDate.parse(it.split(":")[0]) },
            { it.split(":")[1].toFloat() }
        )))
    }
    @TypeConverter @JvmStatic fun mapToString(value: HashMap<LocalDate, Float>?): String {
        return value?.map { "${it.key}:${it.value}" }?.joinToString("|") ?: ""
    }
    @TypeConverter @JvmStatic fun stringToLocalDateTime(dateString: String): LocalDateTime = LocalDateTime.parse(dateString)
    @TypeConverter @JvmStatic fun localDateTimeToString(date: LocalDateTime): String = date.toString()

    @TypeConverter @JvmStatic fun listOfFloatToJson(value: List<Float>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfFloat(value: String) = Gson().fromJson(value, Array<Float>::class.java).toList()

    @TypeConverter @JvmStatic fun listOfStringToJson(value: List<String>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfString(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter @JvmStatic fun listOfDateToJson(value: List<LocalDate>): String = value.joinToString("|") { it.toString() }
    @TypeConverter @JvmStatic fun jsonToListOfDate(value: String): List<LocalDate> = value.split("|").filter { it.isNotEmpty() }.map { LocalDate.parse(it) }

    @TypeConverter @JvmStatic fun listOfSerieItemToJson(value: List<SerieItem>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfSerieItem(value: String) = Gson().fromJson(value, Array<SerieItem>::class.java).toList()

    @TypeConverter @JvmStatic fun listOfEquipmentToJson(value: List<Equipment>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfEquipment(value: String) = Gson().fromJson(value, Array<Equipment>::class.java).toList()

    @TypeConverter @JvmStatic fun listOfMuscleToJson(value: List<Muscle>) = Gson().toJson(value)
    @TypeConverter @JvmStatic fun jsonToListOfMuscle(value: String) = Gson().fromJson(value, Array<Muscle>::class.java).toList()

    fun exerciseItemToString(value: ExerciseItem): String {
        return listOf(
            value.id,
            value.name,
            value.description,
            value.video_link,
            value.images_link.joinToString("|"),
            value.source_link,
            value.muscles.joinToString("|"),
            value.equipment.joinToString("|"),
            value.favourite,
        ).joinToString(";")
    }

    fun stringToExerciseItem(value: String): ExerciseItem? {
        return try {
            val list = value.split(";")
            ExerciseItem(
                list[0].toIntOrNull(),
                list[1],
                list[2],
                list[3],
                list[4].split('|'),
                list[5],
                list[6].split('|').filter { it.isNotEmpty() }.map { Muscle.valueOf(it) },
                list[7].split('|').filter { it.isNotEmpty() }.map { Equipment.valueOf(it) },
                list[8] == "true",
            )
        } catch (e: java.lang.IndexOutOfBoundsException) {
            null
        }
    }

    fun userItemToString(value: UserItem): String {
        return listOf(
            value.id,
            value.username,
            value.sex,
            value.height,
            mapToString(value.weight),
        ).joinToString(";")
    }

    fun stringToUserItem(value: String): UserItem? {
        return try {
            val list = value.split(";")
            UserItem(
                list[0].toIntOrNull(),
                list[1],
                Sex.valueOf(list[2]),
                list[3].toFloat(),
                stringToMap(list[4]),
            )
        } catch (e: java.lang.IndexOutOfBoundsException) {
            null
        }
    }

    fun historyItemToString(value: HistoryItem): String {
        return listOf(
            value.id,
            value.exercise_id,
            localDateTimeToString(value.date_start),
            localDateTimeToString(value.date_latest_update),
            value.notes,
            value.series.joinToString("|") {
                listOf(
                    it.time,
                    it.weight,
                    it.reps,
                    it.notes,
                    it.warmup,
                    it.weight_type
                ).joinToString("^")
            },
        ).joinToString(";")
    }

    fun stringToHistoryItem(value: String): HistoryItem? {
        return try {
            val list = value.split(";")
            return HistoryItem(
                list[0].toIntOrNull(),
                list[1].toInt(),
                stringToLocalDateTime(list[2]),
                stringToLocalDateTime(list[3]),
                list[4],
                list[5].split('|').map {
                    val serie = it.split('^')
                    SerieItem(
                        null,
                        serie[0].toIntOrNull(),
                        serie[1].toFloatOrNull(),
                        serie[2].toIntOrNull(),
                        serie[3],
                        serie[4] == "true",
                        WeightType.valueOf(serie[5])) },
            )
        } catch (e: java.lang.IndexOutOfBoundsException) {
            null
        }
    }
}