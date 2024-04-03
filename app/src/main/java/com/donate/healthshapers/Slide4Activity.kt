package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Slide4Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide4)

        val buttonSlide4 = findViewById<Button>(R.id.buttonSlide4)
        buttonSlide4.setOnClickListener {
            val Intent = Intent(this, SigninActivity::class.java)
            startActivity(Intent)
        }
    }
}
