package com.example.trainingtracker.exercise

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.R
import com.example.trainingtracker.choosetrainingexercise.ChooseTrainingExerciseAdapter
import com.example.trainingtracker.databinding.ActivityChooseTrainingExerciseBinding
import com.example.trainingtracker.databinding.ActivityExerciseBinding
import com.example.trainingtracker.dbconnection.Room

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private var exerciseId: Int = 0
    private var trainingExerciseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        exerciseId = intent.getIntExtra("EXTRA_EXERCISE_ID", 0)
        trainingExerciseId = intent.getIntExtra("EXTRA_TRAINING_EXERCISE_ID", 0)

        loadExerciseData()
        loadExerciseHistory()
    }

    @SuppressLint("SetTextI18n")
    private fun loadExerciseData() {
        Thread {
            run {
                val data = Room.getExerciseData(exerciseId)
                binding.name.text = "Name: ${data.name}"
                binding.description.text = "Description: ${data.description}"
//                Log.d("Exercise data", data.toString())
            }
        }.start()
    }

    private fun loadExerciseHistory() {
        Thread {
            run {
                val history = Room.getHistory(trainingExerciseId)
//                Log.d("Exercise history", history.toString())
                var text = ""
                history.forEach { (hist, series) ->
                    text += "On ${hist.date} done series: \n"
                    series.forEach { s -> text += " * ${s.reps} x ${s.weight_kg} kg\n" }
                    text += "saved notes: '${hist.notes}'\n\n"
                }
                binding.history.text = text
            }
        }.start()
    }
}
