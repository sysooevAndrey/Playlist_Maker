package com.practicum.playlistmaker.domain.api

interface TrackPlayer {
    var playerState: Int
    fun startPlayer(runnable: Runnable)
    fun pausePlayer(runnable: Runnable)
    fun preparePlayer(prepRunnable: Runnable, completionRunnable: Runnable )
   }