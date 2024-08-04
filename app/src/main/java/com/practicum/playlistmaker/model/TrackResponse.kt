package com.practicum.playlistmaker.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TrackResponse(val results: List<Track>) {
    data class Track(
        val trackId: String,
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: String,
        val artworkUrl100: String,
        val collectionName: String,
        val releaseDate: String,
        val primaryGenreName: String,
        val country: String,
        val previewUrl: String
    ) {
        fun getLargeArtworkUrl(): String = artworkUrl100
            .replaceAfterLast('/', "512x512bb.jpg")

        fun getCorrectTimeFormat(): String = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(trackTimeMillis.toInt())

        fun getCorrectYearFormat(): String = SimpleDateFormat(
            "yyyy",
            Locale.getDefault()
        ).format(Date())
    }
}
