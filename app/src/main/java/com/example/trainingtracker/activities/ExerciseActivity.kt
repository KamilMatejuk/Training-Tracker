package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.opengl.Visibility
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
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.Period

class ExerciseActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var exercise: ExerciseItem
    private lateinit var history: List<HistoryItem>
    private val viewModelData: CalendarDataViewModel by viewModels()
    private var type: String = "reps"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        exercise = (intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem)!!

        binding.switchTypeReps.setOnClickListener { switchType("reps") }
        binding.switchTypeTime.setOnClickListener { switchType("time") }
        switchType("reps")

        binding.add.setOnClickListener { view ->
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }

        loadExerciseData()
        loadExerciseHistory()
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
                if (type == "reps") si.reps != null && si.weight != null
                else si.time != null && si.weight != null
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
            val maxes = (1..5).map { i -> oneRepMax(typeHistory[i].series) }
            oneRepMax5 = "${maxes.maxOf { it }} kg"
        }
        if (type == "reps") {
            binding.oneRepMaxLast1.visibility = View.VISIBLE
            binding.oneRepMaxLast5.visibility = View.VISIBLE
            binding.oneRepMaxLast1.text = "Estimated One Rep Max (last training): $oneRepMax1"
            binding.oneRepMaxLast5.text = "Estimated One Rep Max (last 5 trainings): $oneRepMax5"
        } else {
            binding.oneRepMaxLast1.visibility = View.GONE
            binding.oneRepMaxLast5.visibility = View.GONE
        }
    }

    private fun switchType(to: String) {
        val colorAccent = ContextCompat.getColor(this, R.color.mint)
        val colorBg = Tools.colorFromAttr(this, R.attr.myBackgroundColor)
        if (to == "reps") {
            binding.switchTypeReps.setTextColor(colorBg)
            binding.switchTypeReps.setBackgroundColor(colorAccent)
            binding.switchTypeTime.setTextColor(colorAccent)
            binding.switchTypeTime.setBackgroundColor(colorBg)
            type = "reps"
        } else {
            binding.switchTypeReps.setTextColor(colorAccent)
            binding.switchTypeReps.setBackgroundColor(colorBg)
            binding.switchTypeTime.setTextColor(colorBg)
            binding.switchTypeTime.setBackgroundColor(colorAccent)
            type = "time"
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
