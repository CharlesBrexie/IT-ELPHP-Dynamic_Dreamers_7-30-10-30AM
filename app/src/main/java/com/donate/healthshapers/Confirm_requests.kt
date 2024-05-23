package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class Confirm_requests : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_requests)

        // Set up the back button
        val crButton = findViewById<Button>(R.id.confirm_donation_button)
        crButton.setOnClickListener {
            val intent = Intent(this, Your_donations::class.java)
            startActivity(intent)
        }

        // Receive intent extras
        val itemName = intent.getStringExtra("itemName")
        val timeOfPreparation = intent.getStringExtra("timeOfPreparation")
        val quantity = intent.getStringExtra("quantity")
        val address = intent.getStringExtra("address")
        val imageUrl = intent.getStringExtra("imageUrl") ?: "" // Provide a default value if null

        // Populate views with received data
        findViewById<TextView>(R.id.confirm_title).text = itemName
        findViewById<TextView>(R.id.confirm_time).text = timeOfPreparation
        findViewById<TextView>(R.id.confirm_quantity).text = quantity
        findViewById<TextView>(R.id.confirm_location).text = address
        val imageView = findViewById<ImageView>(R.id.placeholder_image)

        // Load image URL using Picasso
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.healthshapers)
                .error(R.drawable.healthshapers)
                .into(imageView)
        } else {
            // Set a default image if imageUrl is null or empty
            imageView.setImageResource(R.drawable.healthshapers)
        }
    }
}
