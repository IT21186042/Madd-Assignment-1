package com.example.eyezen
import FirebaseManager
import android.os.Bundle


import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eyezen.R
import com.example.eyezen.TestResultAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestResultsActivity : AppCompatActivity() {

    private lateinit var testResultsRecyclerView: RecyclerView
    private lateinit var testResultAdapter: TestResultAdapter
    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_results)

        // Initialize RecyclerView
        testResultsRecyclerView = findViewById(R.id.testResultsRecyclerView)
        testResultsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch the test results from Firebase and display them
        fetchTestResultsFromFirebase()
    }

    private fun fetchTestResultsFromFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            val testResults = firebaseManager.getTestResults()

            withContext(Dispatchers.Main) {
                if (testResults != null && testResults.isNotEmpty()) {
                    testResultAdapter = TestResultAdapter(testResults)
                    testResultsRecyclerView.adapter = testResultAdapter
                } else {
                    Toast.makeText(this@TestResultsActivity, "No test results found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
