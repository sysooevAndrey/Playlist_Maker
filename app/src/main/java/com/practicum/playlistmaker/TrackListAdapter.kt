package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class TrackListAdapter(private val trackList: ArrayList<TrackResponse.Track>) :
    RecyclerView.Adapter<TrackViewHolder>() {
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
        holder.bind(track)
        holder.itemView.setOnClickListener {
            with(SearchActivity.searchHistoryList) {
                if (contains(track)) {
                    remove(track)
                    add(0, track)
                } else if (size == 10) {
                    removeLast()
                    add(0, track)
                }
             else {
            add(0, track)
        }
            notifyDataSetChanged()
        }
    }
}
}