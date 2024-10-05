package com.example.eyezen

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Retrieve the score passed from the InfantQuizActivity
        val score = intent.getIntExtra("score", 0)

        // Find the views from the layout
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        val scoreValueTextView = findViewById<TextView>(R.id.textView2)
        val visionConditionTextView = findViewById<TextView>(R.id.visionConditionTextView)
        val visionConditionDetailsTextView = findViewById<TextView>(R.id.visionConditionDetails)
        val backToHomeButton = findViewById<Button>(R.id.backToHomeButton)

        // Display the score
        scoreTextView.text = "Score Obtained: "
        scoreValueTextView.text = "$score%"

        // Based on the score, determine the vision condition and display a recommendation
        when {
            score >= 80 -> {
                visionConditionTextView.text = "Vision Condition"
                visionConditionDetailsTextView.text = "• Vision seems to be good."
            }
            score in 50..79 -> {
                visionConditionTextView.text = "Vision Condition"
                visionConditionDetailsTextView.text = "• Vision seems to be a bit weak\n• Recommend to meet an Ophthalmologist."
            }
            else -> {
                visionConditionTextView.text = "Vision Condition"
                visionConditionDetailsTextView.text = "• Vision is weak\n• Urgently recommend to meet an Ophthalmologist."
            }
        }

        // Set up the "Back to home" button to finish this activity and return to the main screen
        backToHomeButton.setOnClickListener {
            finish() // Close the result screen and go back to the home screen
        }
    }
}
