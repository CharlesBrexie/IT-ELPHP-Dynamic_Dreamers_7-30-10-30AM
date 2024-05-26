package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class NGODashboard : Fragment(R.layout.fragment_dashboard_ngo) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yrButton = view.findViewById<Button>(R.id.your_requests)
        yrButton.setOnClickListener {
            val intent = Intent(activity, NGO_Requests::class.java)
            startActivity(intent)
        }

    }

}
