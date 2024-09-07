package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed class PlayerScreenState {
    object Loading : PlayerScreenState()
    data class Content(
        val track: Track,
    ) : PlayerScreenState()
}
