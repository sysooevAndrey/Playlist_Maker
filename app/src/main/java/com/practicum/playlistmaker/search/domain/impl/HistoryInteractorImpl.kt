package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) :
    HistoryInteractor {

    override fun saveHistory(tracks: List<Track>) {
        historyRepository.saveHistory(tracks)
    }

    override fun getHistory(consumer: HistoryInteractor.TrackHistoryConsumer) {
        val tracksHistory = historyRepository.getHistory()
        consumer.consume(tracksHistory)
    }

    override fun clearHistory() {
        historyRepository.saveHistory(emptyList())
    }
}