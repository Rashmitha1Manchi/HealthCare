package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class LabTestPaymentActivity : ComponentActivity() {

    private lateinit var nextButton: Button
    private lateinit var acntNo: EditText
    private lateinit var expDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab_test_payment)

        nextButton = findViewById(R.id.buttonLTPSubmit)
        acntNo = findViewById(R.id.editTextLTPAcntNo)
        expDate = findViewById(R.id.editTextLTPExpiryDate)

        nextButton.setOnClickListener {
            val acnt = acntNo.text.toString()
            val expDate = expDate.text.toString()

            val dbHelper = Database(this, "healthcare.db", null, 1)

            val name = intent.getStringExtra("name")
            val address = intent.getStringExtra("address")
            val contact = intent.getStringExtra("contact")
            val pincode = intent.getStringExtra("pincode")
            val price = intent.getStringExtra("price")
            val date = intent.getStringExtra("date")
            val time = intent.getStringExtra("time")
            Log.d("LabTestPaymentActivity", "$price")

            if(dbHelper.verifyAccount(acnt, expDate, dbHelper)){
                val intent = Intent(this, LabTestCVVActivity::class.java).apply {
                    putExtra("account_no", acnt)
                    putExtra("expiry_date", expDate)

                    putExtra("name", name.toString())
                    putExtra("address", address.toString())
                    putExtra("contact", contact.toString())
                    putExtra("pincode", pincode.toString())
                    putExtra("date", date.toString())
                    putExtra("time", time.toString())
                    putExtra("price", price.toString())
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Invalid Account Number or Expiry Date!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}