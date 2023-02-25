package com.example.trainingtracker.dbconnection

import androidx.room.*
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.HistoryItem
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
    fun getUser(): UserItem = getAllUserItems()[0]
    @Insert fun addUser(item: UserItem)
    // todo
    // edit user
    // add weight measurement


    /***********************************************************************************************
    ****************************************** EXERCISES *******************************************
    ***********************************************************************************************/
    @Query("SELECT * FROM exerciseitem") fun getAllExerciseItems(): List<ExerciseItem>
    @Insert fun insertExerciseItem(item: ExerciseItem)


//    @Query("SELECT * FROM historyitem") fun getAllHistoryItems(): List<HistoryItem>
//
//    @Insert fun insertUserItem(item: UserItem)
//    @Insert fun insertHistoryItem(item: HistoryItem)
//
//    @Delete fun deleteUserItem(item: UserItem)
//    @Delete fun deleteExerciseItem(item: ExerciseItem)
//    @Delete fun deleteHistoryItem(item: HistoryItem)
//
//    @Query("SELECT " +
//            "trainingplanitem.id AS id, " +
//            "trainingplanitem.name AS name, " +
//            "MAX(historyitem.date) AS last_trained " +
//            "FROM trainingplanitem " +
//            "JOIN trainingdayitem ON trainingplanitem.id = trainingdayitem.training_plan_id " +
//            "JOIN trainingexerciseitem ON trainingexerciseitem.training_day_id = trainingdayitem.id " +
//            "LEFT JOIN historyitem ON historyitem.training_exercise_id = trainingexerciseitem.id " +
//            "GROUP BY trainingplanitem.name " +
//            "ORDER BY trainingplanitem.id ASC")
//    fun getTrainingPlans(): List<TrainingPlan>
//
//    @Query("SELECT " +
//            "trainingdayitem.id AS id, " +
//            "trainingdayitem.name AS name, " +
//            "MAX(historyitem.date) AS last_trained " +
//            "FROM trainingplanitem " +
//            "JOIN trainingdayitem ON trainingplanitem.id = trainingdayitem.training_plan_id " +
//            "JOIN trainingexerciseitem ON trainingexerciseitem.training_day_id = trainingdayitem.id " +
//            "LEFT JOIN historyitem ON historyitem.training_exercise_id = trainingexerciseitem.id " +
//            "WHERE trainingplanitem.id = :training_plan_id " +
//            "GROUP BY trainingdayitem.name " +
//            "ORDER BY trainingdayitem.orders ASC")
//    fun getTrainingDays(training_plan_id: Int): List<TrainingDay>
//
//    @Query("SELECT " +
//            "trainingexerciseitem.id AS id, " +
//            "trainingexerciseitem.exercise_id AS exercise_id, " +
//            "trainingexerciseitem.planned_series AS planned_series, " +
//            "exerciseitem.name AS name, " +
//            "exerciseitem.description AS description, " +
//            "exerciseitem.muscles AS muscles, " +
//            "exerciseitem.equipment AS equipment " +
//            "FROM trainingexerciseitem " +
//            "LEFT JOIN exerciseitem ON exerciseitem.firebase_id = trainingexerciseitem.exercise_id " +
//            "WHERE trainingexerciseitem.training_day_id = :training_day_id " +
//            "ORDER BY trainingexerciseitem.orders ASC")
//    fun getTrainingExercises(training_day_id: Int): List<TrainingExercise>
//
//    @Query("SELECT * FROM exerciseitem WHERE id = :exercise_id")
//    fun getExerciseData(exercise_id: Int): ExerciseItem
//
//    @Transaction
//    @Query("SELECT * FROM historyitem WHERE training_exercise_id = :training_exercise_id")
//    fun getHistory(training_exercise_id: Int): List<HistoryAndSeries>
}