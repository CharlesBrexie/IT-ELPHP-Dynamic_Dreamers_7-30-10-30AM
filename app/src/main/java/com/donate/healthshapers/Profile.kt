package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

        val dbBtn = findViewById<Button>(R.id.homebtn)
        dbBtn.setOnClickListener{
            val intent = Intent(this, RestaurantFrontPage::class.java)
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