package com.anikinkirill.tapyou.presentation.chart

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout.LayoutParams
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
        observeState()
        observeActions()
    }

    private fun initTopBar() {
        binding.topBar.title.text = requireContext().getString(R.string.app_name)
        binding.topBar.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.topBar.rightActionButton.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_copy, null)
        )

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                saveImageToLocalStorage()
            } else {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(
                        R.string.no_permission_to_save_image,
                    ),
                    Toast.LENGTH_LONG,
                ).show()
            }
        }

        binding.topBar.rightActionButton.setOnClickListener {
            checkForPermissions(requestPermissionLauncher)
        }
    }

    private fun observeState() {
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

    private fun observeActions() {
        lifecycleScope.launch {
            viewModel.default.effectFlow.collect { action ->
                when (action) {
                    is TapYouPointsChartViewModel.Action.ShowToast -> {
                        Toast.makeText(
                            requireContext(),
                            action.message.getString(requireContext()),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
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
                val xEntry = TextView(context).apply {
                    text = point.x.toString()
                    layoutParams = TableRow.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    gravity = Gravity.CENTER
                }
                val yEntry = TextView(context).apply {
                    text = point.y.toString()
                    layoutParams = TableRow.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    gravity = Gravity.CENTER
                }
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

    private fun checkForPermissions(permissionsLauncher: ActivityResultLauncher<String>) {
        val isGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            saveImageToLocalStorage()
        } else {
            permissionsLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun saveImageToLocalStorage() {
        val bitmap = Bitmap.createBitmap(
            binding.root.width,
            binding.root.height,
            Bitmap.Config.ARGB_8888,
        )
        val canvas = Canvas(bitmap)
        binding.root.draw(canvas)
        viewModel.saveImageToLocalStorage(bitmap, requireContext())
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