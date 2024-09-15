package com.practicum.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor


class App : Application() {

    companion object {
        const val DARK_APP_THEME_DEFAULT = false
    }

    private var isDarkTheme = DARK_APP_THEME_DEFAULT
    override fun onCreate() {
        super.onCreate()
        val themeInteractor = Creator.provideThemeInteractor(this)
        themeInteractor.getTheme(object : ThemeInteractor.ThemeConsumer {
            override fun consume(isDarkTheme: Boolean) {
                this@App.isDarkTheme = isDarkTheme
            }
        })
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