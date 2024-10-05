package com.example.eyezen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfantVisionTestQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infant_quizstart)

        // Initialize views
        val question1 = findViewById<TextView>(R.id.question1)
        val answer1 = findViewById<TextView>(R.id.answer1)
        val question2 = findViewById<TextView>(R.id.question2)
        val answer2 = findViewById<TextView>(R.id.answer2)
        val question3 = findViewById<TextView>(R.id.question3)
        val answer3 = findViewById<TextView>(R.id.answer3)
        val startQuizButton = findViewById<Button>(R.id.startQuizButton)

        // Expand/Collapse question 1 answer
        question1.setOnClickListener {
            toggleAnswerVisibility(answer1, question1)
        }

        // Expand/Collapse question 2 answer
        question2.setOnClickListener {
            toggleAnswerVisibility(answer2, question2)
        }

        // Expand/Collapse question 3 answer
        question3.setOnClickListener {
            toggleAnswerVisibility(answer3, question3)
        }

        // Start Quiz Button functionality
        startQuizButton.setOnClickListener {
            // Implement the quiz starting logic here, for example:
             val intent = Intent(this, InfantQuizActivity::class.java)
            startActivity(intent)
        }
    }

    // Helper function to toggle visibility of the answer
    private fun toggleAnswerVisibility(answer: TextView, question: TextView) {
        if (answer.visibility == View.GONE) {
            answer.visibility = View.VISIBLE
            question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0) // Replace with collapse icon
        } else {
            answer.visibility = View.GONE
            question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0) // Replace with expand icon
        }
    }
}
