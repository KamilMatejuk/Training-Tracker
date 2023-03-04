package com.example.trainingtracker.dbconnection

import androidx.room.*
import com.example.trainingtracker.dbconnection.items.*
import java.time.LocalDate


/**
 * Database access object with defined queries
 *
 *
 * https://stackoverflow.com/a/65754091/6645624
 */
@Dao
interface DAO {
    /***********************************************************************************************
     ******************************************** USER *********************************************
     ***********************************************************************************************/
    @Query("SELECT * FROM useritem") fun getAllUserItems(): List<UserItem>
    @Query("DELETE FROM useritem") fun clearUserItems()
    @Insert fun insertUserItem(item: UserItem)
    @Query("UPDATE useritem SET username = :username WHERE id = :user_id")
    fun updateUsername(user_id: Int, username: String)
    @Query("UPDATE useritem SET sex = :sex WHERE id = :user_id")
    fun updateSex(user_id: Int, sex: Sex)
    @Query("UPDATE useritem SET height = :height WHERE id = :user_id")
    fun updateHeight(user_id: Int, height: Float)
    @Query("UPDATE useritem SET weight_dates = :dates, weight_values = :values WHERE id = :user_id")
    fun updateWeight(user_id: Int, values: List<Float>, dates: List<LocalDate>)


    /***********************************************************************************************
    ****************************************** EXERCISES *******************************************
    ***********************************************************************************************/
    @Query("SELECT * FROM exerciseitem") fun getAllExerciseItems(): List<ExerciseItem>
    @Insert fun insertExerciseItem(item: ExerciseItem)
    @Query("SELECT * FROM exerciseitem WHERE id = :exercise_id")
    fun getExerciseData(exercise_id: Int): ExerciseItem

    @Query("SELECT * FROM historyitem WHERE exercise_id = :exercise_id")
    fun getExerciseHistory(exercise_id: Int): List<HistoryItem>
    @Insert fun insertHistoryItem(item: HistoryItem)

    @Query("UPDATE historyitem SET series = :series WHERE id = :history_id")
    fun updateHistoryItemSeries(history_id: Int, series: List<SerieItem>)

}