package com.example.trainingtracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools

class SwitchOptionsFragment<E: SwitchEnum<E>> : Fragment() {
    private lateinit var resultKey: String
    private lateinit var values: Array<E>
    private lateinit var btns: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            resultKey = requireArguments().getString("key") ?: ""
            values = requireArguments().getSerializable("values") as Array<E>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_switch_options, container, false)
        createButtons(view)
        return view
    }

    private fun createButtons(view: View) {
        if (!this::values.isInitialized) return
        btns = arrayOf()
        val layout = view.findViewById<LinearLayout>(R.id.layout)
        layout.removeAllViews()
        values.forEach {
            val btn = Button(context)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f)
            layoutParams.setMargins(0, 0, 0, 0)
            btn.setPadding(0, 0, 0, 0)
            btn.textSize = 10f
            btn.text = it.desc
            layout.addView(btn, layoutParams)
            btns += btn
        }
        switchBtnOn(requireContext(), 0)
        btns.forEachIndexed { i, btn ->
            btn.setOnClickListener {
                switchBtnOn(requireContext(), i)
                setFragmentResult(resultKey, bundleOf("key" to values[i]))
            }
        }
        layout.invalidate()
        layout.requestLayout()
    }

    fun switchBtnOn(context: Context, i: Int) {
        val colorAccent = ContextCompat.getColor(context, R.color.mint)
        val colorBg = Tools.colorFromAttr(context, R.attr.myBackgroundColor)
        if (!this::btns.isInitialized) return
        btns.forEachIndexed { j, btn ->
            if (i == j) {
                btn.setTextColor(colorBg)
                btn.setBackgroundColor(colorAccent)
            } else {
                btn.setTextColor(colorAccent)
                btn.setBackgroundColor(colorBg)
            }
        }
    }

    companion object {
        inline fun <reified E: SwitchEnum<E>> newInstance(
            resultKey: String,
            values: Array<E>
        ): SwitchOptionsFragment<E> {
            val f = SwitchOptionsFragment<E>()
            val args = Bundle()
            args.putString("key", resultKey)
            args.putSerializable("values", values)
            f.arguments = args
            return f
        }
    }

}