package com.dicoding.dicodingeventapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingeventapp.data.response.ListEventsItem
import com.dicoding.dicodingeventapp.databinding.FragmentFinishedBinding
import com.dicoding.dicodingeventapp.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val mainViewModel by viewModels<MainViewModel>()
    private val binding get() = _binding!!
    private var searchJob: Job? = null
    private lateinit var eventsAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        if (mainViewModel.listEventFinished.value.isNullOrEmpty()) {
            mainViewModel.getDicodingEvents(0)
        }

        mainViewModel.listEventFinished.observe(viewLifecycleOwner) { listEventFinished ->
            setEventsData(listEventFinished)
        }

        mainViewModel.searchResults.observe(viewLifecycleOwner) { searchResult ->
            if (!searchResult.isNullOrEmpty()) {
                setEventsData(searchResult)
            } else {
                mainViewModel.listEventFinished.value?.let { setEventsData(it) }
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        setupSearchListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        binding.rvEventFinished.layoutManager = layoutManager
        eventsAdapter = EventAdapter(emptyList())
        binding.rvEventFinished.adapter = eventsAdapter
    }

    private fun setupSearchListener() {
        binding.svSearchEvent.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svSearchEvent.clearFocus()
                if (!query.isNullOrEmpty()) {
                    mainViewModel.getSearchEvents(query)
                } else {
                    resetSearchToDefault()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(500)
                    if (!newText.isNullOrEmpty()) {
                        mainViewModel.getSearchEvents(newText)
                    } else {
                        resetSearchToDefault()
                    }
                }
                return true
            }
        })
    }

    private fun resetSearchToDefault() {
        mainViewModel.resetSearchResult()
        mainViewModel.listEventFinished.value?.let { setEventsData(it) }
    }

    private fun setEventsData(listEventsData: List<ListEventsItem>) {
        eventsAdapter.updateData(listEventsData)

        if (listEventsData.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvEventFinished.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvEventFinished.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFinished.visibility = View.VISIBLE
        } else {
            binding.progressBarFinished.visibility = View.GONE
        }
    }

}