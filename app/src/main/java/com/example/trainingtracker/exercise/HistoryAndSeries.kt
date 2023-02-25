package com.example.trainingtracker.exercise

import androidx.room.Embedded
import androidx.room.Relation
import com.example.trainingtracker.dbconnection.items.HistoryItem
import com.example.trainingtracker.dbconnection.items.SerieItem

data class HistoryAndSeries(
    @Embedded
    val history: HistoryItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "history_id"
    )
    val series: List<SerieItem>
)