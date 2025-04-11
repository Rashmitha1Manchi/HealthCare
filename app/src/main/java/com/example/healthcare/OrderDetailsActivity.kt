package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.ComponentActivity
import java.util.regex.Pattern

class OrderDetailsActivity : ComponentActivity() {

    private lateinit var btn: Button
    private lateinit var lst: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        btn = findViewById(R.id.buttonODBack)
        lst = findViewById(R.id.listViewOD)

        btn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        val db = Database(applicationContext, "healthcare.db", null, 1)
        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        val dbData: ArrayList<String> = db.getOrderData(username)

            val orderDetails = Array(dbData.size) { Array(5) { "" } }
        for (i in orderDetails.indices) {
            val arrData = dbData[i].toString()
            val strData = arrData.split("$")

            if (strData.size >= 8) {
                orderDetails[i][0] = strData[0].trim()
                orderDetails[i][1] = strData[1].trim()
                orderDetails[i][2] = "Rs." + strData[6].trim()

                orderDetails[i][3] = if (strData[7].trim() == "Medicine") {
                    "Del:" + strData[4].trim()
                } else {
                    "Del: " + strData[4].trim() + " " + strData[5].trim()
                }
                orderDetails[i][4] = strData[7].trim()
            } else {
                Log.e("OrderDetailsActivity", "Insufficient data for dbData[$i]: $arrData")
                orderDetails[i][0] = "Incomplete Data"
                orderDetails[i][1] = ""
                orderDetails[i][2] = ""
                orderDetails[i][3] = ""
                orderDetails[i][4] = ""
            }
        }

        val list = mutableListOf<Map<String, String>>()
        for (i in orderDetails.indices) {
            val item = mapOf(
                "line1" to orderDetails[i][0],
                "line2" to orderDetails[i][1],
                "line3" to orderDetails[i][2],
                "line4" to orderDetails[i][3],
                "line5" to orderDetails[i][4]
            )
            list.add(item)
        }
        val sa = SimpleAdapter(
            this,
            list,
            R.layout.multi_lines,
            arrayOf("line1", "line2", "line3", "line4", "line5"),
            intArrayOf(R.id.textViewLine1, R.id.textViewLine2, R.id.textViewLine3, R.id.textViewLine4, R.id.textViewLine5)
        )
        lst.adapter = sa
    }
}