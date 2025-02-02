package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LabTestDetailsActivity : AppCompatActivity() {

    private lateinit var tvPackageName: TextView
    private lateinit var tvTotalCost: TextView
    private lateinit var edDetails: EditText
    private lateinit var btnAddToCart: Button
    private lateinit var btnGoBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_test_details)

        tvPackageName = findViewById(R.id.textViewLTDPackage)
        tvTotalCost = findViewById(R.id.textViewLTDTotalCost)
        edDetails = findViewById(R.id.editTextLTDTextMultiLine)
        btnAddToCart = findViewById(R.id.buttonLTDAddToCart)
        btnGoBack = findViewById(R.id.buttonLTDGoBack)

        edDetails.setKeyListener(null)

        val intent = intent
        tvPackageName.text = intent.getStringExtra("text1")
        edDetails.setText(intent.getStringExtra("text2"))
        tvTotalCost.text = "Total Cost : ${intent.getStringExtra("text3")}"

        btnGoBack.setOnClickListener {
            startActivity(Intent(this@LabTestDetailsActivity, LabTestActivity::class.java))
        }

        btnAddToCart.setOnClickListener {
            val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", "").orEmpty()
            val product = tvPackageName.text.toString()
            val price = intent.getStringExtra("text3")?.toFloatOrNull() ?: 0f

            val db = Database(applicationContext, "healthcare.db", null, 1)

            if (db.checkCart(username, product) == 1) {
                Toast.makeText(applicationContext, "Product Already Added", Toast.LENGTH_SHORT).show()
            } else {
                db.addCart(username, product, price, "Lab")
                Toast.makeText(applicationContext, "Record Inserted to Cart", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LabTestActivity::class.java))
            }
        }
    }
}
