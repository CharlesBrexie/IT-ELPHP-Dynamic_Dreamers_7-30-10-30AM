package com.donate.healthshapers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay of 2 seconds
        Handler().postDelayed({ // Start LoginActivity
            val intent = Intent(this@SplashActivity, SigninActivity::class.java)
            startActivity(intent)
            // Close SplashActivity
            finish()
        }, 2000) // 2000 milliseconds = 2 seconds
    }
}