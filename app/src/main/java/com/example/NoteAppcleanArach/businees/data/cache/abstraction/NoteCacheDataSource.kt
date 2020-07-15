package com.example.NoteAppcleanArach.businees.data.cache.abstraction

import androidx.room.PrimaryKey
import com.example.NoteAppcleanArach.businees.doman.model.Note
import retrofit2.http.Body
import retrofit2.http.Query

interface NoteCacheDataSource {

    suspend fun insertNote(note: Note): Long
    suspend fun insertNote(list: List<Note>): LongArray
    suspend fun deleteNote(primaryKey: String): Int
    suspend fun deleteNote(list: List<Note>): Int
    suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String): Int
    suspend fun getNumberOfNotes(): Int
    suspend fun searchNotes(
        query: String,
        filterOrOrder: String,
        page: Int
    ): List<Note>
    suspend fun getAllNotes(): List<Note>
    suspend fun getNoteById(primaryKey: String): Note?


}