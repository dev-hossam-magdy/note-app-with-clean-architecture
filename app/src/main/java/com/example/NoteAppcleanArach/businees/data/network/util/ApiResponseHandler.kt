package com.example.NoteAppcleanArach.businees.data.network.util

import com.example.NoteAppcleanArach.businees.doman.state.*


abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
) {

    suspend fun getResult(): DataState<ViewState>? {

        return when (response) {

            is ApiResult.Error ->

                createDataStateError(
                    stateEvent =stateEvent,
                    msg = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessages.toString()}"
                )


            is ApiResult.NetworkError ->

                createDataStateError(
                    stateEvent =stateEvent,
                    msg ="${stateEvent?.errorInfo()}\n\nReason: ${NetworkErrors.NETWORK_ERROR}"
                )


            is ApiResult.Success ->

                if (response.value == null)

                    createDataStateError(
                        stateEvent =stateEvent,
                        msg = "${stateEvent?.errorInfo()}\n\nReason: ${NetworkErrors.NETWORK_DATA_NULL}."
                    )
                else
                    handleSuccess(resultObj = response.value)


        }
    }

    private suspend fun createDataStateError(stateEvent: StateEvent?, msg: String): DataState<ViewState>? =
        DataState.error(
            response = Response(
                messageType = MessageType.Error(),
                uiComponentType = UIComponentType.Dialog(),
                message = msg
            ),
            stateEvent = stateEvent
        )

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

}