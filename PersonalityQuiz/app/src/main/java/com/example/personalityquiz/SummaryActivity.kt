package com.example.personalityquiz

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val resultText = intent.getStringExtra("resultText") ?: ""
        val personalityType = intent.getStringExtra("personalityType") ?: ""

        val textViewResult = findViewById<TextView>(R.id.textViewResult)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        textViewResult.text = resultText

        val imageRes = when(personalityType) {
            "Introwertyk" -> R.drawable.introwertyk
            "Ekstrawertyk" -> R.drawable.ekstrawertyk
            "Ambiwertyk"  -> R.drawable.ambiwertyk
            else -> R.drawable.facesmile
        }

        imageView.setImageResource(imageRes)
    }
}
