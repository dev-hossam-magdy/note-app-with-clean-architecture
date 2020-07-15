package com.example.NoteAppcleanArach.framework.datasource.cache.implementation

import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.util.getCurrentTimeStamp
import com.example.NoteAppcleanArach.framework.datasource.cache.abstraction.NoteDaoService
import com.example.NoteAppcleanArach.framework.datasource.cache.util.CacheMapper
import com.example.NoteAppcleanArach.framework.datasource.database.NoteDao
import com.example.NoteAppcleanArach.framework.datasource.database.returnOrderedQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDaoServiceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteMapper: CacheMapper
) : NoteDaoService {

    override suspend fun insertNote(note: Note): Long =
        noteDao.insertNote(noteMapper.mapToEntity(note))

    override suspend fun insertNote(list: List<Note>): LongArray =
        noteDao.insertNotes(noteMapper.noteListTosEntityList(list))

    override suspend fun deleteNote(primaryKey: String): Int = noteDao.deleteNote(primaryKey)

    override suspend fun deleteNote(list: List<Note>): Int {
        val idsList = list.mapIndexed { index, note -> note.id }
        return noteDao.deleteNotes(idsList)
    }

    override suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String): Int =
        noteDao.updateNote(primaryKey, newTitle, newBody, getCurrentTimeStamp())

    override suspend fun getNumberOfNotes(): Int =
        noteDao.getNumNotes()

    override suspend fun searchNotes(): List<Note> =
        noteMapper.entityListToNoteList(noteDao.searchNotes())


    override suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> =
        noteMapper.entityListToNoteList(noteDao.searchNotesOrderByDateDESC(query, page, pageSize))

    override suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> =
        noteMapper.entityListToNoteList(noteDao.searchNotesOrderByDateASC(query, page, pageSize))


    override suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> =
        noteMapper.entityListToNoteList(noteDao.searchNotesOrderByTitleDESC(query, page, pageSize))


    override suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> =
        noteMapper.entityListToNoteList(noteDao.searchNotesOrderByTitleASC(query, page, pageSize))


    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> =
        noteMapper.entityListToNoteList(noteDao.returnOrderedQuery(query,filterAndOrder, page))


    override suspend fun getNoteById(primaryKey: String): Note? =
        noteDao.searchNoteById(primaryKey)?.let { noteCacheEntity ->
            noteMapper.mapFromEntity(noteCacheEntity)
        }

    override suspend fun getAllNotes(): List<Note> =
        noteMapper.entityListToNoteList(noteDao.searchNotes())
}