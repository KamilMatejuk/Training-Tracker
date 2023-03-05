package com.example.trainingtracker.dbconnection.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Data class containing information about user
 *
 * @param id identification number
 * @param username name chosen by user
 * @param sex user's sex
 * @param height user's height in meters
 * @param weight_values user's weight in kilograms how changed across time
 * @param weight_dates user's weight change dates
 */
@Entity
data class UserItem(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "sex") var sex: Sex,
    @ColumnInfo(name = "height") var height: Float,
    @ColumnInfo(name = "weight_values") var weight_values: List<Float>,
    @ColumnInfo(name = "weight_dates") var weight_dates: List<LocalDate>,
)

enum class Sex {
    MALE,
    FEMALE
}
