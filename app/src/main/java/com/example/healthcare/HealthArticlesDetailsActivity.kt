package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthcare.ui.theme.HealthCareTheme

class HealthArticlesDetailsActivity : ComponentActivity() {

    private lateinit var tv1: TextView
    private lateinit var img: ImageView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_articles_details)

        tv1 = findViewById(R.id.textViewHAD1)
        img = findViewById(R.id.imageViewHAD)
        btnBack = findViewById(R.id.buttonHADBack)

        btnBack.setOnClickListener{
            startActivity(Intent(this, HealthArticlesActivity::class.java))
        }

        val intent = intent
        tv1.text = intent.getStringExtra("text1")

        val bundle = intent.extras
        if (bundle != null) {
            val resId = bundle.getInt("text2")
            img.setImageResource(resId)
        }
    }
}