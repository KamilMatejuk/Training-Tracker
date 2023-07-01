package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @param name name of the exercise
 * @param description description with how it looks
 * @param video_link video with how it looks
 * @param images_link images with how it looks
 * @param source_link link to source information
 * @param muscles list of active muscles' name
 * @param equipment list of necessary equipment
 */
@Entity
data class ExerciseItem(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "video_link") var video_link: String,
    @ColumnInfo(name = "images_link") var images_link: List<String>,
    @ColumnInfo(name = "source_link") var source_link: String,
    @ColumnInfo(name = "muscles") var muscles: List<Muscle>,
    @ColumnInfo(name = "equipment") var equipment: List<Equipment>,
    @ColumnInfo(name = "favourite") var favourite: Boolean = false,
): Serializable

enum class Muscle {
    QUADRICEPS_FEMORIS, // czworogłowy uda
    BICEPS_FEMORIS, // dwugłowy uda
    CALVES,
    ABS,
    BACK,
    CHEST,
    SHOULDERS,
    BICEPS,
    TRICEPS,
}

enum class Equipment {
    MACHINE,
    PULLUP_BAR,
    SQUAT_RACK,
    BENCH,
    DUMBBELL,
    BARBELL,
    RAISED_PLATFORM,
    PARALLETS,
}