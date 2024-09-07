package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) :
    HistoryInteractor {

    override fun saveHistory(trackList: List<Track>) {
        historyRepository.saveHistory(trackList)
    }

    override fun getHistory(consumer: HistoryInteractor.TrackHistoryConsumer) {
        val trackHistoryList = historyRepository.getHistory()
        consumer.consume(trackHistoryList)
    }
}