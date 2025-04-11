package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class BuyMedicineBookActivity : ComponentActivity() {

    private lateinit var edName: EditText
    private lateinit var edAddress: EditText
    private lateinit var edContact: EditText
    private lateinit var edPinCode: EditText
    private lateinit var btnBooking: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_medicine_book)

        edName = findViewById(R.id.editTextBMBFullName)
        edAddress = findViewById(R.id.editTextBMBAddress)
        edContact = findViewById(R.id.editTextBMBContactNumber)
        edPinCode = findViewById(R.id.editTextBMBPinCode)
        btnBooking = findViewById(R.id.buttonBMBBook)
        btnBack = findViewById(R.id.buttonBMBBack)

        val it = intent
        val price = it.getStringExtra("price")?.split(":") ?: emptyList()
        val date = it.getStringExtra("date")
     //   val time = it.getStringExtra("time")

        btnBack.setOnClickListener {
            startActivity(Intent(this, CartBuyMedicineActivity::class.java))
        }

        btnBooking.setOnClickListener {
//            val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
//            val username = sharedPreferences.getString("username", "") ?: ""
//
//            val db = Database(applicationContext, "healthcare.db", null, 1)
//            db.addOrder(
//                username,
//                edName.text.toString(),
//                edAddress.text.toString(),
//                edContact.text.toString(),
//                edPinCode.text.toString(),
//                date.toString(),
//                "",
//                price[1].toFloatOrNull() ?: 0f,
//                "Medicine"
//            )
//            db.removeCart(username, "Medicine")
//
//            Toast.makeText(applicationContext, "Your booking is done successfully", Toast.LENGTH_LONG).show()
//            startActivity(Intent(this, HomeActivity::class.java))

            if(edName.text.toString().isEmpty() || edAddress.text.toString().isEmpty() || edContact.text.toString().isEmpty() || edPinCode.text.toString().isEmpty()){
                Toast.makeText(this, "Please fill all the details!", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, MedicinePaymentActivity::class.java)

                intent.putExtra("name", edName.text.toString())
                intent.putExtra("address", edAddress.text.toString())
                intent.putExtra("contact", edContact.text.toString())
                intent.putExtra("pincode", edPinCode.text.toString())

                intent.putExtra("price", price.getOrNull(1) ?: "0")
                intent.putExtra("date", date ?: "")

                startActivity(intent)
            }
        }
    }
}