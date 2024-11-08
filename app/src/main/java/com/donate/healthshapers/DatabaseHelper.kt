package com.donate.healthshapers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.security.MessageDigest

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserData.db"
        private const val DATABASE_VERSION = 2  // Updated version
        const val TABLE_NAME = "users"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_PHONE = "phoneNumber"
        const val COL_EMAIL = "email"
        const val COL_USER_TYPE = "userType"
        const val COL_PFP = "pfp"
        const val COL_PASSWORD = "password"  // Column for storing password
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_NAME TEXT, " +
                "$COL_PHONE TEXT, " +
                "$COL_EMAIL TEXT, " +
                "$COL_USER_TYPE TEXT, " +
                "$COL_PFP TEXT, " +
                "$COL_PASSWORD TEXT)") // Password column for hashed password
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    // Insert user data into the database
    fun insertUserData(user: DataClass): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, user.name)
            put(COL_PHONE, user.phoneNumber)
            put(COL_EMAIL, user.email)
            put(COL_USER_TYPE, user.userType)
            put(COL_PFP, user.pfp)
            val hashedPassword = hashPassword(user.password ?: "") // Hash password before storing
            put(COL_PASSWORD, hashedPassword)
            Log.d("DatabaseHelper", "Hashed Password (inserting): $hashedPassword") // Log inserted password
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Check if email already exists in the database
    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE LOWER($COL_EMAIL) = LOWER(?)",
            arrayOf(email)
        )

        val exists = cursor?.moveToFirst() == true
        cursor?.close()
        return exists
    }

    // Hash the password using SHA-256
    private fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(password.toByteArray())
            hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            password // Return the original password if hashing fails
        }
    }

    // Validate the email and password for login
    fun isValidUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val hashedPassword = hashPassword(password)  // Hash the entered password

        // Log the hashed password entered during login for debugging
        Log.d("DatabaseHelper", "Hashed Password (entered): $hashedPassword")

        // Query the stored hashed password
        val cursor: Cursor? = db.rawQuery(
            "SELECT $COL_PASSWORD FROM $TABLE_NAME WHERE LOWER($COL_EMAIL) = LOWER(?)",
            arrayOf(email)
        )

        // Check if cursor contains data and the column is present
        if (cursor != null && cursor.moveToFirst()) {
            // Ensure the column exists
            val passwordColumnIndex = cursor.getColumnIndex(COL_PASSWORD)

            if (passwordColumnIndex == -1) {
                // Handle error if column is not found
                Log.e("DatabaseHelper", "Column $COL_PASSWORD not found in the database")
                cursor.close()
                return false
            }

            val storedHashedPassword = cursor.getString(passwordColumnIndex)

            // Log the stored hashed password for debugging
            Log.d("DatabaseHelper", "Stored Hashed Password: $storedHashedPassword")

            // Check if the entered password matches the stored hash
            val isValid = storedHashedPassword == hashedPassword
            cursor.close()
            return isValid
        }

        cursor?.close()
        return false
    }
}
