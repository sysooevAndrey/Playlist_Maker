package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.ThemeRepository
import com.practicum.playlistmaker.domain.api.ThemeInteractor

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {

    override fun saveTheme(isDarkTheme: Boolean) {
        themeRepository.saveTheme(isDarkTheme)
    }

    override fun getTheme(consumer: ThemeInteractor.ThemeConsumer) {
        consumer.consume(themeRepository.getTheme())
    }
}