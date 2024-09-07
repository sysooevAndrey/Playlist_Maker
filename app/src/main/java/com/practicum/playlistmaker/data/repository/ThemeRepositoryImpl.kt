package com.practicum.playlistmaker.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.domain.api.ThemeRepository
import com.practicum.playlistmaker.util.App

class ThemeRepositoryImpl(context: Context) : ThemeRepository {
    companion object {
        const val PLAYLIST_MAKER_PREFERENCES: String = "PLAYLIST_MAKER_PREFERENCES"
        const val APP_THEME_KEY = "APP_THEME"
    }

    private val sharedPreferences = context.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(APP_THEME_KEY, isDarkTheme)
            .apply()
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(
            APP_THEME_KEY,
            App.DARK_APP_THEME_DEFAULT
        )
    }
}