package com.example.workslateapp

import DatabaseManeger
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.workslateapp.Animations.LoginAnimationActivity
import com.example.workslateapp.Fragments.LogInFragment
import com.example.workslateapp.databinding.ActivityLogInBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            navigateToHome()
        }
        initViews()
    }


    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.Login_Fragment,LogInFragment())
            .setCustomAnimations(
                android.R.anim.slide_in_left,  // Enter animation
                android.R.anim.slide_out_right, // Exit animation
                android.R.anim.fade_in, // Pop Enter animation (when coming back)
                android.R.anim.fade_out // Pop Exit animation (when going back)
            )
            .commit()
    }

    fun navigateToHome() {
        val intent = Intent(this, LoginAnimationActivity::class.java)
        val user = auth.currentUser
        if (user != null) {
            intent.putExtra("USER_NAME",user.displayName)
        }
        startActivity(intent)
    }

}