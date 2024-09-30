package com.example.eyezen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class SignInSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_sign_up)

        // Find the buttons
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // Set click listeners
        signInButton.setOnClickListener {
            // Handle Sign In action
           val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            // Handle Sign Up action
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}