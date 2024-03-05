package com.practicum.playlistmaker

class HistoryTrackListAdapter(private val trackList: ArrayList<TrackResponse.Track>) :
    TrackListAdapter(trackList) {

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}