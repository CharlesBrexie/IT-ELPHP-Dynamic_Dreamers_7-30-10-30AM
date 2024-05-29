package com.donate.healthshapers

import RequestAdapterClass
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*

class List_Requests : AppCompatActivity(), RequestAdapterClass.OnItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var requestsAdapter: RequestAdapterClass
    private lateinit var userDonationIds: List<String> // Store user's donation IDs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_requests)

        auth = Firebase.auth

        val ydBackButton = findViewById<ImageButton>(R.id.front_page_back_button)
        ydBackButton.setOnClickListener {
            val intent = Intent(this, Your_donations::class.java)
            startActivity(intent)
        }

        userRecyclerview = findViewById(R.id.listRequestRecycler)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        requestsAdapter = RequestAdapterClass(ArrayList(), this)
        userRecyclerview.adapter = requestsAdapter

        // Fetch user's donation IDs from Firebase
        fetchUserDonationIds()
    }

    private fun fetchUserDonationIds() {
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            // Initialize database reference for user's donations
            val userDonationRef = FirebaseDatabase.getInstance().getReference("donations").child(uid)

            // Fetch user's donation IDs
            userDonationRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Retrieve user's donation IDs and store them
                    val userDonationIds = mutableListOf<String>()
                    snapshot.children.forEach { donationSnapshot ->
                        donationSnapshot.key?.let { donationId ->
                            userDonationIds.add(donationId)
                        }
                    }

                    // After fetching user's donation IDs, fetch request data
                    fetchRequestData(userDonationIds)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read user's donation IDs from Firebase: ${error.message}")
                }
            })
        }
    }

    private fun fetchRequestData(userDonationIds: List<String>) {
        dbref = FirebaseDatabase.getInstance().getReference("Requests")

        // Fetch request data from Firebase
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val requests = mutableListOf<RequestData>()

                // Iterate through request data
                for (requestSnapshot in snapshot.children) {
                    val requestData = requestSnapshot.getValue(RequestData::class.java)
                    requestData?.let {
                        // If the request's donation ID is in the user's donation IDs, add it to the list
                        if (userDonationIds.contains(requestData.donationId)) {
                            requests.add(requestData)
                        }
                    }
                    // Log here to check if requestData is not null
                    Log.d(TAG, "RequestData: $requestData")
                }
                // Update RecyclerView with filtered requests
                requestsAdapter.updateData(requests as ArrayList<RequestData>)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read request data from Firebase: ${error.message}")
            }
        })
    }
    private fun showCustomDialog(data: RequestData) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.rl_custom_dialogbox, null)
        // Access the views in your custom dialog layout
        val userNameTextView = dialogView.findViewById<TextView>(R.id.dialogUserName)
        val userTypeTextView = dialogView.findViewById<TextView>(R.id.dialogUserType)
        val phoneNumTextView = dialogView.findViewById<TextView>(R.id.dialogPhoneNum)
        userNameTextView.text = data.username
        userTypeTextView.text = data.userType
        phoneNumTextView.text = data.phoneNumber

        // Create and show the dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val alertDialog = dialogBuilder.setView(dialogView)
            .setTitle("Donation Details")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()

        val acceptBtn = dialogView.findViewById<Button>(R.id.acceptButton)
        acceptBtn.setOnClickListener {
            // Update the status in the NGO Confirmed Donation and Requests nodes
            updateStatusInFirebase(data)

            // Start Your_donations activity
            val intent = Intent(this, Your_donations::class.java)
            startActivity(intent)

            // Dismiss the dialog
            alertDialog.dismiss()

        }
    }

    private fun updateStatusInFirebase(data: RequestData) {
        updateNGOConfirmedDonationStatus(data)
        updateRequestStatus(data)
    }

    private fun updateNGOConfirmedDonationStatus(data: RequestData) {
        val ngoConfirmedDonationRef = FirebaseDatabase.getInstance().getReference("NGO Confirmed Donations")
        ngoConfirmedDonationRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { userSnapshot ->
                    userSnapshot.children.forEach { donationSnapshot ->
                        if (donationSnapshot.child("donationId").getValue(String::class.java) == data.donationId) {
                            donationSnapshot.ref.child("status").setValue("Confirmed")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to update status in NGO Confirmed Donation: ${error.message}")
            }
        })
    }

    private fun updateRequestStatus(data: RequestData){
        val requestsRef = FirebaseDatabase.getInstance().getReference("Requests")

        // Query the "Requests" node to find the entry with matching donationId
        val query = requestsRef.orderByChild("donationId").equalTo(data.donationId)

        // Execute the query
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check if the snapshot has any matching entry
                if (snapshot.exists()) {
                    // Loop through each matching entry (although there should be only one)
                    for (requestSnapshot in snapshot.children) {
                        // Update the status to "confirmed"
                        requestSnapshot.ref.child("status").setValue("Confirmed")
                    }
                } else {
                    // Handle the case where no matching entry is found
                    Log.d(TAG, "No matching entry found in Requests for donationId: ${data.donationId}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to update status in Requests: ${error.message}")
            }
        })
    }

    override fun onItemClick(data: RequestData) {
        Log.d(TAG, "Item clicked: $data")
        showCustomDialog(data)
    }
}