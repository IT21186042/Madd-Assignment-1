package com.example.eyezen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class InfantEyeCareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infant_eye_care)

        // Initialize views for Question 1
        val question01 = findViewById<TextView>(R.id.question01)
        val answer01 = findViewById<TextView>(R.id.answer01)

        // Initialize views for Question 2
        val question02 = findViewById<TextView>(R.id.question02)
        val answer02 = findViewById<TextView>(R.id.answer02)

        // Initialize views for Question 3
        val question03 = findViewById<TextView>(R.id.question03)
        val answer03 = findViewById<TextView>(R.id.answer03)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val takeQuizButton = findViewById<Button>(R.id.takeQuizButton)

        // Set an OnClickListener for the Take Quiz button
        takeQuizButton.setOnClickListener {
            // Navigate to the Start Quiz activity
            val intent = Intent(this, InfantVisionTestQuizActivity::class.java)
            startActivity(intent)
        }

        // Back button functionality
        backButton.setOnClickListener {
            finish() // Closes the current activity and goes back to the previous screen
        }

        // Expand/Collapse question 1 answer
        question01.setOnClickListener {
            if (answer01.visibility == View.GONE) {
                answer01.visibility = View.VISIBLE
                question01.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0)
            } else {
                answer01.visibility = View.GONE
                question01.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0)
            }
        }

        // Expand/Collapse question 2 answer
        question02.setOnClickListener {
            if (answer02.visibility == View.GONE) {
                answer02.visibility = View.VISIBLE
                question02.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0)
            } else {
                answer02.visibility = View.GONE
                question02.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0)
            }
        }

        // Expand/Collapse question 3 answer
        question03.setOnClickListener {
            if (answer03.visibility == View.GONE) {
                answer03.visibility = View.VISIBLE
                question03.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0)
            } else {
                answer03.visibility = View.GONE
                question03.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0)
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_fragment_infant)

        // Set the current selected item as active
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
