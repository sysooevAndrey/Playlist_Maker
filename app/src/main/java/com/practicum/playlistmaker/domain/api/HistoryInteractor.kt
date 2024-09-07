package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun saveHistory(trackList: List<Track>)
    fun getHistory(consumer: TrackHistoryConsumer)
    interface TrackHistoryConsumer {
        fun consume(trackHistoryList:List<Track>)
    }
}