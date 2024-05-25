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
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

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

            // Register the user in Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        val user = User(name, phoneNumber, email, userType)
                        FirebaseDatabase.getInstance().getReference("users")
                            .child(auth.currentUser!!.uid)
                            .setValue(user)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    // Registration and data save successful
                                    val intent = Intent(this, SigninActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    // Error saving data
                                    // You can handle this according to your app's logic
                                }
                            }
                    } else {
                        // Registration failed
                        val exception = task.exception
                        Log.e("RegisterActivity", "Sign-up failed", exception)
                        // Display an error message to the user or handle the error accordingly
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
}


data class User(
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val userType: String = ""

)
