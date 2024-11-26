package com.example.workslateapp.Fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.workslateapp.Animations.LoginAnimationActivity
import com.example.workslateapp.LogInActivity
import com.example.workslateapp.R
import com.example.workslateapp.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth


class LogInFragment : Fragment() {

    // View binding property
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!! // Use non-nullable binding safely
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root // Use the root view from binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.LoginFragmentLogInButton.setOnClickListener{ InitLogIn() }
        binding.LogInFragmentSignUpButton.setOnClickListener { InitRegister()}
    }

    private fun InitRegister() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.Login_Fragment,SignUpFragment())
            .setCustomAnimations(
                android.R.anim.slide_in_left,  // Enter animation
                android.R.anim.slide_out_right, // Exit animation
                android.R.anim.fade_in, // Pop Enter animation (when coming back)
                android.R.anim.fade_out // Pop Exit animation (when going back)
            )
            .addToBackStack(null)
            .commit()
    }

    private fun InitLogIn() {
        val email = binding.LoginFragmentEmailBox.text.toString()
        val password = binding.LoginFragmentPasswordBox.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    (activity as? LogInActivity)?.navigateToHome()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}