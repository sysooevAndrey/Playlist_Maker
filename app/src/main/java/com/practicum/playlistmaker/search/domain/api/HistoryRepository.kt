package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryRepository {
    fun saveHistory(trackList: List<Track>)
    fun getHistory(): List<Track>
}