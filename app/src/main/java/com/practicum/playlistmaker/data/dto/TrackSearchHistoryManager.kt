package com.practicum.playlistmaker.data.dto

import Track
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class TrackSearchHistoryManager(private val context: Context) {
    companion object {
        const val PLAYLIST_MAKER_PREFERENCES: String = "PLAYLIST_MAKER_PREFERENCES"
        const val SEARCH_HISTORY_KEY: String = "SEARCH_HISTORY"
    }

    private val sharedPreferences =
        context.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )

    fun saveTrackHistory(trackList: List<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(trackList))
            .apply()
    }

    fun getTrackHistory(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return json?.let {
            Gson().fromJson(json, object : TypeToken<ArrayList<TrackDto>>() {}.type)
        } ?: arrayListOf<TrackDto>().map { Track(
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
    }
}

