package com.example.androidassignments;

import android.widget.CheckBox;
import androidx.appcompat.app.AlertDialog;
import android.widget.Switch;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ListItemsActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ListItemsActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 20;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "inside onCreate");

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                launchCamera();
            }
        });

        Switch mySwitch = findViewById(R.id.switchButton);
        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, getString(R.string.switch_on), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.switch_off), Toast.LENGTH_LONG).show();
            }
        });

        CheckBox myCheckBox = findViewById(R.id.checkboxButton);
        myCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.ok, (dialog, id) -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "My information to share");
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    })
                    .show();
        });
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                if (imageBitmap != null) {
                    imageButton.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    private void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override protected void onStart()   { super.onStart();   Log.i(ACTIVITY_NAME, "inside onStart"); }
    @Override protected void onResume()  { super.onResume();  Log.i(ACTIVITY_NAME, "inside onResume"); }
    @Override protected void onPause()   { super.onPause();   Log.i(ACTIVITY_NAME, "inside onPause"); }
    @Override protected void onStop()    { super.onStop();    Log.i(ACTIVITY_NAME, "inside onStop"); }
    @Override protected void onDestroy() { super.onDestroy(); Log.i(ACTIVITY_NAME, "inside onDestroy"); }
}