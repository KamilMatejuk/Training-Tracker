package com.example.trainingtracker.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools
import com.example.trainingtracker.databinding.ActivityAddSerieBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.HistoryItem
import com.example.trainingtracker.dbconnection.items.SerieItem
import com.example.trainingtracker.dbconnection.items.WeightType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.floor

class AddSerieActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityAddSerieBinding
    private lateinit var exercise: ExerciseItem
    private lateinit var history: HistoryItem
    private var bodyWeight: Float = 0f

    private enum class MeasureType { REPS, TIME }
    private var measureType: MeasureType = MeasureType.REPS
    private var weightType: WeightType = WeightType.FREEWEIGHT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSerieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        exercise = (intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem)!!

        binding.switchTypeReps.setOnClickListener { switchMeasureType(MeasureType.REPS) }
        binding.switchTypeTime.setOnClickListener { switchMeasureType(MeasureType.TIME) }
        switchMeasureType(measureType)

        binding.switchWeightBody.setOnClickListener { switchWeightType(WeightType.BODYWEIGHT) }
        binding.switchWeightFree.setOnClickListener { switchWeightType(WeightType.FREEWEIGHT) }
        binding.switchWeightBoth.setOnClickListener { switchWeightType(WeightType.WEIGHTED_BODYWEIGHT) }
        switchWeightType(weightType)

        binding.save.setOnClickListener { save() }
        getBodyWeight()
        getLastHistoryItem()
    }



    private fun switchMeasureType(to: MeasureType) {
        measureType = to
        when (to) {
            MeasureType.REPS -> {
                // switch buttons
                Tools.switchBtn(this, binding.switchTypeReps, true)
                Tools.switchBtn(this, binding.switchTypeTime, false)
                // options
                binding.repsLayout.visibility = View.VISIBLE
                binding.timeLayout.visibility = View.GONE
            }
            MeasureType.TIME -> {
                // switch buttons
                Tools.switchBtn(this, binding.switchTypeReps, false)
                Tools.switchBtn(this, binding.switchTypeTime, true)
                // options
                binding.repsLayout.visibility = View.GONE
                binding.timeLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun switchWeightType(to: WeightType) {
        weightType = to
        when (to) {
            WeightType.FREEWEIGHT -> {
                // switch buttons
                Tools.switchBtn(this, binding.switchWeightFree, true)
                Tools.switchBtn(this, binding.switchWeightBody, false)
                Tools.switchBtn(this, binding.switchWeightBoth, false)
                // options
                binding.weight.isEnabled = true
            }
            WeightType.BODYWEIGHT -> {
                if (bodyWeight <= 0) {
                    Toast.makeText(this, "Body weight is not set", Toast.LENGTH_SHORT).show()
                    return
                }
                // switch buttons
                Tools.switchBtn(this, binding.switchWeightFree, false)
                Tools.switchBtn(this, binding.switchWeightBody, true)
                Tools.switchBtn(this, binding.switchWeightBoth, false)
                // options
                binding.weight.isEnabled = false
                binding.weight.setText("")
            }
            WeightType.WEIGHTED_BODYWEIGHT -> {
                if (bodyWeight <= 0) {
                    Toast.makeText(this, "Body weight is not set", Toast.LENGTH_SHORT).show()
                    return
                }
                // switch buttons
                Tools.switchBtn(this, binding.switchWeightFree, false)
                Tools.switchBtn(this, binding.switchWeightBody, false)
                Tools.switchBtn(this, binding.switchWeightBoth, true)
                // options
                binding.weight.isEnabled = true
            }
        }
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
                WeightType.FREEWEIGHT -> binding.weight.text.toString().toFloat()
                WeightType.BODYWEIGHT -> bodyWeight
                WeightType.WEIGHTED_BODYWEIGHT -> bodyWeight + binding.weight.text.toString()
                    .toFloat()
            }
            val time = if (measureType == MeasureType.TIME) floor(
                binding.time.text.toString().toDouble()
            ).toInt() else null
            val reps = if (measureType == MeasureType.REPS) floor(
                binding.reps.text.toString().toDouble()
            ).toInt() else null
            val warmup = binding.warmup.isChecked
            val item = SerieItem(null, time, weight, reps, null, warmup, weightType)
            Log.d("ITEM", item.toString())

            val now = LocalDateTime.now()
            Thread {
                run {
                    if (history.date.until(now, ChronoUnit.MINUTES) < 15) {
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
