package com.example.eyezen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var isPasswordVisible = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val fullNameInput = findViewById<EditText>(R.id.nameInput)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // Sign up button listener
        signUpButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val fullName = fullNameInput.text.toString().trim()

            if (fullName.isEmpty()) {
                fullNameInput.error = "Full name is required"
                fullNameInput.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Enter a valid email"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                passwordInput.error = "Password should be at least 6 characters"
                passwordInput.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password, fullName)
        }

        // Toggle password visibility
        passwordInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (passwordInput.right - passwordInput.compoundDrawables[2].bounds.width())) {
                    if (isPasswordVisible) {
                        // Hide the password
                        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_visibility_off, 0)
                        isPasswordVisible = false
                    } else {
                        // Show the password
                        passwordInput.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_visibility, 0)
                        isPasswordVisible = true
                    }
                    passwordInput.setSelection(passwordInput.text.length)  // Move cursor to end
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    // Function to register a new user
    private fun registerUser(email: String, password: String, fullName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    saveUserToDatabase(userId, fullName, email)
                } else {
                    Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Function to save user data to Firebase Firestore
    private fun saveUserToDatabase(userId: String, fullName: String, email: String) {
        val user = hashMapOf(
            "id" to userId,
            "fullName" to fullName,
            "email" to email
        )

        db.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                finish() // Close the sign-up activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
