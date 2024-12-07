package com.anikinkirill.tapyou.presentation.config

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anikinkirill.tapyou.R
import com.anikinkirill.tapyou.databinding.FragmentTapYouConfigBinding
import com.anikinkirill.tapyou.presentation.chart.TapYouPointsChartFragment

class TapYouPointsConfigFragment : Fragment(R.layout.fragment_tap_you_config) {

    private val binding by viewBinding(FragmentTapYouConfigBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            applyButton.setOnClickListener {
                if (isPointsAmountValid()) {
                    val pointsAmount = pointsAmountEditText.text.toString().toInt()
                    val fragment = TapYouPointsChartFragment.newInstance(pointsAmount)
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainContainerFrameLayout, fragment)
                        .addToBackStack(fragment::class.java.name)
                        .commit()
                } else {
                    pointsAmountEditText.error = "Enter value between 1 and 1000"
                }
            }
        }
    }

    private fun isPointsAmountValid(): Boolean {
        val pointsAmount = binding.pointsAmountEditText.text.toString().toIntOrNull()
        return pointsAmount != null && pointsAmount in 1..1000
    }
}