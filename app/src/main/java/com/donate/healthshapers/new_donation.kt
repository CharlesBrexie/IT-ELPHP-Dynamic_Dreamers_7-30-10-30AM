package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton

class new_donation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_donation)

        val submitndButton = findViewById<Button>(R.id.submit_new_donation)
        submitndButton.setOnClickListener {
            val intent = Intent(this, your_donations::class.java)
            startActivity(intent)
        }
        val ndBackButton = findViewById<ImageButton>(R.id.new_donations_back_button)
        ndBackButton.setOnClickListener {
            val intent = Intent(this, RestaurantFrontPage::class.java)
            startActivity(intent)
        }
        val pfBtn = findViewById<FrameLayout>(R.id.iconButton)
        pfBtn.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }
}