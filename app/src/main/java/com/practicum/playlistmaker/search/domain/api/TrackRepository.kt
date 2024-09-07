package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}