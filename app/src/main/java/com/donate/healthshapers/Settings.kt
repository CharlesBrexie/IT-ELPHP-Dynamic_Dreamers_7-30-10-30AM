package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth

class Settings : Fragment(R.layout.fragment_settings) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

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
    }
}
