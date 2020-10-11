package com.example.feedme

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AwardsActivity"

class AwardsActivity : AppCompatActivity() {
    private lateinit var achievement_title_label: TextView
    private lateinit var health_1_award: ImageView
    private lateinit var health_7_award: ImageView
    private lateinit var health_30_award: ImageView

    private lateinit var walk_2000_1_award: ImageView
    private lateinit var walk_2000_7_award: ImageView
    private lateinit var walk_5000_1_award: ImageView

    private lateinit var mochi_health_award: ImageView
    private lateinit var age_100_award: ImageView
    private lateinit var japan_award: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awards_linear)

        Log.d(TAG, "Starting to setup awards in onCreate()")
        achievement_title_label = findViewById(R.id.achievements_title)
        health_1_award = findViewById(R.id.health_1)
        health_7_award = findViewById(R.id.health_7)
        health_30_award = findViewById(R.id.health_30)

        walk_2000_1_award = findViewById(R.id.walk_2000_1)
        walk_2000_7_award = findViewById(R.id.walk_2000_7)
        walk_5000_1_award = findViewById(R.id.walk_5000_1)

        mochi_health_award = findViewById(R.id.happy_medal)
        age_100_award = findViewById(R.id.age_100_days)
        japan_award = findViewById(R.id.japan_medal)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Setting listeners in onStart()")
        //award on click for each button
        health_1_award.setOnClickListener {
            changeDetailFragment(Awards.HEALTH_1.awardName)
        }
        health_7_award.setOnClickListener {
            changeDetailFragment(Awards.HEALTH_7.awardName)
        }
        health_30_award.setOnClickListener {
            changeDetailFragment(Awards.HEALTH_30.awardName)
        }

        walk_2000_1_award.setOnClickListener {
            changeDetailFragment(Awards.WALK_1.awardName)
        }
        walk_2000_7_award.setOnClickListener {
            changeDetailFragment(Awards.WALK_2.awardName)
        }
        walk_5000_1_award.setOnClickListener {
            changeDetailFragment(Awards.WALK_3.awardName)
        }

        age_100_award.setOnClickListener {
            changeDetailFragment(Awards.AGE.awardName)
        }
        mochi_health_award.setOnClickListener {
            changeDetailFragment(Awards.HAPPY_MOCHI.awardName)
        }
        japan_award.setOnClickListener {
            changeDetailFragment(Awards.TRAVELLER.awardName)
        }
    }

    fun changeDetailFragment(award_name: String) {
        val fragment = AwardDetailFragment.newInstance(award_name)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.awards_description_fragment_container, fragment)
            .commit()
        Log.d(TAG, "Switch out award detail fragment")
    }
}