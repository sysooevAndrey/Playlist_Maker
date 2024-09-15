package com.practicum.playlistmaker.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.media.MediaActivity
import com.practicum.playlistmaker.setting.ui.SettingActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            search.setOnClickListener { openActivity(SearchActivity::class.java) }
            media.setOnClickListener { openActivity(MediaActivity::class.java) }
            settings.setOnClickListener { openActivity(SettingActivity::class.java) }
        }
    }

    private fun <A : Activity> openActivity(activityType: Class<A>) {
        val displayIntent = Intent(this, activityType)
        startActivity(displayIntent)
    }
}