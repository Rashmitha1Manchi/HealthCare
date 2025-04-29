package com.example.healthcare

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.SQLException
import android.util.Log
import java.security.MessageDigest

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
                price REAL,
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

        val createTableAccounts = """
            CREATE TABLE accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account_number TEXT UNIQUE NOT NULL,
                expiry_date TEXT NOT NULL,
                cvv TEXT NOT NULL,
                balance REAL NOT NULL DEFAULT 10000.00
            );
        """
        db.execSQL(createTableAccounts)
        insertDummyAccounts(db)

        val createTablePaymentDetails = """
            CREATE TABLE paymentDetails(
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                username TEXT,
                account_number TEXT, 
                amount REAL, 
                otype TEXT
            );
        """
        db.execSQL(createTablePaymentDetails)
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

    private fun insertDummyAccounts(db: SQLiteDatabase) {
        val dummyAccounts = listOf(
            "9876543210123456" to "123",
            "1234567890987654" to "456",
            "1111222233334444" to "789",
            "4444555566667777" to "234",
            "9999000011112222" to "567",
            "5555666677778888" to "890",
            "1010101010101010" to "345",
            "2020202020202020" to "678",
            "3030303030303030" to "901",
            "4040404040404040" to "432"
        )

        for ((accountNo, cvv) in dummyAccounts) {
            val hashedAccountNo = hashData(accountNo)
            val hashedCvv = hashData(cvv)

            val values = ContentValues().apply {
                put("account_number", hashedAccountNo)
                put("expiry_date", "12/26") // Static expiry date for now
                put("cvv", hashedCvv)
                put("balance", 10000.00)
            }

            db.insert("accounts", null, values)
        }
    }

    fun hashData(data: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(data.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyCVV(accountNo: String, hashedCvv: String, dbHelper: Database): Boolean {
        val db = readableDatabase
        Log.d("Debug", "Hashed Acnt Num: ${hashData(accountNo.trim())}")
        val cursor = db.rawQuery(
            "SELECT * FROM accounts WHERE account_number = ? AND cvv = ?",
            arrayOf(hashData(accountNo.trim()), hashedCvv)
        )

        val isValid = cursor.moveToFirst()
        cursor.close()
        db.close()

        return isValid
    }

    fun processPayment(username: String, accountNo: String, amount: Double, orderType: String): Boolean {
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT balance FROM accounts WHERE account_number = ?", arrayOf(accountNo))

        if (cursor.moveToFirst()) {
            val currentBalance = cursor.getDouble(0)

            if (currentBalance >= amount) {
                db.beginTransaction()
                try {
                    val updateBalanceQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?"
                    db.execSQL(updateBalanceQuery, arrayOf(amount, accountNo))

                    val insertPaymentQuery = "INSERT INTO paymentDetails (username, account_number, amount, otype) VALUES (?, ?, ?, ?)"
                    db.execSQL(insertPaymentQuery, arrayOf(username, accountNo, amount, orderType))

                    db.setTransactionSuccessful()
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    db.endTransaction()
                }
            }
        }
        cursor.close()
        return false
    }

    fun verifyAccount(accountNo: String, expiryDate: String, dbHelper: Database): Boolean {
        val hashedAccountNo = hashData(accountNo.trim())
        Log.d("DEBUG", "Hashed Input Account Number: $hashedAccountNo")

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM accounts WHERE account_number = ? AND expiry_date = ?",
            arrayOf(hashedAccountNo, expiryDate)
        )

        val isValid = cursor.moveToFirst()
        cursor.close()
        db.close()

        return isValid
    }

    fun removeCartItem(username: String, item: String, otype: String) {
        val db = writableDatabase
        db.delete("cart", "username=? AND product=? AND otype=?", arrayOf(username, item, otype))
        db.close()
    }
}