package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.trainingtracker.databinding.ActivityExerciseBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.fragments.CalendarDataViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.Period

class ExerciseActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var exercise: ExerciseItem
    private val viewModelData: CalendarDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        exercise = (intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem)!!

        loadExerciseData()
        loadExerciseHistory()
    }

    @SuppressLint("SetTextI18n")
    private fun loadExerciseData() {
        Thread {
            run {
                val data = exercise.id?.let { Room.getExercise(it) }
                binding.name.text = data?.name
                binding.description.text = data?.description
                Log.d("Exercise data", data.toString())
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun loadExerciseHistory() {
        Thread {
            run {
                val history = exercise.id?.let { Room.getExerciseHistory(it) }
                val dates = history?.map { LocalDate.of(it.date.year, it.date.month, it.date.dayOfMonth) } ?: listOf()
                viewModelData.setData(dates)

                val now = LocalDate.now()
                val dates7 = dates.filter { now.minus(Period.ofDays(7)) < it }.size
                val dates31 = dates.filter { now.minus(Period.ofDays(31)) < it }.size
                val dates365 = dates.filter { now.minus(Period.ofDays(365)) < it }.size
                binding.trainedWeek.text = "Trained in last 7 days: $dates7"
                binding.trainedMonth.text = "Trained in last 31 days: $dates31"
                binding.trainedYear.text = "Trained in last 365 days: $dates365"
            }
        }.start()
    }
}
