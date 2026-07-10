package com.example.androidassignments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    protected static final int MESSAGE_DETAILS_REQUEST = 10;
    protected static final String FRAGMENT_TAG = "messageFragment";

    ListView chatListView;
    EditText chatEditText;
    Button sendButton;
    ArrayList<String> chatMessages;
    ChatAdapter messageAdapter;
    SQLiteDatabase db;
    Cursor cursor;
    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        chatListView = findViewById(R.id.chatListView);
        chatEditText = findViewById(R.id.chatEditText);
        sendButton = findViewById(R.id.sendButton);

        // If the FrameLayout was loaded, we are using the tablet layout (>= 600dp wide)
        FrameLayout frameLayout = findViewById(R.id.messageFrameLayout);
        isTablet = frameLayout != null;
        Log.i(ACTIVITY_NAME, "isTablet = " + isTablet);

        // Initialize the messages list
        chatMessages = new ArrayList<>();

        // Open (or create) the database and store it as an instance variable
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Load any existing messages from the database into the ArrayList
        requeryMessages();

        // Print information about the Cursor
        Log.i(ACTIVITY_NAME, "Cursor's column count =" + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column name = " + cursor.getColumnName(i));
        }

        // Set up the adapter and attach it to the ListView
        messageAdapter = new ChatAdapter(this);
        chatListView.setAdapter(messageAdapter);

        // Show message details when a message is clicked
        chatListView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString(MessageFragment.KEY_MESSAGE, chatMessages.get(position));
            bundle.putLong(MessageFragment.KEY_ID, id);

            if (isTablet) {
                // Tablet: load the fragment into the FrameLayout next to the list
                MessageFragment fragment = new MessageFragment(ChatWindow.this);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.messageFrameLayout, fragment, FRAGMENT_TAG)
                        .commit();
            } else {
                // Phone: launch the MessageDetails activity
                Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, MESSAGE_DETAILS_REQUEST);
            }
        });

        // Send button click listener
        sendButton.setOnClickListener(v -> {
            String message = chatEditText.getText().toString();
            if (!message.isEmpty()) {
                // Insert the new message into the database
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

                requeryMessages();
                messageAdapter.notifyDataSetChanged(); // refresh ListView
                chatEditText.setText("");              // clear input
                Log.i(ACTIVITY_NAME, "User clicked Send: " + message);
            }
        });
    }

    // Re-run the database query, refreshing the class cursor and the messages list
    private void requeryMessages() {
        if (cursor != null) {
            cursor.close();
        }
        cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null, null);

        chatMessages.clear();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            chatMessages.add(message);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message);
            cursor.moveToNext();
        }
    }

    // Delete a message from the database and refresh the list.
    // On a tablet this is called by the MessageFragment, which is also removed.
    public void deleteMessage(long id, MessageFragment fragment) {
        db.delete(ChatDatabaseHelper.TABLE_NAME,
                ChatDatabaseHelper.KEY_ID + "=?", new String[]{String.valueOf(id)});
        Log.i(ACTIVITY_NAME, "Deleted message with id=" + id);

        requeryMessages();
        messageAdapter.notifyDataSetChanged();

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MESSAGE_DETAILS_REQUEST && resultCode == RESULT_OK && data != null) {
            long id = data.getLongExtra(MessageFragment.KEY_ID, -1);
            if (id != -1) {
                deleteMessage(id, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
    }

    // Inner class — ChatAdapter
    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return chatMessages.size();
        }

        @Override
        public String getItem(int position) {
            return chatMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }
    }
}
