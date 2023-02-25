package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class containing information about a single training plan
 *
 * @param id identification number
 * @param name name of the plan
 */
@Entity
data class TrainingPlanItem(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name") var name: String,
)
