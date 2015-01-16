package com.codefest.rearsensordroid;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Carlos on 1/16/2015.
 */
public class FullscreenActivityTest extends ActivityInstrumentationTestCase2<FullscreenActivity> {

    public FullscreenActivityTest() {
        super(FullscreenActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    /**
     * Example test
     */
    public void testIsProgressBarVisible() {
        onView(withId(R.id.progressBar1)).check(matches(isDisplayed()));
    }
}
