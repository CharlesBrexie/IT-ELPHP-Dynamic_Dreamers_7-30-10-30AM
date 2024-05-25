package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val submitButton = findViewById<Button>(R.id.submit)
        submitButton.setOnClickListener {
            // Get user input
            val name = findViewById<EditText>(R.id.name).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.phoneNumber).text.toString()
            val email = findViewById<EditText>(R.id.Email).text.toString()
            val password = findViewById<EditText>(R.id.Password).text.toString()
            val userType = when (findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId) {
                R.id.radioButtonNGO -> "NGO"
                R.id.radioButtonRestaurant -> "Restaurant"
                R.id.radioButtonPrivateDonation -> "Private Donation"
                else -> "Unknown"
            }

            // Check if email already exists
            checkIfEmailExists(email) { emailExists ->
                if (!emailExists) {
                    // Register the user in Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Registration successful
                                val user = User(name, phoneNumber, email, userType)
                                database.child("users").child(auth.currentUser!!.uid)
                                    .setValue(user)
                                    .addOnCompleteListener { databaseTask ->
                                        if (databaseTask.isSuccessful) {
                                            // Registration and data save successful
                                            val intent = Intent(this, SigninActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            // Error saving data
                                            // You can handle this according to your app's logic
                                            Log.e("RegisterActivity", "Error saving data: ${databaseTask.exception}")
                                        }
                                    }
                            } else {
                                // Registration failed
                                val exception = task.exception
                                Log.e("RegisterActivity", "Sign-up failed", exception)
                                // Display an error message to the user or handle the error accordingly
                                Toast.makeText(this, "Registration failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Email already exists, display message to the user
                    Toast.makeText(this, "The email is already in use.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val mySigninButton = findViewById<Button>(R.id.mySigninButton)
        mySigninButton.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        // Set up password visibility toggle
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val togglePasswordVisibilityButton = findViewById<ImageButton>(R.id.togglePasswordVisibility)

        // Set OnClickListener on the toggle button
        togglePasswordVisibilityButton.setOnClickListener {
            // Toggle password visibility
            if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // Show password
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_hide) // Set your hide icon drawable
            } else {
                // Hide password
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_show) // Set your show icon drawable
            }

            // Move cursor to the end of the text
            passwordEditText.setSelection(passwordEditText.text.length)
        }
    }

    private fun checkIfEmailExists(email: String, callback: (Boolean) -> Unit) {
        val query: Query = database.child("users").orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }
}

data class User(
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val userType: String = ""
)
