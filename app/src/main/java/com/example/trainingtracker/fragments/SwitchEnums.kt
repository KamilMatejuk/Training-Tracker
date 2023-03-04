package com.example.trainingtracker.fragments

interface SwitchEnum<T> {
    val desc: String
}

enum class SwitchMeasureType(override val desc: String): SwitchEnum<SwitchMeasureType> {
    REPS("For reps"),
    TIME("For time")
}

enum class SwitchMeasureType2(override val desc: String): SwitchEnum<SwitchMeasureType> {
    REPS("Show repped"),
    TIME("Show timed")
}

enum class SwitchWeightType(override val desc: String): SwitchEnum<SwitchWeightType> {
    FREEWEIGHT("Free\nweight"),
    BODYWEIGHT("Body\nweight"),
    WEIGHTED_BODYWEIGHT("Body weight +\nfree weight")
}
