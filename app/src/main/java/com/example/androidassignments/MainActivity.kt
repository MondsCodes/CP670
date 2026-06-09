package com.example.androidassignments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACTIVITY_NAME = "MainActivity"
        private const val REQUEST_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(ACTIVITY_NAME, "inside onCreate")

        val button = findViewById<Button>(R.id.button2)   // adjust to your button's ID
        button.setOnClickListener {
            val intent = Intent(this, ListItemsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult")
            if (resultCode == RESULT_OK) {
                val messagePassed = data?.getStringExtra("Response")
                Toast.makeText(this, getString(R.string.passed_message, messagePassed), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart()   { super.onStart();   Log.i(ACTIVITY_NAME, "inside onStart") }
    override fun onResume()  { super.onResume();  Log.i(ACTIVITY_NAME, "inside onResume") }
    override fun onPause()   { super.onPause();   Log.i(ACTIVITY_NAME, "inside onPause") }
    override fun onStop()    { super.onStop();    Log.i(ACTIVITY_NAME, "inside onStop") }
    override fun onDestroy() { super.onDestroy(); Log.i(ACTIVITY_NAME, "inside onDestroy") }
}