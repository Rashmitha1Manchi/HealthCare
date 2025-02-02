package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthcare.ui.theme.HealthCareTheme

class FindDoctorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_doctor)

        val back = findViewById<CardView>(R.id.cardFDBack)
        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        val familyPhysician = findViewById<CardView>(R.id.cardFDFamilyPhysician)
        familyPhysician.setOnClickListener {
            val intent = Intent(this, DoctorDetailsActivity::class.java)
            intent.putExtra("title", "Family Physician")
            startActivity(intent)
        }

        val dietician = findViewById<CardView>(R.id.cardFDDietician)
        dietician.setOnClickListener {
            val intent = Intent(this, DoctorDetailsActivity::class.java)
            intent.putExtra("title", "Dietician")
            startActivity(intent)
        }

        val dentist = findViewById<CardView>(R.id.cardFDDentist)
        dentist.setOnClickListener {
            val intent = Intent(this, DoctorDetailsActivity::class.java)
            intent.putExtra("title", "Dentist")
            startActivity(intent)
        }

        val surgeon = findViewById<CardView>(R.id.cardFDSurgeon)
        surgeon.setOnClickListener {
            val intent = Intent(this, DoctorDetailsActivity::class.java)
            intent.putExtra("title", "Surgeon")
            startActivity(intent)
        }

        val cardiologist = findViewById<CardView>(R.id.cardFDCardiologist)
        cardiologist.setOnClickListener {
            val intent = Intent(this, DoctorDetailsActivity::class.java)
            intent.putExtra("title", "Cardiologist")
            startActivity(intent)
        }

    }
}