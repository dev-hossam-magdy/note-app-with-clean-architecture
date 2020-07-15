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

class DeleteMutableNotes(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {
    private val TAG = "DeleteMutableNotes"

    var onDeletedError = false

    fun deleteMutableNotes(
        list: List<Note>,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {
        val successDeletedNotes: ArrayList<Note> = ArrayList()
        Log.e(TAG,"deleteMutableNotes")
        list.forEach { note ->
            val cacheResult = safeCacheCall(IO) {
                Log.e(TAG,"deleteMutableNotes: safeCacheCall")
                noteCacheDataSource.deleteNote(note.id)
            }
            val response = object : CacheResponseHandler<NoteListViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent

            ) {
                override suspend fun handleSuccess(resultObj: Int): DataState<NoteListViewState> {
                    if (resultObj > 0)
                        successDeletedNotes.add(note)
                    else
                        onDeletedError = true
                    Log.e(TAG,"deleteMutableNotes: safeCacheCall: onDeletedError :$onDeletedError ")


                    return DataState()
                }
            }.getResult()

            if (response?.stateMessage?.response?.message?.contains(stateEvent.errorInfo()) == true)
                onDeletedError = true

        }
        var msg = DELETE_NOTES_SUCCESS
        var uiComponentType: UIComponentType = UIComponentType.None()
        if (onDeletedError) {
            msg = DELETE_NOTES_ERRORS
            uiComponentType = UIComponentType.Dialog()

        }


        emit(
            DataState.data<NoteListViewState>(
                response = Response(
                    messageType = MessageType.None(),
                    uiComponentType = uiComponentType,
                    message = msg
                ),
                data = null,
                stateEvent = stateEvent
            )
        )

        updateNetWork(successDeletedNotes)

    }

    private suspend fun updateNetWork(list: List<Note>) {
        Log.e(TAG,"updateNetWork")
        list.forEach { note ->
            safeApiCall(IO) {
                noteNetworkDataSource.deleteNote(note.id)
            }
            safeApiCall(IO) {
                noteNetworkDataSource.insertDeletedNote(note)
            }
        }
    }

    companion object {
        val DELETE_NOTES_SUCCESS = "Successfully deleted notes."
        val DELETE_NOTES_ERRORS =
            "Not all the notes you selected were deleted. There was some errors."
        val DELETE_NOTES_YOU_MUST_SELECT = "You haven't selected any notes to delete."
        val DELETE_NOTES_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }
}