package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NgoFrontPage : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_front_page)

        val dashboardFragment = NGODashboard()
        val donationListFragment = Donation_List()
        val settingsFragment = Settings()
        val profileFragment = Profile()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

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
            addToBackStack(null)
            commit()
        }
}