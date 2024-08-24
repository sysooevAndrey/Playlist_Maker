package com.practicum.playlistmaker.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.TrackDataManager

class TrackDataManagerImpl(context: Context) :
    TrackDataManager {
    companion object {
        const val PLAYLIST_MAKER_PREFERENCES: String = "PLAYLIST_MAKER_PREFERENCES"
        const val SEARCH_HISTORY_KEY: String = "SEARCH_HISTORY"
    }

    private val sharedPreferences =
        context.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )

    override fun saveData(data: Any) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(data))
            .apply()
    }

    override fun getData(): ArrayList<TrackDto> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return json?.let {
            Gson().fromJson(json, object : TypeToken<ArrayList<TrackDto>>() {}.type)
        } ?: arrayListOf()
    }
}

