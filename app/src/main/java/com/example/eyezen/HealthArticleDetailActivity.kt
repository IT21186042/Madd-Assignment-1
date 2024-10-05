package com.example.eyezen

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView

class HealthArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_article_detail)

        // Find views according to your XML layout IDs
        val articleImageView = findViewById<ImageView>(R.id.healthArticleImageView)
        val articleTitleTextView = findViewById<TextView>(R.id.healthArticleTitleTextView)
        val articleDescriptionTextView = findViewById<TextView>(R.id.articleDescriptionTextView)

        // Get data from Intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set data to views
        articleTitleTextView.text = title
        articleDescriptionTextView.text = description

        // Load image using Coil
        articleImageView.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder) // Replace with your placeholder if needed


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
