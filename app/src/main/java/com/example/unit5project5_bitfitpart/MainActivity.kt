package com.example.unit5project5_bitfitpart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var rvEntries: RecyclerView
    private lateinit var emptyStateView: View
    private lateinit var tvAvgCalories: TextView
    private lateinit var tvTotalEntries: TextView
    private lateinit var tvMinMaxCalories: TextView
    private lateinit var btnAddEntry: MaterialButton

    private lateinit var adapter: FitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvEntries = findViewById(R.id.rvEntries)
        emptyStateView = findViewById(R.id.emptyStateView)
        tvAvgCalories = findViewById(R.id.tvAvgCalories)
        tvTotalEntries = findViewById(R.id.tvTotalEntries)
        tvMinMaxCalories = findViewById(R.id.tvMinMaxCalories)
        btnAddEntry = findViewById(R.id.btnAddEntry)

        adapter = FitAdapter()
        rvEntries.layoutManager = LinearLayoutManager(this)
        rvEntries.adapter = adapter

        btnAddEntry.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                (application as BitFitApplication).db.bitFitDao().getAll().collect { entries ->
                    adapter.updateItems(entries)

                    if (entries.isEmpty()) {
                        emptyStateView.visibility = View.VISIBLE
                        rvEntries.visibility = View.GONE
                        tvAvgCalories.text = "0"
                        tvTotalEntries.text = "0"
                        tvMinMaxCalories.text = "0 / 0"
                    } else {
                        emptyStateView.visibility = View.GONE
                        rvEntries.visibility = View.VISIBLE

                        val totalCalories = entries.sumOf { it.calories }
                        val avgCalories = totalCalories / entries.size
                        val minCal = entries.minOf { it.calories }
                        val maxCal = entries.maxOf { it.calories }

                        tvAvgCalories.text = avgCalories.toString()
                        tvTotalEntries.text = entries.size.toString()
                        tvMinMaxCalories.text = "$minCal / $maxCal"
                    }
                }
            }
        }
    }
}