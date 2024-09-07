package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        const val TRACK_KEY: String = "TRACK"
    }

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            this.finish()
        }

        track = Gson().fromJson(intent.getStringExtra(TRACK_KEY), Track::class.java)

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track)
        )[PlayerViewModel::class.java]

        lifecycle.addObserver(viewModel)

        viewModel.getLoadingLiveData().observe(this) { screenState ->
            when (screenState) {
                is PlayerScreenState.Content -> {
                    changeContentVisibility(loading = false)
                    with(screenState.track) {
                        binding.trackNameValue.text = trackName
                        binding.groupNameValue.text = artistName
                        binding.collectionNameValue.text = collectionName
                        binding.yearValue.text = getCorrectYearFormat()
                        binding.genreValue.text = primaryGenreName
                        binding.countryValue.text = country
                        binding.trackTimeValue.text = getCorrectTimeFormat()
                        setPosterImage()
                    }
                }

                is PlayerScreenState.Loading -> {
                    changeContentVisibility(loading = true)
                }
            }
        }

        viewModel.getPlayerStatusLiveData().observe(this) { playerStatus ->
            changeButtonStyle(playerStatus)
            binding.currentTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerStatus.progress)
        }

        binding.play.setOnClickListener {
            viewModel.play()
        }
    }

    private fun changeContentVisibility(loading: Boolean) {
        binding.root.forEach { view -> view.isVisible = !loading }
        binding.progressCircular.isVisible = loading
    }

    private fun setPosterImage() {
        val displayMetrics = binding.poster.resources.displayMetrics
        val cornerRadius = ((8 * displayMetrics.density) + 0.5).toInt()
        Glide.with(binding.poster)
            .load(track.getLargeArtworkUrl())
            .transform(RoundedCorners(cornerRadius))
            .into(binding.poster)
    }

    private fun changeButtonStyle(playerStatus: PlayerStatus) {
        when (playerStatus.isPlaying) {
            true -> binding.play.setImageResource(R.drawable.pause_icon)
            false -> binding.play.setImageResource(R.drawable.play_icon)
        }
    }


}