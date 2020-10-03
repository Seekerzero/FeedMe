package com.example.feedme

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "AwardsListViewModel"

class AwardsListViewModel : ViewModel() {

    // create list of all awards
    var awards: MutableMap<String, Award> = mutableMapOf(
        Pair(
            Awards.HEALTHY_HEART.awardName,
            Award(
                Awards.HEALTHY_HEART.awardName,
                null,
                Awards.HEALTHY_HEART.description,
                R.drawable.pineapple,
                false
            )
        ),
        Pair(
            Awards.STRONG_EATER.awardName,
            Award(
                Awards.STRONG_EATER.awardName,
                null,
                Awards.STRONG_EATER.description,
                R.drawable.strawberry,
                false
            )
        )
    )

    init {
        Log.d(TAG, "Initialized awards list view model")
    }

}