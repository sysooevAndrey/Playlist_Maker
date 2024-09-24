package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.SelectTrackInteractor
import com.practicum.playlistmaker.player.domain.api.SelectTrackRepository
import com.practicum.playlistmaker.search.domain.models.Track

class SelectedTrackInteractorImpl(private val selectTrackRepository: SelectTrackRepository) :
    SelectTrackInteractor {
    override fun setSelected(track: Track) = selectTrackRepository.setSelected(track)
    override fun getSelected(): Track = selectTrackRepository.getSelected()
}