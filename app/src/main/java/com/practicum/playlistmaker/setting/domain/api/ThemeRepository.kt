package com.practicum.playlistmaker.setting.domain.api

interface ThemeRepository {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme() : Boolean
}