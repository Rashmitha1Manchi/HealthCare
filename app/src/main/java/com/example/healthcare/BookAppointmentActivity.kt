package com.example.healthcare

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var dbHelper: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        dateButton = findViewById(R.id.buttonCartDate)
        timeButton = findViewById(R.id.buttonCartTime)
        dbHelper = Database(this, "healthcare.db", null, 1)

        val tv: TextView = findViewById(R.id.textViewAppTitle)
        val ed1: EditText = findViewById(R.id.editTextAppFullName)
        val ed2: EditText = findViewById(R.id.editTextAppAddress)
        val ed3: EditText = findViewById(R.id.editTextAppContactNumber)
        val ed4: EditText = findViewById(R.id.editTextAppFees)
        val btnBook: Button = findViewById(R.id.buttonBookAppointment)
        val btnBack: Button = findViewById(R.id.ButtonAppBack)

        ed1.keyListener = null
        ed2.keyListener = null
        ed3.keyListener = null
        ed4.keyListener = null

        val it = intent
        val fullName = it.getStringExtra("text1")
        val address = it.getStringExtra("text2")
        val title = it.getStringExtra("text3")
        val contact = it.getStringExtra("text4")
        val fees = it.getStringExtra("text5")

        tv.text = title
        ed1.setText(fullName)
        ed2.setText(address)
        ed3.setText(contact)
        ed4.setText("Cons Fees: $fees")

        initDatePicker()
        initTimePicker()

        dateButton.setOnClickListener {
            datePickerDialog.show()
        }

        timeButton.setOnClickListener {
            timePickerDialog.show()
        }

        btnBack.setOnClickListener{
            val intent = Intent(this, FindDoctorActivity::class.java)
            startActivity(intent)
        }

        btnBook.setOnClickListener {

            val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", "") ?: ""

            if(dateButton.text != "Select Date" && timeButton.text != "Select Time") {

                if (dbHelper.checkAppointmentExists(
                        username,
                        title + "=>" + fullName,
                        address.toString(),
                        contact.toString(),
                        dateButton.text.toString(),
                        timeButton.text.toString()
                    ) == 1
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Appointment already booked",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val intent =
                        Intent(this@BookAppointmentActivity, AppointmentPaymentActivity::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("doctorName", title.toString() + " => " + fullName.toString())
                    intent.putExtra("address", address.toString())
                    intent.putExtra("contact", contact.toString())
                    intent.putExtra("fees", fees ?: "0.0")
                    Log.d("BookAppointmentActivity", "${fees?.toFloatOrNull() ?: 0.0f}")
                    intent.putExtra("date", dateButton.text.toString())
                    intent.putExtra("time", timeButton.text.toString())
                    intent.putExtra("orderType", "Appointment")
                    startActivity(intent)
                    finish()
                }
            }
            else{
                Toast.makeText(this, "Please select date and time!", Toast.LENGTH_SHORT).show()
            }

//            val db = Database(applicationContext, "healthcare.db", null, 1)
//            val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
//            val username = sharedPreferences.getString("username", "") ?: ""
//            if (db.checkAppointmentExists(
//                    username,
//                    title+"=>"+fullName,
//                    address.toString(), contact.toString(), dateButton.text.toString(),timeButton.text.toString()
//                )==1
//            ) {
//                Toast.makeText(applicationContext, "Appointment already booked", Toast.LENGTH_LONG).show()
//            } else {
//                db.addOrder(
//                    username,
//                    title.toString()+" => "+fullName.toString(),
//                    address.toString(),
//                    contact.toString(),
//                    pincode = 0.toString(),
//                    date = dateButton.text.toString(),
//                    time = timeButton.text.toString(),
//                    amount = fees?.toFloatOrNull() ?: 0.0f,
//                    otype = "Appointment"
//                )
//                Toast.makeText(applicationContext, "Your appointment is done successfully", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this@BookAppointmentActivity, HomeActivity::class.java))
//            }

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