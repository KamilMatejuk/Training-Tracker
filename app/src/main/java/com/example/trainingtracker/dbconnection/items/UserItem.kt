package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

/**
 * Data class containing information about user
 *
 * @param id identification number
 * @param username name chosen by user
 * @param height user's height in meters
 * @param weight_values user's weight in kilograms how changed across time
 * @param weight_dates user's weight change dates
 */
@Entity
data class UserItem(
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "height") var height: Float,
    @ColumnInfo(name = "weight_values") var weight_values: List<Float>,
    @ColumnInfo(name = "weight_dates") var weight_dates: List<Date>,
)
