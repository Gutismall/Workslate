package com.example.workslateapp.Fragments

import AccountLogAdapter
import DatabaseManeger
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workslateapp.LogInActivity
import com.example.workslateapp.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        DatabaseManeger.getUser(){ user ->
            if (user != null) {
                binding.FragmentAccountEmailText.text = "Account Email:\n${user.email}"
                binding.FragmentAccountPhoneNumber.text = "Phone Number:\n${user.phoneNumber}"
                binding.FragmentAccountCompanyCode.text = "Company Code:\n${user.companyCode}"
            }
        }
        binding.FragmentAccountLogRecyclerView.adapter = AccountLogAdapter(DatabaseManeger.getLastShifts(4))

        binding.FragmentAccountSignOutBtn.setOnClickListener{ signOut()}
    }

    private fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(requireContext(), LogInActivity::class.java)
        startActivity(intent)
    }

}