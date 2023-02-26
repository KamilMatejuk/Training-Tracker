package com.example.trainingtracker.activities

import android.content.Intent
import android.os.Bundle
import com.example.trainingtracker.databinding.ActivityEditExerciseBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.ExerciseItem

class EditExerciseActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityEditExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val exercise = intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem

        binding.name.setText(exercise?.name)

        binding.save.setOnClickListener {
            Thread {
                run {
                    Room.addExercise(ExerciseItem(
                        null,
                        binding.name.text.toString(),
                        binding.description.text.toString(),
                        null,
                        null,
                        null))
                    val intent = Intent(this, ExerciseActivity::class.java)
                    intent.putExtra("EXTRA_EXERCISE", Room.getAllExerciseItems().last())
                    finish()
                    startActivity(intent)
                }
            }.start()
        }
    }
}
