package com.example.feedme

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "InstructionsActivity"

class InstructionsActivity : AppCompatActivity() {

    private lateinit var instructions_title: TextView
    private lateinit var instructions_text: TextView
    private lateinit var mochi_friend_icon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions)

        instructions_title = findViewById(R.id.instructions_title)
        instructions_text = findViewById(R.id.instructions_body)
        mochi_friend_icon = findViewById(R.id.mochi_friend_icon)

    }

}