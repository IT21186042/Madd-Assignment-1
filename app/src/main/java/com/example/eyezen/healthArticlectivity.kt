package com.example.eyezen

import FirebaseManager
import HealthArticleAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class healthArticlectivity : AppCompatActivity() {

    private lateinit var healthArticleRecyclerView: RecyclerView
    private lateinit var healthArticleAdapter: HealthArticleAdapter
    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_articlectivity)

        healthArticleRecyclerView = findViewById(R.id.healthArticleRecyclerView)
        healthArticleRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch health articles from Firebase
        fetchHealthArticlesFromFirebase()


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

    private fun fetchHealthArticlesFromFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            val healthArticles = firebaseManager.getHealthArticles()

            if (!healthArticles.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    Log.d("HealthArticles", "Fetched health articles: $healthArticles")

                    // Logging each health article data
                    healthArticles.forEach { article ->
                        Log.d("HealthArticleItem", "Title: ${article.title}, Description: ${article.description}, Image URL: ${article.imageUrl}")
                    }

                    healthArticleAdapter = HealthArticleAdapter(healthArticles)
                    healthArticleRecyclerView.adapter = healthArticleAdapter
                }
            } else {
                withContext(Dispatchers.Main) {
                    Log.e("HealthArticles", "No health articles found.")
                    Toast.makeText(this@healthArticlectivity, "No health articles found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
