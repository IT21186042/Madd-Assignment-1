package com.example.eyezen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class QuestionFragment(
    private val questionText: String,
    private val option1: String,
    private val option2: String,
    private val correctAnswer: String, // Correct answer from the database
    private val questionIndex: Int,
    private val totalQuestions: Int,
    private val nextFragmentCallback: (Int, Boolean) -> Unit
) : Fragment() {

    private var userAnswer: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        val questionTextView = view.findViewById<TextView>(R.id.questionTextView)
        val questionDescription = view.findViewById<TextView>(R.id.questionDescription)
        val option1Button = view.findViewById<Button>(R.id.option1Button)
        val option2Button = view.findViewById<Button>(R.id.option2Button)
        val nextButton = view.findViewById<Button>(R.id.nextButton)

        // Set question text
        questionTextView.text = questionIndex.toString()
        questionDescription.text = questionText
        option1Button.text = option1
        option2Button.text = option2

        // Track the user's answer
        option1Button.setOnClickListener {
            userAnswer = option1

        }

        option2Button.setOnClickListener {
            userAnswer = option2

        }

        // Handle the "Next" button
        nextButton.setOnClickListener {
            if (userAnswer != null) {
                // Compare user answer with the correct answer
                val isCorrect = userAnswer == correctAnswer
                nextFragmentCallback(questionIndex + 1, isCorrect)
            } else {
                // Optionally, show a message asking the user to select an answer
            }
        }

        return view
    }
}
