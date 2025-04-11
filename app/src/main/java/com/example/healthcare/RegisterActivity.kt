package com.example.healthcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class RegisterActivity : ComponentActivity() {

    lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edUsername: EditText = findViewById(R.id.editTextAppFullName)
        val edPassword: EditText = findViewById(R.id.editTextAppContactNumber)
        val edEmail: EditText = findViewById(R.id.editTextAppAddress)
        val edConfirm: EditText = findViewById(R.id.editTextAppFees)
        val btn: Button = findViewById(R.id.ButtonAppBack)
        val tv: TextView = findViewById(R.id.textViewNewUser)

        tv.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btn.setOnClickListener{
            val username = edUsername.text.toString()
            val password = edPassword.text.toString()
            val email= edEmail.text.toString()
            val confirm= edConfirm.text.toString()

            database = Database(this, "healthcare.db", null, 1)

            if(username.isEmpty() || password.isEmpty() ||email.isEmpty() || confirm.isEmpty() )
                Toast.makeText(applicationContext, "Please fill all details", Toast.LENGTH_SHORT).show()
            else {
                if(password==confirm){
                    if(isValid(password)){
                        val result = database.register(username, email, password)
                        if (result == "Registration successful") {
                            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "Password must contain at least 8 characters,having letter,digit and special character", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(applicationContext, "Password and confirm password didn't match", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

fun isValid(passwordhere: String): Boolean {
    var f1 = 0
    var f2 = 0
    var f3 = 0

    if (passwordhere.length < 8) {
        return false
    } else {
        for (p in passwordhere.indices) {
            if (passwordhere[p].isLetter()) {
                f1 = 1
            }
        }

        for (r in passwordhere.indices) {
            if (passwordhere[r].isDigit()) {
                f2 = 1
            }
        }

        for (s in passwordhere.indices) {
            val c = passwordhere[s]
            if (c in '!'..'.' || c == '@') {
                f3 = 1
            }
        }

        return f1 == 1 && f2 == 1 && f3==1
    }
}