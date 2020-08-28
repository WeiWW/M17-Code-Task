package com.ann.m17test

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ann.m17test.ui.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.koin.test.KoinTest

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ann.m17test", appContext.packageName)
    }

    @Test
    fun checkAfterTypingKeyboardIsHide(){
        onView(withId(R.id.searchEditText))
            .perform(ViewActions.typeText("test"),ViewActions.closeSoftKeyboard())

        assertTrue(isKeyboardHide())
    }
    @Test
    fun checkAfterTypingClickBackKeyboardIsHide(){
        onView(withId(R.id.searchEditText))
            .perform(ViewActions.typeText("test"),ViewActions.pressBack())

        assertTrue(isKeyboardHide())
    }

    @Test
    fun checkAfterTypingClickIMEActionKeyboardIsHide(){
        onView(withId(R.id.searchEditText))
            .perform(ViewActions.typeText("test"),ViewActions.pressImeActionButton())

        assertTrue(isKeyboardHide())
    }

    @Test
    fun checkAfterTypingClickEnterKeyboardIsHide(){
        onView(withId(R.id.searchEditText))
            .perform(ViewActions.typeText("test"),ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))

        assertTrue(isKeyboardHide())
    }

    @Test
    fun afterTypingProgressBarIsDisplayed(){
        onView(withId(R.id.searchEditText))
            .perform(ViewActions.typeText("test"),ViewActions.pressImeActionButton())

        onView(withId(R.id.progressBar)).check(matches(ViewMatchers.isDisplayed()))
    }

    private fun isKeyboardHide(): Boolean {
        val inputMethodManager = InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }
}