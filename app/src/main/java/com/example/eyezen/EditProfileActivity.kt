package com.example.eyezen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var profileImageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var currentPasswordEditText: EditText
    private lateinit var saveProfileButton: Button

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        profileImageView = findViewById(R.id.profileImageView)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText)
        saveProfileButton = findViewById(R.id.saveProfileButton)

        val currentUser = auth.currentUser
        loadUserProfile(currentUser)

        profileImageView.setOnClickListener {
            // Open gallery to select image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }

        saveProfileButton.setOnClickListener {
            saveProfile(currentUser)
        }
    }

    private fun loadUserProfile(currentUser: FirebaseUser?) {
        currentUser?.let {
            firestore.collection("users").document(it.uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    val fullName = document.getString("fullName")
                    nameEditText.setText(fullName)
                    emailEditText.setText(it.email)
                }
            }
        }
    }

    private fun saveProfile(currentUser: FirebaseUser?) {
        val newName = nameEditText.text.toString()
        val newEmail = emailEditText.text.toString()
        val newPassword = passwordEditText.text.toString()
        val currentPassword = currentPasswordEditText.text.toString()

        if (currentPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your current password to update.", Toast.LENGTH_SHORT).show()
            return
        }

        currentUser?.let { user ->
            val userId = user.uid

            // Step 1: Re-authenticate the user before making sensitive changes
            reauthenticateUser(user, currentPassword) {
                // Step 2: Proceed to update name, email, and password after re-authentication

                // Update name in Firestore
                val userRef = firestore.collection("users").document(userId)
                userRef.update("fullName", newName).addOnSuccessListener {
                    Toast.makeText(this, "Name updated", Toast.LENGTH_SHORT).show()
                }

                // Update email in FirebaseAuth
                user.updateEmail(newEmail).addOnSuccessListener {
                    Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                }

                // Update password in FirebaseAuth
                if (newPassword.isNotEmpty()) {
                    user.updatePassword(newPassword).addOnSuccessListener {
                        Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Password update failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }

                // If a new image is selected, upload it to Firebase Storage
                imageUri?.let { uri ->
                    val storageRef = storage.reference.child("profile_images/$userId.jpg")
                    storageRef.putFile(uri).addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            userRef.update("imageUrl", downloadUrl.toString())
                            Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // After successfully saving the profile, navigate to ProfileActivity
                navigateToProfileActivity()
            }
        }
    }

    // Function to navigate back to ProfileActivity after saving changes
    private fun navigateToProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // Optional: Call finish() to close EditProfileActivity
    }


    private fun reauthenticateUser(user: FirebaseUser, currentPassword: String, onReauthenticated: () -> Unit) {
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onReauthenticated() // Proceed with updates after re-authentication
            } else {
                Toast.makeText(this, "Re-authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }
}
