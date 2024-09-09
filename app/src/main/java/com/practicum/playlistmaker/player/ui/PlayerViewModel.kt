package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Creator

class PlayerViewModel(
    track: Track,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    private val playerScreenStateLiveData =
        MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    private val playerStatusLiveData = MutableLiveData<PlayerStatus>()

    fun getLoadingLiveData(): LiveData<PlayerScreenState> = playerScreenStateLiveData
    fun getPlayerStatusLiveData(): LiveData<PlayerStatus> = playerStatusLiveData

    init {
        playerInteractor.preparePlayer(track.previewUrl, onComplete = {
            playerScreenStateLiveData.postValue(PlayerScreenState.Content(track))
        })
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

    fun forcedPausePlayer() {
        playerInteractor.pausePlayer()
        setPauseStatus()
    }

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playerInteractor = Creator.providePlayerInteractor()
                PlayerViewModel(track, playerInteractor)
            }
        }

        const val DELAY_TIME_MILLIS = 100L
        private val UPDATE_CURRENT_TIME_TOKEN = Any()
    }
}