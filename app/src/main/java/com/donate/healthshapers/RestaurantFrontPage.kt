package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class RestaurantFrontPage : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_front_page)

        val dashboardFragment = Dashboard()
        val donationListFragment = Donation_List()
        val settingsFragment = Settings()
        val profileFragment = Profile()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
//
//        val homeButton = findViewById<Button>(R.id.homebtn)
//        val dlButton = findViewById<Button>(R.id.donation_list_button)

//        val mdButton = findViewById<Button>(R.id.make_a_donation_button)
//        mdButton.setOnClickListener{
//            val intent = Intent(this, New_donation::class.java)
//            startActivity(intent)
//        }
//
//        val ydButton = findViewById<Button>(R.id.your_donations_button)
//        ydButton.setOnClickListener{
//            val intent = Intent(this, Your_donations::class.java)
//            startActivity(intent)
//        }

//        val dlButton = findViewById<Button>(R.id.donation_list_button)
//        dlButton.setOnClickListener{
//            val intent = Intent(this, Available_donations::class.java)
//            startActivity(intent)
//        }
//        val pfBtn = findViewById<Button>(R.id.iconButton)
//        pfBtn.setOnClickListener{
//            val intent = Intent(this, Profile::class.java)
//            startActivity(intent)
//        }

//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.flFragment, dashboardFragment)
//            commit()
//        }
//        homeButton.setOnClickListener{
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.flFragment, dashboardFragment)
//                addToBackStack(null)
//                commit()
//            }
//        }
//        dlButton.setOnClickListener{
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.flFragment, donationListFragment)
//                addToBackStack(null)
//                commit()
//            }
//        }
        setCurrentFragment(dashboardFragment)

        bottomNavigationView.setOnItemSelectedListener  {
            when (it.itemId) {
                R.id.homebtn -> {
                    setCurrentFragment(dashboardFragment)
                    true
                }
                R.id.donation_list_button -> {
                    setCurrentFragment(donationListFragment)
                    true
                }
                R.id.iconButton -> {
                    setCurrentFragment(settingsFragment)
                    true
                }
                R.id.profileButton -> {
                    setCurrentFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}

