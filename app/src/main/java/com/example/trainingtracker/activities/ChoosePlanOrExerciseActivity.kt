package com.example.trainingtracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.trainingtracker.databinding.ActivityChoosePlanOrExerciseBinding

class ChoosePlanOrExerciseActivity : ThemeChangingActivity() {

    private lateinit var binding: ActivityChoosePlanOrExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChoosePlanOrExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.choosePlan.setOnClickListener {
            Toast.makeText(this, "Opcja tymczasowo nie dostępna", Toast.LENGTH_SHORT).show()
        }

        binding.chooseExercise.setOnClickListener {
            val intent = Intent(this, SearchExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.userProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}