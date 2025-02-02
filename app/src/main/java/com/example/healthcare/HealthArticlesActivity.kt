package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
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

class HealthArticlesActivity : ComponentActivity() {

    private val healthDetails = arrayOf(
        arrayOf("Walking Daily", "", "", "", "Click More Details"),
        arrayOf("Home care of COVID-19", "", "", "", "Click More Details"),
        arrayOf("Stop Smoking", "", "", "", "Click More Details"),
        arrayOf("Menstrual Cramps", "", "", "", "Click More Details"),
        arrayOf("Healthy Gut", "", "", "", "Click More Details")
    )

    private val images = intArrayOf(
        R.drawable.health1,
        R.drawable.health2,
        R.drawable.health3,
        R.drawable.health4,
        R.drawable.health5
    )

    private lateinit var list: ArrayList<HashMap<String, String>>
    private lateinit var btnBack: Button
    private lateinit var lst: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_articles)

        btnBack = findViewById(R.id.buttonHABack)
        lst = findViewById(R.id.listViewHA)

        btnBack.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        list = ArrayList()
        for (i in healthDetails.indices) {
            val item = HashMap<String, String>()
            item["line1"] = healthDetails[i][0]
            item["line2"] = healthDetails[i][1]
            item["line3"] = healthDetails[i][2]
            item["line4"] = healthDetails[i][3]
            item["line5"] = healthDetails[i][4]
            list.add(item)
        }

        val adapter = SimpleAdapter(
            this,
            list,
            R.layout.multi_lines,
            arrayOf("line1", "line2", "line3", "line4", "line5"),
            intArrayOf(
                R.id.textViewLine1,
                R.id.textViewLine2,
                R.id.textViewLine3,
                R.id.textViewLine4,
                R.id.textViewLine5
            )
        )
        lst.adapter = adapter

        lst.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, HealthArticlesDetailsActivity::class.java)
            intent.putExtra("text1", healthDetails[i][0])
            intent.putExtra("text2", images[i])

            startActivity(intent)
        }
    }
}