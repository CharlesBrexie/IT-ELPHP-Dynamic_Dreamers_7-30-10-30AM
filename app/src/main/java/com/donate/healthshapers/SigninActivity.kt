package com.donate.healthshapers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import com.donate.healthshapers.RegisterActivity
import com.donate.healthshapers.RestaurantFrontPage
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

        // Ensure checkbox is unchecked initially
        rememberMeCheckBox.isChecked = false

        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            val enteredPassword = passwordInput.text.toString()

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

                        // Redirect to your main activity
                        val intent = Intent(this, RestaurantFrontPage::class.java)
                        startActivity(intent)
                        finish()
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

