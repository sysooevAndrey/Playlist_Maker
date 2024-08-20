package com.practicum.playlistmaker.data.dto

import Track


data class TrackSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
) : Response() {}
