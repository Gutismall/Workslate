package com.example.workslateapp.Fragments

import CurrentWeekListAdapter
import SelectedDateShiftsListAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.transition.Visibility
import com.example.workslateapp.R
import com.example.workslateapp.databinding.FragmentCalendarBinding
import com.example.workslateapp.databinding.FragmentSignUpBinding
import com.google.android.material.textview.MaterialTextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Date


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        val currentDate = LocalDate.now().dayOfWeek
        if ( currentDate >= DayOfWeek.WEDNESDAY  ){
            binding.FragmentCalendarConstraintBox.visibility = View.INVISIBLE
        }
        else{
            binding.FragmentCalendarConstraintsBtn.setOnClickListener{ nextWeekConstraints() }
        }
    }

    private fun nextWeekConstraints() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.Main_Fragment,ConstraintsFragment())
            .addToBackStack(null)
            .commit()
    }

//    private fun selectedDayShifts(selectedDate: CalendarDay) {
//        DatabaseManeger.getDateShifts(
//            LocalDate.of(
//                selectedDate.year,
//                selectedDate.month,
//                selectedDate.day
//            )
//        ) { selectedShifts ->
//            if (selectedShifts != null) {
//                calendar_selected_date_title.text = "Selected Date : ${selectedDate.toString()}"
//                calendar_list_selected_date.adapter = SelectedDateShiftsListAdapter(requireContext(),selectedShifts)
//            } else {
//                // Handle the case where no shift is found
//            }
//        }
//    }

}