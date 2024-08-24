package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun saveHistory(trackList: List<Track>)
    fun getHistory(): List<Track>
}