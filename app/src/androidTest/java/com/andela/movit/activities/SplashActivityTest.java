package com.andela.movit.activities;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andela.movit.activities.SplashActivity;
import com.andela.movit.activities.TrackerActivity;
import com.andela.movit.location.LocationHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> testRule
             = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void testTransition() throws Exception {
        Intents.init();
        Espresso.registerIdlingResources(LocationHelper.getLocationHelper());
        intended(hasComponent(TrackerActivity.class.getCanonicalName()));
        Intents.release();
    }
}

