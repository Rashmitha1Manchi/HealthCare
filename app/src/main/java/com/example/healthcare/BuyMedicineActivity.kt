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

class BuyMedicineActivity : ComponentActivity() {

    private val packages = arrayOf(
        arrayOf("Uprise-D3 1000IU Capsule", "", "", "", "50"),
        arrayOf("HealthVit Chromium Picolinate 200mcg Capsule", "","","", "305"),
        arrayOf("Vitamin B Complex Capsules", "","","", "448"),
        arrayOf("Inlife Vitamin E Wheat Germ Oil Capsule","","","", "539"),
        arrayOf("Dolo 650 Tablet", "","","", "30"),
        arrayOf("Crocin 650 Advance Tablet","","","", "50"),
        arrayOf("Strepsils Medicated Lozenges for Sore Throat","","","", "40"),
        arrayOf("Tata 1mg Calcium + Vitamin D3","","","", "30"),
        arrayOf("Feronia-XT Tablet","" ,"" ,"" , "130")
    )

    private val packageDetails = arrayOf(
        "Building and keeping the bones & teeth strong\n" +
                "Reducing fatigue/stress and muscular pains\n" +
                "Boosting immunity and increasing resistance against infection",

        "Chromium is an essential trace mineral that plays an important role in helping insulin regulate blood glucose ",

        "Provides relief from vitamin B deficiencies\n" +
                "Helps in formation of red blood cells\n" +
                "Maintains a healthy nervous system",

        "It promotes health as well as skin benefits.\n" +
                "It helps reduce skin blemish and pigmentation.\n" +
                "It acts as a safeguard for the skin from harsh UVA and UVB sun rays.",

        "Dolo 650 Tablet helps relieve pain and fever by blocking the release of certain chemical messengers responsible for fever and pain",

        "Helps relieve fever and bring down a high temperature\n" +
                "Suitable for people with a heart condition or high blood pressure",

        "Relieves the symptoms of a bacterial throat infection and soothes the recovery process\n" +
                "Provides a warm and comforting feeling during a sore throat",

        "Reduces the risk of calcium deficiency, Rickets, and Osteoporosis\n" +
                "Promotes mobility and flexibility of joints",

        "Helps to reduce iron deficiency due to chronic blood loss or low intake of iron"
    )

    private lateinit var lst: ListView
    private lateinit var btnBack: Button
    private lateinit var btnGoToCart: Button
    private val list = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_medicine)

        lst = findViewById(R.id.listViewMedicines)
        btnBack = findViewById(R.id.buttonBack)
        btnGoToCart = findViewById(R.id.buttonGoToCart)

        btnGoToCart.setOnClickListener {
             startActivity(Intent(this, CartBuyMedicineActivity::class.java))
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
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
        lst.adapter = adapter
        lst.isVerticalScrollBarEnabled = true

        lst.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, BuyMedicineDetailsActivity::class.java)
            intent.putExtra("text1", packages[i][0])
            intent.putExtra("text2", packageDetails[i])
            intent.putExtra("text3", packages[i][4])

            startActivity(intent)
        }
    }
}