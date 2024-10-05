package com.example.eyezen

import FirebaseManager
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch

class FindNumberActivity : AppCompatActivity() {

    private var currentScore = 0
    private var currentRound = 1
    private var isQuizRunning = true
    private val totalRounds = 5
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private val totalQuizTime = 60000L // 1 minute for the entire quiz
    private lateinit var quizTimer: CountDownTimer
    private val firebaseManager = FirebaseManager() // Reference to FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_number)

        // Initialize the TextViews
        scoreTextView = findViewById(R.id.scoreTextView)
        timerTextView = findViewById(R.id.timerTextView)

        // Start the quiz timer
        startQuizTimer()

        // Start the first round
        loadNextRound()
    }

    private fun loadNextRound() {
        if (!isQuizRunning || isFinishing || supportFragmentManager.isDestroyed) {
            return
        }

        // Update the score in the TextView before loading the next round
        updateScoreText()

        // Replace fragment to show the next round
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fragment_container,
            FindNumberFragment.newInstance(
                currentRound = currentRound,
                totalRounds = totalRounds,
                score = currentScore,
                onRoundComplete = { nextRound, newScore ->
                    currentRound = nextRound
                    currentScore = newScore
                    loadNextRound() // Load the next round
                }
            )
        )
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun updateScoreText() {
        scoreTextView.text = "Score: $currentScore"
    }

    private fun startQuizTimer() {
        quizTimer = object : CountDownTimer(totalQuizTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                timerTextView.text = "⏱ $timeLeft"
            }

            override fun onFinish() {
                // Time's up, end the quiz and update the score in the database
                lifecycleScope.launch {
                    updateScoreInDatabase()
                }
            }
        }.start()
    }

    private fun showFinalScore() {
        // Cancel the timer
        if (this::quizTimer.isInitialized) quizTimer.cancel()

        // Navigate to GameActivity with the final score
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("final_score", currentScore)
        startActivity(intent)
        finish()
    }

    private fun updateScoreInDatabase() {
        lifecycleScope.launch {
            firebaseManager.updateScoreInDatabase("game3", currentScore) { success ->
                if (success) {
                    showFinalScore()
                } else {
                    // Handle failure to update score in the database
                    showFinalScore()
                }
            }
        }
    }

    fun stopQuiz() {
        // Stop the quiz early
        isQuizRunning = false
        lifecycleScope.launch {
            updateScoreInDatabase()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::quizTimer.isInitialized) quizTimer.cancel()
    }
}
