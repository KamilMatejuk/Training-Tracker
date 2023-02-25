package com.example.trainingtracker.choosetrainingday

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import com.example.trainingtracker.choosetrainingexercise.ChooseTrainingExerciseActivity
import com.example.trainingtracker.dbconnection.Room

enum class DayViewType(val id: Int) {
    TRAINING(0),
    CREATE(1)
}

class ChooseTrainingDayAdapter(
    private val items: List<TrainingDay>,
    private val context: Context,
) : RecyclerView.Adapter<ChooseTrainingDayAdapter.ViewHolder>() {

    /**
     * Przypisanie interakcji do elementów
     */
    inner class ViewHolder(private var v: View, private var viewType: Int) :
        RecyclerView.ViewHolder(v) {

        @SuppressLint("SetTextI18n")
        fun bind(item: TrainingDay) {
            when (viewType) {
                DayViewType.TRAINING.ordinal -> {
                    v.findViewById<TextView>(R.id.name).text = item.name
                    if (item.last_trained == null)
                        v.findViewById<TextView>(R.id.last_activity).text = "\nNo activity history"
                    else
                        v.findViewById<TextView>(R.id.last_activity).text = "Last trained:\n${item.last_trained}"

                    v.setOnClickListener {
                        val intent = Intent(context, ChooseTrainingExerciseActivity::class.java)
                        intent.putExtra("EXTRA_TRAINING_EXERCISE_ID", item.id)
                        ContextCompat.startActivity(context, intent, null)
                    }
                }
                DayViewType.CREATE.ordinal -> {}
                else -> return
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseTrainingDayAdapter.ViewHolder {
        val layout = when (viewType) {
            DayViewType.TRAINING.ordinal -> R.layout.recycler_item_choose_training_day_training
            DayViewType.CREATE.ordinal -> R.layout.recycler_item_choose_training_day_create
            else -> R.layout.recycler_item_empty
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return DayViewType.TRAINING.ordinal
//        return when (position) {
//            items.size - 1 -> ViewType.CREATE.ordinal
//            else -> ViewType.TRAINING.ordinal
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChooseTrainingDayAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}