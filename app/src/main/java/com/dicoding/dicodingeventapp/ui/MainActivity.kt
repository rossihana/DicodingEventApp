package com.dicoding.dicodingeventapp.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.dicodingeventapp.R
import com.dicoding.dicodingeventapp.databinding.ActivityMainBinding
import com.dicoding.dicodingeventapp.ui.utils.SettingPreferences
import com.dicoding.dicodingeventapp.ui.utils.dataStore
import com.dicoding.dicodingeventapp.ui.viewmodel.SettingViewModel
import com.dicoding.dicodingeventapp.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_upcoming,
                R.id.navigation_finished,
                R.id.navigation_favorite,
                R.id.navigation_setting
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        networkManager = NetworkManager(this)

        settingViewModel = obtainViewModel(this)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        observeNetworkConnectivity()
    }

    private fun obtainViewModel(activity: AppCompatActivity): SettingViewModel {
        val pref = SettingPreferences.getInstance(activity.dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[SettingViewModel::class.java]
    }

    private fun observeNetworkConnectivity() {
        networkManager.observe(this) { isConnected ->
            if (!isConnected) {
                val intent = Intent(this, RTO::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}