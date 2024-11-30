package com.dicoding.dicodingeventapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingeventapp.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingeventapp.ui.utils.SettingPreferences
import com.dicoding.dicodingeventapp.ui.utils.dataStore
import com.dicoding.dicodingeventapp.ui.viewmodel.FavoriteEventViewModel
import com.dicoding.dicodingeventapp.ui.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteEventViewModel: FavoriteEventViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteEventViewModel = obtainViewModel(requireActivity() as AppCompatActivity)

        setupRecyclerView()
        getFavoriteList()

        favoriteEventViewModel.getFavoriteEvents().observe(viewLifecycleOwner) { favoriteEvents ->
            adapter = FavoriteAdapter(favoriteEvents)
            binding.rvFavorite.adapter = adapter
        }
   }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.layoutManager = layoutManager

        adapter = FavoriteAdapter(emptyList())
        binding.rvFavorite.adapter = adapter
    }

    private fun getFavoriteList() {
        favoriteEventViewModel.getFavoriteEvents().observe(viewLifecycleOwner) { favoriteEvents ->
            adapter.updateData(favoriteEvents)

            if (favoriteEvents.isEmpty()) {
                binding.tvNoFavorite.visibility = View.VISIBLE
                binding.rvFavorite.visibility = View.GONE
            } else {
                binding.tvNoFavorite.visibility = View.GONE
                binding.rvFavorite.visibility = View.VISIBLE
            }

            showLoading(false)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteEventViewModel {
        val pref = SettingPreferences.getInstance(activity.dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[FavoriteEventViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFavorite.visibility = View.VISIBLE
        } else {
            binding.progressBarFavorite.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}