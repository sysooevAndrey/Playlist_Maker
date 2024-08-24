package com.practicum.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val APP_THEME_KEY = "APP_THEME"
        const val DARK_APP_THEME_DEFAULT = false
    }

    private var isDarkTheme = DARK_APP_THEME_DEFAULT
    override fun onCreate() {
        super.onCreate()
        val themeManager = Creator.getThemeManager(this)
        isDarkTheme = themeManager.getData()
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