package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.search.ui.TracksAdapter
import com.practicum.playlistmaker.setting.ui.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { TracksAdapter() }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { PlayerViewModel(get(), get()) }
    viewModel { SettingViewModel(get(), get()) }
}