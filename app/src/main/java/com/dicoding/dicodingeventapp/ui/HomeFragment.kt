package com.dicoding.dicodingeventapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingeventapp.data.response.ListEventsItem
import com.dicoding.dicodingeventapp.databinding.FragmentHomeBinding
import com.dicoding.dicodingeventapp.ui.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val mainViewModel by viewModels<MainViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding
            .inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManagerHorizontal =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeEventUpcoming.layoutManager = layoutManagerHorizontal

        val layoutManagerVertical = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvHomeEventFinished.layoutManager = layoutManagerVertical

        if (mainViewModel.listEventUpComing.value.isNullOrEmpty()) {
            mainViewModel.getDicodingEvents(1)
        }

        if (mainViewModel.listEventFinished.value.isNullOrEmpty()) {
            mainViewModel.getDicodingEvents(0)
        }

        mainViewModel.listEventUpComing.observe(viewLifecycleOwner) { listEventUpComing ->
            setEventsDataUpComing(listEventUpComing)
        }

        mainViewModel.listEventFinished.observe(viewLifecycleOwner) { listEventFinished ->
            setEventsDataFinished(listEventFinished.take(5))
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEventsDataUpComing(listEventsData: List<ListEventsItem>) {
        val adapter = EventAdapter(listEventsData)
        binding.rvHomeEventUpcoming.adapter = adapter
    }

    private fun setEventsDataFinished(listEventsData: List<ListEventsItem>) {
        val adapter = EventAdapter(listEventsData)
        binding.rvHomeEventFinished.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarHome.visibility = View.VISIBLE
        } else {
            binding.progressBarHome.visibility = View.GONE
        }
    }
}



