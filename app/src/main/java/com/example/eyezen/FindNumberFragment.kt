package com.example.eyezen

import FirebaseManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.eyezen.R
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FindNumberFragment : Fragment() {

    private lateinit var onRoundComplete: (Int, Int) -> Unit
    private var currentRound: Int = 1
    private var totalRounds: Int = 5
    private var currentScore: Int = 0
    private lateinit var stopQuizButton: Button
    private lateinit var imageView: ImageView
    private lateinit var answerButtons: List<Button>
    private val firebaseManager = FirebaseManager() // Firebase manager

    private var correctAnswer: Int = 0 // To explicitly store the correct answer

    companion object {
        fun newInstance(
            currentRound: Int,
            totalRounds: Int,
            score: Int,
            onRoundComplete: (Int, Int) -> Unit
        ): FindNumberFragment {
            val fragment = FindNumberFragment()
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
        val view = inflater.inflate(R.layout.fragment_find_number, container, false)

        // Initialize views
        imageView = view.findViewById(R.id.numberImageView) // Properly reference the ImageView from the inflated view
        stopQuizButton = view.findViewById(R.id.stopQuizButton)
        answerButtons = listOf(
            view.findViewById(R.id.answerOption1),
            view.findViewById(R.id.answerOption2),
            view.findViewById(R.id.answerOption3),
            view.findViewById(R.id.answerOption4)
        )

        // Set up the round
        startRound()

        stopQuizButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                stopQuizAndUpdateDatabase()
            }
        }

        return view
    }

    private fun startRound() {
        Log.d("FindNumberFragment", "Starting new round")
        // Load a new image and setup answers
        setupImageAndAnswers()

        // Set click listeners for each answer option
        answerButtons.forEach { button ->
            button.setOnClickListener {
                checkAnswer(button)
            }
        }
    }

    private fun setupImageAndAnswers() {
        // Get the correct answer first
        correctAnswer = getRandomCorrectAnswer()

        // Log the correct answer
        Log.d("FindNumberFragment", "Correct Answer: $correctAnswer")

        // Set the image based on the correct answer
        val imageResId = getImageResourceForAnswer(correctAnswer)
        Log.d("FindNumberFragment", "Setting Image Resource: $imageResId for Correct Answer: $correctAnswer")

        // Ensure the correct image is displayed for the correct answer
        if (imageResId != 0) {
            imageView.setImageResource(imageResId) // Properly set the image resource
            Log.d("FindNumberFragment", "Image successfully set for Correct Answer: $correctAnswer")
        } else {
            Log.e("FindNumberFragment", "Failed to set image. Invalid resource ID for answer: $correctAnswer")
        }

        // Ensure the correct number is included in the options
        val answerOptions = mutableListOf<Int>()
        answerOptions.add(correctAnswer) // Add the correct answer first

        // Add three other random incorrect numbers
        while (answerOptions.size < 4) {
            val randomNumber = (10..99).random()
            if (randomNumber !in answerOptions) {
                answerOptions.add(randomNumber)
            }
        }

        // Shuffle the options and set them on the buttons
        val shuffledOptions = answerOptions.shuffled()
        answerButtons.forEachIndexed { index, button ->
            button.text = shuffledOptions[index].toString()
        }

        Log.d("FindNumberFragment", "Answer Options: ${shuffledOptions.joinToString(", ")}")
    }

    // Helper method to return a random correct answer (e.g., 6, 12, etc.)
    private fun getRandomCorrectAnswer(): Int {
        return listOf(6, 8, 12, 29, 74).random() // Add more correct answers if needed
    }

    // Helper method to return the image resource ID based on the correct answer
    private fun getImageResourceForAnswer(answer: Int): Int {
        return when (answer) {
            6 -> R.drawable.number_6
            8 -> R.drawable.number_8
            12 -> R.drawable.number_12
            29 -> R.drawable.number_29
            74 -> R.drawable.number_74
            else -> {
                Log.e("FindNumberFragment", "Invalid answer: $answer, setting fallback image.")
                R.drawable.ic_placeholder // Fallback image, if needed
            }
        }
    }

    private fun checkAnswer(selectedButton: Button) {
        val selectedAnswer = selectedButton.text.toString().toInt()

        Log.d("FindNumberFragment", "Selected Answer: $selectedAnswer, Correct Answer: $correctAnswer")

        // Compare the selected answer with the correct answer
        if (selectedAnswer == correctAnswer) {
            currentScore += 1000
            Log.d("FindNumberFragment", "Correct Answer! New Score: $currentScore")
        } else {
            currentScore -= 500
            if (currentScore < 0) currentScore = 0
            Log.d("FindNumberFragment", "Incorrect Answer. New Score: $currentScore")
        }

        onRoundComplete(currentRound + 1, currentScore)
    }

    private suspend fun stopQuizAndUpdateDatabase() {
        firebaseManager.updateScoreInDatabase("game3", currentScore) { success ->
            if (success) {
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
