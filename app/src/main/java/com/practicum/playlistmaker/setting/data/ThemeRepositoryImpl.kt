package com.practicum.playlistmaker.setting.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository
import com.practicum.playlistmaker.util.MyApplication

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeRepository {
    companion object {
        const val APP_THEME_KEY = "APP_THEME"
    }

     override fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(APP_THEME_KEY, isDarkTheme)
            .apply()
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(
            APP_THEME_KEY,
            MyApplication.DARK_APP_THEME_DEFAULT
        )
    }
}