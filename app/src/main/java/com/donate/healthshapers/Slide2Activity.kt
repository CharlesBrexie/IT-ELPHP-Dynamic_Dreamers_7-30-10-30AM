package com.michdo.health_shapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Slide2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide2)

        val buttonSlide2 = findViewById<ImageButton>(R.id.buttonSlide2)
        buttonSlide2.setOnClickListener {
            val Intent = Intent(this, Slide3Activity::class.java)
            startActivity(Intent)
        }
    }
}