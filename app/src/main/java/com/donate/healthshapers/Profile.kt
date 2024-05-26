package com.donate.healthshapers
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class Profile : Fragment(R.layout.fragment_profile) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var myProfilePic: CircleImageView
    private lateinit var editProfPicBtn: ImageButton
    private val PICK_IMAGE_REQUEST = 1
    private var currentProfilePictureUri: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val usernameText = view.findViewById<TextView>(R.id.userNameMain)
        val userTypeText = view.findViewById<TextView>(R.id.userType)
        val nameEditText = view.findViewById<EditText>(R.id.name)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.phoneNumber)
        val emailEditText = view.findViewById<EditText>(R.id.Email)
        val submitButton = view.findViewById<Button>(R.id.submit)
        myProfilePic = view.findViewById(R.id.myProfilePic)
        editProfPicBtn = view.findViewById(R.id.editProfPicBtn)

        // Fetch user data from Realtime Database
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            usernameText.text = user.name
                            userTypeText.text = user.userType
                            nameEditText.setText(user.name)
                            phoneNumberEditText.setText(user.phoneNumber)
                            emailEditText.setText(user.email)

                            if(user.userType=="NGO") {
                                userTypeText.text = "Non Governmental Organization"
                            }
                            // Load profile picture if available
                            if (!user.pfp.isNullOrEmpty()) {
                                loadProfilePicture(user.pfp)
                            }
                        }

                    } else {
                        Log.d("Profile", "No such user")
                    }
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Profile", "get failed with ", databaseError.toException())
                }
            })
        }

        editProfPicBtn.setOnClickListener {
            openGallery()
        }

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (userId != null && name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty()) {
                // Get profile picture URL from Firebase Storage
                val profilePictureUrl = "profile_pictures/$userId.jpg" // Assuming this is the correct path

                val userUpdates = mapOf(
                    "name" to name,
                    "phoneNumber" to phoneNumber,
                    "email" to email,
                    "pfp" to profilePictureUrl // Add profile picture URL here
                )
                database.child("users").child(userId).updateChildren(userUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            // Upload profile picture to Firebase Storage only if it has been changed
                            if (currentProfilePictureUri != null) {
                                uploadProfilePicture(userId) // Call uploadProfilePicture only if profile picture has been changed
                            }
                        } else {
                            Toast.makeText(context, "Profile update failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }



    }
    private fun uploadProfilePicture(userId: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_pictures").child("$userId.jpg")

        // Get the data from ImageView as bytes
        myProfilePic.isDrawingCacheEnabled = true
        myProfilePic.buildDrawingCache()
        val bitmap = (myProfilePic.drawable).toBitmap()
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // Upload the image to Firebase Storage
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, get the download URL
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                // Update the database with the download URL
                database.child("users").child(userId).child("pfp").setValue(uri.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Profile", "Profile picture uploaded and database updated")
                        } else {
                            Log.e("Profile", "Error updating database with profile picture URL: ${task.exception}")
                        }
                    }
            }.addOnFailureListener { e ->
                Log.e("Profile", "Error getting download URL: $e")
            }
        }.addOnFailureListener { e ->
            Log.e("Profile", "Error uploading profile picture: $e")
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            currentProfilePictureUri = imageUri // Update currentProfilePictureUri
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                myProfilePic.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun loadProfilePicture(profilePictureUrl: String) {
        // Use a library like Glide or Picasso to load the image into the CircleImageView
        Glide.with(this /* or your activity */)
            .load(profilePictureUrl)
            .placeholder(R.drawable.pfp) // Placeholder image while loading
            .error(R.drawable.pfp) // Image to show if loading fails
            .into(myProfilePic)
    }


}
