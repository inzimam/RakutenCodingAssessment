package com.example.github.repositories

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.isEmptyString
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainFragmentTest {
    val itemCount = 5

    @Rule
    @JvmField
    public var activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun is_List_Visible_App_Launch() {
        onView(withId(R.id.news_list)).check(matches(isDisplayed()))
    }

    @Test
    fun is_All_Title_Is_Visible() {
        for (i in 0 until this.itemCount) {
            onView(
                RecyclerViewMatcher(R.id.news_list)
                    .atPositionOnView(i, R.id.title)
            )
                .check(matches(isDisplayed()));

        }
    }

    @Test
    fun is_All_Title_Is_Non_Empty() {
        for (i in 0 until this.itemCount) {
            onView(
                RecyclerViewMatcher(R.id.news_list)
                    .atPositionOnView(i, R.id.title)
            ).check(matches(withText(not(isEmptyString()))));
        }
    }

    @Test
    fun countPrograms() {
        onView(withId(R.id.news_list))
            .check(matches(RecyclerViewMatcher().withItemCount(20)))
    }

    @Test
    fun test_recyclerview_click_navigation() {
        onView(withId(R.id.news_list)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                click()
            )
        )
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}