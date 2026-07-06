package com.example.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for ChatDatabaseHelper — exercise a real SQLite
 * insert/query round-trip on the device. The database is deleted before
 * and after each test so it never interferes with real app data.
 */
@RunWith(AndroidJUnit4.class)
public class ChatDatabaseInstrumentedTest {

    private Context context;
    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(ChatDatabaseHelper.DATABASE_NAME);
        dbHelper = new ChatDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @After
    public void tearDown() {
        if (db != null) {
            db.close();
        }
        context.deleteDatabase(ChatDatabaseHelper.DATABASE_NAME);
    }

    @Test
    public void insertedMessage_canBeQueriedBack() {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, "Hello database");
        long rowId = db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        assertTrue("insert should return a valid row id", rowId != -1);

        Cursor cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null, null);

        assertTrue("cursor should have at least one row", cursor.moveToFirst());
        String stored = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
        assertEquals("Hello database", stored);
        cursor.close();
    }

    @Test
    public void cursor_hasExpectedColumns() {
        Cursor cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null, null);

        assertEquals(2, cursor.getColumnCount());
        assertTrue(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID) >= 0);
        assertTrue(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE) >= 0);
        cursor.close();
    }

    @Test
    public void multipleMessages_arePersisted() {
        String[] messages = {"one", "two", "three"};
        for (String m : messages) {
            ContentValues values = new ContentValues();
            values.put(ChatDatabaseHelper.KEY_MESSAGE, m);
            db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        }

        Cursor cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null, null);

        assertEquals(3, cursor.getCount());
        cursor.close();
    }
}
