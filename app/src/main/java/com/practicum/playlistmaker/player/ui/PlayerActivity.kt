package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            this.finish()
        }

        viewModel.getLoadingLiveData().observe(this) { screenState ->
            when (screenState) {
                is PlayerScreenState.Content -> {
                    changeContentVisibility(loading = false, isError = false)
                    renderFields(screenState.track)
                }

                is PlayerScreenState.Error -> {
                    changeContentVisibility(loading = false, isError = true)
                    renderFields(screenState.track)
                    Toast
                        .makeText(this, "Ошибка. Трек недоступен", Toast.LENGTH_LONG)
                        .show()
                }

                is PlayerScreenState.Loading -> {
                    changeContentVisibility(loading = true, isError = false)
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

    override fun onPause() {
        super.onPause()
        viewModel.forcedPausePlayer()
    }

    private fun changeContentVisibility(loading: Boolean, isError: Boolean) {
        binding.root.forEach { view -> view.isVisible = !loading }
        binding.progressCircular.isVisible = loading
        isReadyToPlay(isError)
    }

    private fun setPosterImage(track: Track) {
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

    private fun renderFields(track: Track) {
        with(track) {
            binding.trackNameValue.text = trackName
            binding.groupNameValue.text = artistName
            binding.collectionNameValue.text = collectionName
            binding.yearValue.text = getCorrectYearFormat()
            binding.genreValue.text = primaryGenreName
            binding.countryValue.text = country
            binding.trackTimeValue.text = getCorrectTimeFormat()
            setPosterImage(track)
        }
    }

    private fun isReadyToPlay(isError: Boolean) {
        with(binding.play) {
            if (isError) {
                isClickable = false
                setImageResource(R.drawable.play_empty_icon)
            } else {
                isClickable = true
                setImageResource(R.drawable.play_icon)
            }
        }
    }
}