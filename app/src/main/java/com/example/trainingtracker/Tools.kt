package com.example.trainingtracker

import android.content.Context
import android.util.TypedValue

object Tools {

    fun colorFromAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}