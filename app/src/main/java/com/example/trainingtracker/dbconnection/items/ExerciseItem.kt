package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @param name name of the exercise
 * @param description description with how it looks
 * @param image image/video with how it looks
 * @param muscles list of active muscles' name
 * @param equipment list of necessary equipment
 */
@Entity
data class ExerciseItem(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "image") var image: String?,
    @ColumnInfo(name = "muscles") var muscles: String?,
    @ColumnInfo(name = "equipment") var equipment: String?,
): Serializable
