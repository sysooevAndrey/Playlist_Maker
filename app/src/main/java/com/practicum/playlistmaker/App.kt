package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson

class App : Application() {

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "PLAYLIST_MAKER_PREFERENCES"
        const val APP_THEME_KEY = "APP_THEME"
        const val DARK_APP_THEME_DEFAULT = false
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
    }

    private var darkTheme = DARK_APP_THEME_DEFAULT

    override fun onCreate() {
        val sharedPrefs =
            getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(APP_THEME_KEY, DARK_APP_THEME_DEFAULT)

        val searchHistory = sharedPrefs.getString(SEARCH_HISTORY_KEY,null)

        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun getSearchHistoryList(searchHistory: ArrayList<TrackResponse.Track>){

    }
    fun createJsonFromTrack(track: TrackResponse.Track): String {
        return Gson().toJson(track)
    }

    fun createTrackFromJson(json: String): TrackResponse.Track {
        return Gson().fromJson(json, TrackResponse.Track::class.java)
    }
}