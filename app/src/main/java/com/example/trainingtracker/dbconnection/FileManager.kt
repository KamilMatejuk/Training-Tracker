package com.example.trainingtracker.dbconnection

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.*


object FileManager {
    fun load(context: Context, uri: Uri) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String
                    var currType = ""
                    while (reader.readLine().also { line = it } != null) {
                        // read type
                        if (line.startsWith("TYPE")) {
                            currType = line.replace("TYPE", "").trim()
                            continue
                        }
                        // skip headers
                        when (currType) {
                            "EXERCISE" -> Converters.stringToExerciseItem(line)?.let { Room.addExercise(it) }
                            "HISTORY" -> Converters.stringToHistoryItem(line)?.let { Room.addHistoryItem(it) }
                            "USER" -> Converters.stringToUserItem(line)?.let { Room.addUser(it) }
                        }
                    }
                }
            }
        } catch (_: IOException) {} catch (_: NullPointerException) {}
    }

    fun save(context: Context, uri: Uri) {
        try {
            context.contentResolver.openFileDescriptor(uri, "w")?.use { fd ->
                FileOutputStream(fd.fileDescriptor).use { stream ->
                    val lines = arrayListOf<String>()
                    lines.add("TYPE USER")
                    val user = Room.getUser()
                    if (user != null) {
                        lines.add(Converters.userItemToString(user))
                    }
                    lines.add("TYPE EXERCISE")
                    Room.getAllExerciseItems().forEach {
                        lines.add(Converters.exerciseItemToString(it))
                    }
                    lines.add("TYPE HISTORY")
                    Room.getAllHistoryItems().forEach {
                        lines.add(Converters.historyItemToString(it))
                    }
                    stream.write(lines.joinToString("\n").toByteArray())
                }
            }
        } catch (_: IOException) {} catch (_: FileNotFoundException) {}
    }
}
