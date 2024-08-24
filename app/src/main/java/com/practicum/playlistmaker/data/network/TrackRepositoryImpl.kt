package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.util.Resource

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
) : TrackRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val resp = networkClient.doRequest(TrackSearchRequest(expression))
        val result = (resp as TrackSearchResponse).results
        if (resp.resultCode == 200) {
            if (result.isNotEmpty()) {
                val trackList = result.map { Track(
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
                ) }
                return Resource.Success(trackList)
            } else {
                return Resource.Success(emptyList())
            }
        } else {
            return Resource.Error("Ошибка подключения")
        }
    }
}
