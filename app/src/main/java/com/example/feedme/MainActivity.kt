package com.example.feedme

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var mochi_name_label: EditText
    private lateinit var mochi_age_label: TextView
    private lateinit var mochi_icon: ImageView
    private lateinit var food_icon: ImageView
    private lateinit var step_counter: TextView // TODO
    private lateinit var food_tracker_icon_1: ImageView
    private lateinit var food_tracker_icon_2: ImageView
    private lateinit var food_tracker_icon_3: ImageView
    private lateinit var awards_button: Button
    private lateinit var instructions_button: Button
    private var mochi_name: String? = null
    var mealsEaten = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Wire up widgets
        mochi_name_label = findViewById(R.id.mochi_name)
        mochi_age_label = findViewById(R.id.mochi_age)
        mochi_icon = findViewById(R.id.mochi)
        food_icon = findViewById(R.id.food_icon)
        food_tracker_icon_1 = findViewById(R.id.food_tracker_icon1)
        food_tracker_icon_2 = findViewById(R.id.food_tracker_icon2)
        food_tracker_icon_3 = findViewById(R.id.food_tracker_icon3)
        food_tracker_icon_1.setColorFilter(R.color.gray)
        food_tracker_icon_2.setColorFilter(R.color.gray)
        food_tracker_icon_3.setColorFilter(R.color.gray)
        awards_button = findViewById(R.id.awards_icon)
        instructions_button = findViewById(R.id.instructions_icon)

        // set listeners
        mochi_name_label.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                mochi_name = s.toString()
                Log.d(TAG, "Mochi name changed")
                mochi_name_label.setTextColor(getResources().getColor(R.color.blue))
            }
        })

        // for dragging to mochi
        food_icon.setOnLongClickListener { v: View ->
            // Create a new ClipData
            val item = ClipData.Item(v.tag as? CharSequence)
            val dragData = ClipData(
                v.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )
            Log.d(TAG, "Food icon long clicked")
            // instantiate drag shadow builder
            val myShadow = DragShadowBuilder(food_icon)
            // start drag
            v.startDrag(
                dragData,       // data to be dragged
                myShadow,       // drag shadow builder
                null,       // no need to use local data
                0       // flags (not currently used, set to 0)
            )

        }
        mochi_icon.setOnDragListener(dragListen)

        awards_button.setOnClickListener {
            Log.d(TAG, "Awards button clicked")
            val intent = Intent(this, AwardsActivity::class.java)
            startActivity(intent)
        }

        instructions_button.setOnClickListener {
            Log.d(TAG, "Instruction button clicked")
            // TODO: GO TO OTHER SCREEN
        }
    }

    // for dragging food to mochi
    private val dragListen = View.OnDragListener { v, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d(TAG, "Action drag started")
                mochi_icon.setImageResource(R.drawable.green_mochi_eat_happy)
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d(TAG, "Action drag entered")
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                // Ignore the event
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                // no longer within view's box, ignore for now TODO
                Log.d(TAG, "Action drag exited")
                true
            }
            DragEvent.ACTION_DROP -> {
                Log.d(TAG, "Action drop")
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(TAG, "Action drag ended")
                // mochi happy
                mealsEaten++
                mochi_icon.setImageResource(R.drawable.green_mochi_super_happy)
                // make a strawberry bright
                if (mealsEaten == 1) {
                    food_tracker_icon_1.clearColorFilter()
                } else if (mealsEaten == 2) {
                    food_tracker_icon_2.clearColorFilter()
                } else if (mealsEaten == 3) {
                    food_tracker_icon_3.clearColorFilter()
                }
                true
            }
            else -> {
                Log.d(TAG, "Some other drag event" + event.action.toString())
                false
            }
        }

    }
}