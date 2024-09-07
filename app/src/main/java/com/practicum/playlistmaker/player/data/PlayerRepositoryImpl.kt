package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.api.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {
    companion object {
        private val CHECK_COMPLETE_TOKEN = Any()
    }

    private val player = MediaPlayer()

    private val t = Thread {
        Looper.prepare()
        Looper.loop()
    }

    init {
        t.start()
    }

    override fun preparePlayer(url: String, onComplete: () -> Unit) {
        with(player) {
            setOnPreparedListener{onComplete.invoke()}
            setDataSource(url)
            prepareAsync()
        }
    }

    override fun startPlayer() {
        t.run { player.start() }
    }

    override fun pausePlayer() {
        t.run {
            player.pause()
            Handler(Looper.myLooper()!!).removeCallbacksAndMessages(CHECK_COMPLETE_TOKEN)
        }
    }

    override fun releasePlayer() {
        t.run { player.release() }
    }

    override fun provideCurrentPosition(): Int {
        return t.run { player.currentPosition }
    }

    override fun onCompletionListener(onComplete: () -> Unit) {
        player.setOnCompletionListener {
            t.run { Handler(Looper.getMainLooper()).post { onComplete.invoke() } }
        }
    }
}

