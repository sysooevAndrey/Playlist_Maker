package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.data.Converter
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Resource
import java.io.IOException

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
) : TrackRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        return try {
            val resp = networkClient.doRequest(TrackSearchRequest(expression))
            val result = (resp as TrackSearchResponse).results
            if (result.isNotEmpty()) {
                Resource.Success(result.map { Converter.convertModel(it) as Track })
            } else {
                Resource.Success(emptyList())
            }
        } catch (e: IOException) {
            Resource.Error()
        }
    }
}
