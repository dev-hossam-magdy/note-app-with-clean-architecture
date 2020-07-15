package com.example.NoteAppcleanArach.businees.usecases.notedetails

import android.util.Log
import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResponseHandler
import com.example.NoteAppcleanArach.businees.data.network.abstraction.NoteNetworkDataSource
import com.example.NoteAppcleanArach.businees.data.util.safeApiCall
import com.example.NoteAppcleanArach.businees.data.util.safeCacheCall
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.state.*
import com.example.NoteAppcleanArach.framework.presentaion.notedetails.state.NoteDetailsViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNote(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {
    private val TAG = "UpdateNote"


    fun updateNote(
        stateEvent: StateEvent,
        note: Note
    ): Flow<DataState<NoteDetailsViewState>?> = flow {
        val cacheResult = safeCacheCall(IO) {
            Log.e(TAG, "safeCacheCall")

            noteCacheDataSource.updateNote(
                primaryKey = note.id,
                newTitle = note.title,
                newBody = note.body
            )
        }
        val response = object : CacheResponseHandler<NoteDetailsViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<NoteDetailsViewState> =
                if (resultObj > 0)
                    DataState.data(
                        response = Response(
                            messageType = MessageType.Success(),
                            message = UPDATE_NOTE_SUCCESS,
                            uiComponentType = UIComponentType.Toast()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                else
                    DataState.data(
                        response = Response(
                            messageType = MessageType.Error(),
                            message = UPDATE_NOTE_FAILED,
                            uiComponentType = UIComponentType.Toast()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
        }.getResult()

        emit(response)
        updateNetwork(response?.stateMessage?.response?.message ?: UPDATE_NOTE_FAILED, note)
    }

    private suspend fun updateNetwork(message: String, note: Note) {
        if (message.equals(UPDATE_NOTE_SUCCESS))
            safeApiCall(IO) {
                Log.e(TAG, "updateNetwork")
                noteNetworkDataSource.insertOrUpdateNote(note)
            }
    }

    companion object {
        val UPDATE_NOTE_SUCCESS = "Successfully updated note."
        val UPDATE_NOTE_FAILED = "Failed to update note."
        val UPDATE_NOTE_FAILED_PK = "Update failed. Note is missing primary key."

    }

}