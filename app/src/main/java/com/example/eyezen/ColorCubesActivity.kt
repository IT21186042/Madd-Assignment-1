package com.example.eyezen

import FirebaseManager
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eyezen.fragments.ColorCubesFragment
import kotlinx.coroutines.launch

class ColorCubesActivity : AppCompatActivity() {

    private var currentScore = 0
    private var currentRound = 1
    private var isQuizRunning = true
    private val totalRounds = 3
    private lateinit var scoreTextView: TextView // Reference to the score TextView
    private lateinit var timerTextView: TextView // Reference to the timer TextView
    private val timePerRound = 10000L
    private val totalQuizTime = 60000L
    private lateinit var roundTimer: CountDownTimer
    private lateinit var quizTimer: CountDownTimer
    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_cubes)

        // Initialize the TextViews
        scoreTextView = findViewById(R.id.scoreTextView)
        timerTextView = findViewById(R.id.timerTextView)

        // Start the overall quiz timer (1 minute)
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

        // Explicitly using FragmentTransaction to replace the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fragment_container, // Ensure this is a valid FrameLayout in your activity layout
            ColorCubesFragment.newInstance(
                currentRound = currentRound,
                totalRounds = totalRounds, // Pass the totalRounds here
                score = currentScore,
                onRoundComplete = { nextRound, newScore ->
                    currentRound = nextRound
                    currentScore = newScore
                    loadNextRound() // Load the next round only if the quiz is running
                }
            )
        )
        fragmentTransaction.commitAllowingStateLoss()

        // Start the round timer (10 seconds per round)
        startRoundTimer()
    }

    private fun updateScoreText() {
        // Update the scoreTextView with the current score
        scoreTextView.text = "Score: $currentScore"
    }

    private fun startRoundTimer() {
        if (this::roundTimer.isInitialized) {
            roundTimer.cancel() // Cancel previous timer if it exists
        }

        roundTimer = object : CountDownTimer(timePerRound, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                currentRound++
                loadNextRound() // Move to the next round when time is up
            }
        }.start()
    }

    private fun startQuizTimer() {
        quizTimer = object : CountDownTimer(totalQuizTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                timerTextView.text = "Quiz Time Left: $timeLeft"
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
        // Cancel the timers when the quiz ends
        if (this::roundTimer.isInitialized) roundTimer.cancel()
        if (this::quizTimer.isInitialized) quizTimer.cancel()

        // Navigate to the GameActivity with the final score
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("final_score", currentScore)
        startActivity(intent)
        finish() // Close the quiz activity
    }

    private fun updateScoreInDatabase() {
        lifecycleScope.launch {
            // Call FirebaseManager to update the current score in the database
            firebaseManager.updateScoreInDatabase("game1", currentScore) { success ->
                if (success) {
                    showFinalScore() // Once the score is updated in the database, navigate to GameActivity
                } else {
                    // Handle failure to update score in the database
                    showFinalScore() // Navigate to GameActivity regardless of failure
                }
            }
        }
    }

    fun stopQuiz() {
        // Manually stop the quiz (called when the user presses "Stop Quiz")
        isQuizRunning = false
        lifecycleScope.launch {
            updateScoreInDatabase() // Update the score and stop the quiz
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timers if the activity is destroyed
        if (this::roundTimer.isInitialized) roundTimer.cancel()
        if (this::quizTimer.isInitialized) quizTimer.cancel()
    }
}
