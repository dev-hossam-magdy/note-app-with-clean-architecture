package com.example.NoteAppcleanArach.framework.datasource.cache.abstraction

import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.framework.datasource.database.NOTE_PAGINATION_PAGE_SIZE

interface NoteDaoService {

    suspend fun insertNote(note: Note): Long
    suspend fun insertNote(list: List<Note>): LongArray
    suspend fun deleteNote(primaryKey: String): Int
    suspend fun deleteNote(list: List<Note>): Int
    suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String): Int
    suspend fun getNumberOfNotes(): Int
    suspend fun searchNotes(): List<Note>
    suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>

    suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>

    suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>

    suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>
    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note>
    suspend fun getNoteById(primaryKey: String): Note?
    suspend fun getAllNotes(): List<Note>
}