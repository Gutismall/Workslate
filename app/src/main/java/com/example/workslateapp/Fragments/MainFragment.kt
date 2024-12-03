package com.example.workslateapp.Fragments

import CalendarDecorator
import DatabaseManeger
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workslateapp.databinding.FragmentMainBinding
import java.time.LocalDate
import java.time.ZoneId

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var currentMonthShifts = emptyList<LocalDate>()

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
            DatabaseManeger.getUserShifts(LocalDate.now()){ listOfDates->
                if (currentMonthShifts != listOfDates)
                    currentMonthShifts = listOfDates
                    binding.FragmentMainCalendar.addDecorator(CalendarDecorator(currentMonthShifts,requireContext()))
            }
        binding.FragmentMainLLShiftCards.visibility = View.INVISIBLE
        binding.FragmentMainCalendar.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                DatabaseManeger.getArrangementByDate(date.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) { shiftNames ->
                    if (shiftNames.isNotEmpty()) {
                        binding.FragmentMainLLShiftCards.visibility = View.VISIBLE
                        binding.FragmentMainSelectedDateShifts.text = "Selected Date: ${date.day}/${date.month}/${date.year}"
                        binding.FragmentMainMorningShiftCardName.text = "Morning Shift: ${shiftNames[0]}"
                        binding.FragmentMainAfternoonShiftCardName.text = "Afternoon Shift: ${shiftNames[1]}"
                        binding.FragmentMainNightShiftCardName.text = "Night Shift: ${shiftNames[2]}"
                    } else {
                        binding.FragmentMainLLShiftCards.visibility = View.INVISIBLE
                    }
                }
            }
        }
        binding.FragmentMainCalendar.setOnMonthChangedListener { widget, date ->
            DatabaseManeger.getUserShifts(LocalDate.of(date.year,date.month,date.day)){ listOfDates->
                if (currentMonthShifts != listOfDates)
                    currentMonthShifts = listOfDates
                binding.FragmentMainCalendar.addDecorator(CalendarDecorator(currentMonthShifts,requireContext()))
            }
        }
    }
}