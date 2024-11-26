package com.example.workslateapp.Animations

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.workslateapp.MainActivity
import com.example.workslateapp.R

class LoginAnimationActivity : AppCompatActivity() {
    private val splashScreenTimeout: Long = 3000  // Duration for the loading screen (3 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_animation)

        // Get the user's name from the intent that was passed from the login screen
        val userName = intent.getStringExtra("USER_NAME") ?: "User"

        // Set the welcome message with the user's name
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        welcomeText.text = "Welcome, $userName!"

        // Apply fade-in animation to the welcome text
        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        welcomeText.startAnimation(fadeIn)

        // Delay and then move to the MainActivity
        Handler().postDelayed({
            // Apply fade-out animation before transitioning
            val fadeOut: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            welcomeText.startAnimation(fadeOut)

            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Close the LoadingActivity
        }, splashScreenTimeout)
    }
}