package com.example.eyezen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Launch the next activity (e.g., SignInActivity)
            val intent = Intent(this, OnboardingActivity::class.java) // Replace with your main activity
            startActivity(intent)
            finish() // Close the splash screen activity
        }, 3000) // 3 seconds delay
    }
}