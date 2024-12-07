package com.anikinkirill.tapyou.presentation.chart

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anikinkirill.tapyou.R
import com.anikinkirill.tapyou.databinding.FragmentTapYouPointsChartBinding
import com.anikinkirill.tapyou.domain.Text
import com.anikinkirill.tapyou.domain.getString
import com.anikinkirill.tapyou.domain.model.TapYouPoints
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class TapYouPointsChartFragment : Fragment(R.layout.fragment_tap_you_points_chart) {

    private val binding by viewBinding(FragmentTapYouPointsChartBinding::bind)
    private val viewModel by viewModel<TapYouPointsChartViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTopBar()
        loadPoints()
        observe()
    }

    private fun initTopBar() {
        binding.topBar.title.text = requireContext().getString(R.string.app_name)
        binding.topBar.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.collect { state ->
                when {
                    state.errorState != null -> showErrorState(text = state.errorState.errorText)
                    state.isLoading -> showLoadingState()
                    state.points.isNotEmpty() -> showPointsChart(points = state.points)
                }
            }
        }
    }

    private fun showPointsChart(points: List<TapYouPoints>) {
        with(binding) {
            progressBar.isVisible = false

            // Chart
            val lineDataSetEntries = points.map { (x, y) ->
                Entry(x.toFloat(), y.toFloat())
            }.sortedBy { it.x }
            chart.data = LineData(
                LineDataSet(lineDataSetEntries, "TapYouPointsDataSet")
            )
            chart.invalidate()
            chart.isVisible = true

            // Table
            points.forEach { point ->
                // row
                val row = TableRow(context)
                // x and y entries in row
                val xEntry = TextView(context).apply { text = point.x.toString() }
                val yEntry = TextView(context).apply { text = point.y.toString() }
                row.addView(xEntry)
                row.addView(yEntry)
                // add to table
                pointsTableLayout.addView(row)
            }
            pointsTableLayout.isVisible = true
        }
    }

    private fun showErrorState(text: Text) {
        binding.progressBar.isVisible = false
        Toast.makeText(requireContext(), text.getString(requireContext()), Toast.LENGTH_LONG).show()
        parentFragmentManager.popBackStack()
    }

    private fun showLoadingState() {
        binding.progressBar.isVisible = true
    }

    private fun loadPoints() {
        getAmountFromArgs()?.let { amount ->
            viewModel.loadPoints(amount)
        }
    }

    private fun getAmountFromArgs(): Int? {
        return arguments?.getInt(ARGS_KEY)
    }

    companion object {

        private const val ARGS_KEY = "ARGS_KEY"

        fun newInstance(amount: Int): TapYouPointsChartFragment {
            return TapYouPointsChartFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARGS_KEY, amount)
                }
            }
        }
    }
}