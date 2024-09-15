package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.Resource

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(resource: Resource<List<Track>>)
    }
}