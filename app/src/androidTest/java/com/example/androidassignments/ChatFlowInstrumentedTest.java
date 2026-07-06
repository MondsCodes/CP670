package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests — run on an emulator or physical device.
 * Tests UI flow and inter-activity navigation.
 */
@RunWith(AndroidJUnit4.class)
public class ChatFlowInstrumentedTest {

    @Rule
    public ActivityScenarioRule<ChatWindow> activityRule =
            new ActivityScenarioRule<>(ChatWindow.class);

    // ===== Visibility tests =====

    @Test
    public void sendButton_isDisplayed() {
        onView(withId(R.id.sendButton)).check(matches(isDisplayed()));
    }

    @Test
    public void editText_isDisplayed() {
        onView(withId(R.id.chatEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void listView_isDisplayed() {
        onView(withId(R.id.chatListView)).check(matches(isDisplayed()));
    }

    // ===== Interaction tests =====

    @Test
    public void typingMessage_andClickingSend_clearsEditText() {
        onView(withId(R.id.chatEditText))
                .perform(typeText("Hello world"), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());
        onView(withId(R.id.chatEditText)).check(matches(withText("")));
    }

    @Test
    public void clickingSendWithEmptyEditText_doesNotCrash() {
        // Verifies the empty-string guard works
        onView(withId(R.id.sendButton)).perform(click());
        onView(withId(R.id.sendButton)).check(matches(isDisplayed()));
    }

    @Test
    public void multipleMessages_canBeSent() {
        onView(withId(R.id.chatEditText))
                .perform(typeText("First"), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());

        onView(withId(R.id.chatEditText))
                .perform(typeText("Second"), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());

        onView(withId(R.id.chatEditText))
                .perform(typeText("Third"), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());

        // EditText should be cleared after last send
        onView(withId(R.id.chatEditText)).check(matches(withText("")));
    }

    // ===== Persistence test =====

    @Test
    public void sentMessage_persistsAcrossRecreate() {
        String unique = "Persisted-" + System.currentTimeMillis();

        onView(withId(R.id.chatEditText))
                .perform(typeText(unique), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());

        // Recreate the activity — messages should reload from the database
        activityRule.getScenario().recreate();

        onView(withText(unique)).check(matches(isDisplayed()));
    }
}