package com.practicum.playlistmaker

import com.google.gson.Gson

class TrackResponse(val results: List<Track>) {
    data class Track(
        val trackId: String,
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: String,
        val artworkUrl100: String
    )
}
