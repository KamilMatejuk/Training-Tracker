package com.example.trainingtracker.dbconnection.items

import androidx.room.*

/**
 * Data class joining training day with exercise data
 *
 * @param id identification number
 * @param training_day_id identification number of TrainingDayItem
 * @param exercise_id identification number of ExerciseItem
 * @param orders order of exercises in day
 * @param planned_series how many sets and reps are planned
 */
@Entity(
    foreignKeys = [
        ForeignKey(entity = TrainingDayItem::class,
                   parentColumns = ["id"],
                   childColumns = ["training_day_id"],
                   onDelete = ForeignKey.CASCADE,
                   onUpdate = ForeignKey.CASCADE)
    ]
)
data class TrainingExerciseItem(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "training_day_id") var training_day_id: Int,
    @ColumnInfo(name = "exercise_id") var exercise_id: String,
    @ColumnInfo(name = "orders") var orders: Int,
    @ColumnInfo(name = "planned_series") var planned_series: String,
)
