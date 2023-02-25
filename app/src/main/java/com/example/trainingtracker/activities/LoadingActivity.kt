package com.example.trainingtracker.activities

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import com.example.trainingtracker.choosetrainingplan.ChooseTrainingPlanActivity
import com.example.trainingtracker.databinding.ActivityLoadingBinding
import com.example.trainingtracker.dbconnection.Room

class LoadingActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create view
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // set progress bar width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        binding.progressBar.layoutParams.width = (width * 0.7).toInt()

        // create database
        var created = false
        Thread {
            Room.initializeDatabase(this)
            created = true
        }.start()
        // start main activity
        Thread {
            for(i in 0..80){
                binding.progressBar.progress = i
                Thread.sleep(20)
            }
            while(!created) {
                Thread.sleep(20)
            }
            for(i in 81..100){
                binding.progressBar.progress = i
                Thread.sleep(20)
            }
            val intent = Intent(this, ChooseTrainingPlanActivity::class.java)
            startActivity(intent)
            finish()
        }.start()
    }
}