package com.example.eyezen

import FirebaseManager
import GameAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameActivity : AppCompatActivity() {

    private lateinit var gamesRecyclerView: RecyclerView
    private lateinit var gamesAdapter: GameAdapter
    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gamesRecyclerView = findViewById(R.id.gamesRecyclerView)
        gamesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch games data from Firebase
        fetchGamesDataFromFirebase()
    }

    private fun fetchGamesDataFromFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            val gamesList = firebaseManager.getGames()

            // Ensure the list is not null or empty
            if (!gamesList.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    gamesAdapter = GameAdapter(this@GameActivity, gamesList) { gameTitle ->
                        handleGameClick(gameTitle)
                    }
                    gamesRecyclerView.adapter = gamesAdapter
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "No games found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun handleGameClick(gameTitle: String) {
        when (gameTitle) {
            "Color Cubes" -> startActivity(Intent(this, ColorCubesActivity::class.java))
            "Color Match" -> startActivity(Intent(this, ColorMatchActivity::class.java))
            "Find Number" -> startActivity(Intent(this, FindNumberActivity::class.java))
            else -> Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show()
        }
    }
}
