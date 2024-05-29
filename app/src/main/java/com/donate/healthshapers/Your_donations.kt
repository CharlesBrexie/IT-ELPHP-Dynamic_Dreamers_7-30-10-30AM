package com.donate.healthshapers

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class Your_donations : AppCompatActivity(),  AdapterClass.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList: ArrayList<DataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_donations)


        val ydBackButton = findViewById<ImageButton>(R.id.new_donations_back_button)
        ydBackButton.setOnClickListener {
            val intent = Intent(this, RestaurantFrontPage::class.java)
            startActivity(intent)
        }

        userRecyclerview = findViewById(R.id.yourdonationsRecycler)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf<DataClass>()

        getUserData()
    }

    private fun getUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            dbref = FirebaseDatabase.getInstance().getReference("donations").child(userId)

            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userArrayList.clear()
                    for (donationSnapshot in snapshot.children) {
                        val donation = donationSnapshot.getValue(DataClass::class.java)
                        donation?.let {
                            userArrayList.add(it)
                        }
                    }
                    userRecyclerview.adapter = AdapterClass(userArrayList, this@Your_donations)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read data from Firebase: ${error.message}")
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.yd_custom_dialogbox, null)

        // Access the views in your custom dialog layout
        val itemNameTextView = dialogView.findViewById<TextView>(R.id.dialogItemName)
        val timeOfPrepTextView = dialogView.findViewById<TextView>(R.id.dialogTimePrep)
        val quantityTextView = dialogView.findViewById<TextView>(R.id.dialogQuantity)
        val addressTextView = dialogView.findViewById<TextView>(R.id.dialogAddress)
        val utensilsReqTextView = dialogView.findViewById<TextView>(R.id.dialogUtensilRequired)
        val charityTextView =
            dialogView.findViewById<TextView>(R.id.dialogCharity) // New TextView for charity

        val donationIdTextView = dialogView.findViewById<TextView>(R.id.dialogDonationID)

        // Set data to the views
        itemNameTextView.text = data.itemName
        timeOfPrepTextView.text = data.timeOfPreparation
        quantityTextView.text = data.quantity
        addressTextView.text = data.address
        utensilsReqTextView.text = data.utensilsRequired.toString()
        charityTextView.text = data.charity // Set charity name
        donationIdTextView.text = data.donationId

        // Set other data to other views...

        val imageView = dialogView.findViewById<ImageView>(R.id.imahe) // ImageView in dialog

        if (!data.imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(data.imageUrl)
                .placeholder(R.drawable.healthshapers) // Placeholder image while loading
                .error(R.drawable.healthshapers) // Error image if loading fails
                .into(imageView)
        } else {
            // Load placeholder image if imageUrl is null or empty
            imageView.setImageResource(R.drawable.healthshapers)
        }

        // Create and show the dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val alertDialog = dialogBuilder.setView(dialogView)
            .setTitle("Donation Details")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        // Handle delete button click
        val deleteButton = dialogView.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            // Get the current user ID
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                // Reference to the user's donations
                val userDonationsRef =
                    FirebaseDatabase.getInstance().getReference("donations").child(userId)

                // Query to find the donation to delete based on its properties
                val query = userDonationsRef.orderByChild("itemName").equalTo(data.itemName)

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Iterate over the matching donations
                        for (donationSnapshot in snapshot.children) {
                            // Delete the first matching donation found
                            donationSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    Log.d(TAG, "Donation deleted successfully")
                                    alertDialog.dismiss() // Dismiss the dialog after deletion
                                    return@addOnSuccessListener
                                }
                                .addOnFailureListener { exception ->
                                    Log.e(TAG, "Error deleting donation: ${exception.message}")
                                    // Handle error deleting donation
                                    return@addOnFailureListener
                                }
                        }
                        // If no matching donation found
                        Log.e(TAG, "Donation not found")
                        // Handle case where no matching donation is found
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Error querying donations: ${error.message}")
                        // Handle error querying donations
                    }
                })
            }
        }

        val requestListButton = dialogView.findViewById<Button>(R.id.requestListButton)
        requestListButton.setOnClickListener {
            // Start the Confirm_requests activity
            val intent = Intent(this@Your_donations, List_Requests::class.java)
            // Pass any necessary data to the List_requests activity using intent extras
            intent.putExtra("itemName", data.itemName)
            intent.putExtra("timeOfPreparation", data.timeOfPreparation)
            intent.putExtra("quantity", data.quantity)
            intent.putExtra("address", data.address)
            intent.putExtra("utensilsRequired", data.utensilsRequired)
            intent.putExtra("imageUrl", data.imageUrl)
            intent.putExtra("charity", data.charity)
            startActivity(intent)
        }

        alertDialog.show()
    }
}