package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class AppointmentPaymentActivity : ComponentActivity() {

    private lateinit var nextButton: Button
    private lateinit var acntNo: EditText
    private lateinit var expDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_appointment_payment)

        nextButton = findViewById(R.id.buttonCVVSubmit)
        acntNo = findViewById(R.id.editTextAcntNo)
        expDate = findViewById(R.id.editTextExpiryDate)

        val username = intent.getStringExtra("username")
        val desName = intent.getStringExtra("doctorName")
        val address = intent.getStringExtra("address")
        val contact = intent.getStringExtra("contact")
        val fees = intent.getStringExtra("fees")?.toFloatOrNull() ?: 0.0f
//        Log.d("AppointmentPaymentActivity", "Fees received: $fees")

        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val orderType = intent.getStringExtra("orderType")

        nextButton.setOnClickListener{
            val acnt = acntNo.text.toString()
            val expDate = expDate.text.toString()

            val dbHelper = Database(this, "healthcare.db", null, 1)

            if(dbHelper.verifyAccount(acnt, expDate, dbHelper)){
                val intent = Intent(this, AppointmentCVVActivity::class.java).apply {
                    putExtra("account_no", acnt)
                    putExtra("username", username)
                    putExtra("desName", desName)
                    putExtra("address", address)
                    putExtra("contact", contact)
                    putExtra("fees", fees.toString())
                    putExtra("date", date)
                    putExtra("time", time)
                    putExtra("orderType", orderType)
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Invalid Account Number or Expiry Date!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}