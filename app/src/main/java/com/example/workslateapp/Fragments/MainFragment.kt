package com.example.workslateapp.Fragments

import CalendarDecorator
import DatabaseManeger
import SelectedDateShiftsListAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workslateapp.databinding.FragmentMainBinding
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }


    private fun initViews() {
        DatabaseManeger.getUserShifts(getCurrentWeekDays()) { currentWeekShifts ->
            binding.FragmentMainCalendar.addDecorator(CalendarDecorator(currentWeekShifts.map { it.first }, requireContext()))
        }
        binding.FragmentMainCalendar.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                // Fetch names of shifts for the selected date
                DatabaseManeger.getNamesOfShift(date.date) { shiftNames ->
                    // Update the title and the ListView with the shift data
                    binding.FragmentMainSelectedDateShifts.text = "Selected Date: ${date}"
                    binding.FragmentMainListOfWeekShifts.adapter = SelectedDateShiftsListAdapter(requireContext(), shiftNames)
                }
            }
        }
        // Set up date selection listener for the calendar
    }

    fun getCurrentWeekDays(): List<LocalDate> {
        val calendar = Calendar.getInstance()
        val weekDays = mutableListOf<LocalDate>()

        // Set the calendar to the start of the week (e.g., Sunday or Monday)
        calendar.firstDayOfWeek = Calendar.SUNDAY // Change to Calendar.MONDAY if needed
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        for (i in 0 until 7) { // Loop through 7 days
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1 // Months are 0-based
            val year = calendar.get(Calendar.YEAR)
            val date = LocalDate.of(year, month, dayOfMonth)
            weekDays.add(date)

            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
        }
        return weekDays
    }
}