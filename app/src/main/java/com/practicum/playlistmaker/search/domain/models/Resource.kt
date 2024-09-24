package com.practicum.playlistmaker.search.domain.models

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    sealed class Error<T>(data: T?) : Resource<T>(data) {
        class NoNetwork<T>(data: T? = null) : Error<T>(data)
        class NotFound<T>(data: T? = null) : Error<T>(data)
        class ClientError<T>(data: T? = null) : Error<T>(data)
    }
}