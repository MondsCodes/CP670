package com.example.androidassignments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val ACTIVITY_NAME = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Start Chat button
        val startChatButton = findViewById<Button>(R.id.startChatButton)
        startChatButton.setOnClickListener {
            Log.i(ACTIVITY_NAME, "User clicked Start Chat")
            val intent = Intent(this, ChatWindow::class.java)
            startActivity(intent)
        }

        // Test Toolbar button (Part 4)
        val testToolbarButton = findViewById<Button>(R.id.buttonTestToolbar)
        testToolbarButton.setOnClickListener {
            Log.i(ACTIVITY_NAME, "User clicked Test Toolbar")
            startActivity(Intent(this, TestToolbar::class.java))
        }
    }
}