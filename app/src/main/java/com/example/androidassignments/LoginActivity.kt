package com.example.androidassignments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val ACTIVITY_NAME = "LoginActivity"
        private const val PREFS_NAME = "MyPrefs"
        private const val PREF_EMAIL = "DefaultEmail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.i(ACTIVITY_NAME, "inside onCreate")

        val emailField = findViewById<EditText>(R.id.editTextEmail)
        val passwordField = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        // Restore the last-used email (default if none saved yet)
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedEmail = prefs.getString(PREF_EMAIL, "email@domain.com")
        emailField.setText(savedEmail)

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()

            // Validate email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // Validate non-empty password
            if (password.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Save the email for next launch
            prefs.edit().putString(PREF_EMAIL, email).apply()

            // Go to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart()   { super.onStart();   Log.i(ACTIVITY_NAME, "inside onStart") }
    override fun onResume()  { super.onResume();  Log.i(ACTIVITY_NAME, "inside onResume") }
    override fun onPause()   { super.onPause();   Log.i(ACTIVITY_NAME, "inside onPause") }
    override fun onStop()    { super.onStop();    Log.i(ACTIVITY_NAME, "inside onStop") }
    override fun onDestroy() { super.onDestroy(); Log.i(ACTIVITY_NAME, "inside onDestroy") }
}