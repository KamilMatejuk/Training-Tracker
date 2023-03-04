package com.example.trainingtracker.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.trainingtracker.R
import com.example.trainingtracker.databinding.ActivityAddSerieBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.HistoryItem
import com.example.trainingtracker.dbconnection.items.SerieItem
import com.example.trainingtracker.dbconnection.items.WeightType
import com.example.trainingtracker.fragments.SwitchMeasureType
import com.example.trainingtracker.fragments.SwitchOptionsFragment
import com.example.trainingtracker.fragments.SwitchWeightType
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.floor

class AddSerieActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityAddSerieBinding
    private lateinit var exercise: ExerciseItem
    private lateinit var history: HistoryItem
    private var bodyWeight: Float = 0f

    private var measureType: SwitchMeasureType = SwitchMeasureType.REPS
    private var weightType: SwitchWeightType = SwitchWeightType.FREEWEIGHT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSerieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        exercise = (intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem)!!

        binding.save.setOnClickListener { save() }
        getBodyWeight()
        getLastHistoryItem()

        if (savedInstanceState == null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragmentSwitchWeight = SwitchOptionsFragment.newInstance("tagWeightType", enumValues<SwitchWeightType>())
            val fragmentSwitchMeasure = SwitchOptionsFragment.newInstance("tagMeasureType", enumValues<SwitchMeasureType>())
            fragmentTransaction.replace(R.id.switch_weight, fragmentSwitchWeight, "tagWeightType")
            fragmentTransaction.replace(R.id.switch_measure, fragmentSwitchMeasure, "tagMeasureType")
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit()

            fragmentManager.setFragmentResultListener("tagWeightType", fragmentSwitchWeight) { _, bundle ->
                val result = bundle.getSerializable("key") as SwitchWeightType
                weightType = result
                when (result) {
                    SwitchWeightType.FREEWEIGHT -> {
                        binding.weight.isEnabled = true
                    }
                    SwitchWeightType.BODYWEIGHT -> {
                        binding.weight.isEnabled = false
                        binding.weight.setText("")
                    }
                    SwitchWeightType.WEIGHTED_BODYWEIGHT -> {
                        binding.weight.isEnabled = true
                    }
                }
            }

            fragmentManager.setFragmentResultListener("tagMeasureType", fragmentSwitchMeasure) { _, bundle ->
                val result = bundle.getSerializable("key") as SwitchMeasureType
                measureType = result
                when (result) {
                    SwitchMeasureType.REPS -> {
                        binding.repsLayout.visibility = View.VISIBLE
                        binding.timeLayout.visibility = View.GONE
                    }
                    SwitchMeasureType.TIME -> {
                        binding.repsLayout.visibility = View.GONE
                        binding.timeLayout.visibility = View.VISIBLE
                    }
                }
            }
            binding.repsLayout.visibility = View.VISIBLE
            binding.timeLayout.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun getBodyWeight() {
        Thread {
            run {
                val user = Room.getUser()
                bodyWeight = user?.weight_values?.lastOrNull() ?: 0f
            }
        }
    }

    private fun getLastHistoryItem() {
        Thread {
            run {
                history = try {
                    (exercise.id?.let { Room.getExerciseHistory(it) } ?: listOf())
                        .sortedBy { it.date }
                        .reversed()
                        .first()
                } catch (ex: NoSuchElementException) {
                    HistoryItem(null, exercise.id!!, LocalDateTime.now(), "", listOf())
                }
            }
        }.start()
    }

    private fun save() {
        if (!this::history.isInitialized || !this::exercise.isInitialized) {
            Toast.makeText(this, "Failed saving", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        try {
            if (bodyWeight <= 0 && (weightType == SwitchWeightType.BODYWEIGHT || weightType == SwitchWeightType.WEIGHTED_BODYWEIGHT)) {
                Toast.makeText(this, "Body weight is not set", Toast.LENGTH_SHORT).show()
                return
            }
            val weight = when (weightType) {
                SwitchWeightType.FREEWEIGHT -> binding.weight.text.toString().toFloat()
                SwitchWeightType.BODYWEIGHT -> bodyWeight
                SwitchWeightType.WEIGHTED_BODYWEIGHT -> bodyWeight + binding.weight.text.toString()
                    .toFloat()
            }
            val time = if (measureType == SwitchMeasureType.TIME) floor(
                binding.time.text.toString().toDouble()
            ).toInt() else null
            val reps = if (measureType == SwitchMeasureType.REPS) floor(
                binding.reps.text.toString().toDouble()
            ).toInt() else null
            val warmup = binding.warmup.isChecked
            val item = SerieItem(null, time, weight, reps, "", warmup,
                when(weightType){
                    SwitchWeightType.FREEWEIGHT -> WeightType.FREEWEIGHT
                    SwitchWeightType.BODYWEIGHT -> WeightType.BODYWEIGHT
                    SwitchWeightType.WEIGHTED_BODYWEIGHT -> WeightType.WEIGHTED_BODYWEIGHT
                })

            val now = LocalDateTime.now()
            Thread {
                run {
                    if (history.id != null && history.date.until(now, ChronoUnit.MINUTES) < 15) {
                        // add to existing
                        history.series = (history.series.toMutableList() + item).toList()
                        Room.updateHistoryItemSeries(history)
                    } else {
                        // create new
                        val hi = HistoryItem(null, exercise.id!!, now, "", listOf(item))
                        Room.addHistoryItem(hi)
                    }
                    finish()
                }
            }.start()
        } catch (ex: NumberFormatException){
            Toast.makeText(this, "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }
}
