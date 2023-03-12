package com.example.trainingtracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import com.example.trainingtracker.activities.EditExerciseActivity
import com.example.trainingtracker.activities.ExerciseActivity
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import java.util.*


class SearchExerciseAdapter(
    private val items: List<ExerciseItem>,
    private val context: Context,
    private val search: String,
    private val resultLauncher: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<SearchExerciseAdapter.ViewHolder>() {

    inner class ViewHolder(private var v: View):
        RecyclerView.ViewHolder(v) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ExerciseItem) {
            if (search.isNotEmpty()) {
                val text = if (item.id == -1) "Create new: " + item.name else item.name
                val start = if (item.id == -1) 12 else 0
                val startIdx = text.indexOf(search, start, true)
                val endIdx = startIdx + search.length
                val spannable = SpannableString(text)
                val color = ContextCompat.getColor(context, R.color.mint)
                spannable.setSpan(ForegroundColorSpan(color), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                v.findViewById<TextView>(R.id.name).text = spannable
            } else {
                v.findViewById<TextView>(R.id.name).text = item.name
            }
            v.findViewById<ImageView>(R.id.favourite).visibility = if (item.favourite) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            v.setOnClickListener {
                val cls = if (item.id == -1) EditExerciseActivity::class.java else ExerciseActivity::class.java
                val intent = Intent(context, cls)
                intent.putExtra("EXTRA_EXERCISE", item)
                resultLauncher.launch(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = R.layout.recycler_item_search_exercise
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}