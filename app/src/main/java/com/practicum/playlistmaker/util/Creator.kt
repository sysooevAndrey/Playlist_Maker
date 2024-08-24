package com.practicum.playlistmaker.util

import android.content.Context
import com.practicum.playlistmaker.data.ThemeManagerImpl
import com.practicum.playlistmaker.data.TrackDataManagerImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.ThemeManager
import com.practicum.playlistmaker.domain.TrackDataManager
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun getTrackDataManager(context: Context): TrackDataManager {
        return TrackDataManagerImpl(context)
    }

    fun getThemeManager(context: Context): ThemeManager {
        return ThemeManagerImpl(context)
    }


}