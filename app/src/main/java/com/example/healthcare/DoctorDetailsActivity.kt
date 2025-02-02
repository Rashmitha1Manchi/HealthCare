package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.activity.ComponentActivity

class DoctorDetailsActivity : ComponentActivity() {

    private val doctorDetails1: Array<Array<String>> = arrayOf(
        arrayOf("Doctor Name: Ajit Saste", "Hospital Address: Pimpri", "Exp: 5yrs", "Mobile No: 9898989898", "600"),
        arrayOf("Doctor Name: Prasad Pawar", "Hospital Address: Nigdi", "Exp: 15yrs", "Mobile No: 7898989898", "900"),
        arrayOf("Doctor Name: Swapnil Kale", "Hospital Address: Hyd", "Exp: 8yrs", "Mobile No: 8898989898", "300"),
        arrayOf("Doctor Name: Deepak Deshmukh", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No: 9980000000", "500"),
        arrayOf("Doctor Name: Ashok Panda", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No: 7798989898", "800")
    )

    private val doctorDetails2: Array<Array<String>> = arrayOf(
        arrayOf("Doctor Name: Neelam Patil", "Hospital Address: Pimpri", "Exp: 5yrs", "Mobile No: 9898989898", "600"),
        arrayOf("Doctor Name: Swati Pawar", "Hospital Address: Nigdi", "Exp: 15yrs", "Mobile No: 7898989898", "900"),
        arrayOf("Doctor Name: Neeraja Kale", "Hospital Address: Hyd", "Exp: 8yrs", "Mobile No: 8898989898", "300"),
        arrayOf("Doctor Name: Mayuri Deshmukh", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No: 9980000000", "500"),
        arrayOf("Doctor Name: Minakshi Panda", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No: 7798989898", "800")
    )

    private val doctorDetails3: Array<Array<String>> = arrayOf(
        arrayOf("Doctor Name: Seema Patil", "Hospital Address: Pimpri", "Exp: 4yrs", "Mobile No: 9898989898", "200"),
        arrayOf("Doctor Name: Pankaj Parab", "Hospital Address: Nigdi", "Exp: 5yrs", "Mobile No: 7898989898", "300"),
        arrayOf("Doctor Name: Monish Jain", "Hospital Address: Hyd", "Exp: 7yrs", "Mobile No: 8898989898", "300"),
        arrayOf("Doctor Name: Vishal Deshmukh", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No: 9980000000", "500"),
        arrayOf("Doctor Name: Shrikant Panda", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No: 7798989898", "600")
    )

    private val doctorDetails4: Array<Array<String>> = arrayOf(
        arrayOf("Doctor Name: Amol Gawade", "Hospital Address: Pimpri", "Exp: 2yrs", "Mobile No: 9898989898", "200"),
        arrayOf("Doctor Name: Prasad Pawar", "Hospital Address: Nigdi", "Exp: 5yrs", "Mobile No: 7898989898", "300"),
        arrayOf("Doctor Name: Nilesh Kale", "Hospital Address: Hyd", "Exp: 8yrs", "Mobile No: 8898989898", "300"),
        arrayOf("Doctor Name: Abhishek Singh", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No: 9980000000", "500"),
        arrayOf("Doctor Name: Ashok Panda", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No: 7798989898", "600")
    )

    private val doctorDetails5: Array<Array<String>> = arrayOf(
        arrayOf("Doctor Name: Rakesh Patil", "Hospital Address: Pimpri", "Exp: 7yrs", "Mobile No: 9898989898", "1100"),
        arrayOf("Doctor Name: Anil Desai", "Hospital Address: Nigdi", "Exp: 15yrs", "Mobile No: 7898989898", "1600"),
        arrayOf("Doctor Name: Vijay Sharma", "Hospital Address: Hyd", "Exp: 8yrs", "Mobile No: 8898989898", "1400"),
        arrayOf("Doctor Name: Kiran Deshmukh", "Hospital Address: Chinchwad", "Exp: 9yrs", "Mobile No: 9980000000", "1250"),
        arrayOf("Doctor Name: Suresh Mehta", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No: 7798989898","1350")
    )

    var list = ArrayList<Map<String, String>>()

    var doctorDetails = arrayOf<Array<String>>()


    lateinit var tv : TextView
    lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_details)

        tv = findViewById(R.id.textViewDDTitle)
        btn = findViewById(R.id.buttonODBack)

        val intent = intent
        val title = intent.getStringExtra("title")
        tv.text = title

        btn.setOnClickListener {
            startActivity(Intent(this, FindDoctorActivity::class.java))
        }

        if (title == "Family Physicians") {
           doctorDetails = doctorDetails1
        } else if (title == "Dietician") {
            doctorDetails = doctorDetails2
        } else if (title == "Dentist") {
            doctorDetails = doctorDetails3
        } else if (title == "Surgeon") {
            doctorDetails = doctorDetails4
        } else {
            doctorDetails = doctorDetails5
        }

        for (i in doctorDetails.indices) {
            val item = HashMap<String, String>()
            item["line1"] = doctorDetails[i][0]
            item["line2"] = doctorDetails[i][1]
            item["line3"] = doctorDetails[i][2]
            item["line4"] = doctorDetails[i][3]
            item["line5"] = "Consultation Fees:" + doctorDetails[i][4] + "/-"
            list.add(item)
        }

        val sa = SimpleAdapter(
            this, list, R.layout.multi_lines,
            arrayOf("line1", "line2", "line3", "line4", "line5"),
            intArrayOf(R.id.textViewLine1, R.id.textViewLine2, R.id.textViewLine3, R.id.textViewLine4 , R.id.textViewLine5)
        )
        val lst: ListView = findViewById(R.id.listViewOD)
        lst.adapter = sa

        lst.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, BookAppointmentActivity::class.java)
            intent.putExtra("text1", doctorDetails[i][0])
            intent.putExtra("text2", doctorDetails[i][1])
            intent.putExtra("text3", title)
            intent.putExtra("text4", doctorDetails[i][3])
            intent.putExtra("text5", doctorDetails[i][4])
            startActivity(intent)
        }
    }
}