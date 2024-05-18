package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Dashboard : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mdButton = view.findViewById<Button>(R.id.make_a_donation_button)
        mdButton.setOnClickListener {
            val intent = Intent(activity, New_donation::class.java)
            startActivity(intent)
        }

        val ydButton = view.findViewById<Button>(R.id.your_donations_button)
        ydButton.setOnClickListener {
            val intent = Intent(activity, Your_donations::class.java)
            startActivity(intent)
        }
    }
}
