package com.example.feedme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

private const val TAG = "AwardDetailFragment"
private const val CLICKED_AWARD_NAME = "award_clicked_name"

class AwardDetailFragment : Fragment() {
    private lateinit var award_title_label: TextView
    private lateinit var award_description: TextView
    private lateinit var award_icon: ImageView
    private lateinit var award_date_label: TextView
    private var award_name: String? = null

    private var current_award: Award? = null
    private val awardsListViewModel: AwardsListViewModel by lazy {
        ViewModelProviders.of(this).get(AwardsListViewModel::class.java)
    }

    // actually create the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_award, container, false)
        award_name = arguments?.getSerializable(CLICKED_AWARD_NAME) as String?
        award_title_label = view.findViewById(R.id.award_name_label)
        award_description = view.findViewById(R.id.award_description)
        award_icon = view.findViewById(R.id.award_icon)
        award_date_label = view.findViewById(R.id.award_date_label)

        Log.d(TAG, "Award clicked: ${award_name} award")
        Log.d(TAG, "On create view AwardDetailFragment")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        current_award = awardsListViewModel.awards[award_name]
        award_icon.setImageResource(current_award?.award_icon ?: R.id.award_icon)
        award_title_label.text = (current_award?.award_name ?: "Award Name")
        award_description.setText(
            current_award?.award_description_string_resource ?: R.string.award_description_null
        )
        award_date_label.text = (current_award?.award_date?.toString()
            ?: "Date earned") // TODO when user gets award, set date!

    }

    companion object {
        fun newInstance(award_name: String): AwardDetailFragment {
            Log.d(TAG, "new instance of AwardDetailFragment")
            val args = Bundle().apply {
                putSerializable(CLICKED_AWARD_NAME, award_name)
            }
            return AwardDetailFragment().apply { arguments = args }
        }
    }
}