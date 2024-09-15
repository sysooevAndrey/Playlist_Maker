package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.SelectTrackInteractor

class PlayerViewModel(
    selectTrackInteractor: SelectTrackInteractor,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    private val playerScreenStateLiveData =
        MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    private val playerStatusLiveData = MutableLiveData<PlayerStatus>()

    fun getLoadingLiveData(): LiveData<PlayerScreenState> = playerScreenStateLiveData
    fun getPlayerStatusLiveData(): LiveData<PlayerStatus> = playerStatusLiveData

    init {
        val track = selectTrackInteractor.getSelected()
        playerInteractor.preparePlayer(
            track.previewUrl,
            onComplete = { playerScreenStateLiveData.postValue(PlayerScreenState.Content(track)) },
            onError = { playerScreenStateLiveData.postValue(PlayerScreenState.Error(track)) }
        )
        playerInteractor.onPlayListener(object : PlayerInteractor.InteractorObserver {
            override fun onPlay() {
                playerStatusLiveData.value = getCurrentPlayerStatus().copy(isPlaying = true)
                mainThreadHandler.postDelayed(
                    updateCurrentPositionRunnable(),
                    UPDATE_CURRENT_TIME_TOKEN, DELAY_TIME_MILLIS
                )
            }

            override fun onPause() {
                setPauseStatus()
            }


            override fun onComplete() {
                playerStatusLiveData.value =
                    getCurrentPlayerStatus().copy(isPlaying = false, progress = 0)
                removeUpdateCallBack()
            }
        })
    }

    fun play() = playerInteractor.play()

    fun forcedPausePlayer() {
        playerInteractor.pausePlayer()
        setPauseStatus()
    }

    private fun getCurrentPlayerStatus(): PlayerStatus {
        return playerStatusLiveData.value ?: PlayerStatus(isPlaying = false, progress = 0)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
        removeUpdateCallBack()
    }

    private fun updateCurrentPositionRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                playerStatusLiveData.value =
                    getCurrentPlayerStatus()
                        .copy(progress = playerInteractor.provideCurrentPosition())
                mainThreadHandler.postDelayed(
                    this,
                    UPDATE_CURRENT_TIME_TOKEN,
                    DELAY_TIME_MILLIS
                )
            }
        }
    }

    private fun removeUpdateCallBack() {
        mainThreadHandler.removeCallbacksAndMessages(UPDATE_CURRENT_TIME_TOKEN)
    }

    private fun setPauseStatus() {
        playerStatusLiveData.value = getCurrentPlayerStatus().copy(isPlaying = false)
        removeUpdateCallBack()
    }

    companion object {
        const val DELAY_TIME_MILLIS = 100L
        private val UPDATE_CURRENT_TIME_TOKEN = Any()
    }
}