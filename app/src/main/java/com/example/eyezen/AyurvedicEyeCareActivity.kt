package com.example.eyezen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AyurvedicEyeCareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayurvedic_eye_care)

        // FAQ click handling
        val question1 = findViewById<TextView>(R.id.question1)
        val answer1 = findViewById<TextView>(R.id.answer1)

        question1.setOnClickListener {
            if (answer1.visibility == View.GONE) {
                answer1.visibility = View.VISIBLE
            } else {
                answer1.visibility = View.GONE
            }
        }

        // Button click handling
        val treatmentsButton = findViewById<Button>(R.id.treatmentsButton)
        val doctorContactButton = findViewById<Button>(R.id.doctorContactButton)

        treatmentsButton.setOnClickListener {
            // Handle Ayurvedic Treatments navigation
            Toast.makeText(this, "Ayurvedic Treatments Clicked", Toast.LENGTH_SHORT).show()
            // Navigate to Ayurvedic Treatments Activity
            val intent = Intent(this,  AyurvedicTreatmentsActivity::class.java)
            startActivity(intent)
            finish()
        }

        doctorContactButton.setOnClickListener {
            // Handle Doctor Contact navigation
            Toast.makeText(this, "Doctor Contact Clicked", Toast.LENGTH_SHORT).show()
            // Navigate to Doctor Contact Activity
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.doctor_one)


        bottomNavigationView.selectedItemId = R.id.doctorsection  // Assuming Home is the default screen

        // Set a listener for navigation item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (this::class != DashboardActivity::class) {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                R.id.navigation_reports -> {
                    if (this::class != TestResultsActivity::class) {
                        val intent = Intent(this, TestResultsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                R.id.navigation_notifications -> {
                    if (this::class != NotificationActivity::class) {
                        val intent = Intent(this, NotificationActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                R.id.navigation_profile ->  {if (this::class != ProfileActivity::class) {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                    true
                }
                else -> false
            }
        }
    }
}
