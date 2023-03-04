package com.example.trainingtracker.dbconnection

import android.content.Context
import androidx.room.*
import androidx.room.Room
import com.example.trainingtracker.dbconnection.items.*
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime


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
        createDefaultUser()
        loadExercises()
    }

    private fun createDefaultUser() {
        if (db.getAllUserItems().isNotEmpty()) return
        db.insertUserItem(UserItem(null, "Username", Sex.MALE, 1.80f, listOf(), listOf()))
    }

    private fun loadExercises() {
        if (db.getAllExerciseItems().isNotEmpty()) return
        listOf(
            ExerciseItem(null, "Wyciskanie na ławeczce poziomej", "", "", listOf(Muscle.CHEST), listOf(Equipment.BENCH, Equipment.BARBELL)),
            ExerciseItem(null, "Rozpiętki na ławeczce dodatniej", "", "", listOf(Muscle.CHEST, Muscle.SHOULDERS), listOf(Equipment.BENCH, Equipment.BARBELL)),
            ExerciseItem(null, "Rozpiętki na ławeczce ujemnej", "", "", listOf(Muscle.CHEST), listOf(Equipment.BENCH, Equipment.BARBELL)),
            ExerciseItem(null, "Dipy", "", "", listOf(Muscle.CHEST, Muscle.ARMS, Muscle.SHOULDERS), listOf(Equipment.PARALLETS)),
            ExerciseItem(null, "Wyciskanie żołnierskie", "", "", listOf(Muscle.SHOULDERS), listOf(Equipment.BARBELL, Equipment.DUMBBELL)),
            ExerciseItem(null, "Martwy ciąg", "", "", listOf(Muscle.BACK, Muscle.LEGS), listOf(Equipment.BARBELL)),
            ExerciseItem(null, "Przysiady", "", "", listOf(Muscle.BACK, Muscle.LEGS), listOf(Equipment.SQUAT_RACK, Equipment.BARBELL)),
            ExerciseItem(null, "Wzniosy nóg wisząc na drążku", "", "", listOf(Muscle.ABS), listOf(Equipment.PULLUP_BAR)),
            ExerciseItem(null, "Skręty tułowia", "", "", listOf(Muscle.ABS), listOf(Equipment.MACHINE)),
            ExerciseItem(null, "Hip thrust", "", "", listOf(Muscle.LEGS), listOf(Equipment.BARBELL, Equipment.RAISED_PLATFORM)),
        ).forEach { db.insertExerciseItem(it) }
    }

    fun getAllExerciseItems(): List<ExerciseItem> = db.getAllExerciseItems()
    fun getExercise(exercise_id: Int): ExerciseItem = db.getExerciseData(exercise_id)
    fun addExercise(item: ExerciseItem) = db.insertExerciseItem(item)
    fun getExerciseHistory(exercise_id: Int): List<HistoryItem> = db.getExerciseHistory(exercise_id)
    fun getUser(): UserItem? = db.getAllUserItems().firstOrNull()
    fun addUser(item: UserItem) {
        db.clearUserItems()
        db.insertUserItem(item)
    }
    fun updateUsername(user_id: Int, username: String) = db.updateUsername(user_id, username)
    fun updateSex(user_id: Int, sex: Sex) = db.updateSex(user_id, sex)
    fun updateHeight(user_id: Int, height: Float) = db.updateHeight(user_id, height)
    fun updateWeight(user_id: Int, values: List<Float>, dates: List<LocalDate>) = db.updateWeight(user_id, values, dates)
    fun addHistoryItem(item: HistoryItem) = db.insertHistoryItem(item)
    fun updateHistoryItemSeries(item: HistoryItem) = db.updateHistoryItemSeries(item.id!!, item.series)
}