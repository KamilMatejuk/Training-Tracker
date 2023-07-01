package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.adapters.SearchExerciseAdapter
import com.example.trainingtracker.databinding.ActivitySearchExerciseBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import java.util.*


class SearchExerciseActivity : ThemeChangingActivity() {

    private lateinit var binding: ActivitySearchExerciseBinding
    private lateinit var allExercises: List<ExerciseItem>
    private lateinit var searchedExercises: MutableList<ExerciseItem>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadExercises()
            }
        }

        loadExercises()

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { updateSearch(s.toString()) }
        })
    }

    private fun loadExercises() {
        Thread {
            run {
                allExercises = Room.getAllExerciseItems()
                searchedExercises = allExercises.toCollection(mutableListOf())
                if (searchedExercises.isNotEmpty()) {
                    this@SearchExerciseActivity.runOnUiThread {
                        binding.recyclerView.adapter = SearchExerciseAdapter(
                            favouriteFirst(searchedExercises), this, "", resultLauncher)
                        val dividerItemDecoration = DividerItemDecoration(
                            binding.recyclerView.context,
                            RecyclerView.VERTICAL
                        )
                        binding.recyclerView.addItemDecoration(dividerItemDecoration)
                    }
                }
            }
        }.start()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateSearch(search: String){
        searchedExercises.clear()
        if (search.isNotEmpty()) searchedExercises.add(
            ExerciseItem(-1, search, "", "", listOf(), "", listOf(), listOf()))
        searchedExercises.addAll(favouriteFirst(onlyMatching(allExercises.toMutableList(), search)))
        binding.recyclerView.adapter = SearchExerciseAdapter(
            searchedExercises, this, search, resultLauncher)
    }

    private fun favouriteFirst(lst: MutableList<ExerciseItem>): MutableList<ExerciseItem> {
        return (lst.filter { it.favourite } + lst.filter { !it.favourite }).toMutableList()
    }

    private fun onlyMatching(lst: MutableList<ExerciseItem>, search: String): MutableList<ExerciseItem> {
        return lst.filter {
            it.name.lowercase(Locale.getDefault()).contains(search.lowercase(Locale.getDefault()))
        }.toMutableList()
    }
}