package com.example.eyezen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class TestResultAdapter(private val testResults: List<TestResultData>) :
    RecyclerView.Adapter<TestResultAdapter.TestResultViewHolder>() {

    class TestResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testImageView: ImageView = itemView.findViewById(R.id.testImageView)
        val testNameTextView: TextView = itemView.findViewById(R.id.testNameTextView)
        val testTypeTextView: TextView = itemView.findViewById(R.id.testTypeTextView)
        val testRatingTextView: TextView = itemView.findViewById(R.id.testRatingTextView)
        val testDateTextView: TextView = itemView.findViewById(R.id.testDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)
        return TestResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestResultViewHolder, position: Int) {
        val currentTest = testResults[position]
        holder.testNameTextView.text = currentTest.testName
        holder.testTypeTextView.text = currentTest.testType
        holder.testRatingTextView.text = currentTest.score
        holder.testDateTextView.text = currentTest.date

        // Load image using Coil
        holder.testImageView.load(currentTest.imageUrl) {
            placeholder(R.drawable.ic_test_image)
        }
    }

    override fun getItemCount(): Int {
        return testResults.size
    }
}
