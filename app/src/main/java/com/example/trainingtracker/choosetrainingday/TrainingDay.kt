package com.example.trainingtracker.choosetrainingday

import java.sql.Date

data class TrainingDay(
    val id: Int,
    val name: String,
    val last_trained: Date?
)
