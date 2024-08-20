package com.practicum.playlistmaker.domain.api

import Track

interface TrackInteractor {
    fun searchTrack(expression:String,consumer: TrackConsumer)

    interface TrackConsumer{
        fun consume (foundTrack:List<Track>)
    }
}