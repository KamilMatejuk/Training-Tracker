package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools
import com.example.trainingtracker.adapters.VolumeGraphAdapter
import com.example.trainingtracker.databinding.ActivityExerciseBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.HistoryItem
import com.example.trainingtracker.dbconnection.items.SerieItem
import com.example.trainingtracker.fragments.CalendarDataViewModel
import java.time.LocalDate
import java.time.Period


class ExerciseActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var exercise: ExerciseItem
    private lateinit var history: List<HistoryItem>
    private val viewModelData: CalendarDataViewModel by viewModels()

    private enum class MeasureType { REPS, TIME }
    private var measureType: MeasureType = MeasureType.REPS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        exercise = (intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem)!!

        binding.switchTypeReps.setOnClickListener { switchMeasureType(MeasureType.REPS) }
        binding.switchTypeTime.setOnClickListener { switchMeasureType(MeasureType.TIME) }
        switchMeasureType(measureType)

        binding.add.setOnClickListener {
            val intent = Intent(this, AddSerieActivity::class.java)
            intent.putExtra("EXTRA_EXERCISE", exercise)
            startActivity(intent)
        }

        loadExerciseData()
        loadExerciseHistory()
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    private fun loadExerciseData() {
        Thread {
            run {
                val data = exercise.id?.let { Room.getExercise(it) }
                binding.name.text = data?.name
                binding.description.text = data?.description
            }
        }.start()
    }

    private fun loadExerciseHistory() {
        Thread {
            run {
                history = (exercise.id?.let { Room.getExerciseHistory(it) } ?: listOf()).sortedBy { it.date }.reversed()
                reloadFrequency()
                reloadVolume()
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun reloadFrequency() {
        val dates = history.map { LocalDate.of(it.date.year, it.date.month, it.date.dayOfMonth) }
        viewModelData.setData(dates)
        val now = LocalDate.now()
        val dates7 = dates.filter { now.minus(Period.ofDays(7)) < it }.size
        val dates31 = dates.filter { now.minus(Period.ofDays(31)) < it }.size
        val dates365 = dates.filter { now.minus(Period.ofDays(365)) < it }.size
        binding.trainedWeek.text = "Trained in last 7 days: $dates7"
        binding.trainedMonth.text = "Trained in last 31 days: $dates31"
        binding.trainedYear.text = "Trained in last 365 days: $dates365"
    }

    @SuppressLint("SetTextI18n")
    private fun reloadVolume() {
        if (!this::history.isInitialized) return
        val typeHistory = Tools.deepcopy(history).map { hi ->
            hi.series = hi.series.filter { si ->
                when (measureType) {
                    MeasureType.REPS -> si.reps != null && si.weight != null
                    MeasureType.TIME -> si.time != null && si.weight != null
                }
            }
            hi
        }.filter { hi -> hi.series.isNotEmpty() }
        binding.volumeGraph.adapter = VolumeGraphAdapter(typeHistory, this)
        var oneRepMax1 = "?"
        if (typeHistory.size >= 1) {
            oneRepMax1 = "${oneRepMax(typeHistory[0].series)} kg"
        }
        var oneRepMax5 = "?"
        if (typeHistory.size >= 5) {
            val maxes = (0..4).map { i -> oneRepMax(typeHistory[i].series) }
            oneRepMax5 = "${maxes.maxOf { it }} kg"
        }
        when (measureType) {
            MeasureType.REPS -> {
                binding.oneRepMaxLast1.visibility = View.VISIBLE
                binding.oneRepMaxLast5.visibility = View.VISIBLE
                binding.oneRepMaxLast1.text = "Estimated One Rep Max (last training): $oneRepMax1"
                binding.oneRepMaxLast5.text = "Estimated One Rep Max (last 5 trainings): $oneRepMax5"
            }
            MeasureType.TIME -> {
                binding.oneRepMaxLast1.visibility = View.GONE
                binding.oneRepMaxLast5.visibility = View.GONE
            }
        }
    }

    private fun switchMeasureType(to: MeasureType) {
        measureType = to
        when (to) {
            MeasureType.REPS -> {
                Tools.switchBtn(this, binding.switchTypeReps, true)
                Tools.switchBtn(this, binding.switchTypeTime, false)
            }
            MeasureType.TIME -> {
                Tools.switchBtn(this, binding.switchTypeReps, false)
                Tools.switchBtn(this, binding.switchTypeTime, true)
            }
        }
        reloadVolume()
    }

    private fun oneRepMax(items: List<SerieItem>): Float {
        // The Brzycki Equation
        return items.map {
            if (it.reps != null && it.weight != null) {
                it.weight!! / (1.0278f - (0.0278f * it.reps!!))
            } else 0f
        }.maxOf { it }
    }
}
