package com.example.trainingtracker.choosetrainingplan

import java.sql.Date

data class TrainingPlan(
    val id: Int,
    val name: String,
    val last_trained: Date?
)
