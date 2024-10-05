package com.example.eyezen


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EyeTestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eye_tests)

        // Initialize buttons
        val infantTestButton: Button = findViewById(R.id.infantTestButton)
        val kidsTestButton: Button = findViewById(R.id.kidsTestButton)
        val adultTestButton: Button = findViewById(R.id.adultTestButton)

        // Handle Infant Section Test Button click
        infantTestButton.setOnClickListener {
            val intent = Intent(this, InfantEyeCareActivity::class.java)
            startActivity(intent)
        }

        // Handle Kids Section Test Button click
        kidsTestButton.setOnClickListener {
           val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        // Handle Adult Section Test Button click
        adultTestButton.setOnClickListener {
            val intent = Intent(this, HometestActivity::class.java)
            startActivity(intent)
        }
    }
}
