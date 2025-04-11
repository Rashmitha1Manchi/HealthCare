package com.example.healthcare

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class CartLabActivity : AppCompatActivity() {

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var btnCheckout: Button
    private lateinit var btnBack: Button
    private lateinit var tvTotal: TextView
    private lateinit var lst: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_lab)

        dateButton = findViewById(R.id.buttonCartDate)
        timeButton = findViewById(R.id.buttonCartTime)
        btnCheckout = findViewById(R.id.buttonGoToCart)
        btnBack = findViewById(R.id.buttonBack)
        tvTotal = findViewById(R.id.textViewCartTotalCost)
        lst = findViewById(R.id.listViewMedicines)

        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "").orEmpty()

        val db = Database(getApplicationContext(), "healthcare.db", null, 1)
        var totalAmount = 0f
        val dbData = db.getCartData(username, "Lab")

//        if (dbData.isEmpty()) {
//            Toast.makeText(this, "No data found in cart", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "DB Data: $dbData", Toast.LENGTH_SHORT).show()
//        }

        val packages = Array(dbData.size) { Array(5) { "" } }

        for (i in packages.indices) {
            packages[i] = Array(5) { "" }
        }

        for (i in dbData.indices) {
            val arrData = dbData[i].toString()
            val strData = arrData.split(Regex.fromLiteral(" Rs."))
            packages[i][0] = strData[0]
            packages[i][1] = strData[1]
            packages[i][4] = "Cost: ${strData[1]}/-"
            totalAmount += strData[1].toFloat()
        }

        tvTotal.text = "Total Cost: $totalAmount"

        val list = ArrayList<HashMap<String, String>>()
        for (i in packages.indices) {
            val item = HashMap<String, String>()
            item["line1"] = packages[i][0]
            item["line2"] = ""
            item["line3"] = packages[i][2]
            item["line4"] = packages[i][3]
            item["line5"] = packages[i][4]
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

        btnBack.setOnClickListener {
            val intent = Intent(this, LabTestActivity::class.java)
            startActivity(intent)
        }

        btnCheckout.setOnClickListener {
            val selectedDate = dateButton.text.toString()
            val selectedTime = timeButton.text.toString()

            if (selectedDate == "Select Date" || selectedTime == "Select Time") {
                Toast.makeText(this, "Please select both date and time", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, LabTestBookActivity::class.java)
                intent.putExtra("price", tvTotal.text.toString())
//                Log.d("CartLabActivity:", "Price : ${tvTotal.text}")
                intent.putExtra("date", selectedDate)
                intent.putExtra("time", selectedTime)
                startActivity(intent)
            }
        }

        initDatePicker()
        initTimePicker()

        dateButton.setOnClickListener {
            datePickerDialog.show()
        }

        timeButton.setOnClickListener {
            timePickerDialog.show()
        }
    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val formattedDate = "${dayOfMonth}/${month + 1}/$year"
            dateButton.text = formattedDate
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_DARK
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = cal.timeInMillis + 86400000
    }

    private fun initTimePicker() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val formattedTime = "$hourOfDay:$minute"
            timeButton.text = formattedTime
        }

        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val style = AlertDialog.THEME_HOLO_DARK
        timePickerDialog = TimePickerDialog(this, style, timeSetListener, hour, minute, true)
    }
}