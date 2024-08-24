package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.models.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}