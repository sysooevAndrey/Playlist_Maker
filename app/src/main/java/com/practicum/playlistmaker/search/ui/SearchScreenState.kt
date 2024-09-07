package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed class SearchScreenState {
    object Wait : SearchScreenState()
    object Loading : SearchScreenState()
    data class Content(val tracks: List<Track>) : SearchScreenState()
    data class Error(val searchStatus: SearchStatus) : SearchScreenState()
}