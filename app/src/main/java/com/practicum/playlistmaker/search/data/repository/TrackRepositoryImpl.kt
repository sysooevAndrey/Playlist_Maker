package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.Converter
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Resource
import java.io.IOException
import java.lang.ClassCastException

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
                Resource.Error(emptyList())
            }
        } catch (e: IOException) {
            Resource.Error()
        } catch (e: ClassCastException) {
            Resource.Error()
        }
    }
}
