package com.example.trainingtracker.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.trainingtracker.R
import com.example.trainingtracker.databinding.ActivityEditExerciseBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.Equipment
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.Muscle
import com.example.trainingtracker.fragments.*

class EditExerciseActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityEditExerciseBinding
    private lateinit var selectedMuscles: List<Muscle>
    private lateinit var selectedEquipment: List<Equipment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val exercise = intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem

        binding.name.setText(exercise?.name)
        binding.save.setOnClickListener { save() }

        if (savedInstanceState == null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragmentCheckboxMuscle = CheckboxOptionsFragment.newInstance("tagMuscles", enumValues<OptionMuscle>())
            val fragmentCheckboxEquipment = CheckboxOptionsFragment.newInstance("tagEquipment", enumValues<OptionEquipment>())
            fragmentTransaction.replace(R.id.checkbox_muscles, fragmentCheckboxMuscle, "tagMuscles")
            fragmentTransaction.replace(R.id.checkbox_equipment, fragmentCheckboxEquipment, "tagEquipment")
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit()
            fragmentManager.setFragmentResultListener("tagMuscles", fragmentCheckboxMuscle) { _, bundle ->
                selectedMuscles = (bundle.getSerializable("key") as List<OptionMuscle>).map { when (it) {
                    OptionMuscle.LEGS -> Muscle.LEGS
                    OptionMuscle.ABS -> Muscle.ABS
                    OptionMuscle.BACK -> Muscle.BACK
                    OptionMuscle.CHEST -> Muscle.CHEST
                    OptionMuscle.SHOULDERS -> Muscle.SHOULDERS
                    OptionMuscle.ARMS -> Muscle.ARMS
                }}
            }
            fragmentManager.setFragmentResultListener("tagEquipment", fragmentCheckboxEquipment) { _, bundle ->
                selectedEquipment = (bundle.getSerializable("key") as List<OptionEquipment>).map { when (it) {
                    OptionEquipment.MACHINE -> Equipment.MACHINE
                    OptionEquipment.PULLUP_BAR -> Equipment.PULLUP_BAR
                    OptionEquipment.SQUAT_RACK -> Equipment.SQUAT_RACK
                    OptionEquipment.BENCH -> Equipment.BENCH
                    OptionEquipment.DUMBBELL -> Equipment.DUMBBELL
                    OptionEquipment.BARBELL -> Equipment.BARBELL
                    OptionEquipment.RAISED_PLATFORM -> Equipment.RAISED_PLATFORM
                    OptionEquipment.PARALLETS -> Equipment.PARALLETS
                }}
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun save() {
        Thread {
            run {
                Room.addExercise(ExerciseItem(
                    null,
                    binding.name.text.toString(),
                    binding.description.text.toString(),
                    "",
                    selectedMuscles,
                    selectedEquipment
                ))
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra("EXTRA_EXERCISE", Room.getAllExerciseItems().last())
                finish()
                startActivity(intent)
            }
        }.start()
    }
}
