package com.practicum.playlistmaker.domain.api

interface ThemeInteractor {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme(consumer: ThemeConsumer)
    interface ThemeConsumer {
        fun consume(isDarkTheme: Boolean)
    }
}