package com.example.eyezen.fragments

import FirebaseManager
import android.content.Intent
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

import com.example.eyezen.GameActivity

class ColorCubesFragment : Fragment() {

    private lateinit var onRoundComplete: (Int, Int) -> Unit
    private var currentRound: Int = 1
    private var totalRounds: Int = 3
    private var currentScore: Int = 0

    // Make scoreTextView nullable to prevent crashes due to late initialization
    private var scoreTextView: TextView? = null
    private lateinit var stopQuizButton: Button

    private lateinit var colorCubes: List<Button>
    private val firebaseManager = FirebaseManager() // Your Firebase manager class

    companion object {
        fun newInstance(
            currentRound: Int,
            totalRounds: Int, // Add the totalRounds parameter here
            score: Int,
            onRoundComplete: (Int, Int) -> Unit
        ): ColorCubesFragment {
            val fragment = ColorCubesFragment()
            val args = Bundle()
            args.putInt("currentRound", currentRound)
            args.putInt("totalRounds", totalRounds) // Set the totalRounds in the arguments
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
            totalRounds = args.getInt("totalRounds") // Get the totalRounds from the arguments
            currentScore = args.getInt("score")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_color_cubes, container, false)

        // Initialize the views
        stopQuizButton = view.findViewById(R.id.stopQuizButton)
        colorCubes = listOf(
            view.findViewById(R.id.cube1),
            view.findViewById(R.id.cube2),
            view.findViewById(R.id.cube3),
            view.findViewById(R.id.cube4)
        )

        // Set up the current round UI
        startRound()

        // Handle Stop Quiz button click
        stopQuizButton.setOnClickListener {
            // Launch the coroutine from the lifecycleScope
            viewLifecycleOwner.lifecycleScope.launch {
                stopQuizAndUpdateDatabase()
            }
        }

        return view
    }

    private fun startRound() {
        // Set random colors, with one cube having a different color
        setupCubes()

        // Set click listeners to handle cube selection
        colorCubes.forEach { cube ->
            cube.setOnClickListener {
                checkAnswer(cube)
            }
        }
    }

    private fun setupCubes() {
        // Set three cubes to the same color and one cube to a different color
        val differentCubeIndex = (0..3).random()
        colorCubes.forEachIndexed { index, button ->
            val color = if (index == differentCubeIndex) {
                R.color.different_color // Color for the different cube
            } else {
                R.color.same_color // Color for the same cubes
            }
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
        }
    }

    private fun checkAnswer(selectedCube: Button) {
        // Check if the selected cube is the correct one (i.e., the different colored one)
        val selectedColor = (selectedCube.background as ColorDrawable).color
        val correctColor = ContextCompat.getColor(requireContext(), R.color.different_color)

        if (selectedColor == correctColor) {
            currentScore += 1000 // Increment score for correct answer
        } else {
            currentScore -= 500 // Deduct score for incorrect answer
            if (currentScore < 0) currentScore = 0 // Prevent score from going negative
        }

        // Update score display only if scoreTextView is not null
        scoreTextView?.text = "Score: $currentScore"

        // Move to the next round after the answer is selected
        onRoundComplete(currentRound + 1, currentScore)
    }

    private suspend fun stopQuizAndUpdateDatabase() {
        // Save the current score in the Firebase database
        firebaseManager.updateScoreInDatabase("game1", currentScore) { success ->
            if (success) {
                // Navigate back to GameActivity after the score is updated
                navigateToGameActivity()
            } else {
                // Handle failure case (you could show a toast or some feedback to the user)
            }
        }
    }

    private fun navigateToGameActivity() {
        // Check if the fragment is attached to a context before starting the activity
        context?.let {
            val intent = Intent(it, GameActivity::class.java)
            intent.putExtra("final_score", currentScore)
            startActivity(intent)
            requireActivity().finish()
        } ?: run {
            // Handle the case when the fragment is not attached to a context
            println("Fragment not attached to a context.")
        }
    }

}
