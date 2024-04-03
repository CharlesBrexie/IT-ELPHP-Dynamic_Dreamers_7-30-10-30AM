package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Slide3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide3)

        val buttonSlide3 = findViewById<ImageButton>(R.id.buttonSlide3)
        buttonSlide3.setOnClickListener {
            val Intent = Intent(this, Slide4Activity::class.java)
            startActivity(Intent)
        }
    }
}
