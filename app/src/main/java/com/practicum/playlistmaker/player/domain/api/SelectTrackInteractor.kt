package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SelectTrackInteractor {
    fun setSelected(track: Track)
    fun getSelected(): Track
}