package com.example.trainingtracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools

class CheckboxOptionsFragment<E: OptionEnum<E>> : Fragment() {
    private lateinit var resultKey: String
    private lateinit var values: Array<E>
    private lateinit var btns: Array<CheckBox>
    private lateinit var selected: List<E>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            resultKey = requireArguments().getString("key") ?: ""
            values = requireArguments().getSerializable("values") as Array<E>
        }
        selected = listOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkbox_options, container, false)
        createButtons(view)
        return view
    }

    private fun createButtons(view: View) {
        if (!this::values.isInitialized) return
        btns = arrayOf()
        val layout = view.findViewById<LinearLayout>(R.id.layout)
        layout.removeAllViews()
        values.forEach {
            val btn = CheckBox(context)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, 0, 0)
            btn.setPadding(0, 0, 0, 0)
            btn.text = it.desc
            btn.setTextColor(Tools.colorFromAttr(requireContext(), R.attr.myForegroundColor))
            layout.addView(btn, layoutParams)
            btns += btn
        }
        btns.forEachIndexed { i, btn ->
            btn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selected = selected + values[i]
                } else {
                    selected = selected.toMutableList().filter { it != values[i] }.toList()
                }
                setFragmentResult(resultKey, bundleOf("key" to selected))
            }
        }
        layout.invalidate()
        layout.requestLayout()
    }

    companion object {
        inline fun <reified E: OptionEnum<E>> newInstance(
            resultKey: String,
            values: Array<E>
        ): CheckboxOptionsFragment<E> {
            val f = CheckboxOptionsFragment<E>()
            val args = Bundle()
            args.putString("key", resultKey)
            args.putSerializable("values", values)
            f.arguments = args
            return f
        }
    }

}