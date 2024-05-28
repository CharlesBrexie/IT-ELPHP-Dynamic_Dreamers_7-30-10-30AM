package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class Donation_List : Fragment(R.layout.fragment_donation_list), AdapterClass.OnItemClickListener {

    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList: ArrayList<DataClass>
    private var currentUserType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_list, container, false)

        userRecyclerview = view.findViewById(R.id.allDonationRecycler)
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf()

        // Call function to fetch current user's userType
        fetchCurrentUserType()

        // Call function to fetch data from Firebase
        getAllUserData()

        return view
    }

    private fun fetchCurrentUserType() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid
        if (currentUserId != null) {
            val userTypeRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("userType")
            userTypeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUserType = snapshot.getValue(String::class.java)
                    Log.d(TAG, "Current User Type: $currentUserType")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read current userType from Firebase: ${error.message}")
                }
            })
        }
    }

    private fun getAllUserData() {
        val dbref = FirebaseDatabase.getInstance().getReference("donations")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allUserArrayList = ArrayList<DataClass>()
                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key // Get the user ID
                    if (userId != null) {
                        val userTypeRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("userType")
                        userTypeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userTypeSnapshot: DataSnapshot) {
                                val userType = userTypeSnapshot.getValue(String::class.java)
                                for (donationSnapshot in userSnapshot.children) {
                                    val donation = donationSnapshot.getValue(DataClass::class.java)
                                    donation?.let {
                                        // Assign userType to the corresponding DataClass object
                                        it.userType = userType ?: "Default" // Handle null case
                                        Log.d(TAG, "User Type: $userType")
                                        allUserArrayList.add(it)
                                    }
                                }
                                userRecyclerview.adapter = AdapterClass(allUserArrayList, this@Donation_List)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(TAG, "Failed to read userType from Firebase: ${error.message}")
                            }
                        })
                    }
                }
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

        // Access other views in your custom dialog layout
        val itemNameTextView = dialogView.findViewById<TextView>(R.id.dialogItemName)
        val timePrepTextView = dialogView.findViewById<TextView>(R.id.dialogTimePrep)
        val quantityTextView = dialogView.findViewById<TextView>(R.id.dialogQuantity)
        val addressTextView = dialogView.findViewById<TextView>(R.id.dialogAddress)
        val utensilsRequiredTextView = dialogView.findViewById<TextView>(R.id.dialogUtensilRequired)
        val charityTextView = dialogView.findViewById<TextView>(R.id.dialogCharity) // Add TextView for charity
        val requestButton = dialogView.findViewById<Button>(R.id.requestButton) // Request button
        val deleteButton = dialogView.findViewById<Button>(R.id.deleteButton) // Delete button

        // Set data to the views
        itemNameTextView.text = data.itemName
        timePrepTextView.text = data.timeOfPreparation
        quantityTextView.text = data.quantity
        addressTextView.text = data.address
        utensilsRequiredTextView.text = data.utensilsRequired.toString()
        charityTextView.text = data.charity // Set charity name

        val imageView = dialogView.findViewById<ImageView>(R.id.imahe)

        if (!data.imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(data.imageUrl)
                .placeholder(R.drawable.healthshapers)
                .error(R.drawable.healthshapers)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.healthshapers)
        }

        // Conditional visibility for the request button based on userType
        if (currentUserType == "NGO") {
            requestButton.visibility = View.VISIBLE
            requestButton.setOnClickListener {
                // Start the Confirm_requests activity
                val intent = Intent(requireContext(), Confirm_requests::class.java)
                // Pass any necessary data to the Confirm_requests activity using intent extras
                intent.putExtra("itemName", data.itemName)
                intent.putExtra("timeOfPreparation", data.timeOfPreparation)
                intent.putExtra("quantity", data.quantity)
                intent.putExtra("address", data.address)
                intent.putExtra("utensilsRequired", data.utensilsRequired)
                intent.putExtra("imageUrl", data.imageUrl)
                intent.putExtra("charity", data.charity)
                startActivity(intent)
            }
        } else {
            requestButton.visibility = View.GONE
        }

        // Hide the Delete Donation button when not on your_donations view
        if (requireActivity() !is Your_donations) {
            deleteButton.visibility = View.GONE
        }

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)
            .setTitle("Donation Details")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    companion object {
        private const val TAG = "Donation_List"
    }
}
