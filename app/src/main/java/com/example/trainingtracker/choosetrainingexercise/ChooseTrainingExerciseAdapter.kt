package com.example.trainingtracker.choosetrainingexercise

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
import com.example.trainingtracker.exercise.ExerciseActivity

enum class ExerciseViewType(val id: Int) {
    TRAINING(0),
    CREATE(1)
}

class ChooseTrainingExerciseAdapter(
    private val items: List<TrainingExercise>,
    private val context: Context,
) : RecyclerView.Adapter<ChooseTrainingExerciseAdapter.ViewHolder>() {

    /**
     * Przypisanie interakcji do elementów
     */
    inner class ViewHolder(private var v: View, private var viewType: Int) :
        RecyclerView.ViewHolder(v) {

        @SuppressLint("SetTextI18n")
        fun bind(item: TrainingExercise) {
            when (viewType) {
                ExerciseViewType.TRAINING.ordinal -> {
                    v.findViewById<TextView>(R.id.name).text = item.name
                    v.findViewById<TextView>(R.id.planned_series).text = "Series: ${item.planned_series}"

                    v.setOnClickListener {
                        val intent = Intent(context, ExerciseActivity::class.java)
                        intent.putExtra("EXTRA_EXERCISE_ID", item.exercise_id)
                        intent.putExtra("EXTRA_TRAINING_EXERCISE_ID", item.id)
                        ContextCompat.startActivity(context, intent, null)
                    }
                }
                ExerciseViewType.CREATE.ordinal -> {}
                else -> return
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = when (viewType) {
            ExerciseViewType.TRAINING.ordinal -> R.layout.recycler_item_choose_training_exercise_training
            ExerciseViewType.CREATE.ordinal -> R.layout.recycler_item_choose_training_exercise_create
            else -> R.layout.recycler_item_empty
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return ExerciseViewType.TRAINING.ordinal
//        return when (position) {
//            items.size - 1 -> ViewType.CREATE.ordinal
//            else -> ViewType.TRAINING.ordinal
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}