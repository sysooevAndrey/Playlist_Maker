package com.practicum.playlistmaker.player.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.api.SelectTrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SelectedTrackRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SelectTrackRepository, KoinComponent {
    companion object {
        const val SELECTED_TRACK_KEY = "SELECTED_TRACK"
    }

    private val gson: Gson by inject()

    override fun setSelected(track: Track) {
        sharedPreferences
            .edit()
            .putString(SELECTED_TRACK_KEY, gson.toJson(track)).apply()
    }

    override fun getSelected(): Track {
        val json = sharedPreferences.getString(SELECTED_TRACK_KEY, null)
        return gson.fromJson(json, object : TypeToken<Track>() {}.type)
    }
}