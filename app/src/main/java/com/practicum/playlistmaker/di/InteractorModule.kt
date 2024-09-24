package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.SelectTrackInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.impl.SelectedTrackInteractorImpl
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.setting.domain.api.SharingInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.impl.SharingInteractorImpl
import com.practicum.playlistmaker.setting.domain.impl.ThemeInteractorImpl
import org.koin.dsl.module

val interactionModule = module {
    single<HistoryInteractor> { HistoryInteractorImpl(get()) }
    single<TrackInteractor> { TrackInteractorImpl(get()) }
    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }
    single<SharingInteractor> { SharingInteractorImpl(get()) }
    single<ThemeInteractor> { ThemeInteractorImpl(get()) }
    single<SelectTrackInteractor>{SelectedTrackInteractorImpl(get())}
}
