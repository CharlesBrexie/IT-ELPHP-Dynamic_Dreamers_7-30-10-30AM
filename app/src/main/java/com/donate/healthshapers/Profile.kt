package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val abtUsBtn = findViewById<LinearLayout>(R.id.about)
        abtUsBtn.setOnClickListener{
            val intent = Intent(this, About_us::class.java)
            startActivity(intent)
        }
        val trmsBtn = findViewById<LinearLayout>(R.id.terms)
        trmsBtn.setOnClickListener{
            val intent = Intent(this, Terms_and_condition::class.java)
            startActivity(intent)
        }

        val logoutButton = findViewById<LinearLayout>(R.id.logout)
        logoutButton.setOnClickListener{
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }
}