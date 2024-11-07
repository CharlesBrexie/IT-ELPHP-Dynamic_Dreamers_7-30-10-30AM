package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val submitButton = findViewById<Button>(R.id.submit)
        submitButton.setOnClickListener {
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

            // Check if all fields are filled
            if (name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Check if email already exists
                if (!dbHelper.isEmailExists(email)) {
                    // Create DataClass object and set the password
                    val user = DataClass().apply {
                        this.name = name
                        this.phoneNumber = phoneNumber
                        this.email = email
                        this.userType = userType
                        this.pfp = "" // Default profile picture
                        this.password = password // Set the password
                    }

                    // Save user to SQLite using insertUserData
                    val result = dbHelper.insertUserData(user)
                    if (result != -1L) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SigninActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "The email is already in use.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // Sign-in button
        findViewById<Button>(R.id.mySigninButton).setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        // Toggle password visibility
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val togglePasswordVisibilityButton = findViewById<ImageButton>(R.id.togglePasswordVisibility)
        togglePasswordVisibilityButton.setOnClickListener {
            if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_hide)
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_show)
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }
    }
}
