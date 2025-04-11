package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import java.security.MessageDigest

class MedicineCVVActivity : ComponentActivity() {

    private lateinit var cvv: EditText
    private lateinit var dbHelper: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medicine_cvv)

        dbHelper = Database(this, "healthcare.db", null, 1)
        cvv = findViewById(R.id.editTextAcntNo)

        findViewById<Button>(R.id.buttonCVVSubmit).setOnClickListener {
            val accountNo = intent.getStringExtra("account_no").toString()
            val name = intent.getStringExtra("name") ?: ""
            val address = intent.getStringExtra("address") ?: ""
            val contact = intent.getStringExtra("contact") ?: ""
            val pincode = intent.getStringExtra("pincode") ?: ""
            val price = intent.getStringExtra("price")?.toFloatOrNull() ?: 0f
            val date = intent.getStringExtra("date") ?: ""

            val hashedCvv = dbHelper.hashData(cvv.text.toString())

            if (dbHelper.verifyCVV(accountNo, hashedCvv, dbHelper)) {
                val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
                val username = sharedPreferences.getString("username", "") ?: ""

                if (dbHelper.processPayment(username, dbHelper.hashData(accountNo), price.toDouble(), "Medicine Order")) {
                    dbHelper.addOrder(username, name, address, contact, pincode, date, "", price, "Medicine")
                    dbHelper.removeCart(username, "Medicine")

                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    Toast.makeText(this, "Payment Failed! Insufficient Balance", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid CVV", Toast.LENGTH_SHORT).show()
            }
        }
    }
}