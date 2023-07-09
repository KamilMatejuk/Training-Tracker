package com.example.trainingtracker.fragments

interface OptionEnum<T> {
    val desc: String
}

enum class OptionMeasureType(override val desc: String): OptionEnum<OptionMeasureType> {
    REPS("Na powtórzenia"),
    TIME("Na czas")
}

enum class OptionMeasureType2(override val desc: String): OptionEnum<OptionMeasureType2> {
    REPS("Pokaż serie na powtórzenia"),
    TIME("Pokaż serie na czas")
}

enum class OptionTechniqueType(override val desc: String): OptionEnum<OptionTechniqueType> {
    VIDEO("Pokaż film"),
    IMAGES("Pokaż zdjęcia")
}

enum class OptionWeightType(override val desc: String): OptionEnum<OptionWeightType> {
    FREEWEIGHT("Wolne\nciężary"),
    BODYWEIGHT("Masa\nciała"),
    WEIGHTED_BODYWEIGHT("Masa ciała +\nciężar")
}

enum class OptionSex(override val desc: String): OptionEnum<OptionSex> {
    MALE("Mężczyzna"),
    FEMALE("Kobieta")
}

enum class OptionMuscle(override val desc: String): OptionEnum<OptionMuscle> {
    QUADRICEPS_FEMORIS("Czworogłowy uda"),
    BICEPS_FEMORIS("Dwugłowy uda"),
    CALVES("Łydki"),
    ABS("Brzuch"),
    BACK("Plecy"),
    CHEST("Klatka"),
    SHOULDERS("Barki"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
}

enum class OptionEquipment(override val desc: String): OptionEnum<OptionEquipment> {
    MACHINE("Maszyna"),
    PULLUP_BAR("Drążek"),
    SQUAT_RACK("Stojak na sztangę"),
    BENCH("Łąwka"),
    DUMBBELL("Hantle"),
    BARBELL("Sztanga"),
    RAISED_PLATFORM("Platforma"),
    PARALLETS("Paraletki")
}

