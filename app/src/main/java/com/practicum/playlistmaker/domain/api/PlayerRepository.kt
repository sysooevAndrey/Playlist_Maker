package com.practicum.playlistmaker.domain.api

interface PlayerRepository {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun providePlayerState() : Int
    fun provideCurrentPosition() : Int
}