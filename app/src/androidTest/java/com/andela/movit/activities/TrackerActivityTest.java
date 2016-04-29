package com.andela.movit.activities;

import android.location.Location;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageButton;

import com.andela.movit.Movit;
import com.andela.movit.R;
import com.andela.movit.views.activities.MovementActivity;
import com.andela.movit.views.activities.SettingsActivity;
import com.andela.movit.views.activities.TrackerActivity;
import com.andela.movit.views.activities.VisitActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrackerActivityTest {
    @Rule
    public ActivityTestRule<TrackerActivity> rule
            = new ActivityTestRule<>(TrackerActivity.class);

    @Test
    public void testLabelsDisplayed() {
        checkIfAppIdle();
        onView(withId(R.id.trackButton)).perform(click());
        onView(withId(R.id.activity_name))
                .check(matches(withText("Current status: Unknown")));
        pauseRunner();
        onView(withId(R.id.activity_name))
                .check(matches(withText("Current status: Standing Still")));
        setAppIdle();
    }

    @Test
    public void testMovementsDisplayed() {
        checkIfAppIdle();
        Intents.init();
        openDrawer();
        onView(withText("My Movements")).perform(click());
        intended(hasComponent(MovementActivity.class.getCanonicalName()));
        Intents.release();
        setAppIdle();
    }

    @Test
    public void testLocationsDisplayed() {
        checkIfAppIdle();
        Intents.init();
        openDrawer();
        onView(withText("My Locations")).perform(click());
        intended(hasComponent(VisitActivity.class.getCanonicalName()));
        Intents.release();
        setAppIdle();
    }

    private void openDrawer() {
        onView(allOf(isInToolbar, isImageButton)).perform(click());
    }

    private Matcher<View> isImageButton = withClassName(is(ImageButton.class.getName()));
    private Matcher<View> isInToolbar = isDescendantOfA(withId(R.id.toolbar));

    private void pauseRunner() {
        Movit app = Movit.getApp();
        app.setIdle(false);
        Espresso.registerIdlingResources(app);
    }

    private void checkIfAppIdle() {
        if (!isAppIdle()) {
            pauseRunner();
        }
    }

    private boolean isAppIdle() {
        return Movit.getApp().isIdleNow();
    }

    private void setAppIdle() {
        Movit.getApp().setIdle(true);
    }
}