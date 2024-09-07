package com.practicum.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_TEXT_DEF: String = ""
        val tracks = ArrayList<Track>()
        val tracksHistory = ArrayList<Track>()
    }

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: ActivitySearchBinding

    private var searchText: String = SEARCH_TEXT_DEF
    private val searchAdapter = TrackListAdapter(tracks)
    private val historyAdapter = TrackListAdapter(tracksHistory)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            this.finish()
        }

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.getHistoryLiveData().observe(this) { tracks ->
            with(tracksHistory) {
                clear()
                addAll(tracks)
            }
        }

        viewModel.getSearchScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {

                is SearchScreenState.Wait -> {
                    searchItemVisibility(SearchStatus.WAIT)
                }

                is SearchScreenState.Loading -> {
                    searchItemVisibility(SearchStatus.LOADING)
                }

                is SearchScreenState.Content -> {
                    tracks.clear()
                    tracks.addAll(screenState.tracks)
                    searchAdapter.notifyDataSetChanged()
                    searchItemVisibility(SearchStatus.SUCCESS)
                }

                is SearchScreenState.Error -> {
                    searchItemVisibility(
                        screenState.searchStatus
                    )
                }
            }
        }

        binding.searchEditText.setText(searchText)
        binding.updateButton.setOnClickListener {
            viewModel.search(searchText)
        }
        binding.clearText.setOnClickListener {
            binding.searchEditText.setText(SEARCH_TEXT_DEF)
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager
                ?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }

        val searchEditTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                binding.clearText.isVisible = clearButtonVisibility(s)
                searchText = s.toString()
                historyItemVisibility(binding.searchEditText.hasFocus())
                if (s?.isNotEmpty() == true) viewModel.search(searchText, isDebounce = true)
                else viewModel.removeRequest()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.searchEditText.addTextChangedListener(searchEditTextWatcher)
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search(searchText)
            }
            false
        }
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            historyItemVisibility(hasFocus)
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            historyItemVisibility(binding.searchEditText.hasFocus())
        }
        searchAdapter.onItemClickListener = {
            viewModel.addToHistory(it)
            openTrackActivity(it)
        }
        historyAdapter.onItemClickListener = {
            openTrackActivity(it)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun openTrackActivity(track: Track) {
        val displayIntent = Intent(this, PlayerActivity::class.java)
        displayIntent.putExtra(
            PlayerActivity.TRACK_KEY,
            Gson().toJson(track).toString()
        )
        this.startActivity(displayIntent)
    }

    private fun searchItemVisibility(searchStatus: SearchStatus) {
        when (searchStatus) {
            SearchStatus.WAIT -> {
                binding.progressCircular.isVisible = false
                binding.tracksRecyclerView.isVisible = true
                binding.placeholderView.isVisible = false
                binding.updateButton.isVisible = false

            }

            SearchStatus.LOADING -> {
                binding.progressCircular.isVisible = true
                binding.tracksRecyclerView.isVisible = false
                binding.placeholderView.isVisible = false
                binding.updateButton.isVisible = false
            }

            SearchStatus.SUCCESS -> {
                binding.progressCircular.isVisible = false
                binding.tracksRecyclerView.isVisible = true
                binding.placeholderView.isVisible = false
                binding.updateButton.isVisible = false
            }

            SearchStatus.NOT_FOUND -> {
                binding.progressCircular.isVisible = false
                binding.tracksRecyclerView.isVisible = false
                binding.placeholderView.isVisible = true
                binding.updateButton.isVisible = false
                binding.placeholderImage.setImageResource(R.drawable.placeholder_empty)
                binding.placeholderText.setText(R.string.empty_text)
            }

            SearchStatus.NO_NETWORK -> {
                binding.progressCircular.isVisible = false
                binding.tracksRecyclerView.isVisible = false
                binding.placeholderView.isVisible = true
                binding.updateButton.isVisible = true
                binding.placeholderImage.setImageResource(R.drawable.placeholder_network)
                binding.placeholderText.setText(R.string.network_error_text)
            }
        }
    }
    private fun historyItemVisibility(hasFocus: Boolean) {
        if (hasFocus && binding.searchEditText.text.isEmpty() && tracksHistory.isNotEmpty()) {
            binding.tracksRecyclerView.adapter = historyAdapter
            binding.clearHistoryButton.isVisible = true
            binding.searchHistoryHeader.isVisible = true
        } else {
            binding.tracksRecyclerView.adapter = searchAdapter
            binding.clearHistoryButton.isVisible = false
            binding.searchHistoryHeader.isVisible = false
        }
    }
}