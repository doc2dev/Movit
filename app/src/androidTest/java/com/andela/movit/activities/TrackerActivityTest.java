package com.andela.movit.activities;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andela.movit.Movit;
import com.andela.movit.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrackerActivityTest {
    @Rule
    public ActivityTestRule<TrackerActivity> rule
            = new ActivityTestRule<>(TrackerActivity.class);

    @Test
    public void testLabelsDisplayed() {
        Movit app = Movit.getApp();
        app.setIdle(false);
        Espresso.registerIdlingResources(app);
        onView(withId(R.id.trackButton)).perform(click());
        onView(withId(R.id.activity_name))
                .check(matches(withText("Waiting for activity recognition...")));
        app.setIdle(false);
        Espresso.registerIdlingResources(app);
        onView(withId(R.id.activity_name))
                .check(matches(withText("You have been Standing Still for:")));
    }
}