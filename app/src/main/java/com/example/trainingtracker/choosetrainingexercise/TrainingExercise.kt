package com.example.trainingtracker.choosetrainingexercise

data class TrainingExercise (
    val id: Int,
    val exercise_id: Int,
    var planned_series: String,
    val name: String,
    val description: String,
    var muscles: String?, // temporary
    var equipment: String?, // temporary
)
