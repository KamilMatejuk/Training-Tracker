package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools
import com.example.trainingtracker.adapters.VolumeGraphAdapter
import com.example.trainingtracker.databinding.ActivityExerciseBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.HistoryItem
import com.example.trainingtracker.dbconnection.items.SerieItem
import com.example.trainingtracker.fragments.*
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.Period


class ExerciseActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var exercise: ExerciseItem
    private lateinit var history: List<HistoryItem>
    private lateinit var bodyWeight: HashMap<LocalDate, Float>
    private val viewModelData: CalendarDataViewModel by viewModels()

    private var measureType: OptionMeasureType2 = OptionMeasureType2.REPS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        exercise = (intent.getSerializableExtra("EXTRA_EXERCISE") as? ExerciseItem)!!

        if (savedInstanceState == null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragmentSwitchMeasure = SwitchOptionsFragment.newInstance("tagMeasureType", enumValues<OptionMeasureType2>())
            fragmentTransaction.replace(R.id.switch_measure, fragmentSwitchMeasure, "tagMeasureType")
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            fragmentManager.setFragmentResultListener("tagMeasureType", fragmentSwitchMeasure) { _, bundle ->
                val result = bundle.getSerializable("key") as OptionMeasureType2
                measureType = result
                reloadVolume()
            }
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadExerciseHistory()
            }
        }

        binding.favourite.setOnClickListener {
            Thread {
                run {
                    val isOn = binding.favourite.tag == "on"
                    Log.d("STAR", isOn.toString())
                    Room.setExerciseFavourite(exercise.id!!, !isOn)
                    binding.favourite.setImageResource(if (isOn) R.drawable.star_off else R.drawable.star_on)
                    binding.favourite.tag = if (isOn) "off" else "on"
                }
            }.start()
        }

        binding.add.setOnClickListener {
            val intent = Intent(this, AddSerieActivity::class.java)
            intent.putExtra("EXTRA_EXERCISE", exercise)
            resultLauncher.launch(intent)
        }

        loadExerciseData()
        loadExerciseHistory()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun loadExerciseData() {
        Thread {
            run {
                val data = exercise.id?.let { Room.getExercise(it) }
                binding.name.text = data?.name
                binding.description.text = data?.description
                binding.muscles.text = data?.muscles?.joinToString(", ") { OptionMuscle.valueOf(it.name).desc }
                binding.equipment.text = data?.equipment?.joinToString(", ") { OptionEquipment.valueOf(it.name).desc }
                binding.favourite.setImageResource(
                    if (data?.favourite == true) R.drawable.star_on else R.drawable.star_off)
                binding.favourite.tag = if (data?.favourite == true) "on" else "off"
                data?.let { loadImagesAndVideos(data.images_link, data.video_link) }
            }
        }.start()
    }

    private fun loadImagesAndVideos(image_links: List<String>, video_link: String) {
        runOnUiThread {
            image_links.forEach { link ->
                val iv = ImageView(this)
                binding.technique.addView(iv)
                iv.adjustViewBounds = true
                Picasso.get()
                    .load(link)
                    .placeholder(R.drawable.placeholder_loading)
                    .error(R.drawable.placeholder_error)
                    .into(iv)
            }
            val vv = VideoView(this)
            vv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (resources.displayMetrics.widthPixels * 0.5625).toInt() // 16:9 aspect ratio
            )
            binding.technique.addView(vv)
            vv.setVideoURI(Uri.parse(video_link))
            vv.start()
        }
    }

    private fun loadExerciseHistory() {
        Thread {
            run {
                history = (exercise.id?.let { Room.getExerciseHistory(it) } ?: listOf()).sortedBy { it.date_latest_update }.reversed()
                bodyWeight = Room.getUser()?.weight ?: hashMapOf()
                runOnUiThread {
                    reloadFrequency()
                    reloadVolume()
                }
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun reloadFrequency() {
        val dates = history.map { LocalDate.of(it.date_start.year, it.date_start.month, it.date_start.dayOfMonth) }
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
                    OptionMeasureType2.REPS -> si.reps != null && si.weight != null
                    OptionMeasureType2.TIME -> si.time != null && si.weight != null
                }
            }
            hi
        }.filter { hi -> hi.series.isNotEmpty() }
        binding.volumeGraph.adapter = VolumeGraphAdapter(typeHistory, bodyWeight, this)
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
            OptionMeasureType2.REPS -> {
                binding.oneRepMaxLast1.visibility = View.VISIBLE
                binding.oneRepMaxLast5.visibility = View.VISIBLE
                binding.oneRepMaxLast1.text = "Oszacowany One-Rep-Max (last training): $oneRepMax1"
                binding.oneRepMaxLast5.text = "Oszacowany One-Rep-Max (last 5 trainings): $oneRepMax5"
            }
            OptionMeasureType2.TIME -> {
                binding.oneRepMaxLast1.visibility = View.GONE
                binding.oneRepMaxLast5.visibility = View.GONE
            }
        }
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