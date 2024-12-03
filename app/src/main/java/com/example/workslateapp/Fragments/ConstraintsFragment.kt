package com.example.workslateapp.Fragments

import DatabaseManeger
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        if (LocalDate.now().dayOfWeek.value !in listOf(2, 3, 4, 7 ,1,5,6)) {
            binding.FragmentConstraintsCardView.visibility = View.INVISIBLE
        }
        else{
            val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            val currentWeek = "${LocalDate.now().with(DayOfWeek.MONDAY).format(dateFormat)} - ${LocalDate.now().with(DayOfWeek.SUNDAY).dayOfMonth}"
            binding.FragmentConstraintsTopTitle.text = currentWeek
            binding.FragmentConstraintsSubmitBtn.setOnClickListener{ updateConstarintsFromCheckBoxes()}
        }
    }

    private fun updateConstarintsFromCheckBoxes() {
        val checkBoxes = createConstraintCheckBoxes(binding)
        val checkBoxsesStatus = mutableListOf<Boolean>()
        checkBoxes.allCheckBoxes.forEach { checkBox ->
            checkBoxsesStatus.add(checkBox.isChecked)
        }
        DatabaseManeger.updateConstraints(checkBoxsesStatus)
    }
}