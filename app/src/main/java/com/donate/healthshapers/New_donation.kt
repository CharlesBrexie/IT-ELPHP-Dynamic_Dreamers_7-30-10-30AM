package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class New_donation : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_donation)

        auth = FirebaseAuth.getInstance()

        val submitButton = findViewById<Button>(R.id.submit_new_donation)
        submitButton.setOnClickListener {
            saveDonation()
        }
    }

    private fun saveDonation() {
        val itemName = findViewById<EditText>(R.id.itemName).text.toString()
        val timeOfPreparation = findViewById<EditText>(R.id.timeOfPreparation).text.toString()
        val quantity = findViewById<EditText>(R.id.quantity).text.toString()
        val address = findViewById<EditText>(R.id.address).text.toString()
        val utensilsRequired = findViewById<RadioGroup>(R.id.required).checkedRadioButtonId == R.id.yes

        val donationId = UUID.randomUUID().toString() // Generate unique donation ID

        val userId = auth.currentUser?.uid

        if (userId != null) {
            val donationRef = FirebaseDatabase.getInstance().getReference("donations").child(userId).child(donationId)

            val donationData = HashMap<String, Any>()
            donationData["itemName"] = itemName
            donationData["timeOfPreparation"] = timeOfPreparation
            donationData["quantity"] = quantity
            donationData["address"] = address
            donationData["utensilsRequired"] = utensilsRequired

            donationRef.setValue(donationData)
                .addOnSuccessListener {
                    // Donation saved successfully
                    val intent = Intent(this, Your_donations::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    // Failed to save donation
                    // Handle the error
                }
        }
    }
}



