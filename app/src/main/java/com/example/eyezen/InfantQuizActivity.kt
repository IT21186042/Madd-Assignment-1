package com.example.eyezen

import FirebaseManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class InfantQuizActivity : AppCompatActivity() {

    private val firebaseManager = FirebaseManager()
    private var correctAnswers = 0 // Track the number of correct answers
    private lateinit var quizQuestions: List<QuizQuestion> // Questions from Firebase
    private var quizJob: Job? = null // Track coroutines job to handle activity lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infant_quiz)

        // Fetch questions from Firebase and load the first question
        fetchQuestionsFromFirebase()
    }

    private fun fetchQuestionsFromFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            val questions = firebaseManager.getQuizQuestions()

            if (questions.isNotEmpty()) {
                quizQuestions = questions
                withContext(Dispatchers.Main) {
                    loadQuestionFragment(0)
                }
            } else {
                withContext(Dispatchers.Main) {
                    // Show a message or handle the error as needed
                    Toast.makeText(this@InfantQuizActivity, "No quiz questions found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun loadQuestionFragment(questionIndex: Int) {
        if (questionIndex < quizQuestions.size) {
            val question = quizQuestions[questionIndex]

            // Create a new instance of QuestionFragment
            val fragment = QuestionFragment(
                questionText = question.question,
                option1 = question.option1,
                option2 = question.option2,
                correctAnswer = question.correctanswer,
                questionIndex = questionIndex,
                totalQuestions = quizQuestions.size,
                nextFragmentCallback = { nextQuestionIndex, isCorrect ->
                    // Update the score based on whether the user's answer was correct
                    if (isCorrect) correctAnswers++
                    loadQuestionFragment(nextQuestionIndex)
                }
            )

            // Replace the fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        } else {
            // End of quiz, navigate to the result screen
            showQuizResults()
        }
    }

    private fun showQuizResults() {
        // Show the result in a new activity or fragment
        val resultPercentage = (correctAnswers * 100) / quizQuestions.size

        // Navigate to a result screen (e.g., ResultActivity)
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("score", resultPercentage)
        }
        startActivity(intent)
        finish() // Close the quiz activity
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the coroutine job when the activity is destroyed to avoid memory leaks
        quizJob?.cancel()
    }
}
