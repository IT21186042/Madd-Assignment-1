package com.example.eyezen

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var articleImageView: ImageView
    private lateinit var articleTitleTextView: TextView
    private lateinit var articleDescriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        // Initialize the views
        articleImageView = findViewById(R.id.healthArticleImageView)
        articleTitleTextView = findViewById(R.id.healthArticleTitleTextView)
        articleDescriptionTextView = findViewById(R.id.articleDescriptionTextView)

        // Retrieve the article data passed through Intent
        val articleTitle = intent.getStringExtra("title") ?: "N/A"
        val articleImageUrl = intent.getStringExtra("imageUrl") ?: ""
        val articleDescription = intent.getStringExtra("description") ?: "N/A"

        // Populate the views with the article data
        articleTitleTextView.text = articleTitle
        articleDescriptionTextView.text = articleDescription

        // Use Coil to load the article image
        articleImageView.load(articleImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }
    }
}
