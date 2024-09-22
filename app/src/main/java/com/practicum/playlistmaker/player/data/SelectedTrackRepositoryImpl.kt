package com.practicum.playlistmaker.player.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.api.SelectTrackRepository
import com.practicum.playlistmaker.search.domain.models.Track

class SelectedTrackRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) :
    SelectTrackRepository {
    private companion object {
        const val SELECTED_TRACK_KEY = "SELECTED_TRACK"
    }

    override fun setSelected(track: Track) {
        sharedPreferences.edit {
            putString(SELECTED_TRACK_KEY, gson.toJson(track))
        }
    }

    override fun getSelected(): Track {
        val json = sharedPreferences.getString(SELECTED_TRACK_KEY, null)
        return gson.fromJson(json, object : TypeToken<Track>() {}.type)
    }
}