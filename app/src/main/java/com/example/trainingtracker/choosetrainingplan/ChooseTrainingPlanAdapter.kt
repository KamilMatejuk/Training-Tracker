package com.example.trainingtracker.choosetrainingplan

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
import com.example.trainingtracker.choosetrainingday.ChooseTrainingDayActivity

enum class PlanViewType(val id: Int) {
    TRAINING(0),
    CREATE(1)
}

class ChooseTrainingPlanAdapter(
    private val items: List<TrainingPlan>,
    private val context: Context,
) : RecyclerView.Adapter<ChooseTrainingPlanAdapter.ViewHolder>() {

    /**
     * Przypisanie interakcji do elementów
     */
    inner class ViewHolder(private var v: View, private var viewType: Int) :
        RecyclerView.ViewHolder(v) {

        @SuppressLint("SetTextI18n")
        fun bind(item: TrainingPlan) {
            when (viewType) {
                PlanViewType.TRAINING.ordinal -> {
                    v.findViewById<TextView>(R.id.name).text = item.name
                    if (item.last_trained == null)
                        v.findViewById<TextView>(R.id.last_activity).text = "No activity history"
                    else
                        v.findViewById<TextView>(R.id.last_activity).text = "Last trained:\n${item.last_trained}"

                    v.setOnClickListener {
                        val intent = Intent(context, ChooseTrainingDayActivity::class.java)
                        intent.putExtra("EXTRA_TRAINING_PLAN_ID", item.id)
                        ContextCompat.startActivity(context, intent, null)
                    }
                }
                PlanViewType.CREATE.ordinal -> {}
                else -> return
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = when (viewType) {
            PlanViewType.TRAINING.ordinal -> R.layout.recycler_item_choose_training_plan_training
            PlanViewType.CREATE.ordinal -> R.layout.recycler_item_choose_training_plan_create
            else -> R.layout.recycler_item_empty
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return PlanViewType.TRAINING.ordinal
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