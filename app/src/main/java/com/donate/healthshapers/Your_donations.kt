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

class Your_donations : AppCompatActivity(),  AdapterClass.OnItemClickListener{

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<DataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_donations)

        val ydBackButton = findViewById<Button>(R.id.front_page_back_button)
        ydBackButton.setOnClickListener {
            val intent = Intent(this, RestaurantFrontPage::class.java)
            startActivity(intent)
        }

        val pfBtn = findViewById<ImageView>(R.id.profileButton)
        pfBtn.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
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
        showCustomDialog(data)
    }

    private fun showCustomDialog(data: DataClass) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.yd_custom_dialogbox, null)

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
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
            .setTitle("Donation Details")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}