package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import java.lang.Exception

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    private val thread : Thread = Thread()

    init {
        thread.start()
    }

    override fun preparePlayer(url: String, onComplete: () -> Unit, onError: () -> Unit) {
        with(mediaPlayer) {
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
        thread.run { mediaPlayer.start() }
    }

    override fun pausePlayer() {
        thread.run { mediaPlayer.pause() }
    }

    override fun releasePlayer() {
        thread.run { mediaPlayer.release() }
    }

    override fun provideCurrentPosition(): Int {
        return thread.run { mediaPlayer.currentPosition }
    }

    override fun onCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            thread.run { Handler(Looper.getMainLooper()).post { onComplete.invoke() } }
        }
    }
}

