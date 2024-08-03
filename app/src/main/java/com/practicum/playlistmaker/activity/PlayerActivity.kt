package com.practicum.playlistmaker.activity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.button.NavigationButton
import com.practicum.playlistmaker.model.TrackResponse
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_KEY: String = "TRACK"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        const val DELAY_TIME_MILLIS = 500L
    }

    private lateinit var playButton: ImageView

    private lateinit var currentTime: TextView

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var track: TrackResponse.Track

    private lateinit var mainHandler: Handler

    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mainHandler = Handler(Looper.getMainLooper())

        currentTime = findViewById(R.id.current_time)

        track =
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

        playButton = findViewById(R.id.play)

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

        mediaPlayer = MediaPlayer()

        val mediaThread = Thread {
            Looper.prepare()
            Looper.loop()
        }.start()



        preparePlayer()

        playButton.setOnClickListener {

            mediaThread.run {
                val handler = Handler(Looper.myLooper()!!)
                handler.post {
                    playerControl()
                }
            }

        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainHandler.removeCallbacks(updateCurrentTimeRunnable())

    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_icon)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause_icon)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_icon)
        playerState = STATE_PAUSED
    }

    private fun playerControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                Handler(Looper.getMainLooper()).postDelayed(
                    updateCurrentTimeRunnable(), DELAY_TIME_MILLIS
                )
            }
        }
    }

    private fun updateCurrentTimeRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                currentTime.text = SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                Handler(Looper.getMainLooper()).postDelayed(this, DELAY_TIME_MILLIS)
            }
        }
    }

}