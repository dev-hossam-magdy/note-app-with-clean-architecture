package com.example.NoteAppcleanArach.businees.data.network.abstraction

import androidx.room.PrimaryKey
import com.example.NoteAppcleanArach.businees.doman.model.Note

interface NoteNetworkDataSource {

    suspend fun insertOrUpdateNote(note:Note)
    suspend fun insertOrUpdateNote(list: List<Note>)
    suspend fun insertDeletedNote(note: Note) // insert deleted note
    suspend fun insertDeletedNote(list: List<Note>) // insert deleted note
    suspend fun deleteNote(primaryKey: String)
    suspend fun removeDeletedNote(note: Note) // delete deleted note
    suspend fun getDeletedNotes():List<Note>
    suspend fun deleteAllNotes()
    suspend fun searchNote(note: Note):Note?
    suspend fun getALlNotes():List<Note>

}