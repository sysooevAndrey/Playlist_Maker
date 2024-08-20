package com.practicum.playlistmaker.data.dto

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "PLAYLIST_MAKER_PREFERENCES"
        const val APP_THEME_KEY = "APP_THEME"
        const val DARK_APP_THEME_DEFAULT = false
    }

    private var isDarkTheme = DARK_APP_THEME_DEFAULT
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences =
            getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        isDarkTheme = sharedPreferences.getBoolean(APP_THEME_KEY, DARK_APP_THEME_DEFAULT)
        switchTheme(isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}