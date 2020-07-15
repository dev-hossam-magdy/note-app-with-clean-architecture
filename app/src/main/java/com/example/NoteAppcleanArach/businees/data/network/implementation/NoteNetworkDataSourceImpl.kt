package com.example.NoteAppcleanArach.businees.data.network.implementation

import com.example.NoteAppcleanArach.businees.data.network.abstraction.NoteNetworkDataSource
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.framework.datasource.network.abstraction.NoteFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteNetworkDataSourceImpl
@Inject
constructor(
    private val noteFirestoreService: NoteFirestoreService
):NoteNetworkDataSource{

    override suspend fun insertOrUpdateNote(note: Note) = noteFirestoreService.insertOrUpdateNote(note)

    override suspend fun insertOrUpdateNote(list: List<Note>) = noteFirestoreService.insertOrUpdateNote(list)

    override suspend fun insertDeletedNote(note: Note) = noteFirestoreService.insertDeletedNote(note)

    override suspend fun insertDeletedNote(list: List<Note>) = noteFirestoreService.insertDeletedNote(list)

    override suspend fun deleteNote(primaryKey: String) = noteFirestoreService.deleteNote(primaryKey)

    override suspend fun removeDeletedNote(note: Note) = noteFirestoreService.removeDeletedNote(note)

    override suspend fun getDeletedNotes(): List<Note> = noteFirestoreService.getDeletedNotes()

    override suspend fun deleteAllNotes() = noteFirestoreService.deleteAllNotes()

    override suspend fun searchNote(note: Note): Note? = noteFirestoreService.searchNote(note)

    override suspend fun getALlNotes(): List<Note> = noteFirestoreService.getALlNotes()
}