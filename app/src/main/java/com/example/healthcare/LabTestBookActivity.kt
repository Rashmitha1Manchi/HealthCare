package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class LabTestBookActivity : ComponentActivity() {

    private lateinit var edName: EditText
    private lateinit var edAddress: EditText
    private lateinit var edContact: EditText
    private lateinit var edPinCode: EditText
    private lateinit var btnBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_test_book)

        edName = findViewById(R.id.editTextLTBFullName)
        edAddress = findViewById(R.id.editTextLTBAddress)
        edContact = findViewById(R.id.editTextLTBContactNumber)
        edPinCode = findViewById(R.id.editTextLTBPinCode)
        btnBooking = findViewById(R.id.buttonBMBBook)

        val it = intent
        val price = it.getStringExtra("price")?.split(":") ?: emptyList()
        val date = it.getStringExtra("date")
        val time = it.getStringExtra("time")

        btnBooking.setOnClickListener {
            val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", "") ?: ""

            val db = Database(applicationContext, "healthcare.db", null, 1)
            db.addOrder(
                username,
                edName.text.toString(),
                edAddress.text.toString(),
                edContact.text.toString(),
                edPinCode.text.toString(),
                date.toString(),
                time.toString(),
                price[1].toFloatOrNull() ?: 0f,
                "Lab"
            )
            db.removeCart(username, "Lab")

            Toast.makeText(applicationContext, "Your booking is done successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}