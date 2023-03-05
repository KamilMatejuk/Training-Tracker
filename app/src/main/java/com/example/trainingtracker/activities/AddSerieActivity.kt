package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.app.Activity
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
import com.example.trainingtracker.fragments.OptionMeasureType
import com.example.trainingtracker.fragments.SwitchOptionsFragment
import com.example.trainingtracker.fragments.OptionWeightType
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.floor

class AddSerieActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityAddSerieBinding
    private lateinit var exercise: ExerciseItem
    private lateinit var history: HistoryItem
    private var bodyWeight: Float = 0f

    private var measureType: OptionMeasureType = OptionMeasureType.REPS
    private var weightType: OptionWeightType = OptionWeightType.FREEWEIGHT

    @SuppressLint("SetTextI18n")
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
            val fragmentSwitchWeight = SwitchOptionsFragment.newInstance("tagWeightType", enumValues<OptionWeightType>())
            val fragmentSwitchMeasure = SwitchOptionsFragment.newInstance("tagMeasureType", enumValues<OptionMeasureType>())
            fragmentTransaction.replace(R.id.switch_weight, fragmentSwitchWeight, "tagWeightType")
            fragmentTransaction.replace(R.id.switch_measure, fragmentSwitchMeasure, "tagMeasureType")
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            fragmentManager.setFragmentResultListener("tagWeightType", fragmentSwitchWeight) { _, bundle ->
                val result = bundle.getSerializable("key") as OptionWeightType
                weightType = result
                when (result) {
                    OptionWeightType.FREEWEIGHT -> {
                        binding.weight.isEnabled = true
                        binding.weightTv.text = "Weight [kg]"
                    }
                    OptionWeightType.BODYWEIGHT -> {
                        if (bodyWeight <= 0) {
                            fragmentSwitchWeight.switchBtnOn(this, 0)
                            Toast.makeText(this, "Body weight is not set", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.weightTv.text = "Body weight [$bodyWeight kg]"
                        }
                        binding.weight.isEnabled = false
                        binding.weight.setText("")
                    }
                    OptionWeightType.WEIGHTED_BODYWEIGHT -> {
                        if (bodyWeight <= 0) {
                            fragmentSwitchWeight.switchBtnOn(this, 0)
                            Toast.makeText(this, "Body weight is not set", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.weightTv.text = "Body weight [$bodyWeight kg] + weight [kg]"
                            binding.weight.isEnabled = true
                        }
                    }
                }
            }

            fragmentManager.setFragmentResultListener("tagMeasureType", fragmentSwitchMeasure) { _, bundle ->
                val result = bundle.getSerializable("key") as OptionMeasureType
                measureType = result
                when (result) {
                    OptionMeasureType.REPS -> {
                        binding.repsLayout.visibility = View.VISIBLE
                        binding.timeLayout.visibility = View.GONE
                    }
                    OptionMeasureType.TIME -> {
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
        }.start()
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
            val weight = when (weightType) {
                OptionWeightType.FREEWEIGHT -> binding.weight.text.toString().toFloat()
                OptionWeightType.BODYWEIGHT -> bodyWeight
                OptionWeightType.WEIGHTED_BODYWEIGHT -> bodyWeight + binding.weight.text.toString()
                    .toFloat()
            }
            val time = if (measureType == OptionMeasureType.TIME) floor(
                binding.time.text.toString().toDouble()
            ).toInt() else null
            val reps = if (measureType == OptionMeasureType.REPS) floor(
                binding.reps.text.toString().toDouble()
            ).toInt() else null
            val warmup = binding.warmup.isChecked
            val item = SerieItem(null, time, weight, reps, "", warmup,
                when(weightType){
                    OptionWeightType.FREEWEIGHT -> WeightType.FREEWEIGHT
                    OptionWeightType.BODYWEIGHT -> WeightType.BODYWEIGHT
                    OptionWeightType.WEIGHTED_BODYWEIGHT -> WeightType.WEIGHTED_BODYWEIGHT
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
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }.start()
        } catch (ex: NumberFormatException){
            Toast.makeText(this, "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }
}
