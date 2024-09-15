package com.practicum.playlistmaker.util

import android.content.Context
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository
import com.practicum.playlistmaker.setting.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.setting.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.setting.data.SharingRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.SharingInteractor
import com.practicum.playlistmaker.setting.domain.api.SharingRepository
import com.practicum.playlistmaker.setting.domain.impl.SharingInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getTrackHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context)
    }

    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getShareRepository(context: Context) : SharingRepository{
        return SharingRepositoryImpl(context)
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideTrackHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getTrackHistoryRepository(context))
    }

    fun provideThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideShareInteractor(context: Context): SharingInteractor{
        return SharingInteractorImpl(getShareRepository(context))
    }
}