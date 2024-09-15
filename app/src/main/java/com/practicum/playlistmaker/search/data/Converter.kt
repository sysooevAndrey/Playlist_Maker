package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.models.Track

object Converter {
    private const val EMPTY_FIELD = ""
    fun convertModel(model: Any): Any {
        return when (model) {
            is Track -> {
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
            }

            is TrackDto -> {
                with(model) {
                    Track(
                        trackId ?: EMPTY_FIELD,
                        trackName ?: EMPTY_FIELD,
                        artistName ?: EMPTY_FIELD,
                        trackTimeMillis ?: EMPTY_FIELD,
                        artworkUrl100 ?: EMPTY_FIELD,
                        collectionName ?: EMPTY_FIELD,
                        releaseDate ?: EMPTY_FIELD,
                        primaryGenreName ?: EMPTY_FIELD,
                        country ?: EMPTY_FIELD,
                        previewUrl ?: EMPTY_FIELD
                    )
                }
            }

            else -> {
                Any()
            }
        }
    }
}