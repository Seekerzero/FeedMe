package com.example.feedme

import android.location.Location
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

/**
 * This test suite does not test all methods as it should.
 * For simplicity, we selected functions that we had written to demonstrate our understanding of JUnit (instead of doing testing with 3rd party libraries).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        mainActivity = mActivityRule.activity
    }

    @Test
    fun testInJapan() {
        var tokyo: Location = Location("")
        tokyo.latitude = 35.6804
        tokyo.longitude = 139.7690
        Assert.assertTrue(mainActivity.areWeInJapan(tokyo))
    }

    @Test
    fun testNotInJapan() {
        var worcester: Location = Location("")
        worcester.latitude = 42.2626
        worcester.longitude = 71.8023
        Assert.assertFalse(mainActivity.areWeInJapan(worcester))
    }

    @Test
    fun testCreateDate() {
        var today = Date()
        var dateString =
            (today.year + 1900).toString() + (today.month + 1).toString() + today.date.toString()
        Assert.assertEquals(mainActivity.createDateForToday(), dateString)
    }

    @After
    fun tearDown() {
    }
}