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
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private var searchText = SEARCH_TEXT_DEF

    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    private val t = Thread {
        Looper.prepare()
        Looper.loop()
    }

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
        historyLiveData.value = searchTracksHistory
        t.start()
    }

    fun getHistoryLiveData(): LiveData<ArrayList<Track>> = historyLiveData
    fun getSearchScreenStateLiveData(): LiveData<SearchScreenState> = searchScreenStateLiveData

    override fun onCleared() {
        super.onCleared()
        historyInteractor.saveHistory(searchTracksHistory)
    }

    private fun searchDebounce() {
        t.run {
            val handler = Handler(Looper.myLooper()!!)
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
            handler.postDelayed({ createRequest() }, SEARCH_REQUEST_TOKEN, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun createRequest() {
        searchScreenStateLiveData.value = SearchScreenState.Loading
        trackInteractor.searchTrack(searchText, object : TrackInteractor.TrackConsumer {
            override fun consume(resource: Resource<List<Track>>) {
                mainThreadHandler.post {
                    if (resource is Resource.Success) {
                        searchScreenStateLiveData.value = SearchScreenState.Content(resource.data!!)
                    } else {
                        if (resource.data != null) searchScreenStateLiveData.value =
                            SearchScreenState.Error(SearchStatus.NOT_FOUND)
                        else searchScreenStateLiveData.value =
                            SearchScreenState.Error(SearchStatus.NO_NETWORK)
                    }
                }
            }
        })
    }

    fun search(request: String, isDebounce: Boolean = false) {
        this.searchText = request
        if (isDebounce) {
            searchDebounce()
        } else createRequest()
    }

    fun removeRequest() {
        t.run {
            val handler = Handler(Looper.myLooper()!!)
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        }
    }

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
        historyLiveData.value = searchTracksHistory
    }

    fun clearHistory() {
        searchTracksHistory.clear()
        historyLiveData.value = searchTracksHistory
        searchScreenStateLiveData.value = SearchScreenState.Wait
    }
}