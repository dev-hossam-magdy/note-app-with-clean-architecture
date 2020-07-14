package com.example.NoteAppcleanArach.businees.data.cache.implementation

import com.example.NoteAppcleanArach.businees.data.cache.abstraction.NoteCacheDataSource
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.framework.datasource.cache.abstraction.NoteDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheDataSourceIpml
@Inject
constructor(
    private val noteDaoService: NoteDaoService
) : NoteCacheDataSource {

    override suspend fun insertNote(note: Note): Long = noteDaoService.insertNote(note)

    override suspend fun insertNote(list: List<Note>): LongArray = noteDaoService.insertNote(list)

    override suspend fun deleteNote(list: List<Note>): Int = noteDaoService.deleteNote(list)

    override suspend fun deleteNote(primaryKey: String): Int = noteDaoService.deleteNote(primaryKey)

    override suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String): Int =
        updateNote(primaryKey, newTitle, newBody)

    override suspend fun getNumberOfNotes(): Int = noteDaoService.getNumberOfNotes()

    override suspend fun searchNotes(query: String, filterOrOrder: String, page: Int): List<Note> {
        TODO(" check the filter and order before query ")
    }

    override suspend fun getNoteById(primaryKey: String): Note? = noteDaoService.getNoteById(primaryKey)
}

