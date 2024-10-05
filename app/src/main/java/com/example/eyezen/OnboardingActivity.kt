package com.example.eyezen



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Get started button click listener
        val getStartedButton = findViewById<Button>(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            // Navigate to the next activity (e.g., SignInActivity or HomeActivity)
            val intent = Intent(this, SignInSignUpActivity::class.java)  //
            startActivity(intent)
        }
    }
}
