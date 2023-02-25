package com.example.trainingtracker.choosetrainingday

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.activities.ThemeChangingActivity
import com.example.trainingtracker.databinding.ActivityChooseTrainingDayBinding
import com.example.trainingtracker.dbconnection.Room

class ChooseTrainingDayActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityChooseTrainingDayBinding
    private var trainingPlanId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseTrainingDayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        trainingPlanId = intent.getIntExtra("EXTRA_TRAINING_PLAN_ID", 0)

        loadItems()
    }

    private fun loadItems() {
        Thread {
            run {
                val items = Room.getTrainingDays(trainingPlanId)
//                Log.d("Received items", items.toString())
                // todo crashed if items is []
                if (items.isNotEmpty()) {
                    this@ChooseTrainingDayActivity.runOnUiThread {
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)
                        binding.recyclerView.adapter = ChooseTrainingDayAdapter(items, this)
                    }
                }
            }
        }.start()
    }
}