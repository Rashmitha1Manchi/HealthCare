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

class AppointmentCVVActivity : ComponentActivity() {

    private lateinit var cvv: EditText
    private lateinit var dbHelper: Database
    private lateinit var btnNxt : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medicine_cvv)

        dbHelper = Database(this, "healthcare.db", null, 1)
        cvv = findViewById(R.id.editTextAcntNo)
        btnNxt = findViewById(R.id.buttonCVVSubmit)

        btnNxt.setOnClickListener {
            val username = intent.getStringExtra("username").toString()
            val accountNo = intent.getStringExtra("account_no").toString()
            val desName = intent.getStringExtra("desName").toString()
            val address = intent.getStringExtra("address").toString()
            val contact = intent.getStringExtra("contact").toString()
            val fees = intent.getStringExtra("fees")?.toFloatOrNull() ?: 0f
//            Log.d("AppointmentPaymentActivity", "${fees}")
            val date = intent.getStringExtra("date").toString()
            val time = intent.getStringExtra("time").toString()
        //    val orderType = intent.getStringExtra("orderType")

            val hashedCvv = dbHelper.hashData(cvv.text.toString())

            if (dbHelper.verifyCVV(accountNo, hashedCvv, dbHelper)) {
                if (dbHelper.processPayment(username, dbHelper.hashData(accountNo), fees.toDouble(), "Appointment")) {
                    dbHelper.addOrder(username, desName, address, contact,"" , date, time, fees, "Appointment")

                    Toast.makeText(this, "Payment Successful! Appointment Booked", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                else {
                    Toast.makeText(this, "Payment Failed! Insufficient Balance", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Invalid CVV", Toast.LENGTH_SHORT).show()
            }
        }
    }
}