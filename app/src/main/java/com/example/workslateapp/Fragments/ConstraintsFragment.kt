package com.example.workslateapp.Fragments

import DatabaseManeger
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.workslateapp.databinding.FragmentConstraintsBinding
import createConstraintCheckBoxes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ConstraintsFragment : Fragment() {

    private var _binding: FragmentConstraintsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConstraintsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        if (LocalDate.now().dayOfWeek.value !in listOf(2, 3, 4, 6)) {
            binding.FragmentConstraintsCardView.visibility = View.INVISIBLE
        }
        else{
            val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            val currentWeek = "${LocalDate.now().with(DayOfWeek.MONDAY).format(dateFormat)} - ${LocalDate.now().with(DayOfWeek.SUNDAY).dayOfMonth}"
            binding.FragmentConstraintsTopTitle.text = currentWeek
            binding.FragmentConstraintsSubmitBtn.setOnClickListener{ updateConstraintsFromCheckBoxes()}
        }
    }

    private fun updateConstraintsFromCheckBoxes() {
        val checkBoxes = createConstraintCheckBoxes(binding)
        val data: MutableMap<String, List<Boolean>> = (1..7).associate { it.toString() to listOf(false, false, false) }.toMutableMap()
        checkBoxes.allCheckBoxes.chunked(3).forEachIndexed { index, chunk ->
            data[(index + 1).toString()] = listOf(chunk[0].isChecked, chunk[1].isChecked, chunk[2].isChecked)
        }
        DatabaseManeger.updateConstraints(data){ result->
            if (result)
                Toast.makeText(context, "Constraints Updated", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, "Constraints Didn't Update", Toast.LENGTH_SHORT).show()
        }
    }
}