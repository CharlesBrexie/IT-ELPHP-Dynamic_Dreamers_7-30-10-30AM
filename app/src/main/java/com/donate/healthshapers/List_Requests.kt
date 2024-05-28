package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class List_Requests : AppCompatActivity(), RequestAdapterClass.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList: ArrayList<DataClass>
    private lateinit var requestsAdapter: RequestAdapterClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_requests)

        val ydBackButton = findViewById<ImageButton>(R.id.front_page_back_button)
        ydBackButton.setOnClickListener {
            val intent = Intent(this, Your_donations::class.java)
            startActivity(intent)
        }

        // Receive intent extras
        val itemName = intent.getStringExtra("itemName")
        val charity = intent.getStringExtra("charity")
        val timeOfPreparation = intent.getStringExtra("timeOfPreparation")
        val quantity = intent.getStringExtra("quantity")
        val address = intent.getStringExtra("address")
        val utensilsRequired = intent.getBooleanExtra("utensilsRequired", false)
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        // Create a DataClass instance and assign values
        val clickedDonation = DataClass().apply {
            this.itemName = itemName
            this.timeOfPreparation = timeOfPreparation
            this.quantity = quantity
            this.address = address
            this.utensilsRequired = utensilsRequired
            this.imageUrl = imageUrl
            this.charity = charity
        }

        userRecyclerview = findViewById(R.id.listRequestRecycler)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf<DataClass>()

        // Set up the RecyclerView adapter
        requestsAdapter = RequestAdapterClass(userArrayList, this)
        userRecyclerview.adapter = requestsAdapter

        // Fetch confirmed donations that match the clicked donation details
        fetchConfirmedDonations(clickedDonation)
    }

    private fun fetchConfirmedDonations(clickedDonation: DataClass?) {
        clickedDonation?.let {
            dbref = FirebaseDatabase.getInstance().getReference("NGO_Confirmed_donations")

            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val matchingDonations = ArrayList<DataClass>()
                    for (donationSnapshot in snapshot.children) {
                        val donation = donationSnapshot.getValue(DataClass::class.java)
                        donation?.let {
                            if (it.itemName == clickedDonation.itemName &&
                                it.timeOfPreparation == clickedDonation.timeOfPreparation &&
                                it.quantity == clickedDonation.quantity &&
                                it.address == clickedDonation.address &&
                                it.utensilsRequired == clickedDonation.utensilsRequired &&
                                it.charity == clickedDonation.charity
                            ) {
                                matchingDonations.add(it)
                            }
                        }
                    }
                    // Update the adapter with the fetched data
                    requestsAdapter.updateData(matchingDonations)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }


    override fun onItemClick(data: DataClass) {
        Log.d("ItemClick", "Item clicked: ${data.itemName}")

        // Log the imageUrl from the clicked DataClass
        Log.d("ImageUrl", "Clicked item imageUrl: ${data.imageUrl}")
        showCustomDialog(data)
    }

    private fun showCustomDialog(data: DataClass) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.rl_custom_dialogbox, null)

        // Access the views in your custom dialog layout
        val userNameTextView = dialogView.findViewById<TextView>(R.id.dialogUserName)
        val userTypeTextView = dialogView.findViewById<TextView>(R.id.dialogUserType)
        val phoneNumTextView = dialogView.findViewById<TextView>(R.id.dialogPhoneNum)

        userNameTextView.text = data.itemName
        userTypeTextView.text = data.timeOfPreparation
        phoneNumTextView.text = data.quantity

        val imageView = dialogView.findViewById<ImageView>(R.id.pfp) // ImageView in dialog

        // Load image using Picasso library
        if (!data.imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(data.imageUrl)
                .placeholder(R.drawable.pfp) // Placeholder image while loading
                .error(R.drawable.pfp) // Error image if loading fails
                .into(imageView)
        } else {
            // Load placeholder image if imageUrl is null or empty
            imageView.setImageResource(R.drawable.pfp)
        }

        // Create and show the dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val alertDialog = dialogBuilder.setView(dialogView)
            .setTitle("Donation Details")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}
