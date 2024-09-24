package com.practicum.playlistmaker.search.ui

import androidx.recyclerview.widget.DiffUtil
import com.practicum.playlistmaker.search.domain.models.Track

object TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}