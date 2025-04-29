package com.example.healthcare

import CartAdapter
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import java.util.Calendar

class CartBuyMedicineActivity : ComponentActivity() {

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var dateButton: Button
    private lateinit var btnCheckout: Button
    private lateinit var btnBack: Button
    private lateinit var tvTotal: TextView
    private lateinit var lst: ListView

    private var totalAmount = 0f
    private lateinit var list: ArrayList<HashMap<String, String>>
    private lateinit var adapter: CartAdapter
    private lateinit var username: String
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_buy_medicine)

        // Initialize Views
        dateButton = findViewById(R.id.buttonCartDate)
        btnCheckout = findViewById(R.id.buttonCBMGoToCart)
        btnBack = findViewById(R.id.buttonCBMBack)
        tvTotal = findViewById(R.id.textViewCBMTotalCost)
        lst = findViewById(R.id.listViewCBM)

        // Get username from shared preferences
        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").orEmpty()

        db = Database(applicationContext, "healthcare.db", null, 1)

        // Load cart data and populate list
        loadCartData()

        // Back button
        btnBack.setOnClickListener {
            startActivity(Intent(this, BuyMedicineActivity::class.java))
        }

        // Checkout button
        btnCheckout.setOnClickListener {
            if (dateButton.text != "Select date") {
                val intent = Intent(this, BuyMedicineBookActivity::class.java)
                intent.putExtra("price", tvTotal.text.toString())
                intent.putExtra("date", dateButton.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize date picker
        initDatePicker()

        dateButton.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun loadCartData() {
        val dbData = db.getCartData(username, "Medicine")
        totalAmount = 0f

        val packages = Array(dbData.size) { Array(5) { "" } }

        for (i in dbData.indices) {
            val arrData = dbData[i].toString()
            val strData = arrData.split(Regex.fromLiteral(" Rs."))
            packages[i][0] = strData[0]
            packages[i][1] = ""
            packages[i][4] = "Cost: ${strData[1]}/-"
            totalAmount += strData[1].toFloat()
        }

        tvTotal.text = "Total Cost: $totalAmount"

        list = ArrayList()
        for (i in packages.indices) {
            val item = HashMap<String, String>()
            item["line1"] = packages[i][0]
            item["line2"] = packages[i][1]
            item["line3"] = packages[i][2]
            item["line4"] = packages[i][3]
            item["line5"] = packages[i][4]
            list.add(item)
        }

        adapter = CartAdapter(this, list) { position ->
            removeCartItem(position)
        }

        lst.adapter = adapter
    }

    private fun removeCartItem(position: Int) {
        val itemToRemove = list[position]
        val itemName = itemToRemove["line1"] ?: ""

        // Remove from database
        db.removeCartItem(username, itemName, "Medicine")

        // Extract and update total
        val costString = itemToRemove["line5"]
            ?.replace("Cost: ", "")
            ?.replace("/-", "")
            ?.trim()

        val itemCost = costString?.toFloatOrNull() ?: 0f
        totalAmount -= itemCost
        tvTotal.text = "Total Cost: $totalAmount"

        // Remove from list and notify adapter
        list.removeAt(position)
        adapter.notifyDataSetChanged()

        Toast.makeText(this, "$itemName removed from cart", Toast.LENGTH_SHORT).show()
    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val formattedDate = "$dayOfMonth/${month + 1}/$year"
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
}
