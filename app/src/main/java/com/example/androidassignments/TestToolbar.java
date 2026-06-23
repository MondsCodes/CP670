package com.example.androidassignments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    private String snackbarMessage;   // message shown by Item 1; editable via Item 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        snackbarMessage = getString(R.string.snackbar_item_one);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_one) {
            Snackbar.make(findViewById(R.id.toolbarText), snackbarMessage, Snackbar.LENGTH_LONG).show();
            return true;

        } else if (id == R.id.action_two) {
            // Back-confirm dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_back_title);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> finish());
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                // do nothing
            });
            builder.create().show();
            return true;

        } else if (id == R.id.action_three) {
            // Custom dialog to set a new Snackbar message
            View customView = getLayoutInflater().inflate(R.layout.dialog_custom, null);
            EditText input = customView.findViewById(R.id.editNewMessage);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customView);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                String entered = input.getText().toString().trim();
                if (!entered.isEmpty()) {
                    snackbarMessage = entered;   // Item 1 will now show this
                }
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                // do nothing
            });
            builder.create().show();
            return true;

        } else if (id == R.id.action_about) {
            Toast.makeText(this, R.string.about_message, Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}