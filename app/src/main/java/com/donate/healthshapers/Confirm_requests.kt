package com.donate.healthshapers

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.UUID

class Confirm_requests : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_requests)

        auth = FirebaseAuth.getInstance()

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
        findViewById<TextView>(R.id.confirm_title).text =  data.itemName
        findViewById<TextView>(R.id.confirm_time).text = "Pickup before:" + data.timeOfPreparation
        findViewById<TextView>(R.id.confirm_quantity).text = "Quantity: " + data.quantity
        findViewById<TextView>(R.id.confirm_location).text = "Location: " + data.address
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
            // Save data to Firebase
            saveDataToFirebase(data)
            // Launch the NGO_Requests activity and pass the data
            val intent = Intent(this, NGO_Requests::class.java)
            startActivity(intent)
        }
    }
    private fun saveDataToFirebase(data: DataClass) {
        val userId = auth.currentUser?.uid
        // Initialize Firebase database reference
        val database = FirebaseDatabase.getInstance()
        // Get a reference to the "NGO Confirmed Donations" node
        val reference = database.getReference("NGO Confirmed Donations")
        // Generate a unique key for the donation
        val donationKey = reference.push().key
        // Save the data to Firebase under the user's ID
        if (donationKey != null) {
            if (userId != null) {
                reference.child(userId).child(donationKey).setValue(data)
                    .addOnSuccessListener {
                        Log.d(TAG, "Data saved to Firebase successfully")
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Error saving data to Firebase", it)
                        Toast.makeText( this, "Error saving data to Firebase", Toast.LENGTH_LONG).show()
                    }
            }
        } else {
            Log.e(TAG, "Error generating donation key")
        }
    }
}
