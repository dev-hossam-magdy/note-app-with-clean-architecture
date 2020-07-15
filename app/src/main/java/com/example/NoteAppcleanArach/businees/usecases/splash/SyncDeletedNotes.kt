package com.example.NoteAppcleanArach.businees.usecases.splash

import android.util.Log
import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.data.cache.util.CacheResponseHandler
import com.example.NoteAppcleanArach.businees.data.network.abstraction.NoteNetworkDataSource
import com.example.NoteAppcleanArach.businees.data.network.util.ApiResponseHandler
import com.example.NoteAppcleanArach.businees.data.util.safeApiCall
import com.example.NoteAppcleanArach.businees.data.util.safeCacheCall
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.state.DataState
import kotlinx.coroutines.Dispatchers.IO

class SyncDeletedNotes(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
){
    private val TAG = "SyncDeletedNotes"

    suspend fun syncDeletedNotes(){
        Log.e(TAG,"syncDeletedNotes")
        val apiResult = safeApiCall(IO){
            Log.e(TAG,"safeApiCall")
            noteNetworkDataSource.getDeletedNotes()
        }
        val apiResponse = object :ApiResponseHandler<List<Note>,List<Note>>(
            response = apiResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Note>): DataState<List<Note>> =
                DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
        }.getResult()


        val notes = apiResponse?.data?.let { it }?: ArrayList()
        val cacheResult = safeCacheCall(IO){
            Log.e(TAG,"safeCacheCall")
            noteCacheDataSource.deleteNote(notes)
        }
        object : CacheResponseHandler<Int,Int>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                Log.e(TAG,"handleSuccess: the number of deleted note is  ${resultObj}")
                return  DataState.data(
                    response = null,
                    data = null,
                    stateEvent = null
                )
            }

        }
    }

}