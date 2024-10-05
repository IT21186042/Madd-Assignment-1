package com.example.eyezen
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.load
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class OneDoctorActivity: AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_doctor)  // Ensure this is the correct layout file

        // Get the passed doctor data from the intent
        val doctorName = intent.getStringExtra("doctor_name")
        val doctorSpecialty = intent.getStringExtra("doctor_specialty")
        val doctorRating = intent.getDoubleExtra("doctor_rating", 0.0)
        val doctorDistance = intent.getStringExtra("doctor_distance")
        val doctorImageUrl = intent.getStringExtra("doctor_image_url")
        val doctorBio = intent.getStringExtra("doctor_bio")

        // Set the data to the views
        val doctorImageView = findViewById<ImageView>(R.id.doctorImage)
        val doctorNameText = findViewById<TextView>(R.id.doctorName)
        val doctorSpecialtyText = findViewById<TextView>(R.id.doctorSpecialty)
        val doctorRatingText = findViewById<TextView>(R.id.doctorRating)
        val doctorDescription = findViewById<TextView>(R.id.doctorDescription)
        //val doctorDistanceText = findViewById<TextView>(R.id.doctorDistance)

        doctorNameText.text = doctorName
        doctorSpecialtyText.text = doctorSpecialty
        doctorRatingText.text = "$doctorRating ★"
       // doctorDistanceText.text = doctorDistance
        doctorDescription.text = doctorBio


        // Load the image using Coil
        doctorImageView.load(doctorImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder) // Placeholder in case of a slow image load
        }

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

}
