package com.example.NoteAppcleanArach.businees.usecases.notelist

import android.util.Log
import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResponseHandler
import com.example.NoteAppcleanArach.businees.data.network.abstraction.NoteNetworkDataSource
import com.example.NoteAppcleanArach.businees.data.util.safeApiCall
import com.example.NoteAppcleanArach.businees.data.util.safeCacheCall
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.model.createNote
import com.example.NoteAppcleanArach.businees.doman.state.*
import com.example.NoteAppcleanArach.framework.presentaion.notelist.state.NoteListViewState
import com.example.NoteAppcleanArach.framework.util.Constants
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertNewNote(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {

    private val TAG = "InsertNewNote"

    fun insertNote(
        id: String?,
        title: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {
        val newNote = createNote(
            id = id,
            title = title,
            body = ""
        )
        val cacheResult = safeCacheCall(IO) {
            noteCacheDataSource.insertNote(newNote)
        }
        val cacheResponse = object : CacheResponseHandler<NoteListViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<NoteListViewState> =
                if (resultObj > 0) {
                    Log.e(TAG,"handleSuccess : ${Constants.INSERT_NOTE_SUCCESS}")
                    val viewState = NoteListViewState(
                        newNote = newNote
                    )
                    DataState.data(
                        response = Response(
                            message = Constants.INSERT_NOTE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        stateEvent = stateEvent,
                        data = viewState
                    )
                } else {

                    DataState.data(
                        response = Response(
                            message = Constants.INSERT_NOTE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }


        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse.stateMessage?.response.message, newNote)

    }

    private suspend fun updateNetwork(message: String?, newNote: Note) {
        message?.let {
            if (it.equals(Constants.INSERT_NOTE_SUCCESS))
                safeApiCall(IO) {
                    Log.e(TAG,"updateNetwork: ")
                    noteNetworkDataSource.insertOrUpdateNote(note = newNote)
                }
        }
    }

}