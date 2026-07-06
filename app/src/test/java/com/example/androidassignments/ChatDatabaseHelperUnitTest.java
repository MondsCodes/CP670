package com.example.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JVM unit tests for ChatDatabaseHelper constants and configuration.
 * These do not require an Android device — they verify the static schema
 * definition used by both the helper and ChatWindow. The actual SQLite
 * round-trip is exercised in ChatDatabaseInstrumentedTest.
 */
public class ChatDatabaseHelperUnitTest {

    @Test
    public void databaseName_isMessagesDb() {
        assertEquals("Messages.db", ChatDatabaseHelper.DATABASE_NAME);
    }

    @Test
    public void versionNumber_isPositive() {
        assertTrue("VERSION_NUM must be >= 1", ChatDatabaseHelper.VERSION_NUM >= 1);
    }

    @Test
    public void columnConstants_areDefined() {
        assertNotNull(ChatDatabaseHelper.TABLE_NAME);
        assertNotNull(ChatDatabaseHelper.KEY_ID);
        assertNotNull(ChatDatabaseHelper.KEY_MESSAGE);
        assertTrue(ChatDatabaseHelper.TABLE_NAME.length() > 0);
        assertTrue(ChatDatabaseHelper.KEY_MESSAGE.length() > 0);
    }

    @Test
    public void keyConstants_areDistinct() {
        assertTrue(!ChatDatabaseHelper.KEY_ID.equals(ChatDatabaseHelper.KEY_MESSAGE));
    }
}
