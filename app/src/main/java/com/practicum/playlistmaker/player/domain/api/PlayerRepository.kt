package com.practicum.playlistmaker.player.domain.api

interface PlayerRepository {

    fun preparePlayer(url: String, onComplete: () -> Unit, onError: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun provideCurrentPosition(): Int
    fun onCompletionListener(onComplete: () -> Unit)
}