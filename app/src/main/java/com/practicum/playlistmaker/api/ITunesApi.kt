package com.practicum.playlistmaker.api


import com.practicum.playlistmaker.model.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String,
    ): Call<TrackResponse>

}