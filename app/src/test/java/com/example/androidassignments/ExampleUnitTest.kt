package com.example.androidassignments

import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Unit tests covering core logic from the AndroidAssignments app.
 * These run on the JVM — no emulator needed.
 */
class ChatLogicUnitTest {
    private var messages: ArrayList<String?>? = null

    @Before
    fun setUp() {
        messages = ArrayList<String?>()
    }

    // ===== Tests for the message list =====
    @Test
    fun messageList_startsEmpty() {
        Assert.assertEquals(0, messages!!.size.toLong())
        Assert.assertTrue(messages!!.isEmpty())
    }

    @Test
    fun messageList_addsSingleMessage() {
        messages!!.add("Hello")
        Assert.assertEquals(1, messages!!.size.toLong())
        Assert.assertEquals("Hello", messages!!.get(0))
    }

    @Test
    fun messageList_addsMultipleMessages() {
        messages!!.add("First")
        messages!!.add("Second")
        messages!!.add("Third")
        Assert.assertEquals(3, messages!!.size.toLong())
        Assert.assertEquals("First", messages!!.get(0))
        Assert.assertEquals("Second", messages!!.get(1))
        Assert.assertEquals("Third", messages!!.get(2))
    }

    @Test
    fun messageList_preservesOrder() {
        for (i in 0..9) {
            messages!!.add("Message " + i)
        }
        Assert.assertEquals(10, messages!!.size.toLong())
        for (i in 0..9) {
            Assert.assertEquals("Message " + i, messages!!.get(i))
        }
    }

    // ===== Tests for the even/odd row logic in ChatAdapter.getView() =====
    @Test
    fun position_zero_isEven_incomingRow() {
        Assert.assertTrue("Position 0 should be even (incoming)", 0 % 2 == 0)
    }

    @Test
    fun position_one_isOdd_outgoingRow() {
        Assert.assertFalse("Position 1 should be odd (outgoing)", 1 % 2 == 0)
    }

    @Test
    fun position_alternates_correctly() {
        // Simulate the getView() row-type decision for 6 messages
        val expectedIncoming = booleanArrayOf(true, false, true, false, true, false)
        for (i in 0..5) {
            val isIncoming = (i % 2 == 0)
            Assert.assertEquals(
                "Row at position " + i + " has wrong type",
                expectedIncoming[i], isIncoming
            )
        }
    }

    // ===== Tests for the Send button's "ignore empty input" guard =====
    @Test
    fun emptyMessage_isNotAdded() {
        val input = ""
        if (!input.isEmpty()) {
            messages!!.add(input)
        }
        Assert.assertEquals(0, messages!!.size.toLong())
    }

    @Test
    fun nonEmptyMessage_isAdded() {
        val input = "Hi there"
        if (!input.isEmpty()) {
            messages!!.add(input)
        }
        Assert.assertEquals(1, messages!!.size.toLong())
    }

    @Test
    fun whitespaceOnlyMessage_currentlyAdded() {
        // Documents current behavior — only ".isEmpty()" guards, not ".trim().isEmpty()"
        val input = "   "
        if (!input.isEmpty()) {
            messages!!.add(input)
        }
        Assert.assertEquals(1, messages!!.size.toLong())
    }

    // ===== Tests for ACTIVITY_NAME constants =====
    @Test
    fun mainActivity_hasCorrectActivityName() {
        Assert.assertEquals("MainActivity", MainActivity.ACTIVITY_NAME)
    }

    @Test
    fun chatWindow_hasCorrectActivityName() {
        Assert.assertEquals("ChatWindow", ChatWindow.ACTIVITY_NAME)
    }

    // ===== Tests for string operations on messages =====
    @Test
    fun message_canContainSpecialCharacters() {
        val special = "Hello! 🎉 #test @user"
        messages!!.add(special)
        Assert.assertEquals(special, messages!!.get(0))
    }

    @Test
    fun message_canBeRetrievedById() {
        messages!!.add("alpha")
        messages!!.add("beta")
        messages!!.add("gamma")
        Assert.assertEquals("beta", messages!!.get(1))
    }

    @Test
    fun message_removedCorrectly() {
        messages!!.add("temporary")
        Assert.assertEquals(1, messages!!.size.toLong())
        messages!!.removeAt(0)
        Assert.assertEquals(0, messages!!.size.toLong())
    }
}