package com.practicum.playlistmaker.domain.api

import Track

interface TrackRepository {
    fun searchTrack(expression: String) : List<Track>
}