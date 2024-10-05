package com.example.eyezen

import ArticleAdapter
import IconAdapter
import SpacingItemDecoration
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DashboardActivity<ImageButton : View?> : AppCompatActivity() {

    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userNameTextView: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var searchInput: EditText
    private var articleList: MutableList<ArticleData> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find the TextView where the username and profile image should be displayed
        userNameTextView = findViewById(R.id.userName)
        profileImageView = findViewById(R.id.profileImage)
        searchInput = findViewById(R.id.searchInput) // Search EditText

        // Fetch user data and display it
        fetchAndDisplayUserProfile()

        // Setup Icon buttons RecyclerView
        setupIconButtons()

        // Initialize and setup the Articles RecyclerView
        articleRecyclerView = findViewById(R.id.articlesRecyclerView)
        articleRecyclerView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(articleList, this)  // 'this' refers to the Activity context

        articleRecyclerView.adapter = articleAdapter

        // Fetch articles from Firestore
        fetchArticlesFromFirestore()

        // Search functionality
        searchInput.addTextChangedListener {
            val query = it.toString()
            searchArticles(query)
        }

        // Setup bottom navigation
        setupBottomNavigation()
    }

    // Function to fetch and display user profile (name and image)
    private fun fetchAndDisplayUserProfile() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val fullName = document.getString("fullName")
                        val imageUrl = document.getString("imageUrl")

                        if (fullName != null) {
                            userNameTextView.text = fullName
                        }

                        if (imageUrl != null) {
                            // Load profile image using Coil
                            profileImageView.load(imageUrl) {
                                placeholder(R.drawable.profile_placeholder)  // Placeholder image while loading
                                error(R.drawable.profile_placeholder)        // Error image if failed to load
                            }
                        }
                    } else {
                        Log.d("DashboardActivity", "No such document!")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DashboardActivity", "Failed to fetch user data: ", exception)
                }
        }
    }

    private fun fetchArticlesFromFirestore() {
        firestore.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                val articleList = mutableListOf<ArticleData>()
                for (document in result) {
                    try {
                        val article = document.toObject(ArticleData::class.java)
                        Log.d("Firestore", "Fetched article: $article")
                        articleList.add(article)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error deserializing document ${document.id}: ${e.message}")
                    }
                }
                articleAdapter.updateList(articleList)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }

    // Process Firestore documents and update the RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    private fun processArticleDocuments(documents: QuerySnapshot) {
        articleList.clear()
        for (document in documents) {
            val article = document.toObject(ArticleData::class.java)
            articleList.add(article)
        }
        articleAdapter.notifyDataSetChanged()
    }

    // Search function to filter articles by title
    private fun searchArticles(query: String) {
        if (query.isNotEmpty()) {
            val filteredList = articleList.filter { article ->
                article.title.contains(query, ignoreCase = true)
            }
            articleAdapter.updateList(filteredList)
        } else {
            articleAdapter.updateList(articleList)
        }
    }

    // Setup Icon buttons RecyclerView
    private fun setupIconButtons() {
        val iconList = listOf(
            IconData(R.drawable.ic_doctor, "Doctors"),
            IconData(R.drawable.ic_article, "Articles"),
            IconData(R.drawable.ic_test, "Tests"),
            IconData(R.drawable.ic_ayurweda, "Ayurveda")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.iconButtonsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = IconAdapter(iconList) { iconData ->
            when (iconData.label) {
                "Doctors" -> {
                    val intent = Intent(this, DoctorSection::class.java)
                    startActivity(intent)
                }
                "Articles" -> {
                    val intent = Intent(this, healthArticlectivity::class.java)
                    startActivity(intent)
                }
                "Tests" -> {
                    val intent = Intent(this, EyeTestsActivity::class.java)
                    startActivity(intent)
                }
                "Ayurveda" -> {
                    val intent = Intent(this, AyurvedicEyeCareActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)
        recyclerView.addItemDecoration(SpacingItemDecoration(spacingInPixels))
    }

    // Setup bottom navigation
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavFragment)
        bottomNavigationView.selectedItemId = R.id.dashboard

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
                       // val intent = Intent(this, NotificationActivity::class.java)
                        //startActivity(intent)
                        //finish()
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
