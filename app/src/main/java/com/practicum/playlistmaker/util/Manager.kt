package com.practicum.playlistmaker.util

interface Manager {
    fun saveData(data: Any)
    fun getData(): Any
}