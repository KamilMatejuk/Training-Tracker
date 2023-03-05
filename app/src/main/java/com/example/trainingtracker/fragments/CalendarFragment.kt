package com.example.trainingtracker.fragments

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools
import com.example.trainingtracker.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import kotlin.properties.Delegates

class CalendarDataViewModel : ViewModel() {
    private val mutableData = MutableLiveData<List<LocalDate>>()
    val data: LiveData<List<LocalDate>> get() = mutableData

    fun setData(item: List<LocalDate>) {
        mutableData.postValue(item)
    }
}

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding

    private val viewModelData: CalendarDataViewModel by activityViewModels()

    private var colorAccent by Delegates.notNull<Int>()
    private var showingDate: LocalDate = LocalDate.now()
    private var data: List<LocalDate>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCalendarBinding.inflate(layoutInflater)

        showingDate = LocalDate.now()
        colorAccent = context?.let { ContextCompat.getColor(it, R.color.mint) }!!

        binding.btnNext.setOnClickListener { nextMonth() }
        binding.btnPrev.setOnClickListener { prevMonth() }

        viewModelData.data.observe(this) {
            data = it
            showingDate = LocalDate.now()
            reload()
        }
        reload()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    fun prevMonth() {
        showingDate = showingDate.minusMonths(1)
        reload()
    }

    fun nextMonth() {
        showingDate = showingDate.plusMonths(1)
        reload()
    }

    fun reload() {
        // usunięcie poprzedniego kalendarza
        binding.calendar.removeAllViews()
        // aktualizacja nazwy miesiąca
        binding.monthName.text = showingDate.month.name
        // stworzenie listy dni do wyświetlenia
        val maxDays = YearMonth.of(showingDate.year, showingDate.month).lengthOfMonth()
        val firstDay = YearMonth.of(showingDate.year, showingDate.month).atDay(1)
        val shift = firstDay.dayOfWeek.value - 1
        val daysArray = IntArray(shift + 7) { 0 } +
                        IntArray(maxDays) { i -> i + 1 } +
                        IntArray(6 * 7 - maxDays - shift) { 0 }
        val daysRows = daysArray.withIndex()
                                .groupBy { it.index / 7 }
                                .map { it -> it.value.map { it.value } }

        // dodanie wierszy do TableLayout
        daysRows.forEach { row ->
            // stworzenie wiersza
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, // width
                TableRow.LayoutParams.MATCH_PARENT // height
            )

            // dodanie przysicku na początku każdego wiersza
            // aby utrzymać poprawny format kalendarza
            val btn0 = Button(context)
            btn0.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
            btn0.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
            btn0.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.0F)
            tableRow.addView(btn0)

            // dodanie przycisków
            for (day in row) {
                val btn = Button(context)
                if (day != 0) {
                    btn.setTextColor(Tools.colorFromAttr(requireContext(), R.attr.myForegroundColor))
                    btn.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
                    val date = LocalDate.of(showingDate.year, showingDate.month, day)
                    if (data != null && data!!.contains(date)) {
                        val drawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.calendar_btn)
                        drawable!!.colorFilter = BlendModeColorFilter(colorAccent, BlendMode.SRC_ATOP)
                        btn.background = drawable
                    }
                } else {
                    btn.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
                    btn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
                }
                btn.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                   TableRow.LayoutParams.MATCH_PARENT, 1.0F)
                btn.text = day.toString()
                btn.tag = day
                tableRow.addView(btn)
            }
            binding.calendar.addView(tableRow)
        }
    }
}