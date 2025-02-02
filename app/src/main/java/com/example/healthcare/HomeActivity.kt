package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username","") ?: ""
        Toast.makeText(applicationContext,"Welcome $username",Toast.LENGTH_SHORT).show()

        val cardExit = findViewById<CardView>(R.id.cardExit)
        cardExit.setOnClickListener{

            with(sharedPreferences.edit()){
                clear()
                apply()
            }

            startActivity(Intent(this, LoginActivity::class.java))
        }

        val findDoctor = findViewById<CardView>(R.id.cardFindDoctor)
        findDoctor.setOnClickListener{
            startActivity(Intent(this, FindDoctorActivity::class.java))
        }

        val labTest = findViewById<CardView>(R.id.cardLabTest)
        labTest.setOnClickListener{
            startActivity(Intent(this, LabTestActivity::class.java))
        }

        val orderDetails = findViewById<CardView>(R.id.cardOrderDetails)
        orderDetails.setOnClickListener{
            startActivity(Intent(this, OrderDetailsActivity::class.java))
        }

        val buyMedicine = findViewById<CardView>(R.id.cardBuyMedicine)
        buyMedicine.setOnClickListener{
            startActivity(Intent(this, BuyMedicineActivity::class.java))
        }

        val healthArticles = findViewById<CardView>(R.id.cardHealthArticles)
        healthArticles.setOnClickListener{
            startActivity(Intent(this, HealthArticlesActivity::class.java))
        }
    }
}