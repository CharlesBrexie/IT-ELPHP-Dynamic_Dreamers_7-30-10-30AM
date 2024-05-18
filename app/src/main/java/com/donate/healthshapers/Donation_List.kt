package com.donate.healthshapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Donation_List : Fragment(R.layout.fragment_donation_list), AdapterClass.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList: ArrayList<DataClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_list, container, false)

        userRecyclerview = view.findViewById(R.id.allDonationRecycler)
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf<DataClass>()

        // Call function to fetch data from Firebase
        getAllUserData()

        return view
    }

    private fun getAllUserData() {
        // Get a reference to the location in the database where all user donations are stored
        val dbref = FirebaseDatabase.getInstance().getReference("donations")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allUserArrayList = ArrayList<DataClass>() // Initialize a list to hold data from all users
                for (userSnapshot in snapshot.children) {
                    // Loop through each user's donations
                    for (donationSnapshot in userSnapshot.children) {
                        val donation = donationSnapshot.getValue(DataClass::class.java)
                        donation?.let {
                            allUserArrayList.add(it)
                        }
                    }
                }
                // Update your RecyclerView adapter with data from all users
                userRecyclerview.adapter = AdapterClass(allUserArrayList, this@Donation_List)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read data from Firebase: ${error.message}")
            }
        })
    }

    override fun onItemClick(data: DataClass) {
        showCustomDialog(data)
    }

    private fun showCustomDialog(data: DataClass) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.yd_custom_dialogbox, null)

        // Access the views in your custom dialog layout
        val itemNameTextView = dialogView.findViewById<TextView>(R.id.dialogItemName)
        val timePrepTextView = dialogView.findViewById<TextView>(R.id.dialogTimePrep)
        val quantityTextView = dialogView.findViewById<TextView>(R.id.dialogQuantity)
        val addressTextView = dialogView.findViewById<TextView>(R.id.dialogAddress)
        val utensilsRequiredTextView = dialogView.findViewById<TextView>(R.id.dialogAddressUtensilRequired)

        // Set data to the views
        itemNameTextView.text = data.itemName
        timePrepTextView.text = data.timeOfPreparation
        quantityTextView.text = data.quantity
        addressTextView.text = data.address
        utensilsRequiredTextView.text = data.utensilsRequired.toString()

        // Create and show the dialog
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)
            .setTitle("Donation Details")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
