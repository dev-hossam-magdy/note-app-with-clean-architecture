package com.example.NoteAppcleanArach.businees.usecases.notelist

import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResponseHandler
import com.example.NoteAppcleanArach.businees.data.util.safeCacheCall
import com.example.NoteAppcleanArach.businees.doman.state.*
import com.example.NoteAppcleanArach.framework.presentaion.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumNotes(
    private val noteCacheDataSource: NoteCacheDataSource
) {
    private val TAG = "GetNumNotes"


    fun getTotalNumOfNotes(stateEvent: StateEvent): Flow<DataState<NoteListViewState>?> = flow {

        val cacheObject = safeCacheCall(IO) {
            noteCacheDataSource.getNumberOfNotes()
        }


        val response = object : CacheResponseHandler<NoteListViewState, Int>(
            response = cacheObject,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<NoteListViewState> =
                DataState.data(
                    response = Response(
                        messageType = MessageType.Success(),
                        uiComponentType = UIComponentType.None(),
                        message = GET_NUM_NOTES_SUCCESS
                    ),
                    data = NoteListViewState(pageNumber = resultObj),
                    stateEvent = stateEvent
                )

        }.getResult()
        emit(response)
    }

    companion object{
        const val GET_NUM_NOTES_SUCCESS = "successfully retrieval the num of notes from the cache"
    }

}