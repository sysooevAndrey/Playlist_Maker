package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun provideCurrentPosition(): Int {
       return playerRepository.provideCurrentPosition()
    }

    override fun controlPlayer(consumer: PlayerInteractor.PlayerConsumer) {
        when (playerRepository.providePlayerState()) {
            PlayerRepository.STATE_PLAYING -> {
                pausePlayer()
                consumer.onPlay()
            }
            PlayerRepository.STATE_PREPARED, PlayerRepository.STATE_PAUSED -> {
                startPlayer()
                consumer.onPause()
            }
        }
    }
}
