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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import com.example.trainingtracker.activities.ExerciseActivity
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import java.util.*


class SearchExerciseAdapter(
    private val items: List<ExerciseItem>,
    private val context: Context,
    private val search: String
) : RecyclerView.Adapter<SearchExerciseAdapter.ViewHolder>() {

    inner class ViewHolder(private var v: View):
        RecyclerView.ViewHolder(v) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ExerciseItem) {
            if (search.isNotEmpty()) {
                val startIdx = item.name.lowercase(Locale.getDefault()).indexOf(search)
                val endIdx = startIdx + search.length
                val spannable = SpannableString(item.name)
                val color = ContextCompat.getColor(context, R.color.mint)
                spannable.setSpan(ForegroundColorSpan(color), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                v.findViewById<TextView>(R.id.name).text = spannable
            } else {
                v.findViewById<TextView>(R.id.name).text = item.name
            }
            v.setOnClickListener {
                val intent = Intent(context, ExerciseActivity::class.java)
                intent.putExtra("EXTRA_EXERCISE", item)
                ContextCompat.startActivity(context, intent, null)
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