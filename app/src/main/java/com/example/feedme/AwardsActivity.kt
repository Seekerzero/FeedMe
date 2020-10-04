package com.example.feedme

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AwardsActivity"

class AwardsActivity : AppCompatActivity() {
    private lateinit var achievement_title_label: TextView
    private lateinit var achievement_grid_placeholder: ImageView

    private lateinit var achievement_grid_placeholder2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awards)

        achievement_title_label = findViewById(R.id.achievements_title)
        achievement_grid_placeholder = findViewById(R.id.award_gallery)
        achievement_grid_placeholder2 = findViewById(R.id.award_gallery2)

        //award on click for each button
        achievement_grid_placeholder.setOnClickListener {
            changeDetailFragment(Awards.HEALTHY_HEART.awardName)
            Log.d(TAG, "Healthy Heart Award pressed!")
        }

        //award on click for each button
        achievement_grid_placeholder2.setOnClickListener {
            changeDetailFragment(Awards.STRONG_EATER.awardName)
            Log.d(TAG, "Strong eater Award pressed!")
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