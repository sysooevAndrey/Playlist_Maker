package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.data.SelectedTrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.SelectTrackRepository
import com.practicum.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.setting.data.SharingRepositoryImpl
import com.practicum.playlistmaker.setting.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.SharingRepository
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
    single<TrackRepository> { TrackRepositoryImpl(get()) }
    factory<PlayerRepository> { PlayerRepositoryImpl() }
    single<SharingRepository> { SharingRepositoryImpl(androidContext()) }
    single<ThemeRepository> { ThemeRepositoryImpl(androidContext()) }
    single<SelectTrackRepository>{ SelectedTrackRepositoryImpl(get()) }
}