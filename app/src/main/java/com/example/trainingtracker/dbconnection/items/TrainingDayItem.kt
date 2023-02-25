package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Data class containing information about a single training plan's day
 *
 * @param id identification number
 * @param name name of the plan's day
 * @param training_plan_id identification number of TrainingDayItem
 * @param orders order of days in plan
 */
@Entity(
    foreignKeys = [
        ForeignKey(entity = TrainingPlanItem::class,
            parentColumns = ["id"],
            childColumns = ["training_plan_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class TrainingDayItem(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "training_plan_id") var training_plan_id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "orders") var orders: Int,
)
