package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.util.button.NavigationButton
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.util.Creator
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        const val TRACK_KEY: String = "TRACK"
        const val DELAY_TIME_MILLIS = 100L
    }

    private lateinit var playButton: ImageView
    private lateinit var currentTime: TextView
    private lateinit var track: Track
    private lateinit var mainHandler: Handler
    private val updateCurrentTimeRunnable = updateCurrentTime()
    private lateinit var playerInteractor: PlayerInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        mainHandler = Handler(Looper.getMainLooper())
        currentTime = findViewById(R.id.current_time)
        track =
            Gson().fromJson(intent.getStringExtra(TRACK_KEY), Track::class.java)
        playerInteractor = Creator.providePlayerInteractor(track.previewUrl)
        NavigationButton.back<ImageView>(this, R.id.back_button)
        val posterValue = findViewById<ImageView>(R.id.poster)
        val trackNameValue = findViewById<TextView>(R.id.track_name_value)
        val groupNameValue = findViewById<TextView>(R.id.group_name_value)
        val trackTimeValue = findViewById<TextView>(R.id.track_time_value)
        val collectionNameValue = findViewById<TextView>(R.id.collection_name_value)
        val yearValue = findViewById<TextView>(R.id.year_value)
        val genreValue = findViewById<TextView>(R.id.genre_value)
        val countryValue = findViewById<TextView>(R.id.country_value)
        playButton = findViewById(R.id.play)
        with(track) {
            trackNameValue.text = trackName
            groupNameValue.text = artistName
            collectionNameValue.text = collectionName
            yearValue.text = getCorrectYearFormat()
            genreValue.text = primaryGenreName
            countryValue.text = country
            trackTimeValue.text = getCorrectTimeFormat()
            val displayMetrics = posterValue.resources.displayMetrics
            val cornerRadius = ((8 * displayMetrics.density) + 0.5).toInt()
            Glide.with(posterValue)
                .load(track.getLargeArtworkUrl())
                .transform(RoundedCorners(cornerRadius))
                .into(posterValue)
        }
        val mediaThread = Thread {
            Looper.prepare()
            Looper.loop()
        }.start()
        preparePlayerView()
        playButton.setOnClickListener {
            mediaThread.run {
                val handler = Handler(Looper.myLooper()!!)
                handler.post {
                    playerInteractor.controlPlayer(object : PlayerInteractor.PlayerConsumer {
                        override fun onPlay() {
                            mainHandler.removeCallbacks(updateCurrentTimeRunnable)
                            playButton.setImageResource(R.drawable.play_icon)
                        }

                        override fun onPause() {
                            mainHandler.postDelayed(
                                updateCurrentTimeRunnable, DELAY_TIME_MILLIS
                            )
                            playButton.setImageResource(R.drawable.pause_icon)
                        }
                    })
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pausePlayer()
        playButton.setImageResource(R.drawable.play_icon)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(updateCurrentTimeRunnable)
        playerInteractor.releasePlayer()
    }

    private fun preparePlayerView() {
        playButton.isEnabled = true
        mainHandler.removeCallbacks(updateCurrentTimeRunnable)
        playButton.setImageResource(R.drawable.play_icon)
        currentTime.text = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(0L)
    }

    private fun updateCurrentTime(): Runnable {
        return object : Runnable {
            override fun run() {
                currentTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(playerInteractor.provideCurrentPosition())
                mainHandler.postDelayed(this, DELAY_TIME_MILLIS)
            }
        }
    }
}