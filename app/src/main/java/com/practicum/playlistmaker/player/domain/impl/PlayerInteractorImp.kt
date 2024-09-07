package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PLAYING = 1
        private const val STATE_PAUSED = 2
    }

    private lateinit var observer: PlayerInteractor.InteractorObserver

    private var playerState = STATE_DEFAULT

    override fun preparePlayer(url: String, onComplete: () -> Unit) =
        playerRepository.preparePlayer(url, onComplete)

    override fun startPlayer() {
        playerState = STATE_PLAYING
        playerRepository.startPlayer()
        observer.onPlay()
    }

    override fun pausePlayer() {
        playerState = STATE_PAUSED
        playerRepository.pausePlayer()
        observer.onPause()
    }

    override fun releasePlayer() = playerRepository.releasePlayer()

    override fun provideCurrentPosition(): Int = playerRepository.provideCurrentPosition()

    override fun play() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PAUSED -> {
                startPlayer()
            }

            STATE_DEFAULT -> {
                startPlayer()
                playerRepository.onCompletionListener(onComplete = {
                    playerState = STATE_DEFAULT
                    observer.onComplete()
                }
                )
            }
        }
    }

    override fun onPlayListener(observer: PlayerInteractor.InteractorObserver) {
        this.observer = observer
    }
}

