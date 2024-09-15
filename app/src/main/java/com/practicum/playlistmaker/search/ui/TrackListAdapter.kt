package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.models.Track
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

class TrackListAdapter(private val trackList: ArrayList<Track>) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var onItemClickListener: ((track: Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = trackList.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        with(holder) {
            bind(track)
            itemView.setOnClickListener {
                onItemClickListener?.invoke(track)
            }
        }
    }
}

