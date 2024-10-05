package com.example.eyezen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DoctorSection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_section)

        val doctorListButton = findViewById<Button>(R.id.doctorList)

        val viewMaptButton = findViewById<Button>(R.id.viewMap)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_fragment_doc)

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


        if (doctorListButton != null) {
            doctorListButton.setOnClickListener {
                val intent = Intent(this, DoctorContactActivity::class.java)
                startActivity(intent)
            }
        }
        // Listener for View Map button to navigate to MapsActivity
        if (viewMaptButton != null) {
            viewMaptButton.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    }


