package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * TEMPORARY - TODO INPLEMENT IN FIREBASE
 *
 * @param firebase_id id in firebase
 * @param name name of the exercise
 * @param image image/video with how it looks
 * @param muscles list of active muscles' name
 * @param equipment list of necessary equipment
 */
@Entity
data class ExerciseItem(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "firebase_id", index = true) var firebase_id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "image") var image: String?, // temporary
    @ColumnInfo(name = "muscles") var muscles: String?, // temporary
    @ColumnInfo(name = "equipment") var equipment: String?, // temporary
)
