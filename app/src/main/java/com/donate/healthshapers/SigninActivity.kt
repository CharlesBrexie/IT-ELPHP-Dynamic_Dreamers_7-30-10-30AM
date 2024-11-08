package com.donate.healthshapers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SigninActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        dbHelper = DatabaseHelper(this)

        val usernameInput: EditText = findViewById(R.id.username)
        val passwordInput: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signupButton: Button = findViewById(R.id.signupID)
        val rememberMeCheckBox: CheckBox = findViewById(R.id.rememberme)
        val togglePasswordVisibilityButton: ImageButton = findViewById(R.id.togglePasswordVisibility)
        val forgotPassButton: Button = findViewById(R.id.forgotpassword)

        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")

        // Pre-fill username and password if remembered
        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            usernameInput.setText(savedUsername)
            passwordInput.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        } else {
            rememberMeCheckBox.isChecked = false
        }

        // Toggle password visibility
        togglePasswordVisibilityButton.setOnClickListener {
            val currentVisibility = passwordInput.transformationMethod
            passwordInput.transformationMethod =
                if (currentVisibility == PasswordTransformationMethod.getInstance())
                    HideReturnsTransformationMethod.getInstance() else
                    PasswordTransformationMethod.getInstance()

            // Change icon accordingly
            togglePasswordVisibilityButton.setImageResource(
                if (currentVisibility == PasswordTransformationMethod.getInstance())
                    R.drawable.ic_show else R.drawable.ic_hide
            )
        }

        // Login button click
        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            val enteredPassword = passwordInput.text.toString()

            // Check if username and password fields are empty
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this@SigninActivity, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Log entered password (before hashing)
            Log.d("SigninActivity", "Entered Password (before hashing): $enteredPassword")

            // Verify username and password with the database
            if (dbHelper.isValidUser(enteredUsername, enteredPassword)) { // Pass plain password here
                // If "Remember Me" is checked, save credentials
                if (rememberMeCheckBox.isChecked) {
                    with(sharedPreferences.edit()) {
                        putString("username", enteredUsername)
                        putString("password", enteredPassword)
                        apply()
                    }
                }

                // Redirect user based on userType (you can add more logic here based on userType)
                val intent = Intent(this@SigninActivity, RestaurantFrontPage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@SigninActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Signup button click
        signupButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Forgot password button click
        forgotPassButton.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }
}
