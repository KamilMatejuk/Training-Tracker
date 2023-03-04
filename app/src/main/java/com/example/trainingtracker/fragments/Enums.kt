package com.example.trainingtracker.fragments

interface OptionEnum<T> {
    val desc: String
}

enum class OptionMeasureType(override val desc: String): OptionEnum<OptionMeasureType> {
    REPS("For reps"),
    TIME("For time")
}

enum class OptionMeasureType2(override val desc: String): OptionEnum<OptionMeasureType2> {
    REPS("Show repped"),
    TIME("Show timed")
}

enum class OptionWeightType(override val desc: String): OptionEnum<OptionWeightType> {
    FREEWEIGHT("Free\nweight"),
    BODYWEIGHT("Body\nweight"),
    WEIGHTED_BODYWEIGHT("Body weight +\nfree weight")
}

enum class OptionSex(override val desc: String): OptionEnum<OptionSex> {
    MALE("Male"),
    FEMALE("Female")
}

enum class OptionMuscle(override val desc: String): OptionEnum<OptionMuscle> {
    LEGS("Legs"),
    ABS("Abs"),
    BACK("Back"),
    CHEST("Chest"),
    SHOULDERS("Shoulders"),
    ARMS("Arms"),
}

enum class OptionEquipment(override val desc: String): OptionEnum<OptionEquipment> {
    MACHINE("Machine"),
    PULLUP_BAR("Pullup bar"),
    SQUAT_RACK("Squat rack"),
    BENCH("Bench"),
    DUMBBELL("Dumbell"),
    BARBELL("Barbell"),
    RAISED_PLATFORM("Raised platform"),
    PARALLETS("Parallets")
}

