package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton

class confirm_requests : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_requests)

        val crButton = findViewById<Button>(R.id.confirm_donation_button)
        crButton.setOnClickListener{
            val intent = Intent(this, your_donations::class.java)
            startActivity(intent)
        }

        val adBackButton = findViewById<ImageButton>(R.id.available_donations_back_button)
        adBackButton.setOnClickListener{
            val intent = Intent(this, avaliable_donations::class.java)
            startActivity(intent)
        }
        val pfBtn = findViewById<FrameLayout>(R.id.iconButton)
        pfBtn.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }
}