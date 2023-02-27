package com.example.trainingtracker.dbconnection

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trainingtracker.dbconnection.items.*
import java.sql.Date
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
        db.addUser(UserItem(null, "Username", Sex.MALE, 1.80f, listOf(), listOf()))
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

    private fun createDummyHistory() {
        db.insertHistoryItem(HistoryItem(null, 1, LocalDateTime.of(2023, 2, 19, 23, 16), "", listOf(
            SerieItem(null, null, 20.0f, 5, "", true, WeightType.FREEWEIGHT),
            SerieItem(null, null, 40.0f, 5, "", false, WeightType.FREEWEIGHT),
            SerieItem(null, null, 40.0f, 5, "", false, WeightType.FREEWEIGHT),
            SerieItem(null, null, 50.0f, 5, "", false, WeightType.FREEWEIGHT),
        )))
        db.insertHistoryItem(HistoryItem(null, 1, LocalDateTime.of(2023, 2, 22, 22, 18), "", listOf(
            SerieItem(null, null, 20.0f, 5, "", true, WeightType.FREEWEIGHT),
            SerieItem(null, null, 40.0f, 5, "", false, WeightType.FREEWEIGHT),
            SerieItem(null, null, 50.0f, 5, "", false, WeightType.FREEWEIGHT),
            SerieItem(null, null, 60.0f, 5, "", false, WeightType.FREEWEIGHT),
        )))
        db.insertHistoryItem(HistoryItem(null, 1, LocalDateTime.of(2023, 2, 24, 19, 55), "", listOf(
            SerieItem(null, null, 20.0f, 5, "", true, WeightType.FREEWEIGHT),
            SerieItem(null, null, 40.0f, 5, "", false, WeightType.FREEWEIGHT),
            SerieItem(null, null, 50.0f, 5, "", false, WeightType.FREEWEIGHT),
            SerieItem(null, null, 65.0f, 5, "", false, WeightType.FREEWEIGHT),
        )))
    }

    fun getAllExerciseItems(): List<ExerciseItem> = db.getAllExerciseItems()
    fun getExercise(exercise_id: Int): ExerciseItem = db.getExerciseData(exercise_id)
    fun addExercise(item: ExerciseItem) = db.insertExerciseItem(item)
    fun getExerciseHistory(exercise_id: Int): List<HistoryItem> = db.getExerciseHistory(exercise_id)
    fun getUser(): UserItem? = db.getAllUserItems().firstOrNull()
    fun addHistoryItem(item: HistoryItem) = db.insertHistoryItem(item)
    fun updateHistoryItemSeries(item: HistoryItem) = db.updateHistoryItemSeries(item.id!!, item.series)
}