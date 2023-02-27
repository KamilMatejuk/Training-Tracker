package com.example.trainingtracker.activities

import android.os.Bundle
import com.example.trainingtracker.databinding.ActivityUserProfileBinding

class UserProfileActivity : ThemeChangingActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}