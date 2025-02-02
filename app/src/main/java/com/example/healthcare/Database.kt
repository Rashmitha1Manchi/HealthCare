package com.example.healthcare

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.SQLException

class Database(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTableQuery = """
            CREATE TABLE users (
                username TEXT UNIQUE,
                email TEXT UNIQUE,
                password TEXT
            )
        """
        db.execSQL(createUsersTableQuery)

        val createTableCart = """
           CREATE TABLE cart (
                username TEXT,
                product TEXT,
                price FLOAT,
                otype TEXT
           )
        """
        db.execSQL(createTableCart)

        val createOrderPlaced = """
            CREATE TABLE orderplace(
                username TEXT,
                fullname TEXT,
                address TEXT,
                contact TEXT,
                pincode TEXT,
                date TEXT,
                time TEXT,
                amount FLOAT,
                otype TEXT
            )
        """
        db.execSQL(createOrderPlaced)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle schema updates if necessary
    }

    fun register(username: String, email: String, password: String): String {
        val contentValues = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("password", password)
        }

        val db = writableDatabase

        return try {
            db.insertOrThrow("users", null, contentValues)
            "Registration successful"
        }
        catch (e: SQLException) {
            if (e.message?.contains("username") == true) {
                "Username already exists"
            }
            else if (e.message?.contains("email") == true) {
                "Email already exists"
            }
            else {
                "Registration failed due to an unknown error"
            }
        }
        finally {
            db.close()
        }
    }

    fun login(username: String, password: String): Int {
        var result = 0
        val str = arrayOf(username, password)

        val db = readableDatabase
        val c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", str)

        if (c.moveToFirst()) {
            result = 1
        }

        c.close()
        db.close()

        return result
    }

    fun addCart(username: String, product: String, price: Float, otype: String) {
        val cv = ContentValues().apply {
            put("username", username)
            put("product", product)
            put("price", price)
            put("otype", otype)
        }
        val db = writableDatabase
        db.insert("cart", null, cv)
        db.close()
    }

    fun checkCart(username: String, product: String): Int {
        var result = 0
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM cart WHERE username = ? AND product = ?",
            arrayOf(username, product)
        )
        if (cursor.moveToFirst()) {
            result = 1
        }
        cursor.close()
        db.close()
        return result
    }

    fun removeCart(username: String, otype: String) {
        val str = arrayOf(username, otype)
        val db = writableDatabase
        db.delete("cart", "username=? AND otype=?", str)
        db.close()
    }

    fun getCartData(username: String, otype: String): ArrayList<String> {
        val arr = ArrayList<String>()
        val db = readableDatabase
        val str = arrayOf(username, otype)

        val query = "SELECT product, price FROM cart WHERE username = ? AND otype = ?"
        val cursor = db.rawQuery(query, str)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val product = it.getString(it.getColumnIndexOrThrow("product"))
                    val price = it.getString(it.getColumnIndexOrThrow("price"))

                    if (!product.isNullOrEmpty() && !price.isNullOrEmpty()) {
                        arr.add("$product Rs.$price")
                    }
                } while (it.moveToNext())
            }
        }
        db.close()
        return arr
    }

    fun addOrder(username: String, fullname: String, address: String, contact: String, pincode: String, date: String, time: String, amount: Float, otype: String) {
        val cv = ContentValues().apply {
            put("username", username)
            put("fullname", fullname)
            put("address", address)
            put("contact", contact)
            put("pincode", pincode)
            put("date", date)
            put("time", time)
            put("amount", amount)
            put("otype", otype)
        }
        val db = writableDatabase
        db.insert("orderplace", null, cv)
        db.close()
    }

    fun getOrderData(username: String): ArrayList<String> {
        val arr = ArrayList<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM orderplace WHERE username = ?", arrayOf(username))

        if (cursor.moveToFirst()) {
            do {
                val row = cursor.getString(1) + "$" +
                        cursor.getString(2) + "$" +
                        cursor.getString(3) + "$" +
                        cursor.getString(4) + "$" +
                        cursor.getString(5) + "$" +
                        cursor.getString(6) + "$" +
                        cursor.getString(7) + "$" +
                        cursor.getString(8)
                arr.add(row)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return arr
    }

    fun checkAppointmentExists(username: String, fullname: String, address: String, contact: String, date: String, time: String): Int {
        var result = 0
        val str = arrayOf(username, fullname, address, contact, date, time)

        val db = readableDatabase
        val query = "SELECT * FROM orderplace WHERE username = ? AND fullname = ? AND address = ? AND contact = ? AND date = ? AND time = ?"
        val cursor = db.rawQuery(query, str)

        if (cursor.moveToFirst()) {
            result = 1
        }
        cursor.close()
        db.close()
        return result
    }


}
