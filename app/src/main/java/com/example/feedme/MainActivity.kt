package com.example.feedme

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
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
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


private const val TAG = "MainActivity"
private const val MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 0
private const val MY_PERMISSIONS_REQUEST_ACTIVITY_LOCATION = 1
private const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 2
private const val MY_TEST_PERMISSIONS_REQUEST_CODE = 3

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
    private lateinit var layout: ConstraintLayout
    private var mochi_name: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var steps: Long = 0
    var mealsEaten = 0
    var stepCounterEnabled: Boolean = true
    var permissionRecognitionDone = false
    var permissionLocationDone = false
    var permissionRequestProcessDone = false
    private val jsonHandler = JsonHandler()

    private val dailyInfoListViewModel: DailyInfoListViewModel by lazy {
        ViewModelProviders.of(this).get(DailyInfoListViewModel::class.java)
    }

    var fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!File(this.filesDir, "MochiInfo.json").exists()) {
            jsonHandler.createMochiInfoFile(this)
        }
        jsonHandler.readMochiInfoFile(this)

        dailyInfoListViewModel.initializeWithDummyData()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Wire up widgets
        layout = findViewById(R.id.constraint_layout_parent)
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

        // set listeners
        mochi_name_label.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                jsonHandler.mochiInfo.name = s.toString()
                jsonHandler.createMochiInfoFile(this@MainActivity)
            }

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
                mochi_name_label.setTextColor(getResources().getColor(R.color.green))
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

        // TODO: to Sabrina, I just found there are bugs here after first install, so I disable these function in temporally
        // todo there is a bug here which will cause the app close after first install. something should be initialized before this.
        // Process: com.example.feedme, PID: 31229
        //    java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //        at java.util.ArrayList.get(ArrayList.java:437)
        //        at com.example.feedme.MainActivity$onCreate$5.onChanged(MainActivity.kt:186)
        //        at com.example.feedme.MainActivity$onCreate$5.onChanged(MainActivity.kt:52)
        //        at androidx.lifecycle.LiveData.considerNotify(LiveData.java:133)
        //        at androidx.lifecycle.LiveData.dispatchingValue(LiveData.java:151)
        //        at androidx.lifecycle.LiveData.setValue(LiveData.java:309)
        //        at androidx.lifecycle.LiveData$1.run(LiveData.java:93)
        //        at android.os.Handler.handleCallback(Handler.java:938)
        //        at android.os.Handler.dispatchMessage(Handler.java:99)
        //        at android.os.Looper.loop(Looper.java:223)

//        dailyInfoListViewModel.dailyInfoLiveData.observe(
//            this,
//            androidx.lifecycle.Observer { entries: List<DailyInfo> ->
//                Log.d(TAG, "There are ${entries.size} entries")
//
//                Log.d(
//                    TAG,
//                    "An entry looks like this: Date: ${entries[0].date}, Steps: ${entries[0].steps}, Times Eaten: ${entries[0].times_eaten}"
//                )
//                mochi_age_label.text = "Mochi Age: ${entries.size} days"
//            }
//        )

        //todo also bug here I believe it should be the same type of problem
//        Process: com.example.feedme, PID: 7957
//        android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: DailyInfo.date (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)
//        at android.database.sqlite.SQLiteConnection.nativeExecuteForLastInsertedRowId(Native Method)
//        at android.database.sqlite.SQLiteConnection.executeForLastInsertedRowId(SQLiteConnection.java:938)
//        at android.database.sqlite.SQLiteSession.executeForLastInsertedRowId(SQLiteSession.java:790)
//        at android.database.sqlite.SQLiteStatement.executeInsert(SQLiteStatement.java:88)
//        at androidx.sqlite.db.framework.FrameworkSQLiteStatement.executeInsert(FrameworkSQLiteStatement.java:51)
//        at androidx.room.EntityInsertionAdapter.insert(EntityInsertionAdapter.java:64)
//        at com.example.feedme.database.DailyInfoDao_Impl.addDailyInfo(DailyInfoDao_Impl.java:77)
//        at com.example.feedme.DailyInfoRepository$addDailyInfo$1.run(DailyInfoRepository.kt:34)
//        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
//        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
//        at java.lang.Thread.run(Thread.java:923)

//        dailyInfoListViewModel.getEntry(createDateForToday()).observe(
//            this,
//            androidx.lifecycle.Observer { entry: DailyInfo? ->
//                // if entry for today does not exist
//                if (entry == null) {
//                    dailyInfoListViewModel.addDailyInfo(
//                        DailyInfo(
//                            createDateForToday(),
//                            steps,
//                            mealsEaten
//                        )
//                    )
//                }
//            }
//        )

    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onResume() {
        super.onResume()
        if (!permissionRequestProcessDone) {
            checkAuthorization()
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (areWeInJapan(location)) {
                    changeUItoJapan()
                }
//                Log.d(TAG, "Current coordinates: ${location?.latitude}, ${location?.longitude}")
            }
            getCurStepCount()
            refresh(2000)
        }
    }

    /**
     * For creating the unique key for today's date to use as primary key in dao
     * Example: "20201011" for Oct 11, 2020
     */
    @VisibleForTesting
    fun createDateForToday(): String {
        val cal: Calendar = Calendar.getInstance()
        cal.time = Date()
        return cal.get(Calendar.YEAR)
            .toString() + (cal.get(Calendar.MONTH) + 1).toString() + cal.get(Calendar.DATE)
            .toString()
    }

    // for dragging food to mochi
    private val dragListen = View.OnDragListener { _, event ->
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
                    checkHappyChomperAward()
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



    /**
     * Checks to see if they have happy chomper award (ate 3x in one day). If not, give it to them
     */
    private fun checkHappyChomperAward() {
        // todo check if they have happy chomper award. Give if no

    }

    /**
     * Checks mochi's age on start up to see if get 100 day award
     */
    private fun checkAgeAward() {
        // todo check age based on birthday in json
        // todo give them award if >=100
    }



    /**
     * Checks to see if you are within 100 miles of Tokyo
     */
    @VisibleForTesting
    fun areWeInJapan(location: Location?): Boolean {
        if ((kotlin.math.abs((location?.latitude?.toFloat() ?: 0).toFloat() - 36.2048)
                .toFloat() < 2)
            && (kotlin.math.abs((location?.longitude?.toFloat() ?: 0).toFloat() - 138.2529) < 2)
        ) {
            //  we are within 100ish miles of Tokyo!
//            Log.d(TAG, "We in japan!!")
            // TODO add Japan award
            return true
        }
        return false
    }

    private fun changeUItoJapan() {
        // change background and theme
        layout.setBackgroundResource(R.drawable.background)
        food_tracker_icon_1.setImageResource(R.drawable.food_tracker_icon1_jp)
        food_tracker_icon_2.setImageResource(R.drawable.food_tracker_icon2_jp)
        food_tracker_icon_3.setImageResource(R.drawable.food_tracker_icon3_jp)
    }

    /**
     * Award setting function for walking 2000 steps for the last 7 days
     */
    private fun got2000_7award() {
        if (walk2000_7days()) {
            // they get award
        }
        // set award todo
    }

    /**
     * Helper function
     * Calculate whether you have gotten 2000 steps in past 7 days
     */
    private fun walk2000_7days(): Boolean {
        var past7days: MutableList<String> = pastDaysDates(7)
        var dateDoesNotExistOrQualify = false
        for (date in past7days) {
            // get entry
            dailyInfoListViewModel.getEntry(date).observe(
                this,
                androidx.lifecycle.Observer { entry: DailyInfo? ->
                    // if entry for today does not exist or did not take more than 2000 steps
                    if ((entry == null) || (entry.steps < 2000)) {
                        dateDoesNotExistOrQualify = true
                    }
                }
            )
            if (dateDoesNotExistOrQualify) {
                return false
            }
        }
        return true
    }

    /**
     * Award setting function for eating 3 meals a day for the last 7 days
     */
    private fun ate_3_meals_7_days() {
        if (ate_3_meals_7_days_check()) {
            // they get award
        }
        // set award todo
    }

    /**
     * Helper function
     * Calculate whether you have gotten 2000 steps in past 7 days
     */
    private fun ate_3_meals_7_days_check(): Boolean {
        var past7days: MutableList<String> = pastDaysDates(7)
        var dateDoesNotExistOrQualify = false
        for (date in past7days) {
            // get entry
            dailyInfoListViewModel.getEntry(date).observe(
                this,
                androidx.lifecycle.Observer { entry: DailyInfo? ->
                    // if entry for today does not exist or did not take more than 2000 steps
                    if ((entry == null) || (entry.times_eaten < 3)) {
                        dateDoesNotExistOrQualify = true
                    }
                }
            )
            if (dateDoesNotExistOrQualify) {
                return false
            }
        }
        return true
    }

    /**
     * Check to see if ate 3 times a day and walked 2000 steps for last 7 consecutive days
     */
    private fun setAwards_3meals_2000steps() {
        // todo if don't have award
        // and if (helper_3meals_2000steps()){} // then give award!
    }

    private fun helper_3meals_2000steps(): Boolean {
        var past7days: MutableList<String> = pastDaysDates(7)
        var dateDoesNotExist = false
        var walkedAndAte = false
        for (date in past7days) {
            // get entry
            dailyInfoListViewModel.getEntry(date).observe(
                this,
                androidx.lifecycle.Observer { entry: DailyInfo? ->
                    // if entry for today does not exist or did not take more than 2000 steps
                    if (entry == null) {
                        dateDoesNotExist = true
                    } else { // not null
                        if ((entry.times_eaten >= 3) && (entry.steps >= 2000)) {
                            walkedAndAte = true
                        }
                    }
                }
            )
            // if entry for date doesn't exist, or didn't eat 3 times, or didn't walk 2000 steps today
            if (dateDoesNotExist || !walkedAndAte) {
                return false
            }
            // reset 3x eating and 2000 steps flag for next loop
            walkedAndAte = false
        }
        return true
    }

    /**
     * Check to see if ate 3 times a day for last 30 consecutive days
     */
    private fun setAwards_3meals_30days() {
        // todo if don't have award
        // and if (helper_3meals_30days()){} // then give award!
    }

    private fun helper_3meals_30days(): Boolean {
        var past7days: MutableList<String> = pastDaysDates(30)
        var dateDoesNotExist = false
        var ate = false
        for (date in past7days) {
            // get entry
            dailyInfoListViewModel.getEntry(date).observe(
                this,
                androidx.lifecycle.Observer { entry: DailyInfo? ->
                    // if entry for today does not exist or did not take more than 2000 steps
                    if (entry == null) {
                        dateDoesNotExist = true
                    } else { // not null
                        if (entry.times_eaten >= 3) {
                            ate = true
                        }
                    }
                }
            )
            // if entry for date doesn't exist, or didn't eat 3 times, or didn't walk 2000 steps today
            if (dateDoesNotExist || !ate) {
                return false
            }
            // reset 3x eating flag for next loop
            ate = false
        }
        return true
    }

    /**
     * Helper function to create dates for past 7 days
     */
    private fun pastDaysDates(numDays: Int): MutableList<String> {
        var dates: MutableList<String> = mutableListOf()
        var today: String = createDateForToday()
        dates.add(today)
        for (i in 1 until (numDays + 1)) {
            dates.add((today.toInt() - i).toString())
        }
        return dates
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun refresh(timeSteps: Long) {
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
            updateDailyInfoAndAwards()
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
        }
    }

    private fun updateDailyInfoAndAwards() {
        // add or update today's entry with ending steps and meals eaten
        dailyInfoListViewModel.updateDailyInfo(
            DailyInfo(
                createDateForToday(),
                steps,
                mealsEaten
            )
        )
        // check step count for today
        if (steps >= 2000) {
            // todo give award
        }
        if (steps >= 5000) {
            // todo give award
        }
    }

    private fun getPermissionCode(name: String): Int {
        val code = when (name) {
            "android.permission.ACTIVITY_RECOGNITION" -> MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
            "android.permission.ACCESS_COARSE_LOCATION" -> MY_PERMISSIONS_REQUEST_ACTIVITY_LOCATION
            else -> null
        }
        return code ?: 0
    }

    private fun getPermissionName(permission: String): String {
        val name = when (permission) {
            "android.permission.ACTIVITY_RECOGNITION" -> "Accessing your Physics Activity"
            "android.permission.ACCESS_COARSE_LOCATION" -> "Accessing your Location"
            else -> null
        }
        return name ?: "Unknown Activity "
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkAuthorization() {
        permissionRecognitionDone = if (Build.VERSION.SDK_INT < 29) {
            prepareGoogleFitClient()
            subscribe()
            true
        } else {
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_GRANTED)
        }
        permissionLocationDone =
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
        if (permissionRecognitionDone) {
            stepCounterEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE
            )
            prepareGoogleFitClient()
            subscribe()
        }
        if (permissionLocationDone) {
        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE
            )
        }
        permissionRequestProcessDone = permissionRecognitionDone && permissionLocationDone
        if (permissionRequestProcessDone){
            onResume()
        }
    }

    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ): Boolean {
        var result = false
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, id ->
                result = true
                requestPermissions(arrayOf(permission), getPermissionCode(permission))
            }
        builder.create().show()
        return result
    }

    private fun prepareGoogleFitClient() {

        val account: GoogleSignInAccount = if (GoogleSignIn.getLastSignedInAccount(this) != null) {
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

    @SuppressLint("MissingPermission")
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
                    Log.d(TAG, "yay, fit permission granted")
                    permissionRequestProcessDone = true
                    stepCounterEnabled = true
                } else {
                    // should disable the step counter here
                    stepCounterEnabled = false
                    Log.d(TAG, "fit permission is not granted")
                }
                return
            }
            MY_PERMISSIONS_REQUEST_ACTIVITY_LOCATION -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Log.d(TAG, "yay, gps permission granted")
                    permissionRequestProcessDone = true
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (areWeInJapan(location)) {
                            changeUItoJapan()
                        }
                    }

                } else {
                    // should disable the step counter here
                    Log.d(TAG, "gps permission is not granted")
                }
                return
            }
            else -> {
            }
        }
    }

    private fun accessGoogleFit() {
        val cal: Calendar = Calendar.getInstance()
        cal.time = Date()
        val endTime: Long = cal.timeInMillis
        cal.add(Calendar.YEAR, -1)
        val startTime: Long = cal.timeInMillis
        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        val account: GoogleSignInAccount = if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignIn.getLastSignedInAccount(this)!!
        } else {
            GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        }

        Fitness.getHistoryClient(this, account)
            .readData(readRequest)
            .addOnSuccessListener {
                // Use response data here
                Log.d(TAG, "Fitness.getHistoryClient OnSuccess()")
            }
            .addOnFailureListener { e -> Log.d(TAG, "Fitness.getHistoryClient OnFailure()", e) }
    }

    private fun subscribe() {
        Fitness.getRecordingClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
            .listSubscriptions(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addOnSuccessListener { subscriptions ->
                for (sc in subscriptions) {
                    val dt: DataType? = sc.dataType
                    if (dt != null) {
                        Log.i(TAG, "Active subscription for data type: " + dt.name)
                        if (dt.name == "com.google.step_count.cumulative") {
                            Log.i(
                                TAG,
                                "com.google.step_count.cumulative is already activated no need to subscribe again"
                            )
                        }
                    } else {
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
                            .addOnSuccessListener {
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getCurStepCount() {
        if (stepCounterEnabled) {
            Fitness.getHistoryClient(
                this,
                GoogleSignIn.getAccountForExtension(this, fitnessOptions)
            )
                .readDailyTotalFromLocalDevice(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener { result: DataSet ->
                    val curSteps =
                        if (result.isEmpty) 0 else result.dataPoints[0].getValue(Field.FIELD_STEPS)
                            .asInt()
//                                    Log.d(TAG, "current Step Count: $curSteps")
//                    steps = curSteps.toLong()
                    step_counter.text = curSteps.toString()
                }
                .addOnFailureListener { e: java.lang.Exception ->
                    Log.i(
                        TAG, "There was a problem getting steps: " +
                                e.localizedMessage
                    )
                }

        } else {
            step_counter.visibility = View.GONE
            step_tracker_icon.visibility = View.GONE
        }

    }


}