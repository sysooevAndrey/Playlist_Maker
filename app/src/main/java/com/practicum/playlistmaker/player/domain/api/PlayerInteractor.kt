package com.practicum.playlistmaker.player.domain.api

interface PlayerInteractor {
    fun preparePlayer(url: String, onComplete: () -> Unit, onError: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun provideCurrentPosition(): Int
    fun play()
    fun onPlayListener(observer: InteractorObserver)
    interface InteractorObserver {
        fun onPlay()
        fun onPause()
        fun onComplete()
    }
}