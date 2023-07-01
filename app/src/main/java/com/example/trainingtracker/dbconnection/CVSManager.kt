package com.example.trainingtracker.dbconnection

import android.content.Context
import android.util.Log
import com.example.trainingtracker.R
import com.example.trainingtracker.dbconnection.items.Equipment
import com.example.trainingtracker.dbconnection.items.ExerciseItem
import com.example.trainingtracker.dbconnection.items.Muscle
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import java.io.*
import java.nio.charset.Charset
import java.util.concurrent.Callable


object CVSManager {
    private fun loadFromCVS(inputStream: InputStream, callback: (line: Array<String>) -> Unit) {
        try {
            val reader = CSVReaderBuilder(BufferedReader(
                InputStreamReader(inputStream, Charset.forName("UTF-8"))
            )).withSkipLines(1).build()
            var line: Array<String>
            while (reader.readNext().also { line = it } != null) {
                callback(line)
            }
        } catch (e: IOException) {
            Log.e("CVS Manager", "Failed loading", e)
        }
        catch (_: NullPointerException) {}
    }

    fun loadExercises(context: Context, db: DAO) {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.exercises_default_fabrykasily)
        loadFromCVS(inputStream) {
            db.insertExerciseItem(ExerciseItem(null,
                it[0], it[5], it[5], it[4].split('|'), it[2],
                listOf(Muscle.valueOf(it[1])), listOf()
                )
            )
        }
    }
}