package com.example.feedme

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.concurrent.TimeUnit


private const val TAG = "MainActivity"
private const val MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 0
private const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
private const val GPS_PERMISSIONS_REQUEST_CODE = 2
class MainActivity : AppCompatActivity() {
    private lateinit var mochi_name_label: EditText
    private lateinit var mochi_age_label: TextView
    private lateinit var mochi_icon: ImageView
    private lateinit var food_icon: ImageView
    private lateinit var step_counter: TextView
    private lateinit var food_tracker_icon_1: ImageView
    private lateinit var food_tracker_icon_2: ImageView
    private lateinit var food_tracker_icon_3: ImageView
    private lateinit var awards_button: Button
    private lateinit var instructions_button: Button
    private var mochi_name: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var steps: Long = 0
    var mealsEaten = 0

    private val dailyInfoListViewModel: DailyInfoListViewModel by lazy {
        ViewModelProviders.of(this).get(DailyInfoListViewModel::class.java)
    }

    var fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dailyInfoListViewModel.initializeWithDummyData()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


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
        step_counter = findViewById(R.id.step_tracker_number)


        fitAuthorization()
        getGoogleAccount()
        subscribe()
        getCurStepCount()
        gpsAuthorization()

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
                mochi_name_label.setTextColor(getResources().getColor(R.color.white))
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
            val intent = Intent(this, InstructionsActivity::class.java)
            startActivity(intent)
        }

    }

    // for dragging food to mochi
    private val dragListen = View.OnDragListener { v, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                mochi_icon.setImageResource(R.drawable.green_mochi_eat_happy)
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                // Ignore the event
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                mochi_icon.setImageResource(R.drawable.green_mochi_ok)
                true
            }
            DragEvent.ACTION_DROP -> {
                // mochi happy
                mealsEaten++

                // make a strawberry bright
                if (mealsEaten == 1) {
                    food_tracker_icon_1.clearColorFilter()
                    mochi_icon.setImageResource(R.drawable.green_mochi_happy)
                    mochi_icon.maxHeight = 500
                    mochi_icon.maxWidth = 500

                } else if (mealsEaten == 2) {
                    food_tracker_icon_2.clearColorFilter()
                    mochi_icon.setImageResource(R.drawable.green_mochi_happy)
                } else if (mealsEaten >= 3) {
                    food_tracker_icon_3.clearColorFilter()
                    mochi_icon.setImageResource(R.drawable.green_mochi_super_happy)
                }
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                true
            }
            else -> {
                false
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getCurStepCount()
        refresh(1000)

    }

    private fun refresh(timeSteps: Long){
        var handler: Handler = Handler()
        var runnable: Runnable = Runnable {
            kotlin.run {
                onResume()
            }
        }
        handler.postDelayed(runnable, timeSteps)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                accessGoogleFit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // update with daily info
        Log.d(TAG, "Updating daily info database")
        try {

//            dailyInfoListViewModel.addDailyInfo(DailyInfo(Date(), steps, mealsEaten))
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
        }
    }

    fun fitAuthorization() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
                )
            }
            else -> {
                // Permission is already granted, do nothing
            }
        }
    }

    // TODO: SABRINA IS DUMB AND CANNOT GET THIS TO WORK
    fun gpsAuthorization() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d(TAG, "Yay")
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                Log.d(TAG, "Sad rationale, why no permission?")
            }
            else -> {
                // Directly ask for the permission.
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    GPS_PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    private fun getGoogleAccount() {

        var account: GoogleSignInAccount = if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignIn.getLastSignedInAccount(this)!!
        } else {
            GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        }

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this, // your activity
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions
            );
        } else {
            accessGoogleFit();
        }

    }

    //TODO NOTE TO ZHANHONG I ALSO ADDED SOMETHING HERE
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Nothing happened
                } else {
                    // should disable the step counter here
                }
                return
            }
            // this does not work :(
            GPS_PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Nothing happened
                    Log.d(TAG, "yay, permission granted")
                } else {
                    // permission not granted. Sadness
                    Log.d(TAG, "bruh")
                }
                return
            }
            else -> {
            }
        }
    }

    private fun accessGoogleFit() {
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(Date())
        val endTime: Long = cal.getTimeInMillis()
        cal.add(Calendar.YEAR, -1)
        val startTime: Long = cal.getTimeInMillis()
        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        var account: GoogleSignInAccount = if(GoogleSignIn.getLastSignedInAccount(this) != null){
            GoogleSignIn.getLastSignedInAccount(this)!!
        }else{
            GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        }

        Fitness.getHistoryClient(this, account)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // Use response data here
                Log.d(TAG, "Fitness.getHistoryClient OnSuccess()")
            }
            .addOnFailureListener { e -> Log.d(TAG, "Fitness.getHistoryClient OnFailure()", e) }
    }

    fun subscribe() {
        Fitness.getRecordingClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
            .listSubscriptions(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addOnSuccessListener { subscriptions ->
                for (sc in subscriptions) {
                    val dt: DataType? = sc.dataType
                    if (dt != null) {
                        Log.i(TAG, "Active subscription for data type: " + dt.name)
                        if(dt.name == "com.google.step_count.cumulative")
                        {
                            Log.i(
                                TAG,
                                "com.google.step_count.cumulative is already activated no need to subscribe again"
                            )
                        }
                    }
                    else{
                        // To create a subscription, invoke the Recording API. As soon as the subscription is
                        // active, fitness data will start recording.
                        Log.d(TAG, "Start to subscribe TYPE_STEP_COUNT_CUMULATIVE ")
                        Fitness.getRecordingClient(
                            this, GoogleSignIn.getAccountForExtension(
                                this,
                                fitnessOptions
                            )
                        )
                            .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                            .addOnSuccessListener { unused: Void? ->
                                Log.i(
                                    TAG,
                                    "Subscription for TYPE_STEP_COUNT_CUMULATIVE was successful!"
                                )
                            }
                            .addOnFailureListener { e: Exception ->
                                Log.i(
                                    TAG, "There was a problem subscribing: " +
                                            e.localizedMessage
                                )
                            }
                    }
                }
            }
    }

    private fun getCurStepCount(){
        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
            .readDailyTotalFromLocalDevice(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { result: DataSet ->
                val curSteps =
                    if (result.isEmpty) 0 else result.dataPoints[0].getValue(Field.FIELD_STEPS)
                        .asInt()
//                Log.d(TAG, "current Step Count: $curSteps")
                steps = curSteps.toLong()
                step_counter.text = curSteps.toString()
            }
            .addOnFailureListener { e: java.lang.Exception ->
                Log.i(
                    TAG, "There was a problem getting steps: " +
                            e.localizedMessage
                )
            }



    }



}