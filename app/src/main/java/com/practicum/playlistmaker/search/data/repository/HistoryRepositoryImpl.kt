package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.Converter
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : HistoryRepository {
    companion object {
        const val SEARCH_HISTORY_KEY: String = "SEARCH_HISTORY"
    }

    override fun saveHistory(tracks: List<Track>) {
        sharedPreferences.edit {
            putString(
                SEARCH_HISTORY_KEY,
                gson.toJson(tracks.map { Converter.convertModel(it) as TrackDto })
            )
        }
    }

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return json?.let {
            gson.fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
        } ?: arrayListOf()
    }
}