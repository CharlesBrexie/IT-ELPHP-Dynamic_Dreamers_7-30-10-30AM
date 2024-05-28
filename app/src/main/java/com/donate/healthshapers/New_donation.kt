package com.donate.healthshapers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class New_donation : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var photoImageView: ImageView
    private var selectedImageUri: Uri? = null
    private var imageUrl: String? = null

    private lateinit var submitButton: Button
    private lateinit var charitySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_donation)

        auth = FirebaseAuth.getInstance()

        val addPhotoButton = findViewById<Button>(R.id.add_photo)
        photoImageView = findViewById(R.id.photoImageView)
        submitButton = findViewById(R.id.submit_new_donation)
        charitySpinner = findViewById(R.id.charitySpinner)

        val charityOptions =
            arrayOf("Select Charity", "Cross Catholic", "CARE Philippines", "PAC Canada")
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, charityOptions)
        charitySpinner.adapter = adapter

        addPhotoButton.setOnClickListener {
            openGallery()
        }

        val ndBackButton = findViewById<ImageButton>(R.id.new_donations_back_button)
        ndBackButton.setOnClickListener {
            val intent = Intent(this, RestaurantFrontPage::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            if (charitySpinner.selectedItemPosition == 0) {
                // No charity selected, show toast message
                Toast.makeText(this, "Please choose a charity", Toast.LENGTH_SHORT).show()
            } else {
                // Charity selected, proceed to save donation
                saveDonation()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                selectedImageUri = data.data
                photoImageView.setImageURI(selectedImageUri)
                photoImageView.visibility = ImageView.VISIBLE
            } else {
                selectedImageUri = null
                photoImageView.visibility = ImageView.GONE
                Toast.makeText(this, "Input canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDonation() {
        val itemName = findViewById<EditText>(R.id.itemName).text.toString()
        val timeOfPreparation = findViewById<EditText>(R.id.timeOfPreparation).text.toString()
        val quantity = findViewById<EditText>(R.id.quantity).text.toString()
        val address = findViewById<EditText>(R.id.address).text.toString()
        val utensilsRequired =
            findViewById<RadioGroup>(R.id.required).checkedRadioButtonId == R.id.yes
        val donationId = UUID.randomUUID().toString() // Generate unique donation ID
        val userId = auth.currentUser?.uid

        // Get selected charity from Spinner
        val selectedCharity = charitySpinner.selectedItem.toString()

        // Check if all required fields are filled and a charity is selected
        if (itemName.isNotEmpty() && timeOfPreparation.isNotEmpty() && quantity.isNotEmpty() && address.isNotEmpty() && selectedCharity != "Select Charity") {
            if (userId != null) {
                val donationRef =
                    FirebaseDatabase.getInstance().getReference("donations").child(userId)
                        .child(donationId)
                val donationData = hashMapOf<String, Any>(
                    "itemName" to itemName,
                    "timeOfPreparation" to timeOfPreparation,
                    "quantity" to quantity,
                    "address" to address,
                    "utensilsRequired" to utensilsRequired,
                    "charity" to selectedCharity
                )

                // Upload image to Firebase Storage
                if (selectedImageUri != null) {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("images/${UUID.randomUUID()}")
                    val uploadTask = imageRef.putFile(selectedImageUri!!)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Image uploaded successfully, get the download URL
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Set imageUrl to the download URL
                            donationData["imageUrl"] = uri.toString()

                            // Save donation data to Firebase Realtime Database
                            donationRef.setValue(donationData)
                                .addOnSuccessListener {
                                    // Donation saved successfully
                                    Toast.makeText(
                                        this@New_donation,
                                        "Donation Data Successfully Saved",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(this, Your_donations::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                    // Failed to save donation
                                    Toast.makeText(
                                        this@New_donation,
                                        "Donation Data Saving Failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    // Handle the error
                                }
                        }
                    }.addOnFailureListener {
                        // Failed to upload image
                        Toast.makeText(
                            this@New_donation,
                            "Failed to upload image",
                            Toast.LENGTH_LONG
                        ).show()
                        // Handle the error
                    }
                } else {
                    // No image selected
                    Toast.makeText(
                        this@New_donation,
                        "Please select an image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            // Notify user to fill in all fields and choose a charity
            Toast.makeText(
                this@New_donation,
                "Please fill in all fields and choose a charity",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}