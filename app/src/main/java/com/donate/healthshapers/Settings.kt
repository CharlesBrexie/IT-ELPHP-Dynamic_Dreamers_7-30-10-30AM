package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class Settings : Fragment(R.layout.fragment_settings) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var myProfilePic: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        myProfilePic = view.findViewById(R.id.myProfilePic)
        val userTypeText = view.findViewById<TextView>(R.id.userType)

        // Initialize views and set click listeners
        val abtUsBtn = view.findViewById<LinearLayout>(R.id.about)
        abtUsBtn.setOnClickListener {
            val intent = Intent(activity, About_us::class.java)
            startActivity(intent)
        }

        val trmsBtn = view.findViewById<LinearLayout>(R.id.terms)
        trmsBtn.setOnClickListener {
            val intent = Intent(activity, Terms_and_condition::class.java)
            startActivity(intent)
        }

        val logoutButton = view.findViewById<LinearLayout>(R.id.logout)
        logoutButton.setOnClickListener {
            // Sign out the user from Firebase
            auth.signOut()

            // Redirect to the login screen
            val intent = Intent(activity, SigninActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val usernameEditText = view.findViewById<TextView>(R.id.userNameMain)

        // Fetch user data from Realtime Database
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(DataClass::class.java)
                        if (user != null) {
                            usernameEditText.text = user.name
                            userTypeText.text = user.userType

                            if (user.userType == "NGO") {
                                userTypeText.text = "Non Governmental Organization"
                            }

                            // Load profile picture if available
                            val profilePictureUrl = user.pfp
                            if (!profilePictureUrl.isNullOrEmpty()) {
                                loadProfilePicture(profilePictureUrl)
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
    }

    private fun loadProfilePicture(profilePictureUrl: String) {
        // Use a library like Glide or Picasso to load the image into the ImageView
        Glide.with(this)
            .load(profilePictureUrl)
            .placeholder(R.drawable.pfp) // Placeholder image while loading
            .error(R.drawable.pfp)       // Image to show if loading fails
            .into(myProfilePic)
    }
}
