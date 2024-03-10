package com.practicum.playlistmaker.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.button.NavigationButton
import com.practicum.playlistmaker.model.TrackResponse

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_KEY: String = "TRACK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)


        val track: TrackResponse.Track =
            Gson().fromJson(intent.getStringExtra(TRACK_KEY), TrackResponse.Track::class.java)

        NavigationButton.back<ImageView>(this, R.id.back_button)

        val posterValue = findViewById<ImageView>(R.id.poster)

        val trackNameValue = findViewById<TextView>(R.id.track_name_value)

        val groupNameValue = findViewById<TextView>(R.id.group_name_value)

        val trackTimeValue = findViewById<TextView>(R.id.track_time_value)

        val collectionNameValue = findViewById<TextView>(R.id.collection_name_value)

        val yearValue = findViewById<TextView>(R.id.year_value)

        val genreValue = findViewById<TextView>(R.id.genre_value)

        val countryValue = findViewById<TextView>(R.id.country_value)

        with(track) {
            trackNameValue.text = trackName
            groupNameValue.text = artistName
            collectionNameValue.text = collectionName
            yearValue.text = getCorrectYearFormat()
            genreValue.text = primaryGenreName
            countryValue.text = country
            trackTimeValue.text = getCorrectTimeFormat()
            val displayMetrics = posterValue.resources.displayMetrics;
            val cornerRadius = ((8 * displayMetrics.density) + 0.5).toInt()
            Glide.with(posterValue)
                .load(track.getLargeArtworkUrl())
                .transform(RoundedCorners(cornerRadius))
                .into(posterValue)
        }
    }
}