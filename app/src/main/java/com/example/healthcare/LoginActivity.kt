package com.example.healthcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edUsername : EditText = findViewById(R.id.editTextLoginUsername)
        val edPassword : EditText = findViewById(R.id.editTextLoginPassword)
        val btn : Button = findViewById(R.id.ButtonAppBack)
        val tv : TextView = findViewById(R.id.textViewNewUser)

        btn.setOnClickListener{

//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)

            val username = edUsername.text.toString()
            val password = edPassword.text.toString()

            lateinit var db: Database
            db = Database(this, "healthcare.db", null, 1)

            if(username.isEmpty() || password.isEmpty())
                Toast.makeText(applicationContext, "Please fill all details", Toast.LENGTH_SHORT).show()
            else{
                if (db.login(username, password) == 1) {
                    Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()

                    val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.putString("username", username)
                    editor.apply()

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(applicationContext, "Invalid Username and Password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tv.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}