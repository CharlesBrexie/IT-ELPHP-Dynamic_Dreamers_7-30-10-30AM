package com.donate.healthshapers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val usernameInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signupID)
        val rememberMeCheckBox = findViewById<CheckBox>(R.id.rememberme)
        val togglePasswordVisibilityButton = findViewById<ImageButton>(R.id.togglePasswordVisibility)

        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")

        // Pre-fill username and password if remembered
        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            usernameInput.setText(savedUsername)
            passwordInput.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        } else {
            // If no saved credentials, ensure the checkbox is unchecked
            rememberMeCheckBox.isChecked = false
        }

        togglePasswordVisibilityButton.setOnClickListener {
            // Toggle password visibility
            val currentVisibility = passwordInput.transformationMethod
            passwordInput.transformationMethod =
                if (currentVisibility == PasswordTransformationMethod.getInstance())
                    HideReturnsTransformationMethod.getInstance() else
                    PasswordTransformationMethod.getInstance()

            // Change icon accordingly
            togglePasswordVisibilityButton.setImageResource(
                if (currentVisibility == PasswordTransformationMethod.getInstance())
                    R.drawable.ic_hide else R.drawable.ic_show
            )
        }

        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            val enteredPassword = passwordInput.text.toString()

            // Check if username and password fields are empty
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this@SigninActivity, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Authenticate user with Firebase
            auth.signInWithEmailAndPassword(enteredUsername, enteredPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        // If "Remember Me" is checked, save credentials
                        if (rememberMeCheckBox.isChecked) {
                            with(sharedPreferences.edit()) {
                                putString("username", enteredUsername)
                                putString("password", enteredPassword)
                                apply()
                            }
                        }

                        // Redirect user based on userType
                        database.child("users").child(auth.currentUser!!.uid)
                            .get().addOnSuccessListener { dataSnapshot ->
                                val userType = dataSnapshot.child("userType").value.toString()
                                val intent = when (userType) {
                                    "NGO" -> Intent(this, NgoFrontPage::class.java)
                                    "Restaurant" -> Intent(this, RestaurantFrontPage::class.java)
                                    "Private Donation" -> Intent(this, RestaurantFrontPage::class.java)
                                    else -> Intent(this, SigninActivity::class.java) // Default to SigninActivity
                                }
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                                // Handle failure
                                Toast.makeText(baseContext, "Failed to get user type.",
                                    Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
