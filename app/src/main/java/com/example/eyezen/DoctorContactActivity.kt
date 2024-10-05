package com.example.eyezen

import DoctorAdapter
import FirebaseManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class DoctorContactActivity : AppCompatActivity() {

    private lateinit var doctorRecyclerView: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_contact)

        // Initialize RecyclerView
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView)
        doctorRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch doctor data from Firebase using Coroutine
        lifecycleScope.launch {
            fetchDoctorData()
        }

        // Setup Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_fragment_doc_contact)
        setupBottomNavigation(bottomNavigationView)
    }

    // Method to fetch doctor data from Firebase
    private suspend fun fetchDoctorData() {
        try {
            val doctorList = firebaseManager.getAllDoctors() // This returns List<DoctorData>?
            if (!doctorList.isNullOrEmpty()) {  // Null and empty check
                doctorAdapter = DoctorAdapter(doctorList)
                doctorRecyclerView.adapter = doctorAdapter
            } else {
                // Handle case when no doctors are found
                Toast.makeText(this, "No doctors found.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Handle any errors
            Toast.makeText(this, "Failed to retrieve doctor data.", Toast.LENGTH_SHORT).show()
            Log.e("DoctorContactActivity", "Error fetching doctor data", e)
        }
    }

    // Method to set up Bottom Navigation
    private fun setupBottomNavigation(bottomNavigationView: BottomNavigationView) {
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
