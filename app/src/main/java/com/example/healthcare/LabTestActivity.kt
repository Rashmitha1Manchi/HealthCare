package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity

class LabTestActivity : AppCompatActivity() {

    private val packages = listOf(
        listOf("Package 1: Full Body Check-Up", "", "", "", "999"),
        listOf("Package 2: Blood Glucose Fasting", "", "", "", "299"),
        listOf("Package 3: COVID-19 Antibody-IgG", "", "", "", "899"),
        listOf("Package 4: Thyroid Check", "", "", "", "499"),
        listOf("Package 5: Immunity Check", "", "", "", "699")
    )
    private val packageDetails = arrayOf(
        "Blood Glucose Fasting\nComplete Hemogram\nHbA1c\nIron Studies\nKidney Function Test\nLipid Profile\nLiver Function Test",
        "Blood Glucose Fasting",
        "COVID-19 Antibody - IgG",
        "Thyroid Profile-Total (T3, T4 & TSH Ultra-sensitive)\nComplete Hemogram\nIron Studies\nCRP (C Reactive Protein) Quantitative, Serum",
        "Kidney Function Test\nVitamin D Total-25 Hydroxy\nLiver Function Test\nLipid Profile"
    )

    private lateinit var listView: ListView
    private lateinit var btnGoToCart: Button
    private lateinit var btnBack: Button
    private val list = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_test)

        btnGoToCart = findViewById(R.id.buttonGoToCart)
        btnBack = findViewById(R.id.buttonBack)
        listView = findViewById(R.id.listViewMedicines)

        btnBack.setOnClickListener {
            startActivity(Intent(this@LabTestActivity, HomeActivity::class.java))
        }

        for (i in packages.indices) {
            val item = HashMap<String, String>()
            item["line1"] = packages[i][0]
            item["line2"] = packages[i][1]
            item["line3"] = packages[i][2]
            item["line4"] = packages[i][3]
            item["line5"] = "Total Cost: ${packages[i][4]}/-"
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

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, i, _ ->
            val it = Intent(this@LabTestActivity, LabTestDetailsActivity::class.java)
            it.putExtra("text1", packages[i][0])
            it.putExtra("text2", packageDetails[i])
            it.putExtra("text3", packages[i][4])
            startActivity(it)
        }

        btnGoToCart.setOnClickListener {
            startActivity(Intent(this@LabTestActivity, CartLabActivity::class.java))
        }
    }
}