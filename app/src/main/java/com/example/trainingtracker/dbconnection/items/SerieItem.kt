package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class containing information about history from a single day
 *
 * @param id identification number
 * @param time time of serie
 * @param weight weight in kilograms
 * @param reps number of reps
 * @param notes notes about this specific serie
 * @param warmup mark as warmup
 * @param weight_type type of weight
 */
@Entity
data class SerieItem(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "time") var time: Int?,
    @ColumnInfo(name = "weight") var weight: Float?,
    @ColumnInfo(name = "reps") var reps: Int?,
    @ColumnInfo(name = "notes") var notes: String?,
    @ColumnInfo(name = "warmup") var warmup: Boolean,
    @ColumnInfo(name = "weight_type") var weight_type: WeightType,
)

enum class WeightType {
    FREEWEIGHT,
    BODYWEIGHT,
    WEIGHTED_BODYWEIGHT
}
