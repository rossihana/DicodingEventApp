package com.dicoding.dicodingeventapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventapp.R
import com.dicoding.dicodingeventapp.data.entity.FavoriteEvent
import com.dicoding.dicodingeventapp.databinding.ActivityDetailBinding
import com.dicoding.dicodingeventapp.ui.utils.SettingPreferences
import com.dicoding.dicodingeventapp.ui.utils.dataStore
import com.dicoding.dicodingeventapp.ui.viewmodel.EventDatabaseViewModel
import com.dicoding.dicodingeventapp.ui.viewmodel.MainViewModel
import com.dicoding.dicodingeventapp.ui.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityDetailBinding? = null
    private var event: FavoriteEvent? = null
    private var isFavorite = false
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var eventDatabaseViewModel: EventDatabaseViewModel

    private val binding get() = _binding!!

    companion object {
        const val EVENT_DETAIL = "event_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventDatabaseViewModel = obtainViewModel(this@DetailActivity)

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val id = intent.getIntExtra(EVENT_DETAIL, 0)
        mainViewModel.getDetailEvent(id)

        eventDatabaseViewModel.getEventById(id).observe(this) { eventFromDb ->
            if (eventFromDb != null) {
                event = eventFromDb
                isFavorite = eventFromDb.isFavorite
                updateFavoriteIcon(isFavorite)
            }
        }

        mainViewModel.eventDetail.observe(this) { eventDetail ->
            if (event == null) {
                event = FavoriteEvent(
                    title = eventDetail.name,
                    isFavorite = false,
                    id = eventDetail.id,
                    imageEvent = eventDetail.mediaCover
                )
            }
            val quota = eventDetail.quota - eventDetail.registrants
            with(binding) {
                tvDetailEvent.text = eventDetail.name
                tvOwner.text = eventDetail.ownerName
                tvDetailQuota.text = quota.toString()
                tvDetailBeginTime.text = eventDetail.beginTime
                tvDetailEndTime.text = eventDetail.endTime
                tvDetailDeskripsi.text = HtmlCompat.fromHtml(
                    eventDetail.description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            Glide.with(this)
                .load(eventDetail.mediaCover)
                .into(binding.imagePhoto)

            showLoading(false)
        }

        binding.btnRegister.setOnClickListener(this)
        binding.fabFavorite.setOnClickListener(this)

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnRegister.id -> {
                val intentUrl = Intent(Intent.ACTION_VIEW).apply {
                    data = mainViewModel.eventDetail.value?.link?.toUri()
                }
                startActivity(intentUrl)
            }
            binding.fabFavorite.id -> {
                toggleFavoriteStatus()
            }
        }
    }

    private fun toggleFavoriteStatus() {
        event?.let { currentEvent ->
            isFavorite = !isFavorite
            currentEvent.isFavorite = isFavorite

            if (isFavorite) {
                eventDatabaseViewModel.insert(currentEvent)
                Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
            } else {
                eventDatabaseViewModel.delete(currentEvent)
                Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show()
            }
            updateFavoriteIcon(isFavorite)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): EventDatabaseViewModel {
        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[EventDatabaseViewModel::class.java]
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
