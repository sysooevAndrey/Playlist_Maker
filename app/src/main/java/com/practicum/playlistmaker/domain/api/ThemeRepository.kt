package com.practicum.playlistmaker.domain.api

interface ThemeRepository {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme() : Boolean
}