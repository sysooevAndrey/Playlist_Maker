package com.practicum.playlistmaker.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.Converter
import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(context: Context) : HistoryRepository {
    companion object {
        const val PLAYLIST_MAKER_PREFERENCES: String = "PLAYLIST_MAKER_PREFERENCES"
        const val SEARCH_HISTORY_KEY: String = "SEARCH_HISTORY"
    }

    private val sharedPreferences =
        context.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )

    override fun saveHistory(trackList: List<Track>) {
        sharedPreferences.edit()
            .putString(
                SEARCH_HISTORY_KEY,
                Gson().toJson(
                    trackList.map { Converter.convertModel(it) as TrackDto }
                )).apply()
    }

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return json?.let {
            Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
        } ?: arrayListOf()
    }
}