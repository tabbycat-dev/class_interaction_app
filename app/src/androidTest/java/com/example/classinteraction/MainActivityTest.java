package com.example.classinteraction;

import android.content.ComponentName;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    public ActivityTestRule<CheckClass> mCheckClassTestRule =
            new ActivityTestRule<CheckClass>(CheckClass.class);

    private String email ="kevin.ng@gmail.com";
    private String password ="kevin123";
    private String classcode="5501";

    @Test
    public void testUserInputSenario(){
        /*Test user log in successfully */

        //input email
        Espresso.onView(withId(R.id.et_email)).perform(typeText(email));
        Espresso.closeSoftKeyboard();

        //input password
        Espresso.onView(withId(R.id.et_password)).perform(typeText(password));

        //close soft keyboard
        Espresso.closeSoftKeyboard();

        //perform button click
        Espresso.onView(withId(R.id.btnLogin)).perform(click());

        //checking launch CheckClass Activity when log in successful
        Intent intent = new Intent();
        mCheckClassTestRule.launchActivity(intent);

        //type class code
        Espresso.onView(withId(R.id.classCodeET)).perform(typeText(classcode));
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}