package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class Settings : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val intent = Intent(activity, SigninActivity::class.java)
            startActivity(intent)
        }

    }
}
