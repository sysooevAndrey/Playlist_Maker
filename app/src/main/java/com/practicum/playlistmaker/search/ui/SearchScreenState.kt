package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed class SearchScreenState {

    object Wait : SearchScreenState()
    object Loading : SearchScreenState()
    data class History(val tracks: List<Track>) : SearchScreenState()
    data class Content(val tracks: List<Track>) : SearchScreenState()
    data class Error(val searchScreenStatus: SearchScreenStatus) : SearchScreenState()
}