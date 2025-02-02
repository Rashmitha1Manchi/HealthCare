package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

class BuyMedicineBookActivity : ComponentActivity() {

    private lateinit var edName: EditText
    private lateinit var edAddress: EditText
    private lateinit var edContact: EditText
    private lateinit var edPinCode: EditText
    private lateinit var btnBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_medicine_book)

        edName = findViewById(R.id.editTextBMBFullName)
        edAddress = findViewById(R.id.editTextBMBAddress)
        edContact = findViewById(R.id.editTextBMBContactNumber)
        edPinCode = findViewById(R.id.editTextBMBPinCode)
        btnBooking = findViewById(R.id.buttonBMBBook)

        val it = intent
        val price = it.getStringExtra("price")?.split(":") ?: emptyList()
        val date = it.getStringExtra("date")
     //   val time = it.getStringExtra("time")

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
                "",
                price[1].toFloatOrNull() ?: 0f,
                "Medicine"
            )
            db.removeCart(username, "Medicine")

            Toast.makeText(applicationContext, "Your booking is done successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}