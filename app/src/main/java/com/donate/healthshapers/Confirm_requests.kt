package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
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
        val backButton = findViewById<ImageButton>(R.id.available_donations_back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, NgoFrontPage::class.java)
            startActivity(intent)
        }


        // Receive intent extras
        val itemName = intent.getStringExtra("itemName")
        val timeOfPreparation = intent.getStringExtra("timeOfPreparation")
        val quantity = intent.getStringExtra("quantity")
        val address = intent.getStringExtra("address")
        val utensilsRequired = intent.getBooleanExtra("utensilsRequired", false)
        val imageUrl = intent.getStringExtra("imageUrl") ?: "" // Provide a default value if null
        val name = intent.getStringExtra("name")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val email = intent.getStringExtra("email") ?: ""
        val userType = intent.getStringExtra("userType") ?: ""

        // Create a DataClass instance and assign values
        val data = DataClass().apply {
            this.itemName = itemName
            this.timeOfPreparation = timeOfPreparation
            this.quantity = quantity
            this.address = address
            this.utensilsRequired = utensilsRequired
            this.imageUrl = imageUrl
            this.name = name
            this.phoneNumber = phoneNumber
            this.email = email
            this.userType = userType
        }

        // Populate views with received data
        findViewById<TextView>(R.id.confirm_title).text = data.itemName
        findViewById<TextView>(R.id.confirm_time).text = data.timeOfPreparation
        findViewById<TextView>(R.id.confirm_quantity).text = data.quantity
        findViewById<TextView>(R.id.confirm_location).text = data.address
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

        val confirmButton = findViewById<Button>(R.id.confirm_donation_button)
        confirmButton.setOnClickListener {
            // Launch the NGO_Requests activity and pass the data
            val intent = Intent(this, NGO_Requests::class.java)
            intent.putExtra("itemName", data.itemName)
            intent.putExtra("timeOfPreparation", data.timeOfPreparation)
            intent.putExtra("quantity", data.quantity)
            intent.putExtra("address", data.address)
            intent.putExtra("utensilsRequired", data.utensilsRequired)
            intent.putExtra("imageUrl", data.imageUrl)
            startActivity(intent)
        }


    }
}
