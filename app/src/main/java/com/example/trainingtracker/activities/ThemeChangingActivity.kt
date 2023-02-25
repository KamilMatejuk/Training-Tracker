package com.example.trainingtracker.activities

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.trainingtracker.R

open class ThemeChangingActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set theme
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val darkTheme = sharedPref.getBoolean("dark_theme", true)
        if (darkTheme)
            setTheme(R.style.Theme_TrainingTracker_Dark_NoActionBar)
        else
            setTheme(R.style.Theme_TrainingTracker_Light_NoActionBar)
    }
}