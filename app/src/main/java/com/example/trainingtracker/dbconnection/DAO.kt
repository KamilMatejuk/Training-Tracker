package com.example.trainingtracker.dbconnection

import androidx.room.*
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.HistoryItem
import com.example.trainingtracker.dbconnection.items.SerieItem
import com.example.trainingtracker.dbconnection.items.UserItem


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
    @Insert fun addUser(item: UserItem)
    // todo
    // edit user
    // add weight measurement


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