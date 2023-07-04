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
    @Query("UPDATE useritem SET weight = :weight WHERE id = :user_id")
    fun updateWeight(user_id: Int, weight: HashMap<LocalDate, Float>)


    /***********************************************************************************************
    ****************************************** EXERCISES *******************************************
    ***********************************************************************************************/
    @Query("SELECT * FROM exerciseitem") fun getAllExerciseItems(): List<ExerciseItem>
    @Query("SELECT * FROM historyitem") fun getAllHistoryItems(): List<HistoryItem>
    @Query("DELETE FROM exerciseitem") fun clearExerciseItems()
    @Query("DELETE FROM historyitem") fun clearHistoryItems()

    @Insert fun insertExerciseItem(item: ExerciseItem)
    @Query("SELECT * FROM exerciseitem WHERE id = :exercise_id")
    fun getExerciseData(exercise_id: Int): ExerciseItem

    @Query("UPDATE exerciseitem SET favourite = :favourite WHERE id = :exercise_id")
    fun setExerciseFavourite(exercise_id: Int, favourite: Boolean)

    @Query("SELECT * FROM historyitem WHERE exercise_id = :exercise_id")
    fun getExerciseHistory(exercise_id: Int): List<HistoryItem>
    @Insert fun insertHistoryItem(item: HistoryItem)

    @Query("UPDATE historyitem SET series = :series WHERE id = :history_id")
    fun updateHistoryItemSeries(history_id: Int, series: List<SerieItem>)

}