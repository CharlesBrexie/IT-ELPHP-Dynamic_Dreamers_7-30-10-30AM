package com.donate.healthshapers

import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class SigninActivity : AppCompatActivity() {


    companion object {
        const val USERNAME = "user"
        const val PASSWORD = "p@ssword"
    }
    private fun isValidPassword(enteredPassword: String): Boolean {
        if(enteredPassword == PASSWORD)
        {
            val specialCharacters = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?"
            val regex = Regex("^(?=.*[A-Za-z])(?=.*[$specialCharacters]).{8,}$")
            return regex.matches(enteredPassword)

        }
        else
        {
            return false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val usernameInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupID = findViewById<Button>(R.id.signupID)

        // Login button click listener
        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            val enteredPassword = passwordInput.text.toString()

            // Check if entered username and password match the constants
            if (enteredUsername == USERNAME && isValidPassword(enteredPassword)) {
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()

                // Start MainActivity after successful login
                val intent = Intent(this, RestaurantFrontPage::class.java)
                startActivity(intent)
                finish() // Optional - if you want to remove this activity from the stack
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Signup button click listener
        signupID.setOnClickListener {
            val Intent = Intent(this, RegisterActivity::class.java)
            startActivity(Intent)
        }
    }
}
