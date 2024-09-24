package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun saveHistory(tracks: List<Track>)
    fun getHistory(consumer: TrackHistoryConsumer)
    fun clearHistory()
    interface TrackHistoryConsumer {
        fun consume(tracksHistory: List<Track>)
    }
}