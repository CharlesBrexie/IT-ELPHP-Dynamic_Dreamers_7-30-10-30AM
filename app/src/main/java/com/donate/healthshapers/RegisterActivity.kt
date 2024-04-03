package com.michdo.health_shapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val mySigninButton = findViewById<Button>(R.id.mySigninButton)
        mySigninButton.setOnClickListener {
            val Intent = Intent(this, SigninActivity::class.java)
            startActivity(Intent)
        }
    }
}