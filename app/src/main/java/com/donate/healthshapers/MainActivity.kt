package com.donate.healthshapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSlide1 = findViewById<ImageButton>(R.id.buttonSlide1)
        buttonSlide1.setOnClickListener{
            val Intent = Intent(this,Slide2Activity::class.java)
            startActivity(Intent)
        }
    }
}
