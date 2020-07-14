package com.example.NoteAppcleanArach.businees.usecases.notelist

import android.util.Log
import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResponseHandler
import com.example.NoteAppcleanArach.businees.data.util.safeCacheCall
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.state.*
import com.example.NoteAppcleanArach.framework.presentaion.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.Query

class SearchNote(
    private val noteCacheDataSource: NoteCacheDataSource
) {
    private val TAG = "SearchNote"


    fun searchNote(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val pageNum = if (page <= 0) 0 else page
        val cacheResult = safeCacheCall(IO) {
            noteCacheDataSource.searchNotes(
                query = query,
                filterOrOrder = filterAndOrder,
                page = pageNum
            )
        }

        val response = object : CacheResponseHandler<NoteListViewState, List<Note>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Note>): DataState<NoteListViewState> {
                Log.e(TAG,"handleSuccess")
                var msg: String = SEARCH_NOTES_SUCCESS
                var uiComponentType: UIComponentType = UIComponentType.None()
                if (resultObj.size == 0) {
                    Log.e(TAG,"handleSuccess: data is null ")

                    msg = SEARCH_NOTES_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }

                return DataState.data(
                    response = Response(
                        message = msg,
                        messageType = MessageType.Success(),
                        uiComponentType = uiComponentType
                    ),
                    data = NoteListViewState(
                        noteList = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()
        Log.e(TAG,"handleSuccess:  emit:")

        emit(value = response)

    }

    companion object {

        val SEARCH_NOTES_SUCCESS = "Successfully retrieved list of notes."
        val SEARCH_NOTES_NO_MATCHING_RESULTS = "There are no notes that match that query."
        val SEARCH_NOTES_FAILED = "Failed to retrieve the list of notes."

    }
}