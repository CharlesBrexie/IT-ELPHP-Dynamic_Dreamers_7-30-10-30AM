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
        val charity = intent.getStringExtra("charity")
        val timeOfPreparation = intent.getStringExtra("timeOfPreparation")
        val quantity = intent.getStringExtra("quantity")
        val address = intent.getStringExtra("address")
        val utensilsRequired = intent.getBooleanExtra("utensilsRequired", false)
        val imageUrl = intent.getStringExtra("imageUrl") ?: "" // Provide a default value if null
        val name = intent.getStringExtra("name")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val email = intent.getStringExtra("email") ?: ""
        val userType = intent.getStringExtra("userType") ?: ""
        val donationId = intent.getStringExtra("donationId") ?:""

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
            this.charity = charity
            this.donationId = donationId
        }

        // Populate views with received data
        findViewById<TextView>(R.id.confirm_title).text =  data.itemName
        findViewById<TextView>(R.id.confirm_time).text = "Pickup before:" + data.timeOfPreparation
        findViewById<TextView>(R.id.confirm_quantity).text = "Quantity: " + data.quantity
        findViewById<TextView>(R.id.confirm_location).text = "Location: " + data.address
        findViewById<TextView>(R.id.charity).text = "Charity: " + data.charity
        findViewById<TextView>(R.id.donationId).text = "Donation ID: " + data.donationId
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

        // Save the confirmed donation to "NGO Confirmed Donations"
        val confirmedDonationsRef = database.getReference("NGO Confirmed Donations")

        // Generate a unique key for the confirmed donation
        val donationKey = confirmedDonationsRef.push().key

        // Save the data to Firebase under the user's ID
        if (donationKey != null && userId != null) {
            // Set the status field to "Pending" when saving data
            data.status = "Pending"
            confirmedDonationsRef.child(userId).child(donationKey).setValue(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Confirmed donation saved to Firebase successfully")

                    // After saving the confirmed donation, save the request details
                    saveRequestToFirebase(userId, donationKey, data.donationId.toString())
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error saving confirmed donation to Firebase", it)
                    Toast.makeText( this, "Error saving confirmed donation to Firebase", Toast.LENGTH_LONG).show()
                }
        } else {
            Log.e(TAG, "Error generating donation key or user ID is null")
        }
    }

    private fun saveRequestToFirebase(ngoId: String, donationKey: String, donationId: String) {
        // Initialize Firebase database reference
        val database = FirebaseDatabase.getInstance()

        // Save the request details to "Requests"
        val requestsRef = database.getReference("Requests")

        // Generate a unique key for the request
        val requestKey = requestsRef.push().key

        // Save the request details to Firebase
        if (requestKey != null) {
            // Retrieve user information from the "Users" node
            val usersRef = database.getReference("users").child(ngoId)
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.child("name").getValue(String::class.java)
                    val userType = dataSnapshot.child("userType").getValue(String::class.java)
                    val phoneNumber = dataSnapshot.child("phoneNumber").getValue(String::class.java)

                    // Create request data map
                    val requestData = mapOf(
                        "ngoId" to ngoId,
                        "donationId" to donationId,
                        "status" to "Pending",
                        "username" to username,
                        "userType" to userType,
                        "phoneNumber" to phoneNumber
                    )

                    // Save request data to Firebase
                    requestsRef.child(requestKey).setValue(requestData)
                        .addOnSuccessListener {
                            Log.d(TAG, "Request saved to Firebase successfully")
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "Error saving request to Firebase", it)
                            Toast.makeText(this@Confirm_requests, "Error saving request to Firebase", Toast.LENGTH_LONG).show()
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error reading user data from Firebase", databaseError.toException())
                }
            })
        } else {
            Log.e(TAG, "Error generating request key")
        }
    }
}