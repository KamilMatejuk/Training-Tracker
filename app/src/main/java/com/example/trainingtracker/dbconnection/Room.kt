package com.example.trainingtracker.dbconnection

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trainingtracker.dbconnection.items.*
import com.example.trainingtracker.choosetrainingday.TrainingDay
import com.example.trainingtracker.choosetrainingexercise.TrainingExercise
import com.example.trainingtracker.choosetrainingplan.TrainingPlan
import com.example.trainingtracker.exercise.HistoryAndSeries
import java.sql.Date
import java.sql.Time


@Database(entities = [
    UserItem::class,
    TrainingPlanItem::class,
    TrainingDayItem::class,
    TrainingExerciseItem::class,
    ExerciseItem::class,
    HistoryItem::class,
    SerieItem::class], version = 1)
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
//         createDummyData()
    }

    fun createDummyData() {
        db.insertUserItem(UserItem(1, "Kamil", 1.80f, 70.3f, 65.5f, 144.4f))
        // training
        db.insertTrainingPlanItem(TrainingPlanItem(1, "Push, Pull, Legs #1"))
        db.insertTrainingDayItem(TrainingDayItem(1, 1, "Push", 1))
        db.insertTrainingDayItem(TrainingDayItem(2, 1, "Pull", 2))
        db.insertTrainingDayItem(TrainingDayItem(3, 1, "Legs", 3))
        // exercises
        db.insertExerciseItem(ExerciseItem(1, "1", "Wyciskanie na ławeczce poziomej", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(2, "2", "Rozpiętki na ławeczce dodatniej", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(3, "3", "Rozpiętki w bramie, wyciąg na górze", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(4, "4", "Rozpiętki w bramie, wyciąg po środku", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(5, "5", "Rozpiętki w bramie, wyciąg na dole", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(6, "6", "Dipy", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(7, "7", "Wyciskanie żołnierskie", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(8, "8", "Unoszenie rąk na boki z hantlami", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(9, "9", "Prostowanie ramion na wyciągu", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(10, "10", "Martwy ciąg", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(11, "11", "Wiosłowanie końcem sztangi", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(12, "12", "Podciąganie", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(13, "13", "Przyciąganie linki wyciągu górnego, siedząc, wąski chwyt", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(14, "14", "Odwrotne rozpiętki na maszynie", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(15, "15", "Szrugsy", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(16, "16", "Uginannie rąk ze sztangą na modlitewniku", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(17, "17", "Przysiady", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(18, "18", "Wykroki", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(19, "19", "Prostowanie nóg na maszynie", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(20, "20", "Zginanie nóg na maszynie", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(21, "21", "Wspięcia na palce", "", null, null, null))
        db.insertExerciseItem(ExerciseItem(22, "22", "Wzniosy nóg wisząc na drążku", "", null, null, null))
        // exercises in training
        // push
        db.insertTrainingExerciseItem(TrainingExerciseItem(1, 1, "1", 1, "4x8"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(2, 1, "2", 2, "4x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(3, 1, "3", 3, "2x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(4, 1, "4", 4, "2x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(5, 1, "5", 5, "2x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(6, 1, "6", 6, "3x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(7, 1, "7", 7, "4x8"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(8, 1, "8", 8, "4x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(9, 1, "9", 9, "3x12"))
        // pull
        db.insertTrainingExerciseItem(TrainingExerciseItem(10, 2, "10", 1, "3x8"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(11, 2, "11", 2, "3x8"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(12, 2, "12", 3, "5x5"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(13, 2, "13", 4, "3x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(14, 2, "14", 5, "3x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(15, 2, "15", 6, "3x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(16, 2, "16", 7, "3x10"))
        // legs
        db.insertTrainingExerciseItem(TrainingExerciseItem(17, 3, "17", 1, "4x8"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(18, 3, "18", 2, "3x20m"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(19, 3, "19", 3, "4x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(20, 3, "20", 4, "4x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(21, 3, "21", 5, "3x10"))
        db.insertTrainingExerciseItem(TrainingExerciseItem(22, 3, "22", 6, "3x10"))
        // history
        db.insertHistoryItem(HistoryItem(1, 1, null, Date.valueOf("2022-04-16"), ""))
        db.insertSerieItem(SerieItem(1, 1, Time(15, 55, 0), 60f, 132f, 6, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(2, 1, Time(15, 57, 0), 60f, 132f, 8, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(3, 1, Time(15, 58, 9), 60f, 132f, 7, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(4, 1, Time(16, 0, 45), 60f, 132f, 5, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(5, 1, Time(16, 1, 10), 60f, 132f, 7, "", false, WeightType.FREE_WEIGHT))
        db.insertHistoryItem(HistoryItem(2, 1, null, Date.valueOf("2022-04-18"), ""))
        db.insertSerieItem(SerieItem(6, 2, Time(12, 55, 0), 60f, 132f, 7, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(7, 2, Time(12, 57, 0), 60f, 132f, 7, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(8, 2, Time(12, 58, 9), 60f, 132f, 7, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(9, 2, Time(13, 0, 45), 60f, 132f, 5, "", false, WeightType.FREE_WEIGHT))
        db.insertSerieItem(SerieItem(10, 2, Time(13, 1, 10), 60f, 132f, 4, "", false, WeightType.FREE_WEIGHT))
    }

    fun getTrainingPlans(): List<TrainingPlan> = db.getTrainingPlans()
    fun getTrainingDays(training_plan_id: Int): List<TrainingDay> = db.getTrainingDays(training_plan_id)
    fun getTrainingExercises(training_day_id: Int): List<TrainingExercise> = db.getTrainingExercises(training_day_id)
    fun getExerciseData(exercise_id: Int): ExerciseItem = db.getExerciseData(exercise_id)
    fun getHistory(training_exercise_id: Int): List<HistoryAndSeries> = db.getHistory(training_exercise_id)
}