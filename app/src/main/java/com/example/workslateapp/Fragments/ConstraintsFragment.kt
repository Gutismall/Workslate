package com.example.workslateapp.Fragments

import DatabaseManeger
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.workslateapp.R
import com.example.workslateapp.databinding.FragmentCalendarBinding
import com.example.workslateapp.databinding.FragmentConstraintsBinding


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
        // Inflate the layout for this fragment
        _binding = FragmentConstraintsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val matrix = Array(7) { Array(3) { false } }
        if (view is ViewGroup) {
            for (i in 0 until binding.root.childCount) {
                val child = binding.root.getChildAt(i)
                if (child is RadioButton) {
                    matrix[i/3][i%3] = child.isChecked
                }
            }
        }
        binding.FragmentConstraintsSubmitBtn.setOnClickListener { DatabaseManeger.updateConstraints()}
        parentFragmentManager.beginTransaction()
            .replace(R.id.Main_Fragment,CalendarFragment())
            .commit()
    }

}