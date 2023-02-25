package com.example.trainingtracker.choosetrainingplan

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.activities.ThemeChangingActivity
import com.example.trainingtracker.databinding.ActivityChooseTrainingPlanBinding
import com.example.trainingtracker.dbconnection.Room

class ChooseTrainingPlanActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityChooseTrainingPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseTrainingPlanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loadItems()
    }

    private fun loadItems() {
        Thread {
            run {
                val items = Room.getTrainingPlans()
//                Log.d("Received items", items.toString())
                // todo crashed if items is []
                if (items.isNotEmpty()) {
                    this@ChooseTrainingPlanActivity.runOnUiThread {
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)
                        binding.recyclerView.adapter = ChooseTrainingPlanAdapter(items, this)
                    }
                }
            }
        }.start()
    }
}