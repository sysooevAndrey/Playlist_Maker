package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.Converter
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) : HistoryRepository,
    KoinComponent {
    companion object {
        const val SEARCH_HISTORY_KEY: String = "SEARCH_HISTORY"
    }

    private val gson : Gson by inject()

    override fun saveHistory(trackList: List<Track>) {
        sharedPreferences.edit()
            .putString(
                SEARCH_HISTORY_KEY,
                gson.toJson(
                    trackList.map { Converter.convertModel(it) as TrackDto }
                )).apply()
    }

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return json?.let {
            gson.fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
        } ?: arrayListOf()
    }
}