package com.example.workslateapp.Fragments

import AccountLogAdapter
import DatabaseManeger
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workslateapp.DataClasses.Shift
import com.example.workslateapp.LogInActivity
import com.example.workslateapp.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var accountLogAdapter: AccountLogAdapter
    private var listOfShifts = mutableListOf<Shift>()


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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.FragmentAccountLogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        accountLogAdapter = AccountLogAdapter(listOfShifts)
        binding.FragmentAccountLogRecyclerView.adapter = accountLogAdapter
        DatabaseManeger.getUser(){ user ->
            if (user != null) {
                binding.FragmentAccountEmailText.text = "Account Email:\n${user.email}"
                binding.FragmentAccountPhoneNumber.text = "Phone Number:\n${user.phoneNumber}"
                binding.FragmentAccountCompanyCode.text = "Company Code:\n${user.companyCode}"
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            // Fragment is now visible
            DatabaseManeger.getLastShifts(4) { fetchListOfShifts ->
                listOfShifts.clear()
                listOfShifts.addAll(fetchListOfShifts)
                accountLogAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initViews() {
        binding.FragmentAccountSignOutBtn.setOnClickListener{ signOut()}
    }

    private fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(requireContext(), LogInActivity::class.java)
        startActivity(intent)
    }

}