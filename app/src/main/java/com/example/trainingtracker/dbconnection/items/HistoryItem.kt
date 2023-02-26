package com.example.trainingtracker.dbconnection.items

import androidx.room.*
import java.time.LocalDateTime

/**
 * Data class containing information about history from a single day
 *
 * @param id identification number
 * @param exercise_id identification number of ExerciseItem (if switched, else null)
 * @param date day of training
 * @param notes notes about this exercise as a whole
 * @param series list of series
 */
@Entity(
    foreignKeys = [
        ForeignKey(entity = ExerciseItem::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "exercise_id") var exercise_id: Int,
    @ColumnInfo(name = "date") var date: LocalDateTime,
    @ColumnInfo(name = "notes") var notes: String,
    @ColumnInfo(name = "series") var series: List<SerieItem>,
)
