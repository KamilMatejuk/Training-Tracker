package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @param name name of the exercise
 * @param description description with how it looks
 * @param video_link video with how it looks
 * @param muscles list of active muscles' name
 * @param equipment list of necessary equipment
 */
@Entity
data class ExerciseItem(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "video_link") var video_link: String,
    @ColumnInfo(name = "muscles") var muscles: List<Muscle>,
    @ColumnInfo(name = "equipment") var equipment: List<Equipment>,
): Serializable

enum class Muscle {
    LEGS,
    ABS,
    BACK,
    CHEST,
    SHOULDERS,
    ARMS
}

enum class Equipment {
    MACHINE,
    PULLUP_BAR,
    SQUAT_RACK,
    BENCH,
    DUMBBELL,
    BARBELL,
    RAISED_PLATFORM,
    PARALLETS
}