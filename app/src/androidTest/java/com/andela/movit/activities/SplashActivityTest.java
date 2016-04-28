package com.andela.movit.activities;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andela.movit.Movit;
import com.andela.movit.views.activities.SplashActivity;
import com.andela.movit.views.activities.TrackerActivity;

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
        Movit app = Movit.getApp();
        app.setIdle(false);
        Espresso.registerIdlingResources(app);
        intended(hasComponent(TrackerActivity.class.getCanonicalName()));
        Intents.release();
    }
}