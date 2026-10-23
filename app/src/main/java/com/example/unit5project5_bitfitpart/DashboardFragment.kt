package com.example.unit5project5_bitfitpart

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var tvAvgCalories: TextView
    private lateinit var tvTotalEntries: TextView
    private lateinit var tvMinMaxCalories: TextView
    private lateinit var llTrendContainer: LinearLayout
    private lateinit var tvNoChartData: TextView
    private lateinit var btnTestNotification: Button

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                NotificationHelper.showNotification(requireContext())
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAvgCalories = view.findViewById(R.id.tvAvgCalories)
        tvTotalEntries = view.findViewById(R.id.tvTotalEntries)
        tvMinMaxCalories = view.findViewById(R.id.tvMinMaxCalories)
        llTrendContainer = view.findViewById(R.id.llTrendContainer)
        tvNoChartData = view.findViewById(R.id.tvNoChartData)
        btnTestNotification = view.findViewById(R.id.btnTestNotification)

        btnTestNotification.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    NotificationHelper.showNotification(requireContext())
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                NotificationHelper.showNotification(requireContext())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val app = requireActivity().application as BitFitApplication
                app.db.bitFitDao().getAll().collect { entries ->
                    if (entries.isEmpty()) {
                        tvAvgCalories.text = "0"
                        tvTotalEntries.text = "0"
                        tvMinMaxCalories.text = "0 / 0"
                        llTrendContainer.removeAllViews()
                        tvNoChartData.visibility = View.VISIBLE
                    } else {
                        tvNoChartData.visibility = View.GONE

                        val totalCalories = entries.sumOf { it.calories }
                        val avgCalories = totalCalories / entries.size
                        val minCal = entries.minOf { it.calories }
                        val maxCal = entries.maxOf { it.calories }

                        tvAvgCalories.text = avgCalories.toString()
                        tvTotalEntries.text = entries.size.toString()
                        tvMinMaxCalories.text = "$minCal / $maxCal"

                        // Build visual bar chart breakdown
                        llTrendContainer.removeAllViews()
                        val maxVal = maxOf(maxCal, 1)

                        val inflater = LayoutInflater.from(requireContext())
                        entries.take(8).forEach { item ->
                            val barView = inflater.inflate(
                                android.R.layout.simple_list_item_1,
                                llTrendContainer,
                                false
                            ) as TextView

                            val progressPercent = (item.calories * 100) / maxVal
                            barView.text = "• ${item.name}: ${item.calories} cal ($progressPercent%)"
                            barView.textSize = 15f
                            barView.setPadding(0, 12, 0, 12)
                            barView.setTextColor(requireContext().getColor(R.color.text_primary))
                            llTrendContainer.addView(barView)
                        }
                    }
                }
            }
        }
    }
}