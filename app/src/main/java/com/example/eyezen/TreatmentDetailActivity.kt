package com.example.eyezen

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load

class TreatmentDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_detail)

        // Find views
        val treatmentImageView = findViewById<ImageView>(R.id.treatmentImageView)
        val treatmentTitleTextView = findViewById<TextView>(R.id.treatmentTitleTextView)
        val treatmentDescriptionTextView = findViewById<TextView>(R.id.treatmentDescriptionTextView)

        // Get data from Intent
        val title = intent.getStringExtra("treatment_title")
        val description = intent.getStringExtra("treatment_description")
        val imageUrl = intent.getStringExtra("treatment_image_url")

        // Set data to views
        treatmentTitleTextView.text = title
        treatmentDescriptionTextView.text = description

        // Load image using Coil
        treatmentImageView.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }
    }
}
