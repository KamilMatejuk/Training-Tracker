package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class containing information about user
 *
 * @param id identification number
 * @param username name chosen by user
 * @param height_m user's height in meters
 * @param height_in user's height in inches
 * @param weight_kg user's weight in kilograms
 * @param weight_lbs user's weight in lbs
 */
@Entity
data class UserItem(
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "height_m") var height_m: Float,
    @ColumnInfo(name = "height_in") var height_in: Float,
    @ColumnInfo(name = "weight_kg") var weight_kg: Float,
    @ColumnInfo(name = "weight_lbs") var weight_lbs: Float,
)
