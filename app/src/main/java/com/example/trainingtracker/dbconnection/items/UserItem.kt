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
 * @param weight user's weight in kilograms how changed across time
 */
@Entity
data class UserItem(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "sex") var sex: Sex,
    @ColumnInfo(name = "height") var height: Float,
    @ColumnInfo(name = "weight") var weight: HashMap<LocalDate, Float>,
)

enum class Sex {
    MALE,
    FEMALE
}
