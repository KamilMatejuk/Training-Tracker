package com.example.trainingtracker.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import com.example.trainingtracker.dbconnection.items.HistoryItem
import com.example.trainingtracker.dbconnection.items.WeightType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap


class VolumeGraphAdapter(
    private val items: List<HistoryItem>,
    private val bodyWeights: HashMap<LocalDate, Float>,
    private val context: Context
) : RecyclerView.Adapter<VolumeGraphAdapter.ViewHolder>() {

    private var maxVolume: Float = 0f

    inner class ViewHolder(private var v: View):
        RecyclerView.ViewHolder(v) {

        @SuppressLint("SetTextI18n")
        fun bind(item: HistoryItem) {
            v.findViewById<TextView>(R.id.date).text = item.date_start.format(DateTimeFormatter.ofPattern("dd.MM"))
            v.findViewById<ImageView>(R.id.img).setImageBitmap(getGraphImg(item))
            v.setOnClickListener {
                // todo show popup with more data
                val date = item.date_start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                val bw = getBodyWeight(item.date_start.toLocalDate())
                val series = item.series.map {
                    if (it.weight == null) return@map ""
                    val weight = when (it.weight_type) {
                        WeightType.FREEWEIGHT -> "${it.weight} kg"
                        WeightType.BODYWEIGHT -> "BW $bw kg" // todo
                        WeightType.WEIGHTED_BODYWEIGHT -> "BW $bw + ${it.weight} kg" // todo
                    }
                    if (it.reps != null) {
                        "$weight x ${it.reps} reps"
                    } else if (it.time != null) {
                        "$weight x ${it.time} s"
                    } else ""
                }.filter { it.isNotEmpty() }.joinToString("\n")
                val total = getVolumes(item).sum()
                val text = "On $date did:\n$series\nTotal volume $total kg"
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = R.layout.recycler_item_volume_graph
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        maxVolume = items.maxOfOrNull { getVolumes(it).sum() } ?: 0.0f
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun getVolumes(item: HistoryItem): List<Float> {
        // todo
        return item.series.map {
            if (it.reps != null && it.weight != null) {
                it.reps!! * it.weight!!
            } else if (it.time != null && it.weight != null) {
                it.time!! * it.weight!!
            } else 0.0f
        }
    }

    fun getBodyWeight(date: LocalDate): Float {
        val dates = bodyWeights.keys.sorted()
        for (i in 1 until dates.size) {
            if (dates[i-1] <= date && date < dates[i]) {
                return bodyWeights[dates[i-1]] ?: 0f
            }
        }
        return bodyWeights[dates.lastOrNull() ?: LocalDate.now()] ?: 0f
    }

    fun getGraphImg(item: HistoryItem): Bitmap? {
        val volumes = getVolumes(item)
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels * 0.8f).toInt()
        val height = (width * 0.02).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.mint)
        var startPx = 0f
        val allVolume = items.map { getVolumes(it).sum() }.sum()
        volumes.forEach { vol ->
            val w = width * vol / maxVolume
            val gap = allVolume * 0.005f
            canvas.drawRect(startPx, 0f, startPx + w - gap, height.toFloat(), paint)
            startPx += w
        }
        return bitmap
    }
}