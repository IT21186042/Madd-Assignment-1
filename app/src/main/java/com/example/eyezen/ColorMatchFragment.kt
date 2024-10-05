package com.example.eyezen.fragments

import FirebaseManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.eyezen.R
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.random.Random

import com.example.eyezen.GameActivity

class ColorMatchFragment : Fragment() {

    private lateinit var onRoundComplete: (Int, Int) -> Unit
    private var currentRound: Int = 1
    private var totalRounds: Int = 3
    private var currentScore: Int = 0

    private lateinit var colorTargetView: View // The target color view
    private lateinit var stopQuizButton: Button
    private lateinit var colorOptions: List<Button>
    private val firebaseManager = FirebaseManager() // Your Firebase manager class

    companion object {
        fun newInstance(
            currentRound: Int,
            totalRounds: Int,
            score: Int,
            onRoundComplete: (Int, Int) -> Unit
        ): ColorMatchFragment {
            val fragment = ColorMatchFragment()
            val args = Bundle()
            args.putInt("currentRound", currentRound)
            args.putInt("totalRounds", totalRounds)
            args.putInt("score", score)
            fragment.arguments = args
            fragment.onRoundComplete = onRoundComplete
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Extract the arguments passed to the fragment
        arguments?.let { args ->
            currentRound = args.getInt("currentRound")
            totalRounds = args.getInt("totalRounds")
            currentScore = args.getInt("score")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_color_match, container, false)

        // Initialize the views
        colorTargetView = view.findViewById(R.id.colorTargetView)
        stopQuizButton = view.findViewById(R.id.stopQuizButton)
        colorOptions = listOf(
            view.findViewById(R.id.colorOption1),
            view.findViewById(R.id.colorOption2),
            view.findViewById(R.id.colorOption3),
            view.findViewById(R.id.colorOption4)
        )

        // Set up the current round UI
        startRound()

        // Handle Stop Quiz button click
        stopQuizButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                stopQuizAndUpdateDatabase()
            }
        }

        return view
    }

    private fun startRound() {
        // Set random colors for target and options
        setupColors()

        // Set click listeners to handle color selection
        colorOptions.forEach { option ->
            option.setOnClickListener {
                checkAnswer(option)
            }
        }
    }

    private fun setupColors() {
        // Generate random colors for the options and one target color
        val randomColors = List(4) { getRandomColor() }
        val correctColor = randomColors.random()

        // Set the target color
        colorTargetView.setBackgroundColor(correctColor)

        // Assign random colors to the buttons
        colorOptions.forEachIndexed { index, button ->
            button.setBackgroundColor(randomColors[index])
        }
    }

    private fun getRandomColor(): Int {
        return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }

    private fun checkAnswer(selectedOption: Button) {
        val selectedColor = (selectedOption.background as ColorDrawable).color
        val targetColor = (colorTargetView.background as ColorDrawable).color

        if (selectedColor == targetColor) {
            currentScore += 1000 // Increment score for correct answer
        } else {
            currentScore -= 500 // Deduct score for incorrect answer
            if (currentScore < 0) currentScore = 0 // Prevent score from going negative
        }

        onRoundComplete(currentRound + 1, currentScore)
    }

    private suspend fun stopQuizAndUpdateDatabase() {
        // Save the current score in the Firebase database
        firebaseManager.updateScoreInDatabase("game2", currentScore) { success ->
            if (success) {
                // Navigate back to GameActivity after the score is updated
                navigateToGameActivity()
            }
        }
    }

    private fun navigateToGameActivity() {
        context?.let {
            val intent = Intent(it, GameActivity::class.java)
            intent.putExtra("final_score", currentScore)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
