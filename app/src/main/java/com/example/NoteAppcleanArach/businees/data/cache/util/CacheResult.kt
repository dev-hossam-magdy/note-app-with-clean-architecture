package com.example.NoteAppcleanArach.businees.data.cache.util

sealed class CacheResult <out T>{
    data class Success<out T>(val value :T):CacheResult<T>()

    data class Error(val errorMessages: String?):CacheResult<Nothing>()

}
