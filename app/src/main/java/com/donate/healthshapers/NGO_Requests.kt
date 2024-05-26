package com.donate.healthshapers

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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

class NGO_Requests : AppCompatActivity(),  AdapterClass.OnItemClickListener{

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList: ArrayList<DataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_requests)

        val ydBackButton = findViewById<ImageButton>(R.id.front_page_back_button)
        ydBackButton.setOnClickListener {
            val intent = Intent(this, NgoFrontPage::class.java)
            startActivity(intent)
        }

        userRecyclerview = findViewById(R.id.ngoRequestRecycler)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf()
        val checker = intent.getStringExtra("itemName")
        if (checker == null ){
            userRecyclerview.visibility = View.GONE
        } else {
            userRecyclerview.visibility = View.VISIBLE
            // Retrieve data passed from Confirm_requests activity
            val itemName = intent.getStringExtra("itemName")
            val timeOfPreparation = intent.getStringExtra("timeOfPreparation")
            val quantity = intent.getStringExtra("quantity")
            val address = intent.getStringExtra("address")
            val utensilsRequired = intent.getBooleanExtra("utensilsRequired", false)
            val imageUrl = intent.getStringExtra("imageUrl")

            // Create a DataClass instance and add it to the userArrayList
            val data = DataClass().apply {
                this.itemName = itemName
                this.timeOfPreparation = timeOfPreparation
                this.quantity = quantity
                this.address = address
                this.utensilsRequired = utensilsRequired
                this.imageUrl = imageUrl
            }
            userArrayList.add(data)

            // Initialize your RecyclerView adapter and set it to the RecyclerView
            val adapter = AdapterClass(userArrayList, this)
            userRecyclerview.adapter = adapter
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
        val timePrepTextView = dialogView.findViewById<TextView>(R.id.dialogTimePrep)
        val quantityTextView = dialogView.findViewById<TextView>(R.id.dialogQuantity)
        val addressTextView = dialogView.findViewById<TextView>(R.id.dialogAddress)
        val utensilsRequiredTextView = dialogView.findViewById<TextView>(R.id.dialogAddressUtensilRequired)
        val deleteButton = dialogView.findViewById<TextView>(R.id.deleteButton)
        deleteButton.visibility = View.GONE
        // Set data to the views
        itemNameTextView.text = data.itemName
        timePrepTextView.text = data.timeOfPreparation
        quantityTextView.text = data.quantity
        addressTextView.text = data.address
        utensilsRequiredTextView.text = data.utensilsRequired.toString()
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
        dialogBuilder.setView(dialogView)
            .setTitle("Donation Details")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}