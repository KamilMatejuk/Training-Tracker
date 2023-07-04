package com.example.trainingtracker.dbconnection

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.room.*
import androidx.room.Room
import com.example.trainingtracker.dbconnection.items.*
import java.time.LocalDate


@Database(entities = [
    UserItem::class,
    ExerciseItem::class,
    HistoryItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun Dao(): DAO
}

object Room {
    private lateinit var db: DAO

    // todo - export data to file (csv)
    // todo - import data from file (csv)

    /**
     * Create database instance at the beginning of app lifecycle
     */
    fun initializeDatabase(context: Context) {
        db = Room.databaseBuilder(context,
            AppDatabase::class.java, "trainings-database"
        ).build().Dao()
        if (db.getAllExerciseItems().isEmpty()) {
            Log.d("LOADING", "default")
            FileManager.load(context, Uri.parse(
                "android.resource://" +
                        context.applicationContext.packageName + "/" +
                        com.example.trainingtracker.R.raw.default_fabrykasily
            ))
        }
    }

    fun getAllExerciseItems(): List<ExerciseItem> = db.getAllExerciseItems()
    fun getExercise(exercise_id: Int): ExerciseItem = db.getExerciseData(exercise_id)
    fun addExercise(item: ExerciseItem) = db.insertExerciseItem(item)
    fun setExerciseFavourite(exercise_id: Int, favourite: Boolean) = db.setExerciseFavourite(exercise_id, favourite)
    fun getExerciseHistory(exercise_id: Int): List<HistoryItem> = db.getExerciseHistory(exercise_id)
    fun getAllHistoryItems(): List<HistoryItem> = db.getAllHistoryItems()
    fun getUser(): UserItem? = db.getAllUserItems().firstOrNull()
    fun addUser(item: UserItem) {
        db.clearUserItems()
        db.insertUserItem(item)
    }
    fun updateUsername(user_id: Int, username: String) = db.updateUsername(user_id, username)
    fun updateSex(user_id: Int, sex: Sex) = db.updateSex(user_id, sex)
    fun updateHeight(user_id: Int, height: Float) = db.updateHeight(user_id, height)
    fun updateWeight(user_id: Int, weight: HashMap<LocalDate, Float>) = db.updateWeight(user_id, weight)
    fun addHistoryItem(item: HistoryItem) = db.insertHistoryItem(item)
    fun updateHistoryItemSeries(item: HistoryItem) = db.updateHistoryItemSeries(item.id!!, item.series)

    fun clearDatabase() {
        db.clearUserItems()
        db.clearHistoryItems()
        db.clearExerciseItems()
    }
}