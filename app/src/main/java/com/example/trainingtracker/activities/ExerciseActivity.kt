package com.example.trainingtracker.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.trainingtracker.databinding.ActivityExerciseBinding
import com.example.trainingtracker.dbconnection.items.ExerciseItem

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val exercise = intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem
        Log.d("EXERCISE", exercise.toString())


//        loadExerciseData()
//        loadExerciseHistory()
    }

//    @SuppressLint("SetTextI18n")
//    private fun loadExerciseData() {
//        Thread {
//            run {
//                val data = Room.getExerciseData(exerciseId)
//                binding.name.text = "Name: ${data.name}"
//                binding.description.text = "Description: ${data.description}"
////                Log.d("Exercise data", data.toString())
//            }
//        }.start()
//    }
//
//    private fun loadExerciseHistory() {
//        Thread {
//            run {
//                val history = Room.getHistory(trainingExerciseId)
////                Log.d("Exercise history", history.toString())
//                var text = ""
//                history.forEach { (hist, series) ->
//                    text += "On ${hist.date} done series: \n"
//                    series.forEach { s -> text += " * ${s.reps} x ${s.weight_kg} kg\n" }
//                    text += "saved notes: '${hist.notes}'\n\n"
//                }
//                binding.history.text = text
//            }
//        }.start()
//    }
}
