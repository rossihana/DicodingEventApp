package com.dicoding.dicodingeventapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dicodingeventapp.R

class RTO : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rto)
        supportActionBar?.hide()

        val networkManager = NetworkManager(this)
        networkManager.observe(this) { isConnected ->
            if (isConnected) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}