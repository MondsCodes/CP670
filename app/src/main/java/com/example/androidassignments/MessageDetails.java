package com.example.androidassignments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        if (savedInstanceState == null) {
            // Running on a phone, so pass null for the ChatWindow reference
            MessageFragment fragment = new MessageFragment(null);
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.messageFrameLayout, fragment)
                    .commit();
        }
    }
}
