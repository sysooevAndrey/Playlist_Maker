package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun saveHistory(trackList: List<Track>)
    fun getHistory(consumer: TrackHistoryConsumer)
    interface TrackHistoryConsumer {
        fun consume(trackHistoryList: List<Track>)
    }
}