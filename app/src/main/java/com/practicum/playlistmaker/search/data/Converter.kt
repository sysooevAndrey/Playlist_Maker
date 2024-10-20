package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.models.Track

object Converter {
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
                        trackId.orEmpty(),
                        trackName.orEmpty(),
                        artistName.orEmpty(),
                        trackTimeMillis.orEmpty(),
                        artworkUrl100.orEmpty(),
                        collectionName.orEmpty(),
                        releaseDate.orEmpty(),
                        primaryGenreName.orEmpty(),
                        country.orEmpty(),
                        previewUrl.orEmpty()
                    )
                }
            }

            else -> {
                Any()
            }
        }
    }
}