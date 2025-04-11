package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

class LabTestCVVActivity : ComponentActivity() {

    private lateinit var cvv: EditText
    private lateinit var dbHelper: Database
    private lateinit var btnNxt : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab_test_cvv)

        dbHelper = Database(this, "healthcare.db", null, 1)
        cvv = findViewById(R.id.editTextLTCVV)
        btnNxt = findViewById(R.id.buttonLTCVVSubmit)

        btnNxt.setOnClickListener {
            val accountNo = intent.getStringExtra("account_no").toString()
        //    val expDate = intent.getStringExtra("expiry_date").toString()

            val name = intent.getStringExtra("name")
            val address = intent.getStringExtra("address")
            val contact = intent.getStringExtra("contact")
            val pincode = intent.getStringExtra("pincode")
            val price = intent.getStringExtra("price")?.toFloatOrNull() ?: 0f
            Log.d("LabTestCVVActivity", "$price")
            val date = intent.getStringExtra("date")
            val time = intent.getStringExtra("time")

            val hashedCVV = dbHelper.hashData(cvv.text.toString())

            if(dbHelper.verifyCVV(accountNo, hashedCVV, dbHelper)){
                val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
                val username = sharedPreferences.getString("username", "") ?: ""

                if (dbHelper.processPayment(username, dbHelper.hashData(accountNo), price.toDouble(), "lab")){
                    dbHelper.addOrder(
                        username,
                        name.toString(),
                        address.toString(),
                        contact.toString(),
                        pincode.toString(),
                        date.toString(),
                        time.toString(),
                        price,
                        "Lab"
                    )
                    dbHelper.removeCart(username, "Lab")

                    Toast.makeText(this, "Payment Successful! Lab Test Booked", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                else{
                    Toast.makeText(this, "Payment Failed! Insufficient Balance", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Invalid CVV", Toast.LENGTH_SHORT).show()
            }
        }
    }
}