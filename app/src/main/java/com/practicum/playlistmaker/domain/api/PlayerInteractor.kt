package com.practicum.playlistmaker.domain.api

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun provideCurrentPosition() : Int
    fun controlPlayer(consumer: PlayerConsumer)
    interface PlayerConsumer {
        fun onPlay()
        fun onPause()
    }
}