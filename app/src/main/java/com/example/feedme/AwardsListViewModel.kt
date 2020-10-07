package com.example.feedme

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "AwardsListViewModel"

class AwardsListViewModel : ViewModel() {

    // create list of all awards
    var awards: MutableMap<String, Award> = mutableMapOf(
        Pair(
            Awards.HEALTH_1.awardName,
            Award(
                Awards.HEALTH_1.awardName,
                null,
                Awards.HEALTH_1.description,
                R.drawable.health_1,
                false
            )
        ),
        Pair(
            Awards.HEALTH_7.awardName,
            Award(
                Awards.HEALTH_7.awardName,
                null,
                Awards.HEALTH_7.description,
                R.drawable.health_7,
                false
            )
        ),
        Pair(
            Awards.HEALTH_30.awardName,
            Award(
                Awards.HEALTH_30.awardName,
                null,
                Awards.HEALTH_30.description,
                R.drawable.health_30,
                false
            )
        ),
        Pair(
            Awards.WALK_1.awardName,
            Award(
                Awards.WALK_1.awardName,
                null,
                Awards.WALK_1.description,
                R.drawable.walk_2000_1day,
                false
            )
        ),
        Pair(
            Awards.WALK_2.awardName,
            Award(
                Awards.WALK_2.awardName,
                null,
                Awards.WALK_2.description,
                R.drawable.walk_2000_7days,
                false
            )
        ),
        Pair(
            Awards.WALK_3.awardName,
            Award(
                Awards.WALK_3.awardName,
                null,
                Awards.WALK_3.description,
                R.drawable.walk_5000_1_day,
                false
            )
        ),
        Pair(
            Awards.TRAVELLER.awardName,
            Award(
                Awards.TRAVELLER.awardName,
                null,
                Awards.TRAVELLER.description,
                R.drawable.japan_medal,
                false
            )
        ),
        Pair(
            Awards.HAPPY_MOCHI.awardName,
            Award(
                Awards.HAPPY_MOCHI.awardName,
                null,
                Awards.HAPPY_MOCHI.description,
                R.drawable.happy_mochi_medal,
                false
            )
        ),
        Pair(
            Awards.AGE.awardName,
            Award(
                Awards.AGE.awardName,
                null,
                Awards.AGE.description,
                R.drawable.age_100_days,
                false
            )
        )
    )

    init {
        Log.d(TAG, "Initialized awards list view model")
    }

}