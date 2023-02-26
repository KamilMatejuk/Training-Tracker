package com.example.trainingtracker

import android.content.Context
import android.util.TypedValue
import com.example.trainingtracker.dbconnection.items.HistoryItem

object Tools {

    fun colorFromAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    fun deepcopy(items: List<HistoryItem>): List<HistoryItem> {
        return items.map { hi ->
            val item = hi.copy(series = hi.series.map { it.copy() })
            item
        }
    }
}