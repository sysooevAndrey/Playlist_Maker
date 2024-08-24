package com.practicum.playlistmaker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.button.NavigationButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NavigationButton.navigate<MaterialButton, SearchActivity>(
            this, R.id.search, SearchActivity::class.java
        )
        NavigationButton.navigate<MaterialButton, MediaActivity>(
            this, R.id.media, MediaActivity::class.java
        )
        NavigationButton.navigate<MaterialButton, SettingActivity>(
            this, R.id.settings, SettingActivity::class.java
        )
    }
}