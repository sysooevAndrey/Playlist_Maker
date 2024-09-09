package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.Creator

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trackInteractor = Creator.provideTrackInteractor()
                val historyInteractor =
                    Creator.provideTrackHistoryInteractor(this[APPLICATION_KEY] as App)
                SearchViewModel(trackInteractor, historyInteractor)
            }
        }

        private const val SEARCH_TEXT_DEF: String = ""
        private const val SEARCH_DEBOUNCE_DELAY: Long = 2000L
    }

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val requestRunnable = Runnable { createRequest() }

    private var searchText = SEARCH_TEXT_DEF

    private val searchScreenStateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.Wait)

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()

    private var searchTracksHistory = ArrayList<Track>()

    init {
        historyInteractor.getHistory(object : HistoryInteractor.TrackHistoryConsumer {
            override fun consume(trackHistoryList: List<Track>) {
                with(searchTracksHistory) {
                    clear()
                    addAll(trackHistoryList)
                }
            }
        })
        setHistory(searchTracksHistory)
    }

    override fun onCleared() {
        super.onCleared()
        historyInteractor.saveHistory(searchTracksHistory)
    }

    fun getHistoryLiveData(): LiveData<ArrayList<Track>> = historyLiveData
    fun getSearchScreenStateLiveData(): LiveData<SearchScreenState> = searchScreenStateLiveData

    fun addToHistory(track: Track) {
        with(searchTracksHistory) {
            if (contains(track)) {
                remove(track)
                add(0, track)
            } else if (size == 10) {
                removeLast()
                add(0, track)
            } else {
                add(0, track)
            }
        }
        setHistory(searchTracksHistory)
    }

    fun search(request: String, isDebounce: Boolean = false) {
        if (request.isNotEmpty()) {
            setState(SearchScreenState.Loading)
            this.searchText = request
            if (isDebounce) {
                searchDebounce()
            } else createRequest()
        } else {
            removeSearchCallback()
            setState(SearchScreenState.Wait)
        }
    }

    fun clearHistory() {
        searchTracksHistory.clear()
        setHistory(searchTracksHistory)
        setState(SearchScreenState.Wait)
    }

    private fun createRequest() {
        trackInteractor.searchTrack(searchText, object : TrackInteractor.TrackConsumer {
            override fun consume(resource: Resource<List<Track>>) {
                if (resource is Resource.Success) {
                    setState(SearchScreenState.Content(resource.data!!))
                } else {
                    if (resource.data != null)
                        setState(SearchScreenState.Error(SearchStatus.NOT_FOUND))
                    else setState(SearchScreenState.Error(SearchStatus.NO_NETWORK))
                }
            }
        })
    }

    private fun setState(state: SearchScreenState) = searchScreenStateLiveData.postValue(state)
    private fun setHistory(value: ArrayList<Track>) = historyLiveData.postValue(value)
    private fun searchDebounce() {
        mainThreadHandler.removeCallbacks(requestRunnable)
        mainThreadHandler.postDelayed(requestRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun removeSearchCallback() = mainThreadHandler.removeCallbacks(requestRunnable)
}