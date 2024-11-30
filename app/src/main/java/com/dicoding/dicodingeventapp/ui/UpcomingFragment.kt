package com.dicoding.dicodingeventapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingeventapp.data.response.ListEventsItem
import com.dicoding.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingeventapp.ui.viewmodel.MainViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val mainViewModel by viewModels<MainViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding
            .inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUpcoming.layoutManager = layoutManager

        if (mainViewModel.listEventUpComing.value.isNullOrEmpty()) {
            mainViewModel.getDicodingEvents(1)
        }

        mainViewModel.listEventUpComing.observe(viewLifecycleOwner) { listEventUpComing ->
            setEventsData(listEventUpComing)
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEventsData(listEventsData: List<ListEventsItem>) {
        val adapter = EventAdapter(listEventsData)
        binding.rvUpcoming.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarUpComing.visibility = View.VISIBLE
        } else {
            binding.progressBarUpComing.visibility = View.GONE
        }
    }

}