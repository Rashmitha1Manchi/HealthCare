package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import java.security.MessageDigest

class MedicinePaymentActivity : ComponentActivity() {

    private lateinit var nextButton: Button
    private lateinit var acntNo: EditText
    private lateinit var expDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medicine_payment)

        nextButton = findViewById(R.id.buttonCVVSubmit)
        acntNo = findViewById(R.id.editTextAcntNo)
        expDate = findViewById(R.id.editTextExpiryDate)

        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        val contact = intent.getStringExtra("contact")
        val pincode = intent.getStringExtra("pincode")
        val price = intent.getStringExtra("price")
        val date = intent.getStringExtra("date")

        nextButton.setOnClickListener{
            val acnt = acntNo.text.toString()
            val date = expDate.text.toString()

            val dbHelper = Database(this, "healthcare.db", null, 1)

            if(dbHelper.verifyAccount(acnt, date, dbHelper)){
                val intent = Intent(this, MedicineCVVActivity::class.java).apply {
                    putExtra("account_no", acnt)
                    putExtra("expiry_date", date)
                    putExtra("name", name)
                    putExtra("address", address)
                    putExtra("contact", contact)
                    putExtra("pincode", pincode)
                    putExtra("price", price)
                    putExtra("date", date)
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Invalid Account Number or Expiry Date!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}