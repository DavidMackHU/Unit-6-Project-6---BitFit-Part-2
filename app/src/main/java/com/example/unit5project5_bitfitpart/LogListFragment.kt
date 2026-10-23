package com.example.unit5project5_bitfitpart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class LogListFragment : Fragment(R.layout.fragment_log_list) {

    private lateinit var rvEntries: RecyclerView
    private lateinit var emptyStateView: View
    private lateinit var btnAddEntry: MaterialButton
    private lateinit var adapter: FitAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvEntries = view.findViewById(R.id.rvEntries)
        emptyStateView = view.findViewById(R.id.emptyStateView)
        btnAddEntry = view.findViewById(R.id.btnAddEntry)

        adapter = FitAdapter()
        rvEntries.layoutManager = LinearLayoutManager(requireContext())
        rvEntries.adapter = adapter

        btnAddEntry.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val app = requireActivity().application as BitFitApplication
                app.db.bitFitDao().getAll().collect { entries ->
                    adapter.updateItems(entries)

                    if (entries.isEmpty()) {
                        emptyStateView.visibility = View.VISIBLE
                        rvEntries.visibility = View.GONE
                    } else {
                        emptyStateView.visibility = View.GONE
                        rvEntries.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}