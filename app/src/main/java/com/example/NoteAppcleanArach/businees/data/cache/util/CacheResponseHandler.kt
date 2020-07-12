package com.example.NoteAppcleanArach.businees.data.cache.util

import com.example.NoteAppcleanArach.businees.doman.state.*


abstract class CacheResponseHandler<ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
) {
    suspend fun getResult(): DataState<ViewState>? =
        when (response) {

            is CacheResult.Error -> createDataStateError(stateEvent, response.errorMessages ?: "")

            is CacheResult.Success ->
                if (response.value == null)
                    createDataStateError(stateEvent, CacheErrors.CACHE_DATA_NULL)
                else
                    handleSuccess(resultObj = response.value)
        }

    private suspend fun createDataStateError(
        stateEvent: StateEvent?,
        msg: String
    ): DataState<ViewState>? =
        DataState.error(
            response = Response(
                message = "${stateEvent?.errorInfo()}\n\nReason: ${msg}.",
                uiComponentType = UIComponentType.Dialog(),
                messageType = MessageType.Error()
            ),
            stateEvent = stateEvent
        )


    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

}