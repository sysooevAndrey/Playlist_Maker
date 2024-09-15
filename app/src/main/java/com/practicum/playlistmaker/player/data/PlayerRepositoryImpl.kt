package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import java.lang.Exception
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerRepositoryImpl : PlayerRepository, KoinComponent {

    private val player: MediaPlayer by inject()

    private val thread : Thread by inject()

    init {
        thread.start()
    }

    override fun preparePlayer(url: String, onComplete: () -> Unit, onError: () -> Unit) {
        with(player) {
            setOnPreparedListener { onComplete.invoke() }
            try {
                setDataSource(url)
                prepareAsync()
            } catch (e: Exception) {
                onError.invoke()
            }
        }
    }

    override fun startPlayer() {
        thread.run { player.start() }
    }

    override fun pausePlayer() {
        thread.run { player.pause() }
    }

    override fun releasePlayer() {
        thread.run { player.release() }
    }

    override fun provideCurrentPosition(): Int {
        return thread.run { player.currentPosition }
    }

    override fun onCompletionListener(onComplete: () -> Unit) {
        player.setOnCompletionListener {
            thread.run { Handler(Looper.getMainLooper()).post { onComplete.invoke() } }
        }
    }
}

