package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.util.Model

data class TrackDto(
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
) : Model