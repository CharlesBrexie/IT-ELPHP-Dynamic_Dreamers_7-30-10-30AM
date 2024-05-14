package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout

class RestaurantFrontPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_front_page)

        val mdButton = findViewById<Button>(R.id.make_a_donation_button)
        mdButton.setOnClickListener{
            val intent = Intent(this, New_donation::class.java)
            startActivity(intent)
        }

        val ydButton = findViewById<Button>(R.id.your_donations_button)
        ydButton.setOnClickListener{
            val intent = Intent(this, Your_donations::class.java)
            startActivity(intent)
        }

        val dlButton = findViewById<Button>(R.id.donation_list_button)
        dlButton.setOnClickListener{
            val intent = Intent(this, Available_donations::class.java)
            startActivity(intent)
        }
        val pfBtn = findViewById<Button>(R.id.iconButton)
        pfBtn.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

    }
}