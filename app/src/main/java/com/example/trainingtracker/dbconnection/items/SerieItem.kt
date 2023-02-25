package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Time

/**
 * Data class containing information about history from a single day
 *
 * @param id identification number
 * @param history_id identification number of HistoryItem
 * @param time time of serie
 * @param weight_kg weight in kilograms
 * @param weight_lbs weight in lbs
 * @param reps number of reps
 * @param notes notes about this specific serie
 * @param warmup mark as warmup
 * @param weight_type type of weight
 */
@Entity(
    foreignKeys = [
        ForeignKey(entity = HistoryItem::class,
            parentColumns = ["id"],
            childColumns = ["history_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class SerieItem(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "history_id") var history_id: Int,
    @ColumnInfo(name = "time") var time: Time,
    @ColumnInfo(name = "weight_kg") var weight_kg: Float,
    @ColumnInfo(name = "weight_lbs") var weight_lbs: Float,
    @ColumnInfo(name = "reps") var reps: Int,
    @ColumnInfo(name = "notes") var notes: String,
    @ColumnInfo(name = "warmup") var warmup: Boolean,
    @ColumnInfo(name = "weight_type") var weight_type: WeightType,
)

enum class WeightType {
    FREE_WEIGHT,
    BODYWEIGHT,
    WEIGHTED_BODYWEIGHT
}
