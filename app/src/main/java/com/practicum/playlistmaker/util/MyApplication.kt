package com.practicum.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactionModule
import com.practicum.playlistmaker.di.presentationModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class MyApplication : Application(),KoinComponent {

    companion object {
        const val DARK_APP_THEME_DEFAULT = false
    }

    private var isDarkTheme = DARK_APP_THEME_DEFAULT
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(dataModule, repositoryModule, interactionModule, presentationModule)
        }
        val themeInteractor : ThemeInteractor = getKoin().get()
        themeInteractor.getTheme(object : ThemeInteractor.ThemeConsumer {
            override fun consume(isDarkTheme: Boolean) {
                this@MyApplication.isDarkTheme = isDarkTheme
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