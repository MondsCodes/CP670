package com.example.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ToolbarUnitTest {

    // Mirrors the Item 3 -> Item 1 logic: a new non-empty message replaces the default
    private String applyNewMessage(String current, String entered) {
        if (entered != null && !entered.trim().isEmpty()) {
            return entered.trim();
        }
        return current;
    }

    @Test
    public void newMessage_replacesDefault() {
        String result = applyNewMessage("You selected item 1", "Hello World");
        assertEquals("Hello World", result);
        assertNotEquals("You selected item 1", result);
    }

    @Test
    public void emptyMessage_keepsDefault() {
        String result = applyNewMessage("You selected item 1", "   ");
        assertEquals("You selected item 1", result);
    }
}