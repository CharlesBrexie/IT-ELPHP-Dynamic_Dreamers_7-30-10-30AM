package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        val resetPasswordButton = findViewById<Button>(R.id.FP_reset_pass)

        resetPasswordButton.setOnClickListener {
            // Get the entered email
            val emailEditText: EditText = findViewById(R.id.forg_email_add)
            val email: String = emailEditText.text.toString().trim { it <= ' ' }

            // Check if the email is not empty
            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
                Toast.makeText(this, "An email has been sent", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // Show a toast message if the email is empty
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(
                        this,
                        "Password reset email sent to $email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Failed to send password reset email
                    Toast.makeText(
                        this,
                        "Failed to send password reset email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}