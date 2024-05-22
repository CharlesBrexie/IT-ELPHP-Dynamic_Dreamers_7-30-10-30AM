package com.donate.healthshapers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : Fragment(R.layout.fragment_profile) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val nameEditText = view.findViewById<EditText>(R.id.name)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.phoneNumber)
        val emailEditText = view.findViewById<EditText>(R.id.Email)
        val submitButton = view.findViewById<Button>(R.id.submit)

        // Fetch user data from Realtime Database
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            nameEditText.setText(user.name)
                            phoneNumberEditText.setText(user.phoneNumber)
                            emailEditText.setText(user.email)
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

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (userId != null && name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty()) {
                val userUpdates = mapOf(
                    "name" to name,
                    "phoneNumber" to phoneNumber,
                    "email" to email
                )
                database.child("users").child(userId).updateChildren(userUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Profile update failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
