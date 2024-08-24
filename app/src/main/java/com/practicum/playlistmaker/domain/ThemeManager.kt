package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.util.Manager

interface ThemeManager : Manager {
    override fun saveData(data: Any)
    override fun getData(): Boolean
}