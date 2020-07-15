package com.example.NoteAppcleanArach.businees.usecases.common

import android.util.Log
import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResponseHandler
import com.example.NoteAppcleanArach.businees.data.network.abstraction.NoteNetworkDataSource
import com.example.NoteAppcleanArach.businees.data.util.safeApiCall
import com.example.NoteAppcleanArach.businees.data.util.safeCacheCall
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNote<ViewState>(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {

    private val TAG = "DeleteNote"


    fun deleteNote(
        note:Note,
        stateEvent: StateEvent
    ):Flow<DataState<ViewState>?> = flow {

        Log.e(TAG,"deleteNote")
        val cacheResponse = safeCacheCall(IO){
            noteCacheDataSource.deleteNote(note.id)
        }
        val response =object :CacheResponseHandler<ViewState , Int>(
            response =cacheResponse,
            stateEvent =  stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState> {
                var uiComponentType:UIComponentType = UIComponentType.None()
                var msg = DELETE_NOTE_SUCCESS
                var messageType:MessageType = MessageType.Success()
                if (resultObj < 0){
                    Log.e(TAG,"deleteNote: DELETE_NOTE_FAIL")

                    uiComponentType = UIComponentType.Toast()
                    msg = DELETE_NOTE_FAIL
                    messageType = MessageType.Error()
                }
                return DataState.data(
                    response = Response(
                        uiComponentType = uiComponentType,
                        message = msg,
                        messageType = messageType
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
        updateNetwork(
            message = response?.stateMessage?.response?.message ?: DELETE_NOTE_FAIL,
            note = note
        )
    }

    private suspend fun updateNetwork(message: String , note: Note){
        Log.e(TAG,"updateNetwork")
        if (message.equals(DELETE_NOTE_SUCCESS)){
            safeApiCall(IO){
                noteNetworkDataSource.deleteNote(note.id)
            }
            safeApiCall(IO){
                noteNetworkDataSource.insertDeletedNote(note)
            }
        }
    }


    companion object{
        const val DELETE_NOTE_SUCCESS ="DELETE_NOTE_SUCCESS"
        const val DELETE_NOTE_FAIL ="DELETE_NOTE_FAIL"
    }


}