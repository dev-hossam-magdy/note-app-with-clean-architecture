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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncNotes (
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {

    private val TAG = "SyncNotes"

    suspend fun syncNotes(){
        val cachedNotesList = getCachedNotes()
        syncNetworkNotesWithCachedNotes(ArrayList(cachedNotesList))
    }


    private suspend fun getCachedNotes():List<Note>{
        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.getAllNotes()
        }

        val cacheResponse = object :CacheResponseHandler<List<Note>,List<Note>>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Note>): DataState<List<Note>> =
                DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )


        }.getResult()

        return cacheResponse?.data ?: ArrayList()
    }

    private suspend fun syncNetworkNotesWithCachedNotes(list: ArrayList<Note>) = withContext(IO) {
        val apiResult = safeApiCall(IO){
            noteNetworkDataSource.getALlNotes()
        }

        val apiResponse = object :ApiResponseHandler<List<Note> , List<Note>>(
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

        val noteList = apiResponse?.data ?: ArrayList()

        val job = launch {
            noteList.forEach { note ->
                noteCacheDataSource.getNoteById(note.id)?.let { cachedNote ->
                    list.remove(cachedNote)
                    checkIfCachedNoteRequiresUpdate(cachedNote, note)
                }?: noteCacheDataSource.insertNote(note)

            }
        }

        job.join()

        // insert remaining into network
        list.forEach{
            noteNetworkDataSource.insertOrUpdateNote(it)
        }
    }

    private suspend fun checkIfCachedNoteRequiresUpdate(cachedNote: Note, networkNote: Note) {
        val cacheUpdatedAt = cachedNote.updatedAt
        val networkUpdatedAt = networkNote.updatedAt

        // update cache (network has newest data)
        if(networkUpdatedAt > cacheUpdatedAt){
            Log.e(TAG, "cacheUpdatedAt: ${cacheUpdatedAt}, " +
                    "networkUpdatedAt: ${networkUpdatedAt}, " +
                    "note: ${cachedNote.title}")

            safeCacheCall(IO){
                noteCacheDataSource.updateNote(
                    networkNote.id,
                    networkNote.title,
                    networkNote.body
                )
            }
        }
        // update network (cache has newest data)
        else{
            safeApiCall(IO){
                noteNetworkDataSource.insertOrUpdateNote(cachedNote)
            }
        }
    }
}