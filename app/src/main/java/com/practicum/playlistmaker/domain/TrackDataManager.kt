package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.util.Manager

interface TrackDataManager : Manager {
    override fun saveData(data: Any)
    override fun getData(): ArrayList<TrackDto>
}