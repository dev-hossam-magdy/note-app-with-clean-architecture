package com.example.NoteAppcleanArach.businees.usecases.notelist

import android.util.Log
import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResponseHandler
import com.example.NoteAppcleanArach.businees.data.network.abstraction.NoteNetworkDataSource
import com.example.NoteAppcleanArach.businees.data.util.safeApiCall
import com.example.NoteAppcleanArach.businees.data.util.safeCacheCall
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.state.*
import com.example.NoteAppcleanArach.framework.presentaion.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestoreDeletedNote (
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {

    private val TAG = "RestoreDeletedNote"
    fun restoreDeletedNote (
        note:Note,
        stateEvent: StateEvent
    ):Flow<DataState<NoteListViewState>?> = flow {
        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.insertNote(note)
        }
        val response = object :CacheResponseHandler<NoteListViewState , Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<NoteListViewState> {
                Log.e(TAG,"handleSuccess: ")
                var msg = RESTORE_NOTE_FAILED
                var messageType:MessageType = MessageType.Error()
                var viewState:NoteListViewState? = null
                if (resultObj > 0){
                    Log.e(TAG,"handleSuccess : the note inserted")
                    msg = RESTORE_NOTE_SUCCESS
                    messageType = MessageType.Success()
                    viewState = NoteListViewState(
                        notePendingDelete = NoteListViewState.NotePendingDelete(
                            note = note
                        )
                    )
                }

                return DataState.data(
                    response = Response(
                        message = msg,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = messageType
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
        updateNetwork(response?.stateMessage?.response?.message ?:RESTORE_NOTE_FAILED , note)

    }

    private suspend fun updateNetwork(message:String, note: Note){
        if (message.equals(RESTORE_NOTE_SUCCESS))
        safeApiCall(IO){
            Log.e(TAG,"updateNetwork:")
            noteNetworkDataSource.insertOrUpdateNote(note)
        }
    }


    companion object{
        val RESTORE_NOTE_SUCCESS = "Successfully restored the deleted note."
        val RESTORE_NOTE_FAILED = "Failed to restore the deleted note."
    }
}