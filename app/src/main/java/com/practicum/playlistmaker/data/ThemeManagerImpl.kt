package com.practicum.playlistmaker.data

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.domain.ThemeManager
import android.content.Context
import com.practicum.playlistmaker.util.App

class ThemeManagerImpl(context: Context) :
    ThemeManager {
    companion object {
        const val PLAYLIST_MAKER_PREFERENCES: String = "PLAYLIST_MAKER_PREFERENCES"
        const val APP_THEME_KEY = "APP_THEME"
    }

    private val sharedPreferences = context.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun saveData(data: Any) {
        sharedPreferences.edit()
            .putBoolean(App.APP_THEME_KEY, data as Boolean)
            .apply()
    }

    override fun getData(): Boolean {
        return sharedPreferences.getBoolean(APP_THEME_KEY, App.DARK_APP_THEME_DEFAULT)
    }
}
