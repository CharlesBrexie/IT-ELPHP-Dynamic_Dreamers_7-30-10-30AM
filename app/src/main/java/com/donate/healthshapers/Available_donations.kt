package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Available_donations : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList:Array<Int>
    lateinit var titleList:Array<String>
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
            "Paper",
            "Star",
            "ProfileIcon",
            "ProfilePic",
            "RedCrossPfP",
            "LogoutIcon"
        )

        recyclerView = findViewById(R.id.donationRecycler);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<DataClass>()
        getData()
    }
    private fun getData(){
        for (i in imageList.indices){
            val dataClass = DataClass(imageList[i],titleList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = AdapterClass(dataList)
    }

}