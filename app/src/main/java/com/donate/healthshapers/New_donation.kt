package com.donate.healthshapers


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class New_donation : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var photoImageView: ImageView
    var selectedImageUri : Uri? = null
    private var imageUrl: String? = null

    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_donation)

        auth = FirebaseAuth.getInstance()

        val addPhotoButton = findViewById<Button>(R.id.add_photo)
        photoImageView = findViewById(R.id.photoImageView)
        addPhotoButton.setOnClickListener {
            openGallery()
        }
        val ndBackButton = findViewById<Button>(R.id.new_donations_back_button)
        ndBackButton.setOnClickListener {
            val intent = Intent(this, RestaurantFrontPage::class.java)
            startActivity(intent)
        }

         submitButton = findViewById(R.id.submit_new_donation)
         submitButton.isEnabled = false
         submitButton.setOnClickListener {
            saveDonation()
        }
        val uploadPhoto = findViewById<Button>(R.id.upload_photo)
        uploadPhoto.setOnClickListener {
            uploadImage()
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
                // User selected an image
                selectedImageUri = data.data
                photoImageView.setImageURI(selectedImageUri)
                photoImageView.visibility = ImageView.VISIBLE


            } else {
                // User canceled the inputting
                selectedImageUri = null
                photoImageView.visibility = ImageView.GONE
                // Disable submit button
                submitButton.isEnabled = false
                Toast.makeText(this, "Input canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun uploadImage() {
        if (selectedImageUri != null) {
            val storageReference = FirebaseStorage.getInstance().getReference().child(UUID.randomUUID().toString())
            val uploadTask = storageReference.putFile(selectedImageUri!!)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Get the download URL for the uploaded image
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    Toast.makeText(this@New_donation, "Image URL: $imageUrl", Toast.LENGTH_LONG).show()
                    Log.d("ImageUrl", "Current item imageUrl: $imageUrl")
                    // Enable submit button
                    submitButton.isEnabled = true
                }.addOnFailureListener { exception ->
                    // Handle failure to get download URL
                    Log.e(TAG, "Failed to get download URL: $exception")
                    Toast.makeText(this@New_donation, "Failed to get download URL", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { exception ->
                // Handle failure of the upload task
                Log.e(TAG, "File upload failed: $exception")
                Toast.makeText(this@New_donation, "File Upload Failed...", Toast.LENGTH_LONG).show()
            }.addOnCanceledListener {
                // Handle cancellation of the upload task
                Log.e(TAG, "File upload canceled")
                Toast.makeText(this@New_donation, "File Upload Canceled...", Toast.LENGTH_LONG).show()
                // Clear selectedImageUri to indicate no image is selected
                selectedImageUri = null
            }
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
            donationData["imageUrl"] = imageUrl.toString()

            donationRef.setValue(donationData)
                .addOnSuccessListener {
                    // Donation saved successfully
                    Toast.makeText(this@New_donation, "Donation Data Successfully Saved", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, Your_donations::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    // Failed to save donation
                    Toast.makeText(this@New_donation, "Donation Data Saving Failed", Toast.LENGTH_LONG).show()
                    // Handle the error
                }
        }
    }
}