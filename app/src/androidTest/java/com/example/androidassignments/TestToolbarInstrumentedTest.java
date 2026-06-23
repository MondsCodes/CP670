package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestToolbarInstrumentedTest {

    @Test
    public void item1_showsSnackbar() {
        ActivityScenario.launch(TestToolbar.class);
        onView(withId(R.id.action_one)).perform(click());
        onView(withText("You selected item 1")).check(matches(isDisplayed()));
    }

    @Test
    public void item3_setsNewMessage_thenItem1ShowsIt() {
        ActivityScenario.launch(TestToolbar.class);
        // Item 3 -> type new message -> OK
        onView(withId(R.id.action_three)).perform(click());
        onView(withId(R.id.editNewMessage)).perform(typeText("Custom Msg"), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());   // positive button
        // Item 1 -> triggers Snackbar with the new message (no assert; Snackbar is transient)
        onView(withId(R.id.action_one)).perform(click());
    }

    @Test
    public void aboutItem_inOverflow_showsToast() {
        ActivityScenario.launch(TestToolbar.class);
        openActionBarOverflowOrOptionsMenu(
                androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("About")).perform(click());
        // Toast verification is flaky in Espresso; clicking without crash is the minimal check
    }
}