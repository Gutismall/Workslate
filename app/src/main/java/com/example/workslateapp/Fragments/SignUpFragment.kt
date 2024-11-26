package com.example.workslateapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.workslateapp.R
import com.example.workslateapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.LogInFragmentSignUpButton.setOnClickListener{ initRegister()}
    }

    private fun initRegister() {
        val firstName = binding.LoginFragmentFirstName.text.toString()
        val lastName = binding.LoginFragmentLastName.text.toString()
        val phoneNumber = binding.LoginFragmentPhoneNumber.text.toString()
        val email = binding.LoginFragmentEmailBox.text.toString()
        val password = binding.LoginFragmentPasswordBox.text.toString()
        val password_confirm = binding.LoginFragmentPasswordConfirmBox.text.toString()
        val companyCode = binding.LoginFragmentCompanyCode.text.toString()

        when {
            password.isEmpty() -> {
                binding.LoginFragmentPasswordBox.error = "Field cannot be empty"
            }
            password_confirm.isEmpty() -> {
                binding.LoginFragmentPasswordBox.error = "Field cannot be empty"
            }
            password != password_confirm -> {
                binding.LoginFragmentPasswordConfirmBox.error = "Password must be the same"
            }
            else -> {
                DatabaseManeger.createUser(
                    firstName,
                    lastName,
                    phoneNumber,
                    email,
                    password,
                    companyCode,
                    onSuccess = {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.Login_Fragment,LogInFragment())
                            .commit()
                    })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}