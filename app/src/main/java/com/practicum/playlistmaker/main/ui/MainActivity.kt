package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.setting.ui.SettingActivity
import com.practicum.playlistmaker.media.MediaActivity
import com.practicum.playlistmaker.util.button.NavigationButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationButton.click(this, binding.search, SearchActivity::class.java)
        NavigationButton.click(this, binding.media, MediaActivity::class.java)
        NavigationButton.click(this, binding.settings, SettingActivity::class.java)
    }
}