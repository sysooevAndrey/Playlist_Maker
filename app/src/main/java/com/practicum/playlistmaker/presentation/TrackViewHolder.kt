package com.practicum.playlistmaker.presentation

import Track
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.dto.TrackSearchResponse

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name_value)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.getCorrectTimeFormat()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(convertDpToPx(2)))
            .into(trackImage)
    }

    private fun convertDpToPx(dp: Int): Int {
        val displayMetrics = itemView.resources.displayMetrics;
        return ((dp * displayMetrics.density) + 0.5).toInt()
    }
}