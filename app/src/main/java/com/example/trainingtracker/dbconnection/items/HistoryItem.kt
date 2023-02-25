package com.example.trainingtracker.dbconnection.items

import androidx.room.*
import java.sql.Date

/**
 * Data class containing information about history from a single day
 *
 * @param id identification number
 * @param training_exercise_id identification number of TrainingExerciseItem
 * @param training_exercise_id identification number of ExerciseItem (if switched, else null)
 * @param date day of training
 * @param notes notes about this exercise as a whole
 */
@Entity(
    foreignKeys = [
        ForeignKey(entity = TrainingExerciseItem::class,
            parentColumns = ["id"],
            childColumns = ["training_exercise_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class HistoryItem(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "training_exercise_id") var training_exercise_id: Int,
    @ColumnInfo(name = "switched_exercise_id") var switched_exercise_id: String?,
    @ColumnInfo(name = "date") var date: Date,
    @ColumnInfo(name = "notes") var notes: String,
)
