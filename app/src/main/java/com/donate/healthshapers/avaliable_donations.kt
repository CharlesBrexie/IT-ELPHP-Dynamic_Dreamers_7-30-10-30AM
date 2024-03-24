package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.cardview.widget.CardView

class avaliable_donations : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliable_donations)

        val confirmDonation1 = findViewById<CardView>(R.id.donation1)
        confirmDonation1.setOnClickListener{
            val intent = Intent(this, confirm_requests::class.java)
            startActivity(intent)
        }
        val confirmDonation2 = findViewById<CardView>(R.id.donation2)
        confirmDonation2.setOnClickListener{
            val intent = Intent(this, confirm_requests::class.java)
            startActivity(intent)
        }
        val confirmDonation3 = findViewById<CardView>(R.id.donation3)
        confirmDonation3.setOnClickListener{
            val intent = Intent(this, confirm_requests::class.java)
            startActivity(intent)
        }
        val confirmDonation4 = findViewById<CardView>(R.id.donation4)
        confirmDonation4.setOnClickListener{
            val intent = Intent(this, confirm_requests::class.java)
            startActivity(intent)
        }
        val fpBackButton = findViewById<ImageButton>(R.id.front_page_back_button)
        fpBackButton.setOnClickListener{
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