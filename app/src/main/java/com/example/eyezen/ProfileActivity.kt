package com.example.eyezen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        profileImageView = findViewById(R.id.profileImageView)

        // Update profile information
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)

        val user = auth.currentUser
        user?.let {
            // Display email
            emailTextView.text = it.email

            // Fetch the fullName and profile image from Firestore
            fetchUserProfile(it.uid, nameTextView)
        }

        // Set up navigation actions for profile options
        findViewById<ConstraintLayout>(R.id.mySavedLayout).setOnClickListener {
            startActivity(Intent(this, MySavedActivity::class.java))
        }

        findViewById<ConstraintLayout>(R.id.appointmentLayout).setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        findViewById<ConstraintLayout>(R.id.faqsLayout).setOnClickListener {
            startActivity(Intent(this, FAQsActivity::class.java))
        }

        findViewById<ConstraintLayout>(R.id.editProfileLayout).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        findViewById<ConstraintLayout>(R.id.logoutLayout).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    // Function to fetch the user's fullName and imageUrl from Firestore
    private fun fetchUserProfile(userId: String, nameTextView: TextView) {
        val docRef = firestore.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = document.getString("fullName")
                    val imageUrl = document.getString("imageUrl")

                    if (fullName != null) {
                        nameTextView.text = fullName
                    }

                    // Load profile image using Coil library
                    if (imageUrl != null) {
                        profileImageView.load(imageUrl) {
                            placeholder(R.drawable.profile_placeholder)  // Placeholder image while loading
                            error(R.drawable.profile_placeholder)        // Error image if failed to load
                        }
                    }
                } else {
                    Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur while fetching the data
                Toast.makeText(this, "Error fetching profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
