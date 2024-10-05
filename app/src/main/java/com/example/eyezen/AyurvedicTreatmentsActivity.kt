package com.example.eyezen

import FirebaseManager
import TreatmentAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class AyurvedicTreatmentsActivity : AppCompatActivity() {

    private lateinit var treatmentRecyclerView: RecyclerView
    private lateinit var treatmentAdapter: TreatmentAdapter
    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayurvedic_treatments)

        treatmentRecyclerView = findViewById(R.id.treatmentRecyclerView)
        treatmentRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch treatments (articles) from Firebase
        fetchTreatmentsFromFirebase()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.doctor_one)

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
    private fun fetchTreatmentsFromFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            val treatments = firebaseManager.getTreatments()

            if (!treatments.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    Log.d("Treatments", "Fetched treatments: $treatments")

                    // Logging each treatment data
                    treatments.forEach { treatment ->
                        Log.d("TreatmentItem", "Title: ${treatment.title}, Description: ${treatment.description}, Image URL: ${treatment.imageUrl}")
                    }

                    treatmentAdapter = TreatmentAdapter(treatments)
                    treatmentRecyclerView.adapter = treatmentAdapter
                }
            } else {
                withContext(Dispatchers.Main) {
                    Log.e("Treatments", "No treatments found.")
                    Toast.makeText(this@AyurvedicTreatmentsActivity, "No treatments found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





}
