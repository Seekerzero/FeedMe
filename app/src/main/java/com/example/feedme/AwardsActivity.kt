package com.example.feedme

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AwardsActivity"

class AwardsActivity : AppCompatActivity() {
    private lateinit var achievement_title_label: TextView
    private lateinit var achievement_grid_placeholder: ImageView
    private lateinit var achievement_description_placeholder: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awards)

        achievement_title_label = findViewById(R.id.achievements_title)
        achievement_grid_placeholder = findViewById(R.id.award_gallery)
        achievement_description_placeholder = findViewById(R.id.award_description)
    }
}