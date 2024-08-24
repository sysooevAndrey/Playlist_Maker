package com.practicum.playlistmaker.domain.models

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.util.Model
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track(
    override val trackId: String,
    override val trackName: String,
    override val artistName: String,
    override val trackTimeMillis: String,
    override val artworkUrl100: String,
    override val collectionName: String,
    override val releaseDate: String,
    override val primaryGenreName: String,
    override val country: String,
    override val previewUrl: String
) : Model {
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