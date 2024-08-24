package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.TrackPlayer

class TrackPlayerImpl(private val previewUrl: String) :
    MediaPlayer(), TrackPlayer {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    override var playerState = STATE_DEFAULT
    override fun preparePlayer(prepRunnable: Runnable, completionRunnable: Runnable) {
        setDataSource(previewUrl)
        prepareAsync()
        setOnPreparedListener {
            prepRunnable.run()
            playerState = STATE_PREPARED
        }
        setOnCompletionListener {
            completionRunnable.run()
            playerState = STATE_PREPARED
        }
    }

    override fun startPlayer(runnable: Runnable) {
        start()
        runnable.run()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer(runnable: Runnable) {
        pause()
        runnable.run()
        playerState = STATE_PAUSED
    }
}