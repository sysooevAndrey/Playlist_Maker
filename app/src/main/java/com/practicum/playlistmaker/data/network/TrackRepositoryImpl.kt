package com.practicum.playlistmaker.data.network

import Track
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.domain.api.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): List<Track> {
        val resp = networkClient.doRequest(TrackSearchRequest(expression))
        if (resp.resultCode == 200) {
            return (resp as TrackSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else return emptyList()
    }
}
