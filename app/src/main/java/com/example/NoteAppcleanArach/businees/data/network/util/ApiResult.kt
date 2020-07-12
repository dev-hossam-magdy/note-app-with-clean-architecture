package com.example.NoteAppcleanArach.businees.data.network.util

import com.google.android.gms.common.internal.ConnectionErrorMessages

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class Error(val code: Int? = null, val errorMessages: String? = null):ApiResult<Nothing>()

    object NetworkError:ApiResult<Nothing>()
}