package com.practicum.playlistmaker.util

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track

object Converter {
    fun convertModel(model: Model): Model {
        return if (model is Track) {
            with(model) {
                TrackDto(
                    trackId,
                    trackName,
                    artistName,
                    trackTimeMillis,
                    artworkUrl100,
                    collectionName,
                    releaseDate,
                    primaryGenreName,
                    country,
                    previewUrl
                )
            }
        } else {
            with(model) {
                Track(
                    trackId,
                    trackName,
                    artistName,
                    trackTimeMillis,
                    artworkUrl100,
                    collectionName,
                    releaseDate,
                    primaryGenreName,
                    country,
                    previewUrl
                )
            }
        }
    }
}