package com.example.trainingtracker.choosetrainingexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.R
import com.example.trainingtracker.choosetrainingday.ChooseTrainingDayAdapter
import com.example.trainingtracker.choosetrainingplan.ChooseTrainingPlanAdapter
import com.example.trainingtracker.databinding.ActivityChooseTrainingDayBinding
import com.example.trainingtracker.databinding.ActivityChooseTrainingExerciseBinding
import com.example.trainingtracker.dbconnection.Room

class ChooseTrainingExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseTrainingExerciseBinding
    private var trainingExerciseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseTrainingExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        trainingExerciseId = intent.getIntExtra("EXTRA_TRAINING_EXERCISE_ID", 0)

        loadItems()
    }

    private fun loadItems() {
        Thread {
            run {
                val items = Room.getTrainingExercises(trainingExerciseId)
//                Log.d("Received items", items.toString())
                // todo crashed if items is []
                if (items.isNotEmpty()) {
                    this@ChooseTrainingExerciseActivity.runOnUiThread {
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)
                        binding.recyclerView.adapter = ChooseTrainingExerciseAdapter(items, this)
                    }
                }
            }
        }.start()
    }
}