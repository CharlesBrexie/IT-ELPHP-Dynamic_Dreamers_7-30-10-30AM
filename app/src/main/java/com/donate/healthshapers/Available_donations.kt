package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Available_donations : AppCompatActivity(), AdapterClass.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList:Array<Int>
    lateinit var titleList:Array<String>
    lateinit var quantityList:Array<Int>
    lateinit var locationList:Array<String>
    lateinit var pickupList:Array<String>
    lateinit var descriptionList:Array<String>
    lateinit var timeSentList:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliable_donations)

        val fpBackButton = findViewById<ImageButton>(R.id.front_page_back_button)
        fpBackButton.setOnClickListener{
            val intent = Intent(this, RestaurantFrontPage::class.java)
            startActivity(intent)
        }

        imageList = arrayOf(
            R.drawable.paper,
            R.drawable.star,
            R.drawable.profileicon,
            R.drawable.profilepic,
            R.drawable.redcrosspfp,
            R.drawable.logouticon
        )

        titleList = arrayOf(
            "Paper Company",
            "Dominic & Co.",
            "Seb's",
            "Siomai sa Tisa",
            "Julies",
            "Orange & Lemons"
        )
        quantityList = arrayOf(
            15,
            30,
            100,
            20,
            25,
            12
        )
        locationList = arrayOf(
            "Kamputhaw",
            "Oppra",
            "Sibonga",
            "Banilad",
            "Pasil",
            "Danao"
        )
        pickupList = arrayOf(
            "2:30pm",
            "1:30pm",
            "10:30am",
            "11:30am",
            "4:30pm",
            "3:30pm"
        )
        descriptionList = arrayOf(
            "15 packets of food in Barangay Kamputhaw Cebu City at 2:30pm for people with disabilities.",
            "30 packets of food in Barangay Oppra Cebu City at 1:30pm",
            "100 packets of food in Barangay Sibonga Cebu City at 1:30pm",
            "20 packets of food in Barangay Banilad Cebu City at 1:30pm",
            "25 packets of food in Barangay Pasil Cebu City at 1:30pm",
            "12 packets of food in Barangay Danao Cebu City at 1:30pm"

        )
        timeSentList = arrayOf(
            "1 hours ago",
            "2 hours ago",
            "3 hours ago",
            "4 hours ago",
            "5 hours ago",
            "6 hours ago"

        )

        recyclerView = findViewById(R.id.donationRecycler);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<DataClass>()
        getData()

    }
    private fun getData(){
        for (i in imageList.indices){
            val dataClass = DataClass(imageList[i],titleList[i],quantityList[i],locationList[i],pickupList[i], descriptionList[i]
            ,timeSentList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = AdapterClass(dataList, this)
    }
    override fun onItemClick(data: DataClass) {
        // Handle item click event here
        showCustomDialog(data)

    }

    private fun showCustomDialog(data: DataClass) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.activity_donation_dialog_box, null)
        dialogBuilder.setView(dialogView)

        // Initialize views in the dialog layout
        val imageImageView = dialogView.findViewById<ImageView>(R.id.imahe)
        val titleTextView = dialogView.findViewById<TextView>(R.id.title)
        val quantityTextView = dialogView.findViewById<TextView>(R.id.quantity)
        val locationTextView = dialogView.findViewById<TextView>(R.id.location)
        val pickupTextView = dialogView.findViewById<TextView>(R.id.pickup_time)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.description)

        // Set data to views
        imageImageView.setImageResource(data.dataImage)
        titleTextView.text = data.dataTitle
        quantityTextView.text = data.dataQuantity.toString()
        locationTextView.text = data.dataLocation
        pickupTextView.text = data.dataPickup
        descriptionTextView.text = data.dataDescription

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

}