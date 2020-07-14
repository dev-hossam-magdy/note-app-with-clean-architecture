package com.example.NoteAppcleanArach.businees.data.util

import android.util.Log
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheConstants
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheErrors
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResult
import com.example.NoteAppcleanArach.businees.data.network.util.ApiResult
import com.example.NoteAppcleanArach.businees.data.network.util.NetworkConstants
import com.example.NoteAppcleanArach.businees.data.network.util.NetworkErrors
import com.example.NoteAppcleanArach.businees.data.network.util.NetworkErrors.NETWORK_ERROR_TIMEOUT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException


/**
 *
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 *
 */

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    val TAG = "safeApiCall"
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NetworkConstants.NETWORK_TIMEOUT){
                Log.e(TAG," no time out ")
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    Log.e(TAG," TimeoutCancellationException ${throwable.message}")
                    ApiResult.Error(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    Log.e(TAG," IOException ")

                    ApiResult.NetworkError
                }
                is HttpException -> {

                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    Log.e(TAG," HttpException: code:${code} message: ${errorResponse} ")

                    ApiResult.Error(
                        code = code,
                        errorMessages = errorResponse

                    )
                }
                else -> {
                    Log.e(TAG,"${NetworkErrors.NETWORK_ERROR_UNKNOWN} ${throwable.message}")
                    ApiResult.Error(
                        null,
                        NetworkErrors.NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    val TAG = "safeCacheCall"
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CacheConstants.CACHE_TIMEOUT){
                Log.e(TAG,"withTimeout")
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {

                is TimeoutCancellationException -> {
                    Log.e(TAG,"TimeoutCancellationException: ${throwable.message}")
                    CacheResult.Error(CacheErrors.CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    Log.e(TAG,"${CacheErrors.CACHE_ERROR_UNKNOWN}: ${throwable.message}")

                    CacheResult.Error(CacheErrors.CACHE_ERROR_UNKNOWN)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        GenericErrors.ERROR_UNKNOWN
    }
}



