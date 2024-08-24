package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl(url: String) : PlayerRepository {

    private val player = MediaPlayer()

    private var playerState = PlayerRepository.STATE_DEFAULT

    init {
        preparePlayer(url)
    }

    override fun startPlayer() {
        player.start()
        playerState = PlayerRepository.STATE_PLAYING
    }

    override fun pausePlayer() {
        player.pause()
        playerState = PlayerRepository.STATE_PAUSED
    }

    override fun releasePlayer() {
        player.release()
    }

    override fun providePlayerState(): Int {
        return playerState
    }

    override fun provideCurrentPosition(): Int {
       return player.currentPosition
    }

    private fun preparePlayer(url: String) {
        with(player) {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerRepository.STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = PlayerRepository.STATE_PREPARED
            }
        }
    }
}