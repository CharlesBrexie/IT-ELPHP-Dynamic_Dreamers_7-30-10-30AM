package com.donate.healthshapers

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton

class NGO_New_donation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_new_donation)

          val NGOndBackButton = findViewById<ImageButton>(R.id.new_donations_back_button)
        NGOndBackButton.setOnClickListener {
            val intent = Intent(this, RestaurantFrontPage::class.java)
            startActivity(intent)

            val NGOsubmitButton = findViewById<Button>(R.id.NGO_submit_new_donation)
            NGOsubmitButton.setOnClickListener {
                val intent = Intent(this, RestaurantFrontPage::class.java)
                startActivity(intent)

                //saveDonation()
            }

        }
    }
}