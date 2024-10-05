package com.example.eyezen

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isNotEmpty()) {
                // Logic for sending password reset email
                // For now, just show a toast message
                Toast.makeText(this, "Reset link sent to $email", Toast.LENGTH_SHORT).show()
            } else {
                emailInput.error = "Email is required"
            }
        }
    }
}
