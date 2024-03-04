package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "PLAYLIST_MAKER_PREFERENCES"
        const val APP_THEME_KEY = "APP_THEME"
        const val DARK_APP_THEME_DEFAULT = false
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
    }

    private var darkTheme = DARK_APP_THEME_DEFAULT

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs =
            getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(APP_THEME_KEY, DARK_APP_THEME_DEFAULT)
        switchTheme(darkTheme)
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}