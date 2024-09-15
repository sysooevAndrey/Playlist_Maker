package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.SelectTrackInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track


class SearchViewModel(
    private val selectTrackInteractor: SelectTrackInteractor,
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val requestRunnable = Runnable { createRequest() }

    private var searchText = SEARCH_TEXT_DEF

    private val searchScreenStateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.Wait)

    private val historyLiveData = MutableLiveData<List<Track>>()

    init {
        historyInteractor.getHistory(object : HistoryInteractor.TrackHistoryConsumer {
            override fun consume(trackHistoryList: List<Track>) {
                setHistory(trackHistoryList)
            }
        })
    }

    fun getHistoryLiveData(): LiveData<List<Track>> = historyLiveData
    fun getSearchScreenStateLiveData(): LiveData<SearchScreenState> = searchScreenStateLiveData

    fun addToHistory(currentHistory: List<Track>, track: Track) {
        val updateHistory = ArrayList(currentHistory)
        with(updateHistory) {
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
        setHistory(updateHistory)
    }

    fun saveHistory(currentHistory: List<Track>) = historyInteractor.saveHistory(currentHistory)

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
        setHistory(emptyList())
        setState(SearchScreenState.Wait)
    }

    fun selectTrack(track: Track) = selectTrackInteractor.setSelected(track)

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
    private fun setHistory(value: List<Track>) = historyLiveData.postValue(value)
    private fun searchDebounce() {
        mainThreadHandler.removeCallbacks(requestRunnable)
        mainThreadHandler.postDelayed(requestRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun removeSearchCallback() = mainThreadHandler.removeCallbacks(requestRunnable)

    companion object {
        private const val SEARCH_TEXT_DEF: String = ""
        private const val SEARCH_DEBOUNCE_DELAY: Long = 2000L
    }
}